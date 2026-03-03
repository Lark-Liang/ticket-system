package com.example.ticketsystem.service.impl;

import com.example.ticketsystem.dto.ListResponseDTO;
import com.example.ticketsystem.dto.admin.*;
import com.example.ticketsystem.entity.Show;
import com.example.ticketsystem.entity.TicketTier;
import com.example.ticketsystem.mapper.ShowMapper;
import com.example.ticketsystem.mapper.TicketTierMapper;
import com.example.ticketsystem.service.AdminShowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lark
 * @ date 2026/3/2  22:10
 * @ description 管理员演出管理服务实现类
 */
@Service
public class AdminShowServiceImpl implements AdminShowService{
    @Autowired
    private ShowMapper showMapper;

    @Autowired
    private TicketTierMapper ticketTierMapper;

    @Override
    public ListResponseDTO<AdminShowListDTO> getShowList(AdminShowQueryDTO queryDTO) {
        // 参数处理
        if (queryDTO.getPage() < 1) queryDTO.setPage(1);
        if (queryDTO.getSize() < 1 || queryDTO.getSize() > 100) queryDTO.setSize(10);

        // 使用 PageHelper 开始分页
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getSize());

        // 执行查询
        List<Show> shows = showMapper.adminFindByConditions(
                queryDTO.getTitle(),
                queryDTO.getCity(),
                queryDTO.getCategory(),
                queryDTO.getStatus(),
                queryDTO.getStartDate(),
                queryDTO.getEndDate()
        );

        // 获取分页信息
        PageInfo<Show> pageInfo = new PageInfo<>(shows);

        // 转换为 DTO
        List<AdminShowListDTO> showList = shows.stream()
                .map(this::convertToAdminShowListDTO)
                .collect(Collectors.toList());

        return ListResponseDTO.of(
                showList,
                pageInfo.getTotal(),
                pageInfo.getPageNum(),
                pageInfo.getPageSize()
        );
    }

    @Override
    public AdminShowDetailDTO getShowDetail(Long showId) {
        if (showId == null) {
            throw new RuntimeException("演出ID不能为空");
        }

        Show show = showMapper.findById(showId);
        if (show == null) {
            throw new RuntimeException("演出不存在");
        }

        // 查询票档
        List<TicketTier> ticketTiers = ticketTierMapper.findByShowId(showId);

        return convertToAdminShowDetailDTO(show, ticketTiers);
    }

    @Override
    @Transactional
    public AdminShowDetailDTO createShow(AdminShowRequestDTO requestDTO) {
        // 参数校验
        validateShowRequest(requestDTO);

        // 计算最低价和最高价
        Double minPrice = requestDTO.getTicketTiers().stream()
                .map(AdminTicketTierRequestDTO::getPrice)
                .min(Double::compareTo)
                .orElse(0.0);

        Double maxPrice = requestDTO.getTicketTiers().stream()
                .map(AdminTicketTierRequestDTO::getPrice)
                .max(Double::compareTo)
                .orElse(0.0);

        // 计算总库存
        Integer totalStock = requestDTO.getTicketTiers().stream()
                .mapToInt(AdminTicketTierRequestDTO::getTotalStock)
                .sum();

        // 创建演出对象
        Show show = new Show();
        show.setTitle(requestDTO.getTitle());
        show.setDescription(requestDTO.getDescription());
        show.setCategory(requestDTO.getCategory());
        show.setCity(requestDTO.getCity());
        show.setVenue(requestDTO.getVenue());
        show.setCoverImage(requestDTO.getCoverImage());
        show.setStartTime(requestDTO.getStartTime());
        show.setEndTime(requestDTO.getEndTime());
        show.setMinPrice(minPrice);
        show.setMaxPrice(maxPrice);
        show.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : 1);
        show.setTotalStock(totalStock);
        show.setAvailableStock(totalStock);  // 初始可用库存等于总库存
        show.setSaleStartTime(requestDTO.getSaleStartTime());

        // 插入演出
        int rows = showMapper.insert(show);
        if (rows == 0) {
            throw new RuntimeException("创建演出失败");
        }

        // 创建票档
        if (requestDTO.getTicketTiers() != null && !requestDTO.getTicketTiers().isEmpty()) {
            List<TicketTier> ticketTiers = requestDTO.getTicketTiers().stream()
                    .map(tierDTO -> {
                        TicketTier tier = new TicketTier();
                        tier.setShowId(show.getId());
                        tier.setName(tierDTO.getName());
                        tier.setDescription(tierDTO.getDescription());
                        tier.setPrice(tierDTO.getPrice());
                        tier.setTotalStock(tierDTO.getTotalStock());
                        tier.setAvailableStock(tierDTO.getTotalStock());  // 初始可用库存等于总库存
                        tier.setStatus(tierDTO.getStatus() != null ? tierDTO.getStatus() : 1);
                        return tier;
                    })
                    .collect(Collectors.toList());

            ticketTierMapper.batchInsert(ticketTiers);
            return convertToAdminShowDetailDTO(show, ticketTiers);
        }

        return convertToAdminShowDetailDTO(show, new ArrayList<>());
    }

    @Override
    @Transactional
    public AdminShowDetailDTO updateShow(Long showId, AdminShowRequestDTO requestDTO) {
        if (showId == null) {
            throw new RuntimeException("演出ID不能为空");
        }

        // 查询演出是否存在
        Show show = showMapper.findById(showId);
        if (show == null) {
            throw new RuntimeException("演出不存在");
        }

        // 更新演出信息
        if (requestDTO.getTitle() != null) show.setTitle(requestDTO.getTitle());
        if (requestDTO.getDescription() != null) show.setDescription(requestDTO.getDescription());
        if (requestDTO.getCategory() != null) show.setCategory(requestDTO.getCategory());
        if (requestDTO.getCity() != null) show.setCity(requestDTO.getCity());
        if (requestDTO.getVenue() != null) show.setVenue(requestDTO.getVenue());
        if (requestDTO.getCoverImage() != null) show.setCoverImage(requestDTO.getCoverImage());
        if (requestDTO.getStartTime() != null) show.setStartTime(requestDTO.getStartTime());
        if (requestDTO.getEndTime() != null) show.setEndTime(requestDTO.getEndTime());
        if (requestDTO.getStatus() != null) show.setStatus(requestDTO.getStatus());
        if (requestDTO.getSaleStartTime() != null) show.setSaleStartTime(requestDTO.getSaleStartTime());

        // 如果有票档更新，需要处理
        if (requestDTO.getTicketTiers() != null && !requestDTO.getTicketTiers().isEmpty()) {
            // 重新计算价格范围
            Double minPrice = requestDTO.getTicketTiers().stream()
                    .map(AdminTicketTierRequestDTO::getPrice)
                    .min(Double::compareTo)
                    .orElse(show.getMinPrice());

            Double maxPrice = requestDTO.getTicketTiers().stream()
                    .map(AdminTicketTierRequestDTO::getPrice)
                    .max(Double::compareTo)
                    .orElse(show.getMaxPrice());

            show.setMinPrice(minPrice);
            show.setMaxPrice(maxPrice);

            // 重新计算总库存
            Integer totalStock = requestDTO.getTicketTiers().stream()
                    .mapToInt(AdminTicketTierRequestDTO::getTotalStock)
                    .sum();
            show.setTotalStock(totalStock);

            // 注意：availableStock 不应该直接覆盖，需要考虑已售出的票
            // 这里简化处理，实际应该根据现有订单计算

            // 删除原有票档，重新添加（简化处理）
            ticketTierMapper.deleteByShowId(showId);

            List<TicketTier> ticketTiers = requestDTO.getTicketTiers().stream()
                    .map(tierDTO -> {
                        TicketTier tier = new TicketTier();
                        tier.setShowId(showId);
                        tier.setName(tierDTO.getName());
                        tier.setDescription(tierDTO.getDescription());
                        tier.setPrice(tierDTO.getPrice());
                        tier.setTotalStock(tierDTO.getTotalStock());
                        tier.setAvailableStock(tierDTO.getTotalStock());
                        tier.setStatus(tierDTO.getStatus() != null ? tierDTO.getStatus() : 1);
                        return tier;
                    })
                    .collect(Collectors.toList());

            ticketTierMapper.batchInsert(ticketTiers);
        }

        // 执行更新
        int rows = showMapper.update(show);
        if (rows == 0) {
            throw new RuntimeException("更新演出失败");
        }

        // 返回更新后的完整信息
        return getShowDetail(showId);
    }

    @Override
    @Transactional
    public void deleteShow(Long showId) {
        if (showId == null) {
            throw new RuntimeException("演出ID不能为空");
        }

        // 查询演出是否存在
        Show show = showMapper.findById(showId);
        if (show == null) {
            throw new RuntimeException("演出不存在");
        }

        // 逻辑删除（将status设为0）
        int rows = showMapper.delete(showId);
        if (rows == 0) {
            throw new RuntimeException("删除演出失败");
        }

        // 可以选择同时下架所有票档，也可以保留
        // ticketTierMapper.deleteByShowId(showId);
    }

    // ====== 私有辅助方法 ======

    private void validateShowRequest(AdminShowRequestDTO requestDTO) {
        if (requestDTO.getTitle() == null || requestDTO.getTitle().trim().isEmpty()) {
            throw new RuntimeException("演出标题不能为空");
        }
        if (requestDTO.getCategory() == null || requestDTO.getCategory().trim().isEmpty()) {
            throw new RuntimeException("演出分类不能为空");
        }
        if (requestDTO.getCity() == null || requestDTO.getCity().trim().isEmpty()) {
            throw new RuntimeException("城市不能为空");
        }
        if (requestDTO.getVenue() == null || requestDTO.getVenue().trim().isEmpty()) {
            throw new RuntimeException("场馆不能为空");
        }
        if (requestDTO.getStartTime() == null) {
            throw new RuntimeException("开始时间不能为空");
        }
        if (requestDTO.getEndTime() == null) {
            throw new RuntimeException("结束时间不能为空");
        }
        if (requestDTO.getEndTime().isBefore(requestDTO.getStartTime())) {
            throw new RuntimeException("结束时间不能早于开始时间");
        }
        if (requestDTO.getTicketTiers() == null || requestDTO.getTicketTiers().isEmpty()) {
            throw new RuntimeException("至少需要一个票档");
        }
    }

    private AdminShowListDTO convertToAdminShowListDTO(Show show) {
        AdminShowListDTO dto = new AdminShowListDTO();
        dto.setId(show.getId());
        dto.setTitle(show.getTitle());
        dto.setCategory(show.getCategory());
        dto.setCity(show.getCity());
        dto.setVenue(show.getVenue());
        dto.setCoverImage(show.getCoverImage());
        dto.setStartTime(show.getStartTime());
        dto.setEndTime(show.getEndTime());
        dto.setMinPrice(show.getMinPrice());
        dto.setMaxPrice(show.getMaxPrice());
        dto.setStatus(show.getStatus());
        dto.setTotalStock(show.getTotalStock());
        dto.setAvailableStock(show.getAvailableStock());
        dto.setSaleStartTime(show.getSaleStartTime());
        dto.setCreatedAt(show.getCreatedAt());
        return dto;
    }

    private AdminShowDetailDTO convertToAdminShowDetailDTO(Show show, List<TicketTier> ticketTiers) {
        AdminShowDetailDTO dto = new AdminShowDetailDTO();
        dto.setId(show.getId());
        dto.setTitle(show.getTitle());
        dto.setDescription(show.getDescription());
        dto.setCategory(show.getCategory());
        dto.setCity(show.getCity());
        dto.setVenue(show.getVenue());
        dto.setCoverImage(show.getCoverImage());
        dto.setStartTime(show.getStartTime());
        dto.setEndTime(show.getEndTime());
        dto.setMinPrice(show.getMinPrice());
        dto.setMaxPrice(show.getMaxPrice());
        dto.setStatus(show.getStatus());
        dto.setTotalStock(show.getTotalStock());
        dto.setAvailableStock(show.getAvailableStock());
        dto.setSaleStartTime(show.getSaleStartTime());
        dto.setCreatedAt(show.getCreatedAt());
        dto.setUpdatedAt(show.getUpdatedAt());

        if (ticketTiers != null) {
            List<AdminTicketTierDTO> tierDTOs = ticketTiers.stream()
                    .map(this::convertToAdminTicketTierDTO)
                    .collect(Collectors.toList());
            dto.setTicketTiers(tierDTOs);
        }

        return dto;
    }

    private AdminTicketTierDTO convertToAdminTicketTierDTO(TicketTier tier) {
        AdminTicketTierDTO dto = new AdminTicketTierDTO();
        dto.setId(tier.getId());
        dto.setName(tier.getName());
        dto.setDescription(tier.getDescription());
        dto.setPrice(tier.getPrice());
        dto.setTotalStock(tier.getTotalStock());
        dto.setAvailableStock(tier.getAvailableStock());
        dto.setStatus(tier.getStatus());
        return dto;
    }
}
