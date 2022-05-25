package com.example.blogsystem.controller;

import com.example.blogsystem.model.Blog;
import com.example.blogsystem.model.User;
import com.example.blogsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/blog-system")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/blog")
    public List<Blog> getAll() {
        return userService.getAll();
    }

    @RequestMapping("/detail")
    public Blog getBlogById(Integer blogId) {
        return userService.getBlogById(blogId);
    }

    @RequestMapping("/login")
    public Object login(String username, String password) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("success", 200);
        String msg = "";
        int state = -1;
        int num = userService.findUsername(username);
        if (num < 1) {
            // 登录失败,用户名不存在
            msg = "用户名不存在! 请先注册";
            result.put("state", state);
            result.put("msg", msg);
            return result;
        }
        User user = userService.findPassword(username);
        if (user.getPassword().equals(password)) {
            // 登录成功
            state = 1;
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
        } else {
            // 登录失败
            msg = "密码输入错误,请先检查";
        }
        result.put("state", state);
        result.put("msg", msg);
        return result;
    }

    @RequestMapping("/user")
    public User getUser() {
        HttpSession session = request.getSession(false);
        return (User) session.getAttribute("user");
    }

    @RequestMapping("/permission")
    public User getUserPermission(Integer blogId) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        Blog author = userService.getBlogById(blogId);
        user.setIsYourBlog(user.getUserId() == author.getUserId() ? 1 : 0);
        return user;
    }

//    @RequestMapping("del")
//    public int del(Integer blogId) {
//
//    }
}
