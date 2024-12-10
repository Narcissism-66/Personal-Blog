package com.example.test_end_01.mapper;

import com.example.test_end_01.entity.pojo.Blog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogMapper {
    //添加新博客
    @Insert("INSERT INTO blog(title,authorId,updateDate,content,tag) VALUES(#{title},#{authorId},#{updateDate},#{content},#{tag})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertNewBlog(Blog blog);

    @Delete(("DELETE FROM blog WHERE id =#{id}"))
    int deleteBlogById(int id);

    //查询所有博客，按id序
    @Select("SELECT id,title,authorId,tag,updateDate,substring(content,1,120) AS content FROM blog ORDER BY id DESC LIMIT 50")
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
    @Select("SELECT * FROM blog WHERE authorId=#{authorId} ORDER BY id DESC")
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

    //搜索Blog
    @Select("SELECT id, title, authorId,tag, substring(content, 1, 200) AS content " +
            "FROM blog " +
            "WHERE title LIKE CONCAT('%', #{x}, '%') " +
            "OR content LIKE CONCAT('%', #{x}, '%') " +
            "OR tag LIKE CONCAT('%', #{x}, '%') "+
            "ORDER BY id DESC " +
            "LIMIT 50")
    List<Blog> searchBlogs(String x);
//    WHERE title LIKE CONCAT('%',#{title},'%')：
//    这部分表示查询条件。LIKE关键字用于模糊匹配，CONCAT函数用于将字符串拼接在一起。
//    #{title}是一个参数占位符，表示输入的标题字符串。CONCAT('%',#{title},'%')会将输入的标题字符串前后加上百分号（%），从而实现模糊匹配。


    //获取浏览量前8的blog
    @Select("SELECT * FROM blog ORDER BY watches DESC LIMIT 8")
    //LIMIT 8: 限制查询结果只返回前 8 条记录
    List<Blog> getBlogsOrderByWatches();

    //修改blog
    @Update("UPDATE blog SET title = #{title},authorId=#{authorId}, content = #{content}, updateDate = #{updateDate},tag=#{tag} WHERE id = #{id}")
    int updateBlog(Blog blog);

    @Select("SELECT * FROM blog WHERE tag =#{tag} ORDER BY watches DESC  ")
    List<Blog> getBlogsOrderByType(String tag);

    @Select("SELECT * FROM blog WHERE tag ='小知识' ORDER BY watches DESC LIMIT 3 ")
        //LIMIT 8: 限制查询结果只返回前 8 条记录
    List<Blog> getBlogsOfKnowledge();
    @Select("SELECT * FROM blog WHERE tag ='项目' ORDER BY watches DESC LIMIT 3 ")
        //LIMIT 8: 限制查询结果只返回前 8 条记录
    List<Blog> getBlogsOfProject();
    @Select("SELECT * FROM blog WHERE tag ='算法' ORDER BY watches DESC LIMIT 3 ")
        //LIMIT 8: 限制查询结果只返回前 8 条记录
    List<Blog> getBlogsOfAlgorithm();


}

