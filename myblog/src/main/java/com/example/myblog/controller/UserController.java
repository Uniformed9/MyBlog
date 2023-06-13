package com.example.myblog.controller;

import com.example.myblog.common.R;
import com.example.myblog.entity.User;
import com.example.myblog.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/user")
@CrossOrigin
@Api(tags = "用户管理")
public class UserController {
    @Autowired
    IUserService userService;
    @ApiOperation("登录")
    @PostMapping("/login")

    public R<User> login(@RequestBody @ApiParam("userName和Password") Map<String, String> map, HttpSession session, ServletRequest servletRequest){

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        User user = userService.login(map, session);
        if (user!=null){
            return R.success(user);
        }else{
            return R.error("登录失败");
        }

    }

}
