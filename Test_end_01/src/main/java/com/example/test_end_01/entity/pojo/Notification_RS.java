package com.example.test_end_01.entity.pojo;

import lombok.Data;

@Data
public class Notification_RS {
    private Integer id, userId,notiId;
    private boolean isRead;
    public Notification_RS(){}
    public Notification_RS(Integer userId, Integer notiId,Boolean isRead) {
        this.userId = userId;
        this.notiId = notiId;
        this.isRead = isRead;
    }
}
