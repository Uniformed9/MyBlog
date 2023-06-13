package com.example.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myblog.entity.User;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


public interface IUserService extends IService<User> {

    User login(@RequestBody Map<String,String> map, HttpSession session);
    Map<Integer,User> getUserMap();
}
