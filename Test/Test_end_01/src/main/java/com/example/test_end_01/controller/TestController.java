package com.example.test_end_01.controller;

import com.example.test_end_01.entity.test.RegisterBody;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/test")
public class TestController {
    @PostMapping("login")
    public String login(@RequestParam("zhanghao") String username,
                        @RequestParam("mima") String password,
                        @RequestParam(value = "zidongdenglu",required = false) Boolean isTrue){
        System.out.println(username+" "+password);
        return null;
    }

    @PostMapping("register")
    public String register(@RequestBody RegisterBody requestBody){
        System.out.println(requestBody);
        return null;
    }

    @GetMapping("{id}")
    public String getSomeOne(@PathVariable Integer id){
        //@PathVariable Integer id: 这是一个注解，用于将路径变量 {id} 绑定到方法参数 id 上。
        System.out.println(id);
        return null;
    }
}
