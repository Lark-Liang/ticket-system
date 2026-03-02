package com.example.ticketsystem.service.impl;

import com.example.ticketsystem.dto.*;
import com.example.ticketsystem.entity.Show;
import com.example.ticketsystem.mapper.ShowMapper;
import com.example.ticketsystem.service.SeckillService;
import com.example.ticketsystem.service.ShowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lark
 * @ date 2026/2/13  21:08
 * @ description 演出服务实现类
 */
@Service
public class ShowServiceImpl implements ShowService{
    @Autowired
    private ShowMapper showMapper;

    @Autowired
    private SeckillService seckillService;  // 抢票服务

    @Override
    public ListResponseDTO<ShowListDTO> getHomeShows(String city, int limit) {
        // 参数处理
        if (city == null || city.trim().isEmpty()) {
            city = "北京";
        }
        if (limit <= 0) {
            limit = 10;
        }

        // 查询数据
        List<Show> shows = showMapper.findHomeShows(city, limit);

        // 构建返回格式
        List<ShowListDTO> showList = shows.stream()
                .map(ShowListDTO::fromShow)
                .toList();
        return ListResponseDTO.of(showList, (long) showList.size(), 1, showList.size());
    }

    @Override
    public ListResponseDTO<ShowSearchDTO> searchShows(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new RuntimeException("搜索关键词不能为空");
        }

        List<Show> shows = showMapper.searchByKeyword(keyword.trim());

        List<ShowSearchDTO> searchList = shows.stream()
                .map(ShowSearchDTO::fromShow)
                .toList();

        return ListResponseDTO.of(searchList, (long) searchList.size(), 1, searchList.size());
    }

    @Override
    public ListResponseDTO<ShowListDTO> listShows(String city, String category, int page, int size) {
        // 参数处理
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 10;

        // 使用 PageHelper 开始分页
        PageHelper.startPage(page, size);

        // 查询数据
        List<Show> shows = showMapper.findByConditions(city, category);

        // 使用 PageInfo 获取分页信息
        PageInfo<Show> pageInfo = new PageInfo<>(shows);

        List<ShowListDTO> showList = shows.stream()
                .map(ShowListDTO::fromShow)
                .toList();

        // 返回统一结构体
        return ListResponseDTO.of(
                showList,
                pageInfo.getTotal(),
                pageInfo.getPageNum(),
                pageInfo.getPageSize()
        );
    }

    @Override
    public ShowDetailDTO getShowDetail(Long showId) {
        Show show = showMapper.findById(showId);

        if (show == null) {
            throw new RuntimeException("演出不存在");
        }

        // 构建详情数据（从Controller复制）
        ShowDetailDTO detailDTO = ShowDetailDTO.fromShow(show);

        // 票档信息（简化版）
        List<Map<String, Object>> ticketTiers = new ArrayList<>();

        Map<String, Object> vipTier = new HashMap<>();
        vipTier.put("id", 1);
        vipTier.put("name", "VIP");
        vipTier.put("price", show.getMaxPrice());
        vipTier.put("stockAvailable", show.hasStock());
        ticketTiers.add(vipTier);

        Map<String, Object> standardTier = new HashMap<>();
        standardTier.put("id", 2);
        standardTier.put("name", "普通票");
        standardTier.put("price", show.getMinPrice());
        standardTier.put("stockAvailable", show.hasStock());
        ticketTiers.add(standardTier);

        detailDTO.setTicketTiers(ticketTiers);

        return detailDTO;
    }

    @Override
    @Transactional
    public String seckillTicket(Long userId, Long showId, SeckillRequest request) {
        // 参数校验
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        if (showId == null) {
            throw new RuntimeException("演出ID不能为空");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new RuntimeException("购买数量必须大于0");
        }
        if (request.getTicketTierId() == null) {
            throw new RuntimeException("请选择票档");
        }

        // 调用抢票服务
        return seckillService.seckillTicket(userId, showId, request);
    }

    @Override
    public List<String> getAllCities() {
        return showMapper.findAllCities();
    }

    @Override
    public List<String> getAllCategories() {
        return showMapper.findAllCategories();
    }
}
