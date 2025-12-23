package com.example.ticketsystem.mapper;

import com.example.ticketsystem.entity.Show;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Lark
 * @ date 2025/12/12  20:12
 * @ description
 */
@Mapper
public interface ShowMapper {

    //首页推荐：按城市和状态查询，按开售时间倒序
    @Select("SELECT * FROM `show_info` WHERE status = 1 AND city = #{city} ORDER BY sale_start_time DESC LIMIT #{limit}")
    List<Show> findHomeShows(@Param("city") String city, @Param("limit") int limit);

    //搜索演出：按标题或描述模糊匹配
    @Select("SELECT * FROM show_info WHERE status = 1 AND (title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))")
    List<Show> searchByKeyword(@Param("keyword") String keyword);

    //条件查询：城市、分类、分页
    @Select({
            "<script>",
            "SELECT * FROM `show_info` WHERE status = 1",
            "<if test='city != null'> AND city = #{city} </if>",
            "<if test='category != null'> AND category = #{category} </if>",
            " ORDER BY sale_start_time DESC",
            " LIMIT #{offset}, #{pageSize}",
            "</script>"
    })
    List<Show> findByConditions(@Param("city") String city,
                                @Param("category") String category,
                                @Param("offset") int offset,
                                @Param("pageSize") int pageSize);

    //条件查询总数（用于分页）
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM `show_info` WHERE status = 1",
            "<if test='city != null'> AND city = #{city} </if>",
            "<if test='category != null'> AND category = #{category} </if>",
            "</script>"
    })
    int countByConditions(@Param("city") String city, @Param("category") String category);

    //根据ID查询演出详情
    @Select("SELECT * FROM `show_info` WHERE id = #{id}")
    Show findById(@Param("id") Long id);

    //更新库存（抢票用）
    @Update("UPDATE `show_info` SET available_stock = available_stock - #{quantity}, updated_at = NOW() WHERE id = #{id} AND available_stock >= #{quantity}")
    int reduceStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    //查询所有城市（用于前端筛选）
    @Select("SELECT DISTINCT city FROM `show_info` WHERE status = 1 ORDER BY city")
    List<String> findAllCities();

    //查询所有分类（用于前端筛选）
    @Select("SELECT DISTINCT category FROM `show_info` WHERE status = 1 ORDER BY category")
    List<String> findAllCategories();
}
