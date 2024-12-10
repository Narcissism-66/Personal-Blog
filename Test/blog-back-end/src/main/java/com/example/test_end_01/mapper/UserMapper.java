package com.example.test_end_01.mapper;

import com.example.test_end_01.entity.pojo.Blog;
import com.example.test_end_01.entity.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper//@Mapper 注解只能在接口中使用
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username=#{username} AND password=#{password}")
        //使用 @Select 注解定义了一个查询方法 getUserByUsernameAndPassword，#{username} 和 #{password} 是 MyBatis 的占位符，用于动态替换 SQL 语句中的参数值。
        // 它们从 getUserByUsernameAndPassword 方法的参数中获取值，并在执行查询时将其替换到 SQL 语句中
        //也就是传入getUserByUsernameAndPassword的参数也会传入#{username}和#{password}中
    User getUserByUsernameAndPassword(String username, String password);

    @Select("SELECT username FROM users WHERE id=#{id}")
    String getUsernameById(Integer id);

    @Select("SELECT avator FROM users WHERE id=#{id}")
    String getAvatorById(Integer id);

    @Select("SELECT * FROM users WHERE id=#{id}")
    User getUserById(Integer id);

    @Insert("INSERT INTO users(username,password,mail,avator,role) VALUES(#{username},#{password},#{mail},#{avator},#{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertNewUser(User user);

    //管理用户
    @Select("SELECT * FROM users ")
    List<User> getAllUsers();

    //删除用户
    @Delete(("DELETE FROM users WHERE id =#{id}"))
    int deleteUserById(int id);

    //更新role
    @Update("UPDATE users SET role =#{role} WHERE id =#{userId}")
    int updateRoleById(int userId, String role);

    //更新在线
    @Update("UPDATE users SET IsOnline = #{online} WHERE id = #{userId}")
    int updateOnlineById(Integer userId, String online);

}
