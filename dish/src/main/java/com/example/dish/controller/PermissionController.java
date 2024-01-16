package com.example.dish.controller;

import com.example.dish.entity.Permission;
import com.example.dish.common.Result;
import com.example.dish.services.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class PermissionController {
    @Autowired
    private RoleService roleService;
    @RequestMapping(value = "/permissions",method = RequestMethod.GET)
    public Result<List<Permission>> getAllPermissions(){
        List<Permission> permissions = roleService.getAllPermissions();
        return new Result<>(200,"查询成功",permissions);
    }
    @RequestMapping(value = "/permissions",method = RequestMethod.POST)
    public Result<String> savePermission(@Valid @RequestBody Permission permission){
        try{
            roleService.savePermission(permission);
        }catch (Exception e){
            return new Result<>(400,e.getMessage());
        }
        return new Result<>(200,"添加成功");
    }
    @RequestMapping(value = "/permissions/{id}",method = RequestMethod.DELETE)
    public Result<String> deletePermission(@PathVariable @Min(value = 1 , message = "编号不能小于1")
                                           Long id){
        try{
            roleService.deletePermission(id);
        }catch (Exception e){
            return new Result<>(400,e.getMessage());
        }
        return new Result<>(200,"删除成功");
    }
}
