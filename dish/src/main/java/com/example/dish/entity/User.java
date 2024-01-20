package com.example.dish.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户实体类
 */
@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String password;
    private String salt;
    private String phone;
    private Integer status;

    private String verifyCode;
    private List<Role> roles;
    private Boolean rememberMe = Boolean.FALSE;
    private Boolean needEncode = Boolean.FALSE;
}
