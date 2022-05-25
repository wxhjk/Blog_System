package com.example.blogsystem.mapper;

import com.example.blogsystem.model.Blog;
import com.example.blogsystem.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<Blog> getAll();

    Blog getBlogById(Integer blogId);

    User findPassword(String username);

    Integer findUsername(String username);
}
