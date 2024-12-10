package com.example.test_end_01.controller;

import com.example.test_end_01.entity.pojo.NotificationQueryObject;
import com.example.test_end_01.entity.pojo.RestBean;
import com.example.test_end_01.entity.pojo.User;
import com.example.test_end_01.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/user")//注解用于定义这个控制器类中所有方法的公共路径前缀,这意味着这个控制器类中的所有方法的路径都会以 /api/user 开头
public class UserController {
    @Resource
    UserService userService;

    @GetMapping("information")
    public RestBean<User> getMyInfo(HttpServletRequest request){//没有正确的令牌的话进不去，所以不用判断是不是空了
//        if(session.getAttribute("user")==null)return RestBean.failure(401,"未登陆~");
        User theUser=userService.getUserById((Integer) request.getAttribute("id"));
        theUser.setPassword(null);
        theUser.setReadCount(userService.getUnreadNotificationCountByUserId(theUser.getId()));
        //获取键为user的值
        return RestBean.success("获取成功！",theUser);
    }


    @GetMapping("logout")
    public RestBean<String> logout(HttpSession session){
        if(session.getAttribute("user")==null)return RestBean.failure(401,"未登陆~");
        session.removeAttribute("user");
        session.invalidate();
        return RestBean.success("退出成功！");
    }

    @PostMapping("addNewUser")
    public RestBean<String> register(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     @RequestParam("mail") String mail,
                                     @RequestParam("avator") String avator,
                                     @RequestParam("role") String role){
        User user = new User(username, password, mail,avator,role);
        int status = userService.addNewUser(user);
        if(status>0) return RestBean.success("添加成功！");
        else return RestBean.failure(503,"出现错误，请联系管理员~");
    }

    @GetMapping("TheUser")
    public RestBean<User> getUser(HttpServletRequest request){
        int id = (Integer) request.getAttribute("id");
        return RestBean.success("获取成功！",userService.getUserById(id));
    }


    @GetMapping("getNotifications")
    public RestBean<List<NotificationQueryObject>> getAllUsersNotifications(@RequestParam("type")Integer type,
                                                                            HttpServletRequest request){
        int userId = (Integer) request.getAttribute("id");
        return RestBean.success("请求成功！",userService.getAllUserNotificationAndType(userId,type));
    }

    @PostMapping("readNotification")
    public RestBean<String> readNotifications(@RequestParam("id") Integer id,HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("id");
        String status = userService.readNotificationById(id,userId);
        if(status == null)return RestBean.success("成功读取~");
        else return RestBean.failure(503,status);
    }

    @PostMapping("clearNotifications")
    public RestBean<String> clearNotifications(@RequestParam("type") Integer type,HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("id");
        String status = userService.clearNotificationByType(userId,type);
        if(status == null)return RestBean.success("成功清空~");
        else return RestBean.failure(503,status);
    }

    @GetMapping("getAllUsers")
    public RestBean<List<User>> getAllUsers(){
        return RestBean.success("获取成功！",userService.getAllUsers());
    }

    @PostMapping("deleteUserById")
    public RestBean<String> deleteUserById(@RequestParam("id") Integer id){
        int status = userService.deleteUserById(id);
        if(status>0) return RestBean.success("删除成功！");
        else return RestBean.failure(503,"出现错误，请联系管理员~");
    }

    @PostMapping("updateRole")
    public RestBean<String> updateRoleById(@RequestParam("userId") Integer userId,
                                           @RequestParam("role") String role){
        int status =userService.updateRoleById(userId, role);
        if(status>0)
        return RestBean.success("修改成功！");
        else return RestBean.failure(503,"出现错误，请联系管理员~");
    }

    @PostMapping("UpdateOnline")
    public RestBean<String> updateOnline(@RequestParam("userId") Integer userId,
                                           @RequestParam("isOnline") String isOnline) {
        int status = userService.updateOnlineById(userId, isOnline);
        if(status>0)return RestBean.success("更新成功！");
        else return RestBean.failure(503,"出现错误，请联系管理员~");
    }
}
