package com.example.test_end_01.service;

import com.example.test_end_01.entity.pojo.Blog;
import com.example.test_end_01.entity.pojo.Review;
import com.example.test_end_01.entity.pojo.ReviewQueryObject;
import com.example.test_end_01.entity.pojo.Review_RS;
import com.example.test_end_01.entity.vo.BlogVo;
import com.example.test_end_01.entity.vo.ReviewVo;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BlogService {
    int addNewBlog(Blog blog);
    int deleteBlogById(int blogId);
    List<BlogVo> getBlogsOrderById();
    BlogVo getBlogById(int id,int userId);
    String addNewLike(int userId,int blogId);
    String deleteLike(int userId,int blogId);
    int checkUserLikedBlog(int userId,int blogId);
    String addNewFavourite(int userId,int blogId);
    String deleteFavourite(int userId,int blogId);
    int checkUserFavouriteBlog(int userId,int blogId);
    List<Blog> getBlogsByAuthorId(int authorId);
    List<Blog> getBlogsLikedByUserId(int userId);
    List<Blog> getBlogsFavouritesByUserId(int userId);
    int addNewReview(int blogId,String question,int userId,int replyId);
    int addNewReply(int blogId,String question,int userId,int authorId,int replyId);
    List<ReviewVo> getReviewsByBlogId(int blogId);
    List<ReviewQueryObject> getAllReviewByBlogId(int blogId);
    List<BlogVo>searchBlogs(String title);
    List<Blog>getBlogsOrderByWatches();
    int updateBlog(Blog blog);
    List<Blog>getBlogsOrderByType(String tag);
    List<Blog>getBlogsOfKnowledge();
    List<Blog>getBlogsOfProject();
    List<Blog>getBlogsOfAlgorithm();
}
