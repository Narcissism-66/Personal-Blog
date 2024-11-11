package com.example.test_end_01.entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Favourite {
    private int id,userId,blogId;
    private Date favouritetime;
    public Favourite(){}
}
