package com.example.test_end_01.entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Plan {
    private  String plan,finish;
    private Integer authorId,id;
    private Date time;
    public Plan(){}

    public Plan(String plan, String finish,Integer authorId, Date date) {
        this.plan = plan;
        this.finish = finish;
        this.authorId = authorId;
        this.time = date;
    }
}
