package com.example.test_end_01.controller;

import com.example.test_end_01.entity.pojo.NotificationQueryObject;
import com.example.test_end_01.entity.pojo.RestBean;
import com.example.test_end_01.entity.pojo.User;
import com.example.test_end_01.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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
    @PostMapping("/UpdateAvatar")
    public RestBean<String> UpdateAvatar(HttpServletRequest request,
                                         @RequestParam("avatar")String avatar)
    {
        Integer userId=(Integer) request.getAttribute("id");
        int status=userService.UpdateAvatar(userId,avatar);
        if(status!=0)return RestBean.success("cg");
        else return RestBean.failure(503,"失败");
    }
}
