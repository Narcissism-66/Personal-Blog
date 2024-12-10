package com.example.test_end_01.entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewQueryObject {
    private Integer id,userId,authorId,blogId,reviewId,replyId;
    private String question,username,useravator,authorname;
    private Date replytime;
    public ReviewQueryObject(){}
}
