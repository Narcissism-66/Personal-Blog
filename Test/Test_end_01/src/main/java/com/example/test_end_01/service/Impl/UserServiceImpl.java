package com.example.test_end_01.service.Impl;

import com.example.test_end_01.entity.pojo.NotificationQueryObject;
import com.example.test_end_01.entity.pojo.User;
import com.example.test_end_01.mapper.NotificationMapper;
import com.example.test_end_01.mapper.UserMapper;
import com.example.test_end_01.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    NotificationMapper notificationMapper;

    @Override
    public User login(String username, String password) {
        return userMapper.getUserByUsernameAndPassword(username, password);
    }

    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public int addNewUser(User user) {
        return userMapper.insertNewUser(user);
    }

    //记录未读信息的数量
    @Override
    public int getUnreadNotificationCountByUserId(int id) {
        return notificationMapper.getUnreadNotificationCountByUserId(id);
    }

    //获取所有的信息
    @Override
    public List<NotificationQueryObject> getAllUserNotificationAndType(Integer userId,Integer type) {
        return notificationMapper.getAllNotificationsByUserId(userId,type);
    }

    @Override
    public String readNotificationById(Integer id,Integer userId) {
        if(notificationMapper.updateIsReadByRsId(id,1,userId)==1)
        {
            return null;
        }
        else return "发生错误，请联系管理员！";
    }

    @Override
    public String clearNotificationByType(Integer userId, Integer type) {
        if(notificationMapper.updateIsReadByUserId(type,userId)>0)//会影响很多行
        {
            return null;
        }
        else return "发生错误，请联系管理员！";
    }
}
