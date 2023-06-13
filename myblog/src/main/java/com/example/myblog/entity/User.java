package com.example.myblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")

public class User implements Serializable {
    private static final long serialVersionUID = 1L;            //指定序列化版本号

    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户的密码")
    private String password;

    @ApiModelProperty(value = "用户是否禁用 0表示禁用 1表示正常")
    private Integer status;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "邮箱")
    private  String email;
    @ApiModelProperty(value = "头像")
    private  String avatar;

}
