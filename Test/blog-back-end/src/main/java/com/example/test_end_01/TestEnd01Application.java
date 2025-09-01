package com.example.test_end_01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class TestEnd01Application {

    public static void main(String[] args) {
        SpringApplication.run(TestEnd01Application.class, args);
    }

}
