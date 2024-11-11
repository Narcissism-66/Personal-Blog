package com.example.test_end_01.service;

import com.example.test_end_01.entity.pojo.Link;

import java.util.List;

public interface LinkService {
    int addNewLink(String name, String location);
    List<Link> getAllLinks();
}
