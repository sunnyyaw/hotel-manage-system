package com.example.dish.controller;

import com.example.dish.entity.Role;
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
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping(value="/roles",method = RequestMethod.GET)
    public Result<List<Role>> getAllRoles(){
        List<Role> roles = roleService.getAllRoleInfo();
        return new Result<>(200,"查询成功",roles);
    }
    @RequestMapping(value="/roles",method = RequestMethod.POST)
    public Result<String> addRole(@Valid @RequestBody Role role){
        try{
            roleService.saveRole(role);
        } catch (Exception e) {
            return new Result<>(400,e.getMessage());
        }
        return new Result<>(200,"保存成功");
    }
    @RequestMapping(value="/roles/{id}",method = RequestMethod.DELETE)
    public Result<String> deleteRole(@PathVariable(name="id")
                                         @Min(message = "编号不能小于1", value = 1L)Long id){
        try{
            roleService.deleteRole(id);
        } catch (Exception e) {
            return new Result<>(400,e.getMessage());
        }
        return new Result<>(200,"删除成功");
    }
}
