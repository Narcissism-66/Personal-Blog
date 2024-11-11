package com.example.test_end_01.entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Review {
    private Integer id, blogId,authorId,replyId;
    private String question,authorname,authoravator;
    private Date time;
    public Review(){}
    public Review(int blogId, String question, Integer authorId,String authorname,String authoravator,Integer replyId){
        this.blogId = blogId;
        this.question = question;
        this.authorId = authorId;
        this.authorname = authorname;
        this.authoravator = authoravator;
        this.replyId = replyId;
    }


    public Review(int blogId, String question) {
        this.blogId = blogId;
        this.question = question;
    }
}
