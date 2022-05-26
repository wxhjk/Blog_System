package com.example.blogsystem.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Blog {
    private int blogId;
    public String title;
    private String content;
    private Timestamp postTime;
    private int userId;
}
