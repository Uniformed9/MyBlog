package com.example.myblog.common;

import com.example.myblog.dto.UserDto;

public class UserHolder {
    private static  final ThreadLocal<UserDto> t1=new ThreadLocal<>();
    public static void setUser(UserDto user){
        t1.set(user);
    }
    public static UserDto getUser(){return t1.get();}
    public static void removeUser(){t1.remove();}

}
