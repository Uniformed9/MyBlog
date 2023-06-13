package com.example.myblog.service.Impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myblog.dto.UserDto;
import com.example.myblog.entity.User;
import com.example.myblog.exception.HaveDisabledException;
import com.example.myblog.exception.PasswordWrongException;
import com.example.myblog.mapper.UserMapper;
import com.example.myblog.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.myblog.util.RedisUtil.LOGIN_USER_KEY;
import static com.example.myblog.util.RedisUtil.LOGIN_USER_TTL;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Value("E:/code/myblog/source/user/")
    private String UserPath;

    public User login(@RequestBody Map<String,String> map, HttpSession session){
        String userName = map.get("userName");
        String password = map.get("password");
        //查看该用户是否为新用户
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName,userName);
        User user = getOne(userLambdaQueryWrapper);
        if(user==null){
            //是新用户,自动注册
            user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            user.setStatus(1);
            save(user);
            //将用户的信息存到session中，这样可以通过过滤器
            //随机生成token作为登录令牌
            String token = UUID.randomUUID().toString();

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setToken(token);
            userDto.setUserName(userName);
            userDto.setPassword(password);
            userDto.setStatus(1);

            Map<String, Object> userMap = BeanUtil.beanToMap(userDto, new HashMap<>(),
                    CopyOptions.create()
                            .setIgnoreNullValue(true)
                            //在setFieldValueEditor中也需要判空
                            .setFieldValueEditor((fieldName,fieldValue) -> {
                                if (fieldValue == null){
                                    fieldValue = "0";
                                }else {
                                    fieldValue = fieldValue + "";
                                }
                                return fieldValue;
                            }));
            //存储
            stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY+token,userMap);
            //设置有效期
            stringRedisTemplate.expire(LOGIN_USER_KEY+token,LOGIN_USER_TTL, TimeUnit.MINUTES);
            //在用户文件夹下创建一个和用户名字同名的文件夹
            String userDir = UserPath+userName;
            java.io.File file = new java.io.File(userDir);
            if(!file.exists()){
                file.mkdirs();
            }

            return userDto;
        }else{
            if(user.getStatus()==0){

                throw new HaveDisabledException("用户已被禁用");
            }else if(!Objects.equals(user.getPassword(), password)){
                throw new PasswordWrongException("密码错误");

            }else{
                //将用户的信息存到session中，这样可以通过过滤器
                //随机生成token作为登录令牌
                String token = UUID.randomUUID().toString();
                session.setAttribute(token,user);
                UserDto userDto = new UserDto();
                userDto.setId(user.getId());
                userDto.setToken(token);
                userDto.setUserName(user.getUserName());
                userDto.setPassword(user.getPassword());
                userDto.setStatus(user.getStatus());
                Map<String, Object> userMap = BeanUtil.beanToMap(userDto, new HashMap<>(),
                        CopyOptions.create()
                                .setIgnoreNullValue(true)
                                //在setFieldValueEditor中也需要判空
                                .setFieldValueEditor((fieldName,fieldValue) -> {
                                    if (fieldValue == null){
                                        fieldValue = "0";
                                    }else {
                                        fieldValue = fieldValue + "";
                                    }
                                    return fieldValue;
                                }));
                //存储
                stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY+token,userMap);
                //设置有效期
                stringRedisTemplate.expire(LOGIN_USER_KEY+token,LOGIN_USER_TTL, TimeUnit.MINUTES);
                return userDto;
            }
        }
    }
    @Override
    public Map<Integer, User> getUserMap() {
        return userMapper.getUserMap();
    }
}
