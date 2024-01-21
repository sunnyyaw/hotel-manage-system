package com.example.dish.controller;

import com.example.dish.common.PageUtils;
import com.example.dish.common.Query;
import com.example.dish.entity.User;
import com.example.dish.dto.UserDTO;
import com.example.dish.common.Result;
import com.example.dish.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/users")
    public PageUtils listUsers(@RequestParam Map<String,Object> params){
        Query query = new Query(params);
        List<User> users = userService.listUsers(query);
        int count = userService.count(query);
        return new PageUtils(users,count,query.getPageSize());
    }
    @GetMapping("/users/{id}")
    public Result<User> getUserById(@PathVariable Long id)throws Exception{
        return Result.success("查询成功",userService.get(id));
    }

    @GetMapping(value="/userInfo")
    public Result<User> getUserInfo()throws Exception{
        User user = userService.getUserInfo();
        return Result.success("查找成功",user);
    }
    @RequestMapping(value="/users",method = RequestMethod.POST)
    public Result<String> saveUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
        userService.saveUser(userDTO);
        return Result.success("保存成功");
    }
    @PutMapping(value="/users")
    public Result<String> updateUser(@RequestBody User user)throws Exception {
        userService.updateUser(user);
        return Result.success("修改成功");
    }
    @RequestMapping(value="/userInfo",method = RequestMethod.POST)
    public Result<User> saveInfo(@Valid @RequestBody UserDTO userDTO) throws Exception {
        userService.saveUser(userDTO);
        return Result.success("修改成功");
    }
    @RequestMapping(value="/users/{id}",method = RequestMethod.DELETE)
    public Result<String> deleteUser(@PathVariable @Min(value=1,message = "编号不能小于1")
                                         Long id) throws Exception {
        userService.deleteUser(id);
        return Result.success("删除成功");
    }
}
