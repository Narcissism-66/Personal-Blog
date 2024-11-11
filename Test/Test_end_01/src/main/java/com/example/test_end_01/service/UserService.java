package com.example.test_end_01.service;

import com.example.test_end_01.entity.pojo.NotificationQueryObject;
import com.example.test_end_01.entity.pojo.User;

import java.util.List;

public interface UserService {
    User login(String username, String password);
    User getUserById(int id);
    int addNewUser(User user);
    int getUnreadNotificationCountByUserId(int id);
    List<NotificationQueryObject> getAllUserNotificationAndType(Integer userId,Integer type);
    String readNotificationById(Integer id,Integer userId);
    String clearNotificationByType(Integer userId,Integer type);
}
