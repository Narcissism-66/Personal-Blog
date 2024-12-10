package com.example.test_end_01.entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Review_RS {
    private Integer id, userId, blogId,authorId,reviewId,replyId;
    private String username,useravator,authorname;
    private Date reviewtime;

    public Review_RS(Integer userId, Integer blogId, Integer authorId,String authorname, Integer reviewId, Integer replyId,String username,String useravator,Date reviewtime) {
        this.userId = userId;
        this.blogId = blogId;
        this.authorId = authorId;
        this.authorname = authorname;
        this.reviewId = reviewId;
        this.replyId=replyId;
        this.username = username;
        this.useravator = useravator;
        this.reviewtime = reviewtime;
    }
}
