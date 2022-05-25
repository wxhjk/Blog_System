package com.example.blogsystem.model;

import lombok.Data;

@Data
public class User {
    private int userId;
    private String username;
    private String password;
    private int isYourBlog;
}
