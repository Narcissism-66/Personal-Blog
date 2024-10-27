package com.example.test_end_01.mapper;

import com.example.test_end_01.entity.pojo.Blog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogMapper {
    //添加新博客
    @Insert("INSERT INTO blog(title,authorId,updateDate,content) VALUES(#{title},#{authorId},#{updateDate},#{content})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertNewBlog(Blog blog);

    //查询所有博客，按id序
    @Select("SELECT id,title,authorId,substring(content,1,100) AS content FROM blog ORDER BY id DESC LIMIT 50")
    List<Blog> getBlogsOrderById();

    //查询单个博客
    @Select("SELECT * FROM blog WHERE id=#{id}")
    Blog getBlogById(int id);

    //更新观看的数量
    @Update("UPDATE blog SET watches=watches+1 WHERE id=#{id}")
    void updateWatchesById(int id);

    //减少点赞的数量
    @Update("UPDATE blog SET likes=likes-1 WHERE id=#{id}")
    int increaseLikesById(int id);

    //添加点赞数量
    @Update("UPDATE blog SET likes=likes+1 WHERE id=#{id}")
    int addLikesById(int id);

    //减少收藏数量
    @Update("UPDATE blog SET favourites=favourites-1 WHERE id=#{id}")
    int increaseFavouritesById(int id);

    //添加收藏数量
    @Update("UPDATE blog SET favourites=favourites+1 WHERE id=#{id}")
    int addFavouritesById(int id);

    //获取作者的Blog
    @Select("SELECT * FROM blog WHERE authorId=#{authorId} ORDER BY id ASC")
    List<Blog> getBlogsByAuthorId(int authorId);

    @Select("SELECT a.id,a.content,a.title,a.updateDate,a.likes,a.favourites,a.watches\n"+
            "FROM blog a\n"+
            "INNER  JOIN `like` b ON a.id = b.blogId\n"+
            "WHERE b.userId =#{userId} ORDER BY b.id DESC")
    List<Blog> getBlogsLikedByUserId(int userId);

    @Select("SELECT a.id,a.content,a.title,a.updateDate,a.likes,a.favourites,a.watches\n"+
                   "FROM blog a\n"+
                   "INNER  JOIN favourite b ON a.id = b.blogId\n"+
                   "WHERE b.userId =#{userId} ORDER BY b.id DESC")
    List<Blog> getBlogsFavouritesByUserId(int userId);

}
