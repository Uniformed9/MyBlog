package com.example.myblog.dto;

import com.example.myblog.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends User {
    String token;
}
