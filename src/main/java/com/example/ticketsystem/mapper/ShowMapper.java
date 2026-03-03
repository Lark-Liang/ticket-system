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
            "</script>"
    })
    List<Show> findByConditions(@Param("city") String city,
                                @Param("category") String category);

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

    // ====== 管理员方法 ======

    /**
     * 管理员条件查询所有演出（包含下架的）
     */
    @Select({
            "<script>",
            "SELECT * FROM `show_info` WHERE 1=1",
            "<if test='title != null and title != \"\"'> AND title LIKE CONCAT('%', #{title}, '%')</if>",
            "<if test='city != null and city != \"\"'> AND city = #{city}</if>",
            "<if test='category != null and category != \"\"'> AND category = #{category}</if>",
            "<if test='status != null'> AND status = #{status}</if>",
            "<if test='startDate != null and startDate != \"\"'> AND DATE(start_time) >= #{startDate}</if>",
            "<if test='endDate != null and endDate != \"\"'> AND DATE(end_time) <= #{endDate}</if>",
            " ORDER BY created_at DESC",
            "</script>"
    })
    List<Show> adminFindByConditions(@Param("title") String title,
                                     @Param("city") String city,
                                     @Param("category") String category,
                                     @Param("status") Integer status,
                                     @Param("startDate") String startDate,
                                     @Param("endDate") String endDate);

    /**
     * 插入演出
     */
    @Insert("INSERT INTO show_info (title, description, category, city, venue, cover_image, " +
            "start_time, end_time, min_price, max_price, status, total_stock, available_stock, sale_start_time) " +
            "VALUES (#{title}, #{description}, #{category}, #{city}, #{venue}, #{coverImage}, " +
            "#{startTime}, #{endTime}, #{minPrice}, #{maxPrice}, #{status}, #{totalStock}, #{availableStock}, #{saleStartTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Show show);

    /**
     * 更新演出
     */
    @Update("<script>" +
            "UPDATE show_info SET updated_at = NOW()" +
            "<if test='title != null'> , title = #{title}</if>" +
            "<if test='description != null'> , description = #{description}</if>" +
            "<if test='category != null'> , category = #{category}</if>" +
            "<if test='city != null'> , city = #{city}</if>" +
            "<if test='venue != null'> , venue = #{venue}</if>" +
            "<if test='coverImage != null'> , cover_image = #{coverImage}</if>" +
            "<if test='startTime != null'> , start_time = #{startTime}</if>" +
            "<if test='endTime != null'> , end_time = #{endTime}</if>" +
            "<if test='minPrice != null'> , min_price = #{minPrice}</if>" +
            "<if test='maxPrice != null'> , max_price = #{maxPrice}</if>" +
            "<if test='status != null'> , status = #{status}</if>" +
            "<if test='totalStock != null'> , total_stock = #{totalStock}</if>" +
            "<if test='availableStock != null'> , available_stock = #{availableStock}</if>" +
            "<if test='saleStartTime != null'> , sale_start_time = #{saleStartTime}</if>" +
            " WHERE id = #{id}" +
            "</script>")
    int update(Show show);

    /**
     * 逻辑删除（修改status为0）
     */
    @Update("UPDATE show_info SET status = 0, updated_at = NOW() WHERE id = #{id}")
    int delete(@Param("id") Long id);
}
