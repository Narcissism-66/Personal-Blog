package com.example.test_end_01.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FavouriteMapper {
    @Insert(" INSERT INTO favourite (userId,blogId,favouritetime) VALUES (#{userId},#{blogId},CURRENT_TIMESTAMP)")
    int addNewFavourite(Integer userId, Integer blogId);

    @Select("SELECT id FROM favourite WHERE userId=#{userId} AND blogId=#{blogId}")
    Integer checkUserFavouriteBlog(Integer userId, Integer blogId);

    @Delete("DELETE FROM favourite WHERE userId=#{userId} AND blogId=#{blogId}")
    int deleteFavourite(Integer userId, Integer blogId);
}
