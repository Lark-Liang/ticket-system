package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.ListResponseDTO;
import com.example.ticketsystem.dto.admin.AdminShowDetailDTO;
import com.example.ticketsystem.dto.admin.AdminShowListDTO;
import com.example.ticketsystem.dto.admin.AdminShowQueryDTO;
import com.example.ticketsystem.dto.admin.AdminShowRequestDTO;

/**
 * @author Lark
 * @ date 2026/3/2  22:09
 * @ description 管理员演出管理服务接口
 */
public interface AdminShowService {
    //分页查询演出列表
    ListResponseDTO<AdminShowListDTO> getShowList(AdminShowQueryDTO queryDTO);

    //查询演出详情（包含票档）
    AdminShowDetailDTO getShowDetail(Long showId);

    //添加演出
    AdminShowDetailDTO createShow(AdminShowRequestDTO requestDTO);

    //修改演出
    AdminShowDetailDTO updateShow(Long showId, AdminShowRequestDTO requestDTO);

    //删除演出（逻辑删除）
    void deleteShow(Long showId);
}
