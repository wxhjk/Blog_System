package com.example.blogsystem.model;

import lombok.Data;

@Data
public class Blog {
    private int blogId;
    public String title;
    private String content;
    private String postTime;
    private int userId;
}
