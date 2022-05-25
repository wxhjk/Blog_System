package com.example.blogsystem.service;

import com.example.blogsystem.mapper.UserMapper;
import com.example.blogsystem.model.Blog;
import com.example.blogsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    public UserMapper userMapper;

    public List<Blog> getAll() {
        return userMapper.getAll();
    }

    public Blog getBlogById(Integer blogId) {
        return userMapper.getBlogById(blogId);
    }

    public User findPassword(String username) {
        return userMapper.findPassword(username);
    }

    public Integer findUsername(String username) {
        return userMapper.findUsername(username);
    }
}
