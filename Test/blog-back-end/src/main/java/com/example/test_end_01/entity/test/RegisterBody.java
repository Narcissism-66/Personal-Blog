package com.example.test_end_01.entity.test;

import lombok.Data;

@Data
public class RegisterBody {
    private String test_username, test_password, test_mail;
    private RegisterBody(String test_username, String test_password, String test_mail)
    {
        this.test_username = test_username;
        this.test_password = test_password;
        this.test_mail = test_mail;
    }
}
