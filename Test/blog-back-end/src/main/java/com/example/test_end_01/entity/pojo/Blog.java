package com.example.test_end_01.entity.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class Blog {
    private Integer id;
    private Integer authorId, watches, likes, favourites;
    private String title, content,tag;
    private Date updateDate;
    private short isPublic;
    public Blog(){}
    public Blog(String title, Integer authorId, Date updateDate, String content,String tag)
    {
        this.title=title;
        this.authorId=authorId;
        this.updateDate=updateDate;
        this.content=content;
        this.tag=tag;
    }
    public Blog(Integer id,String title, Integer authorId, Date updateDate, String content,String tag)
    {
        this.id=id;
        this.title=title;
        this.authorId=authorId;
        this.updateDate=updateDate;
        this.content=content;
        this.tag=tag;
    }
}
