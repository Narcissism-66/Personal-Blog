package com.example.test_end_01.controller;

import com.example.test_end_01.entity.pojo.RestBean;
import com.example.test_end_01.entity.pojo.User;
import com.example.test_end_01.service.UserService;
import com.example.test_end_01.utils.JWTUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")//注解用于定义这个控制器类中所有方法的公共路径前缀,这意味着这个控制器类中的所有方法的路径都会以 /api/user 开头
public class AuthController {
    @Resource
    UserService userService;

    //可以从前端获取信息并返回给后端，同时后端也会将处理结果返回给前端
    @PostMapping("login")
    //@PostMapping 注解用于将 HTTP POST 请求映射到特定的处理方法上。处理方法的返回值会被 Spring MVC 框架转换为 HTTP 响应，并发送给前端（看成发送给前端）。
    public RestBean<String> login(@RequestParam String username, @RequestParam String password)
    //@RequestParam用于从 HTTP 请求中提取参数。它可以从 URL 查询参数、表单数据或 JSON 请求体中提取数据，并将其绑定到方法参数上
    {
        User loginuser=userService.login(username, password);
        if(loginuser==null)
        {
            return RestBean.failure(401,"账号或密码错误~");
        }
        else
        {
            //将当前登录的用户信息存储在会话中，以便在后续的请求中可以方便地访问和使用这些信息。
            return RestBean.success("登陆成功~", JWTUtil.createToken(loginuser));
        }
    }
}
