package com.example.ticketsystem.dto;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author Lark
 * @ date 2026/3/2  15:45
 * @ description 统一的返回结构体
 */
@Data
public class ListResponseDTO<T> {
    private List<T> list;      // 数据列表
    private Long total;         // 总记录数
    private Integer page;       // 当前页码
    private Integer size;       // 每页大小
    private Integer totalPages; // 总页数
    private Boolean hasNext;    // 是否有下一页
    private Boolean hasPrev;    // 是否有上一页

    public static <T> ListResponseDTO<T> of(List<T> list, Long total, Integer page, Integer size) {
        ListResponseDTO<T> response = new ListResponseDTO<>();
        response.setList(list != null ? list : Collections.emptyList());
        response.setTotal(total != null ? total : 0L);
        response.setPage(page);
        response.setSize(size);

        int totalPages = (int) Math.ceil((double) total / size);
        response.setTotalPages(totalPages);
        response.setHasNext(page < totalPages);
        response.setHasPrev(page > 1);

        return response;
    }
}
