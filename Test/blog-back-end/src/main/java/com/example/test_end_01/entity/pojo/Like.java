package com.example.test_end_01.entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Like {
    private Integer id,userId,blogId;
    private Date likeTime;

    public Like(){}

}
