package com.example.test_end_01.entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationQueryObject {
    private Integer id,type;
    private  Boolean isRead;
    private String title,content;
    private Date createTime;

    public NotificationQueryObject(){}
}
