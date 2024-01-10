package com.example.dish.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDTO extends User{
    private Long id;
    @NotBlank(message="用户名不得为空")
    private String username;
    @NotBlank(message="密码不得为空")
    private String password;
    private String salt;
    private String verifyCode;
    private List<Permission> permissions;
    private List<Role> roles;
    private Boolean rememberMe = Boolean.FALSE;
    private Boolean needEncode = Boolean.FALSE;
}
