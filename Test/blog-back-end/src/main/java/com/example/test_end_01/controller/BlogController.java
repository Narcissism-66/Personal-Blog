package com.example.test_end_01.controller;

import com.example.test_end_01.entity.pojo.*;
import com.example.test_end_01.entity.vo.BlogVo;
import com.example.test_end_01.entity.vo.ReviewVo;
import com.example.test_end_01.service.BlogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    @Resource
    BlogService blogService;

    @PostMapping("addNew")
    public RestBean<String> addNewBlog(HttpServletRequest request,
                                       @RequestParam("title") String title,
                                       @RequestParam ("content")String content,
                                       @RequestParam("tag") String tag) {
        Blog blog=new Blog(title, (Integer) request.getAttribute("id"), new Date(), content,tag);
        int status = blogService.addNewBlog(blog);
        if(status>0) return RestBean.success("发布成功！");
        else return RestBean.failure(503,"出现错误，请联系管理员~");
    }
    @PostMapping("deleteBlog")
    public RestBean<String> deleteBlog(@RequestParam("blogId") int blogId){
        int status=blogService.deleteBlogById(blogId);
        if(status!=0)return RestBean.success("删除成功！");
        else return RestBean.failure(503,"出现错误，请联系管理员！");
    }
    @GetMapping("getIntroduce")
    public RestBean<List<BlogVo>> getIntroduceBlog(){
        return RestBean.success("获取成功！",blogService.getBlogsOrderById());
    }
    @GetMapping ("getBlog")
    public RestBean<BlogVo> getBlog(@RequestParam("id") int id,HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("id");
        return RestBean.success("成功！",blogService.getBlogById(id,userId));
    }

    @PostMapping("like")
    public RestBean<String> likeBlog(@RequestParam("id") int id, HttpServletRequest request){
        int userId=(Integer)request.getAttribute("id");
        String s=blogService.addNewLike(userId,id);
        if(s==null)return RestBean.success("点赞成功！");
        else return RestBean.failure(503,s);
    }

    @PostMapping("unlike")
    public RestBean<String> unlikeBlog(@RequestParam("id") int id, HttpServletRequest request){
        int userId=(Integer)request.getAttribute("id");
        String s=blogService.deleteLike(userId,id);
        if(s==null)return RestBean.success("已取消点赞！");
        else return RestBean.failure(503,s);
    }

    @GetMapping("checkLike")
    public RestBean<Integer> checkLikeBlog(@RequestParam("id") int id,HttpServletRequest request){
        int userId=(Integer)request.getAttribute("id");
        int status= blogService.checkUserLikedBlog(userId,id);
        if(status==1)return RestBean.success("已经点过赞！",status);
        else return RestBean.success("未点过赞！",status);
    }

    @PostMapping("addNewFavourite")
    public RestBean<Integer> addNewFavourite(HttpServletRequest request,
                                             @RequestParam("blogId") int blogId){
        int userId=(Integer)request.getAttribute("id");
        String status= blogService.addNewFavourite(userId,blogId);
        if(status==null){
            return RestBean.success("收藏成功~");
        }
        else return RestBean.failure(503,"出现错误，请联系管理员~");
    }

    @PostMapping("unfavourite")
    public RestBean<String> unfavouriteBlog(@RequestParam("blogId") int blogId, HttpServletRequest request){
        int userId=(Integer)request.getAttribute("id");
        String s=blogService.deleteFavourite(userId,blogId);
        if(s==null)return RestBean.success("已取消收藏！");
        else return RestBean.failure(503,s);
    }

    @GetMapping("checkFavourite")
    public RestBean<Integer> checkFavouriteBlog(@RequestParam("id") int id,HttpServletRequest request){
        int userId=(Integer)request.getAttribute("id");
        int status= blogService.checkUserFavouriteBlog(userId,id);
        if(status==1)return RestBean.success("已经收藏过了！",status);
        else return RestBean.success("未收藏！",status);
    }

    @GetMapping("getBlogsByAuthorId")
    public RestBean<List<Blog>> getBlogByAuthorId(HttpServletRequest request){
        int authorId=(Integer) request.getAttribute("id");
        return RestBean.success("获取成功！",blogService.getBlogsByAuthorId(authorId));
    }

    @GetMapping("getBlogsLikedByUserId")
    public RestBean<List<Blog>> getBlogsLikedByUserId(HttpServletRequest request){
        int userId=(Integer) request.getAttribute("id");
        return RestBean.success("获取成功！",blogService.getBlogsLikedByUserId(userId));
    }

    @GetMapping("getBlogsFavouritesByUserId")
    public RestBean<List<Blog>> getBlogsFavouritesByUserId(HttpServletRequest request){
        int userId=(Integer) request.getAttribute("id");
        return RestBean.success("获取成功！",blogService.getBlogsFavouritesByUserId(userId));
    }

    @PostMapping("addNewReview")
    public RestBean<String> addNewReview(@RequestParam("blogId") int blogId,
                                         @RequestParam("question") String question,
                                         @RequestParam("replyId") int replyId,
                                         HttpServletRequest request){
        int userId=(Integer)request.getAttribute("id");
        int status=blogService.addNewReview(blogId,question,userId,replyId);
        if(status!=0)
        {
            return RestBean.success("评论成功！");
        }
        else return RestBean.failure(503,"评论失败，请重试~");
    }
    @PostMapping("addNewReply")
    public RestBean<String> addNewReply(@RequestParam("blogId") int blogId,
                                         @RequestParam("question") String question,
                                         @RequestParam("authorId" )int authorId,
                                         @RequestParam("replyId") int replyId,
                                         HttpServletRequest request){
        int userId=(Integer)request.getAttribute("id");
        int status=blogService.addNewReply(blogId,question,userId,authorId,replyId);
        if(status!=0)
        {
            return RestBean.success("回复成功！");
        }
        else return RestBean.failure(503,"回复失败，请重试~");
    }

    @GetMapping("getReviewsByBlogId")
    public RestBean<List<ReviewVo>> getReviewsByBlogId(@RequestParam("blogId") int blogId){
        return RestBean.success("获取成功！",blogService.getReviewsByBlogId(blogId));
    }

    @GetMapping("getAllReviewByBlogId")
    public RestBean<List<ReviewQueryObject>> getAllReviewByBlogId(@RequestParam("blogId") int blogId){
        return RestBean.success("获取成功！",blogService.getAllReviewByBlogId(blogId));
    }


    @GetMapping("SearchBlogs")
    public RestBean<List<BlogVo>> searchBlogs(@RequestParam("title") String title){
        return RestBean.success("获取成功！",blogService.searchBlogs(title));
    }

    @GetMapping("getBlogsOrderByWatches")
    public RestBean<List<Blog>> getBlogsOrderByWatches(){
        return RestBean.success("获取成功！",blogService.getBlogsOrderByWatches());
    }

    @PostMapping("UpdateBlog")
    public RestBean<Blog> updateBlog(@RequestParam("title") String title,
                                     @RequestParam("content") String content,
                                     @RequestParam("blogId") int blogId,
                                     @RequestParam("tag") String tag,
                                     HttpServletRequest request){
        int authorId=(Integer)request.getAttribute("id");
        Blog blog=new Blog(blogId,title,authorId,new Date(),content,tag);
        int status = blogService.updateBlog(blog);
        if(status>0) return RestBean.success("修改成功！");
        else return RestBean.failure(503,"出现错误，请联系管理员~");
    }

    @GetMapping("getBlogsOrderByType")
    public RestBean<List<Blog>> getBlogsOrderByType(@RequestParam("type") String type){
        return RestBean.success("获取成功！",blogService.getBlogsOrderByType(type));
    }

    @GetMapping("Algorithm")
    public RestBean<List<Blog>> getBlogsOfAlgorithm(){
        return RestBean.success("获取成功！",blogService.getBlogsOfAlgorithm());
    }
    @GetMapping("Project")
    public RestBean<List<Blog>> getBlogsOfProject(){
        return RestBean.success("获取成功！",blogService.getBlogsOfProject());
    }
    @GetMapping("Knowledge")
    public RestBean<List<Blog>> getBlogsOfKnowledge(){
        return RestBean.success("获取成功！",blogService.getBlogsOfKnowledge());
    }
}
