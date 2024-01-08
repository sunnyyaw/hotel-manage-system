package com.example.dish.controller;

import com.example.dish.Exception.UserExistException;
import com.example.dish.entity.User;
import com.example.dish.entity.UserDTO;
import com.example.dish.result.Result;
import com.example.dish.services.UserService;
import jakarta.validation.Valid;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class LoginController {
    @Autowired
    private UserService userService;
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public Result<String> login(@Valid @RequestBody UserDTO userForm){
        try{
            userService.login(userForm);
        }catch (AuthenticationException e){
            return new Result<>(401,e.getMessage());
        }
        return new Result<>(200,"登录成功");
    }
    @RequestMapping(value="/register",method = RequestMethod.POST)
    public Result<String> register(@Valid @RequestBody UserDTO user){
        try{
            userService.register(user);
        } catch (Exception e) {
            return new Result<>(400,e.getMessage());
        }
        return new Result<>(200,"注册成功");
    }
    @RequestMapping(value="/logout",method = RequestMethod.GET)
    public Result<String> logout(){
        userService.logout();
        return new Result<>(200,"登出成功");
    }
    @RequestMapping(value="/authentication",method = RequestMethod.GET)
    public String authentication(){
        return "身份认证成功";
    }
}
