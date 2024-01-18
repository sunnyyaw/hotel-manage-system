package com.example.dish.controller;

import com.example.dish.entity.Permission;
import com.example.dish.common.Result;
import com.example.dish.services.RolePermService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class PermissionController {
    @Autowired
    private RolePermService roleService;

    @RequiresRoles(value = {"超级管理员","管理员"},logical = Logical.OR)
    @RequestMapping(value = "/permissions",method = RequestMethod.GET)
    public Result<List<Permission>> getAllPermissions(){
        List<Permission> permissions = roleService.getAllPermissions();
        return Result.success("查询成功",permissions);
    }
    @RequiresRoles(value = {"超级管理员","管理员"},logical = Logical.OR)
    @RequestMapping(value = "/permissions",method = RequestMethod.POST)
    public Result<String> savePermission(@Valid @RequestBody Permission permission) throws Exception {
        roleService.savePermission(permission);
        return Result.success("添加成功");
    }
    @RequiresRoles(value = {"超级管理员","管理员"},logical = Logical.OR)
    @RequestMapping(value = "/permissions/{id}",method = RequestMethod.DELETE)
    public Result<String> deletePermission(@PathVariable @Min(value = 1 , message = "编号不能小于1")
                                           Long id) throws Exception {
        roleService.deletePermission(id);
        return Result.success("删除成功");
    }
}
