package com.example.test_end_01.entity.pojo;

import lombok.Data;

@Data
public class User {
    private Integer id,ReadCount;
    private String username,password,mail,avator,role;
    private String IsOnline;
    public User(){}

    public User( String username, String password, String mail,String avator,String role ) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.avator =avator;
        this.role = role;
    }
}
