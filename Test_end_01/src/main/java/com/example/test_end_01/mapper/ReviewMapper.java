package com.example.test_end_01.mapper;

import com.example.test_end_01.entity.pojo.NotificationQueryObject;
import com.example.test_end_01.entity.pojo.Review;
import com.example.test_end_01.entity.pojo.ReviewQueryObject;
import com.example.test_end_01.entity.pojo.Review_RS;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewMapper {
    @Insert("INSERT INTO review(blogId,question,authorId,authorname,authoravator,replyId,time) VALUES (#{blogId},#{question},#{authorId},#{authorname},#{authoravator},#{replyId},CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int addNewReview(Review review);

    @Select("SELECT * FROM review WHERE blogId=#{blogId}")
    List<Review> getReviewsByBlogId(Integer blogId);

    @Insert("INSERT INTO rs_review(userId,blogId,authorId,authorname,reviewId,replyId,username,useravator,reviewtime) VALUES(#{userId},#{blogId},#{authorId},#{authorname},#{reviewId},#{replyId},#{username},#{useravator},#{reviewtime})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    int addNewReview_RS(Review_RS review_rs);

    @Select("SELECT a.id,a.blogId,a.question,a.authorId,a.time,b.userId,b.reviewId,a.replyId ,b.username,b.useravator,b.authorname\n"+
            "FROM review a\n"+
            "INNER  JOIN rs_review b ON a.blogId = b.BlogId AND a.authorId = b.userId AND a.id = b.reviewId AND a.replyId=b.replyId\n"+
            "WHERE b.blogId =#{blogId}  ORDER BY a.id DESC")
    List<ReviewQueryObject>getAllReviewByBlogId(Integer blogId);
}
