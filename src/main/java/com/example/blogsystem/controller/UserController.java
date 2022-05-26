package com.example.blogsystem.controller;

import com.example.blogsystem.model.Blog;
import com.example.blogsystem.model.User;
import com.example.blogsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;

@Controller
@ResponseBody
@RequestMapping("/blog-system")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    // 根据有没有带参数确定返回,是博客列表页还是博客详情页
    @RequestMapping("/blog")
    public Object getAll(Integer blogId) {
        if (blogId == null) {
            return userService.getAll();
        } else {
            return userService.getBlogById(blogId);
        }
    }

    // 实现登录功能
    @RequestMapping("/login")
    public Object login(String username, String password) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("success", 200);
        String msg = "";
        int state = -1;
        // 进行非空校验(前端和后端都需要进行非空校验)
        if (username == null || password == null || username.equals("") || password.equals("")) {
            msg = "用户名或密码为空";
            result.put("state", state);
            result.put("msg", msg);
            return result;
        }
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

    // 根据有没有带参数确定,获取当前登录的用户是否有权限删除,编辑和查询当前用户有多少文章
    @RequestMapping("/user")
    public HashMap<String, Object> getUser(Integer blogId) {
        HashMap<String, Object> result = new HashMap<>();
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        int userId = user.getUserId();
        int articleNum = userService.getArticleNum(userId);
        result.put("articleNum", articleNum);
        if (blogId == null) {
            result.put("user", user);
        } else {
            Blog author = userService.getBlogById(blogId);
            user.setIsYourBlog(user.getUserId() == author.getUserId() ? 1 : 0);
            result.put("blogId", blogId);
            result.put("user", user);
        }
        return result;
    }

    // 实现删除功能
    @RequestMapping("/del")
    public int del(Integer blogId) {
        return userService.del(blogId);
    }

    // 实现注册功能
    @RequestMapping("/reg")
    public HashMap<String, Object> reg(String username, String password) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("success", 200);
        String msg = "";
        int state = -1;
        // 1. 进行非空校验
        if (username != null && password != null && !username.equals("") && !password.equals("")) {
            // 2. 调用 service 实现添加业务
            int num = userService.findUsername(username);
            if (num >= 1) {
                // 注册失败,用户名已存在
                msg = "用户名已存在! 请重新注册";
                result.put("state", state);
                result.put("msg", msg);
                return result;
            } else {
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                int row = userService.add(user);
                state = 1;
            }
        } else {
            // 参数传递不全
            msg = "用户名或密码为空!";
        }
        // 3. 将执行结果返回给前端
        result.put("msg", msg);
        result.put("state", state);
        return result;
    }

    // 实现注销功能
    @RequestMapping("/logout")
    public void loginOut() throws IOException {
        HttpSession session = request.getSession(false);
        session.removeAttribute("user");
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    // 实现文章发布功能
    @RequestMapping("/submit")
    public HashMap<String, Object> submit(@RequestBody Blog blog) {
        HashMap<String, Object> result = new HashMap<>();
        String msg = "标题或者正文内容为空! ";
        int state = -1;
        if (blog.getTitle() != null && blog.getContent() != null
                && !blog.getTitle().equals("") && !blog.getContent().equals("")) {
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("user");
            blog.setUserId(user.getUserId());
            blog.setPostTime(new Timestamp(System.currentTimeMillis()));
            state = 1;
            int num = userService.addBlog(blog);
            System.out.println(blog);
            System.out.println("插入成功");
        }
        result.put("state", state);
        result.put("msg", msg);
        return result;
    }

    // 实现在更新时,传入博客原有的数据
    @RequestMapping("/getBlogTC")
    public Blog getBlogTc(Integer blogId) {
        return userService.getBlogById(blogId);
    }

    // 实现更新博客的功能
    @RequestMapping("/update")
    public HashMap<String, Object> update(Integer blogId, @RequestBody Blog newBlog) {
        HashMap<String, Object> result = new HashMap<>();
        String msg = "标题或者正文内容为空! ";
        int state = -1;
        if (newBlog.getTitle() != null && newBlog.getContent() != null
                && !newBlog.getTitle().equals("") && !newBlog.getContent().equals("")) {
            int num = userService.updateBlog(blogId, newBlog.getTitle(), newBlog.getContent());
            System.out.println(num);
            state = 1;
        }
        result.put("state", state);
        result.put("msg", msg);
        return result;
    }
    // 判断当前用户否有权限
//    @RequestMapping("/permission")
//    public User getUserPermission(Integer blogId) {
//        HttpSession session = request.getSession(false);
//        User user = (User) session.getAttribute("user");
//        Blog author = userService.getBlogById(blogId);
//        user.setIsYourBlog(user.getUserId() == author.getUserId() ? 1 : 0);
//        return user;
//    }

    //    // 获取到博客详情页
//    @RequestMapping("/detail")
//    public Blog getBlogById(Integer blogId) {
//        return userService.getBlogById(blogId);
//    }
}
