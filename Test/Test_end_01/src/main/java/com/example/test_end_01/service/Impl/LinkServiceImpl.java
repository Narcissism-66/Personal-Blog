package com.example.test_end_01.service.Impl;

import com.example.test_end_01.entity.pojo.Link;
import com.example.test_end_01.mapper.LinkMapper;
import com.example.test_end_01.service.LinkService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {
    @Resource
    LinkMapper linkMapper;

    @Override
    public int addNewLink(String name, String location) {
        return linkMapper.insertLink(name, location);
    }

    @Override
    public List<Link> getAllLinks() {
        return linkMapper.getAllLinks();
    }
}
