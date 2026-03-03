package com.example.ticketsystem.controller.admin;

import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.dto.ListResponseDTO;
import com.example.ticketsystem.dto.admin.AdminShowDetailDTO;
import com.example.ticketsystem.dto.admin.AdminShowListDTO;
import com.example.ticketsystem.dto.admin.AdminShowQueryDTO;
import com.example.ticketsystem.dto.admin.AdminShowRequestDTO;
import com.example.ticketsystem.service.AdminShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lark
 * @ date 2026/3/2  22:15
 * @ description 管理员演出管理控制器
 */
@RestController
@RequestMapping("/admin/show")
public class AdminShowController {
    @Autowired
    private AdminShowService adminShowService;

    /**
     * 分页查询演出列表
     */
    @GetMapping("/list")
    public ApiResponse<?> getShowList(AdminShowQueryDTO queryDTO) {
        try {
            ListResponseDTO<AdminShowListDTO> result = adminShowService.getShowList(queryDTO);
            return ApiResponse.success(result);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 查询演出详情
     */
    @GetMapping("/{id}")
    public ApiResponse<?> getShowDetail(@PathVariable Long id) {
        try {
            AdminShowDetailDTO showDetail = adminShowService.getShowDetail(id);
            return ApiResponse.success(showDetail);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("不存在")) {
                return ApiResponse.error(404, e.getMessage());
            }
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 添加演出
     */
    @PostMapping
    public ApiResponse<?> createShow(@RequestBody AdminShowRequestDTO requestDTO) {
        try {
            AdminShowDetailDTO showDetail = adminShowService.createShow(requestDTO);
            return ApiResponse.success("添加成功", showDetail);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 修改演出
     */
    @PutMapping("/{id}")
    public ApiResponse<?> updateShow(
            @PathVariable Long id,
            @RequestBody AdminShowRequestDTO requestDTO) {
        try {
            AdminShowDetailDTO showDetail = adminShowService.updateShow(id, requestDTO);
            return ApiResponse.success("修改成功", showDetail);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 删除演出（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteShow(@PathVariable Long id) {
        try {
            adminShowService.deleteShow(id);
            return ApiResponse.success("删除成功");
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
