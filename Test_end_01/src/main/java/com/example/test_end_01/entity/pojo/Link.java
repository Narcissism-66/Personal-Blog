package com.example.test_end_01.entity.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class Link {
    private String name,location;
    private Integer id;
    private Date time;
    public Link(){}
    public Link(String name, String location , Integer id, Date time) {
        this.name = name;
        this.location = location;
        this.id = id;
        this.time = time;
    }
}
