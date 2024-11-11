package com.example.test_end_01.mapper;

import com.example.test_end_01.entity.pojo.Notification;
import com.example.test_end_01.entity.pojo.NotificationQueryObject;
import com.example.test_end_01.entity.pojo.Notification_RS;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Insert("INSERT INTO notification(title,content,type,createTime)VALUES"+
           "(#{title},#{content},#{type},#{createTime})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    int insertNewNotificationData(Notification notification);

    @Insert("INSERT INTO rs_notification (userId,notiId,isRead) VALUES"+" (#{userId},#{notiId},#{isRead})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    int insertNewNotificationRS(Notification_RS notificationrs);

    //未读信息的数量
    @Select("SELECT COUNT(*) AS 'CNT' FROM rs_notification WHERE userId =#{userId} AND isRead=false")
    int getUnreadNotificationCountByUserId(Integer userId);


    @Select("SELECT a.id,a.content,a.title,a.createTime,a.type,b.isRead\n"+
            "FROM notification a\n"+
            "INNER  JOIN rs_notification b ON a.id = b.notiId\n"+
            "WHERE b.userId =#{userId} AND a.type=#{type} ORDER BY a.id DESC")
    //      a.id, a.content, a.title, a.createTime, a.type 是从 notification 表中查询的字段。

    //      FROM notification a:  这部分表示查询的表是 notification，并将其别名为 a。

    //      INNER JOIN rs_notification b ON a.id = b.notiId:
    //      这部分表示使用 INNER JOIN 将 notification 表和 rs_notification 表连接起来。
    //      连接条件是 a.id = b.notiId，即 notification 表中的 id 字段与 rs_notification 表中的 notiId 字段相等。
    //      rs_notification 表的别名为 b。
    //      ORDER BY a.id DESC   根据notification表中的id进行逆序排序
    List<NotificationQueryObject>getAllNotificationsByUserId(Integer userId,Integer type);

    //更新某条信息的状态（未读取）
    @Update("UPDATE rs_notification SET isRead=#{isRead} WHERE notiId=#{notiId} AND userId=#{userId}")
    int updateIsReadByRsId(Integer notiId,Integer isRead,Integer userId);

    @Update("UPDATE rs_notification,notification SET rs_notification.isRead=1 WHERE notification.type=#{type} AND notification.id=rs_notification.notiId AND rs_notification.userId=#{userId}")
    int updateIsReadByUserId(Integer type,Integer userId);
    //  UPDATE rs_notification, notification：
    //  表示要更新rs_notification表，并且使用notification表进行条件筛选。
}
