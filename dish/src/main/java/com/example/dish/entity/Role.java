package com.example.dish.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class Role {
    private Long id;
    @NotBlank(message = "角色名不能为空")
    private String roleName;
    private List<Permission> permissions;
}
