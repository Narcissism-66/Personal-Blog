package com.example.test_end_01.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper {
    @Insert("INSERT INTO `like`(userId,blogId,likeTime) VALUES (#{userId},#{blogId},CURRENT_TIMESTAMP)")//like是关键字
    int addNewLike(Integer userId, Integer blogId);

    @Select("SELECT id FROM `like` WHERE userId=#{userId} AND blogId=#{blogId}")
    Integer checkUserLikedBlog(Integer userId, Integer blogId);

    @Delete("DELETE FROM `like` WHERE userId=#{userId} AND blogId=#{blogId}")
    int deleteLike(Integer userId, Integer blogId);
}
