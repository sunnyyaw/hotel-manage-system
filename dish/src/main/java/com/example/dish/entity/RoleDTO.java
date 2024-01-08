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
public class RoleDTO extends Role{
    private Long id;
    @NotBlank(message = "角色名不能为空")
    private String roleName;
    private List<Permission> permissions;
}
