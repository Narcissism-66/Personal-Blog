package com.example.test_end_01.controller;

import com.example.test_end_01.entity.pojo.Link;
import com.example.test_end_01.entity.pojo.RestBean;
import com.example.test_end_01.service.LinkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/link")
public class LinkController {
    @Resource
    LinkService linkService;

    @PostMapping("addNew")
    public RestBean<String> listLinks(@RequestParam("name") String name,
                                      @RequestParam ("location")String location) {
        int status=linkService.addNewLink(name, location);
        if(status>0) return RestBean.success("添加成功！");
        else return RestBean.failure(503,"出现错误，请联系管理员~");
    }

    @GetMapping("getAllLinks")
    public RestBean<List<Link>> getAllLinks(){
        return RestBean.success("获取成功！",linkService.getAllLinks());
    }
}
