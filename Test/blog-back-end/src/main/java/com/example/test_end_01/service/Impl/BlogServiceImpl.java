package com.example.test_end_01.service.Impl;

import com.example.test_end_01.entity.pojo.*;
import com.example.test_end_01.entity.vo.BlogVo;
import com.example.test_end_01.entity.vo.ReviewVo;
import com.example.test_end_01.mapper.*;
import com.example.test_end_01.service.BlogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {
    @Resource
    BlogMapper blogMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    LikeMapper likeMapper;
    @Resource
    NotificationMapper notificationMapper;
    @Resource
    FavouriteMapper favouriteMapper;
    @Resource
    ReviewMapper reviewMapper;

    @Override
    public int addNewBlog(Blog blog) {
        return blogMapper.insertNewBlog(blog);
    }

    @Override
    public int deleteBlogById(int blogId) {
        return blogMapper.deleteBlogById(blogId);
    }

    @Override
    public List<BlogVo> getBlogsOrderById() {
        List<BlogVo> result=new ArrayList<BlogVo>();
        for(Blog blog : blogMapper.getBlogsOrderById()){
            result.add((new BlogVo(blog,userMapper.getUsernameById(blog.getAuthorId()),userMapper.getAvatorById(blog.getAuthorId()))));
        }
        return result;
    }

    @Override
    public BlogVo getBlogById(int id,int userId) {
        blogMapper.updateWatchesById(id);
        Blog blog=blogMapper.getBlogById(id);
        return new BlogVo(blogMapper.getBlogById(id),userMapper.getUsernameById(blog.getAuthorId()),userMapper.getAvatorById(blog.getAuthorId()),likeMapper.checkUserLikedBlog(userId,id)!=null,favouriteMapper.checkUserFavouriteBlog(userId,id)!=null);
    }
    //点赞
    @Override
    public String addNewLike(int userId, int blogId) {
        Blog blog=blogMapper.getBlogById(blogId);
        if(likeMapper.checkUserLikedBlog(userId,blogId)!=null) return "已经点过赞了~";
        int status = likeMapper.addNewLike(userId, blogId);
        if (status == 1&&blogMapper.addLikesById(blogId)==1) {
            Notification notification=new Notification(0,"点赞通知","您的文章:\""+blog.getTitle()+"\"刚刚被用户uid:"+userId+"点赞了。",new Date());
            int a=notificationMapper.insertNewNotificationData(notification);
            int b=notificationMapper.insertNewNotificationRS(new Notification_RS(blog.getAuthorId(),notification.getId(),false));
            return null;
        }
        else {
            return "发生错误，请联系管理员！";
        }
    }

    @Override
    public String deleteLike(int userId, int blogId) {
        if(likeMapper.checkUserLikedBlog(userId,blogId)==null) return "您未点过赞~";
        if(likeMapper.deleteLike(userId,blogId)==1&&blogMapper.increaseLikesById(blogId)==1) return null;
        else return "发生错误，请联系管理员！";
    }

    @Override
    public int checkUserLikedBlog(int userId, int blogId) {
        if(likeMapper.checkUserLikedBlog(userId,blogId)==null)return 0;
        else return 1;
    }

    //收藏
    @Override
    public String addNewFavourite(int userId, int blogId) {
        Blog blog=blogMapper.getBlogById(blogId);
        int status=favouriteMapper.addNewFavourite(userId,blogId);
        if(status!=0&&blogMapper.addFavouritesById(blogId)==1)
        {
            Notification notification=new Notification(1,"收藏通知","您的文章:\""+blog.getTitle()+"\"刚刚被用户uid:"+userId+"收藏了。",new Date());
            int a=notificationMapper.insertNewNotificationData(notification);
            int b=notificationMapper.insertNewNotificationRS(new Notification_RS(blog.getAuthorId(),notification.getId(),false));
            return null;
        }
        else{
            return "发生错误，请联系管理员！";
        }
    }

    @Override
    public String deleteFavourite(int userId, int blogId) {
        int status=favouriteMapper.deleteFavourite(userId,blogId);
        if(status!=0&&blogMapper.increaseFavouritesById(blogId)==1)return null;
        else return "发生错误，请联系管理员~";
    }

    @Override
    public int checkUserFavouriteBlog(int userId, int blogId) {
        if(favouriteMapper.checkUserFavouriteBlog(userId,blogId)==null)return 0;
        else return 1;
    }

    @Override
    public List<Blog> getBlogsByAuthorId(int authorId) {
        return blogMapper.getBlogsByAuthorId(authorId);
    }

    @Override
    public List<Blog> getBlogsLikedByUserId(int userId) {
        return blogMapper.getBlogsLikedByUserId(userId);
    }

    @Override
    public List<Blog> getBlogsFavouritesByUserId(int userId) {
        return blogMapper.getBlogsFavouritesByUserId(userId);
    }

    @Override
    public int addNewReview(int blogId, String question,int userId,int replyId) {
        Blog blog=blogMapper.getBlogById(blogId);
//        Review review1=new Review(blogId,question,userId,replyId);//因为是使用review的getId()方法，所以需要以Review插入
        Review review=new Review(blogId,question,userId,userMapper.getUsernameById(userId),userMapper.getAvatorById(userId),replyId);
        int b=reviewMapper.addNewReview(review);//需要先插入然后菜才能使用id
        Review_RS review_rs=new Review_RS(userId,blogId,blog.getAuthorId(),null,review.getId(),0,userMapper.getUsernameById(userId),userMapper.getAvatorById(userId),new Date());
        int a=reviewMapper.addNewReview_RS(review_rs);
        if(a!=0&&b!=0)
        return 1;
        else return 0;
    }

    @Override
    public int addNewReply(int blogId, String question,int userId,int authorId,int replyId) {
        Review review=new Review(blogId,question,userId,userMapper.getUsernameById(userId),userMapper.getAvatorById(userId),replyId);//因为是使用review的getId()方法，所以需要以Review插入
        int b=reviewMapper.addNewReview(review);//需要先插入然后菜才能使用id
        Review_RS review_rs=new Review_RS(userId,blogId,authorId,userMapper.getUsernameById(authorId),review.getId(),replyId,userMapper.getUsernameById(userId),userMapper.getAvatorById(userId),new Date());
        int a=reviewMapper.addNewReview_RS(review_rs);
        if(a!=0&&b!=0)
            return 1;
        else return 0;
    }

    @Override
    public List<ReviewVo> getReviewsByBlogId(int blogId) {
        List<ReviewVo> result=new ArrayList<ReviewVo>();
        boolean isReply=false;
        for(Review review : reviewMapper.getReviewsByBlogId(blogId)){
            result.add(new ReviewVo(review,isReply));
        }
        return result;
    }

    @Override
    public List<ReviewQueryObject> getAllReviewByBlogId(int blogId) {
        return reviewMapper.getAllReviewByBlogId(blogId);
    }

    @Override
    public List<BlogVo> searchBlogs(String title) {
        List<BlogVo> result=new ArrayList<BlogVo>();
        for(Blog blog : blogMapper.searchBlogs(title)){
            result.add((new BlogVo(blog,userMapper.getUsernameById(blog.getAuthorId()),userMapper.getAvatorById(blog.getAuthorId()))));
        }
        return result;
    }

    @Override
    public List<Blog> getBlogsOrderByWatches() {
        return blogMapper.getBlogsOrderByWatches();
    }

    @Override
    public int updateBlog(Blog blog) {
        return blogMapper.updateBlog(blog);
    }

    @Override
    public List<Blog> getBlogsOrderByType(String tag) {
        return blogMapper.getBlogsOrderByType(tag);
    }

    @Override
    public List<Blog> getBlogsOfKnowledge() {
        return blogMapper.getBlogsOfKnowledge();
    }

    @Override
    public List<Blog> getBlogsOfProject() {
        return blogMapper.getBlogsOfProject();
    }

    @Override
    public List<Blog> getBlogsOfAlgorithm() {
        return blogMapper.getBlogsOfAlgorithm();
    }

    @Override
    public Blog getBlogOfShare(int id) {
        return blogMapper.getBlogById(id);
    }
}
