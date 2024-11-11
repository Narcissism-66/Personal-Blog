package com.example.test_end_01.mapper;

import com.example.test_end_01.entity.pojo.Blog;
import com.example.test_end_01.entity.pojo.Link;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface LinkMapper {
    @Insert("INSERT INTO link(name,location) VALUES(#{name},#{location})")
    int insertLink(String name, String location);

    @Select("SELECT * FROM link")
    List<Link> getAllLinks();
}
