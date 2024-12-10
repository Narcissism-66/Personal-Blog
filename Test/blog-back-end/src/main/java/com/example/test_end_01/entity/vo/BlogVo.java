package com.example.test_end_01.entity.vo;

import com.example.test_end_01.entity.pojo.Blog;
import lombok.Data;

import java.util.List;

@Data
public class BlogVo {
    private Blog blog;
    private String username, avator;
    private Boolean isLike,isFavourite;

    public BlogVo(Blog blog, String username, String avator, Boolean isLike, Boolean isFavourite) {
        this.blog = blog;
        this.username = username;
        this.avator = avator;
        this.isLike = isLike;
        this.isFavourite = isFavourite;
    }

    public BlogVo(Blog blog, String username, String avator) {
        this.blog = blog;
        this.username = username;
        this.avator = avator;
    }
}
