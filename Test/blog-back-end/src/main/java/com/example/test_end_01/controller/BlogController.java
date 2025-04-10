package com.example.test_end_01.controller;

import com.example.test_end_01.entity.pojo.*;
import com.example.test_end_01.entity.vo.BlogVo;
import com.example.test_end_01.entity.vo.ReviewVo;
import com.example.test_end_01.service.BlogService;
import jakarta.annotation.Resource;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @GetMapping ("/share")
    public RestBean<Blog> getBlogOfShare(@RequestParam("id") int id){
        System.out.println("blog--share");
        return RestBean.success("成功！",blogService.getBlogOfShare(id));
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

    @Value("${file.upload-dir}") // 从配置文件中读取上传目录路径
    private String uploadDir;

    @PostMapping("/uploadImg")
    public RestBean<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return RestBean.failure(400, "上传的文件为空");
            }

            System.out.println("1");
            // 创建上传目录（如果不存在）
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                if (!directory.mkdirs()) { // 创建目录
                    return RestBean.failure(500, "无法创建上传目录");
                }
            }

            System.out.println("2");
            // 生成唯一的文件名
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // 将文件保存到服务器
            Files.copy(file.getInputStream(), filePath);
            System.out.println("3");

            // 返回成功响应，包含文件的相对路径
            String fileUrl = "uploads/" + fileName; // 相对路径
            System.out.println("4");
            return RestBean.success("文件上传成功", fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("失败了");
            return RestBean.failure(500, "文件上传失败：" + e.getMessage());
        }
    }



}
