package com.example.dish.controller;

import com.example.dish.Exception.PasswordCollideException;
import com.example.dish.Exception.UserNotFoundException;
import com.example.dish.entity.User;
import com.example.dish.entity.UserDTO;
import com.example.dish.result.Result;
import com.example.dish.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value="/users",method = RequestMethod.GET)
    public Result<List<UserDTO>> getAllUsers(){
        List<UserDTO> users = userService.getAllUsers();
        return new Result<>(200,"查询成功",users);
    }

    @RequestMapping(value="/userInfo",method = RequestMethod.GET)
    public Result<UserDTO> getUserInfo(){
        try{
            UserDTO user = userService.getUserInfo();
            return new Result<>(200,"查找成功",user);
        }catch (UserNotFoundException e){
            return new Result<>(404,e.getMessage());
        }
    }
    @RequestMapping(value="/users",method = RequestMethod.POST)
    public Result<String> saveUser(@Valid @RequestBody UserDTO userDTO){
        try{
            userService.saveUser(userDTO);
            return new Result<>(200,"保存成功");
        } catch (Exception e) {
            return new Result<>(400,e.getMessage());
        }
    }
    @RequestMapping(value="/userInfo",method = RequestMethod.POST)
    public Result<User> modifyPassword(@Valid @RequestBody UserDTO userDTO){
        try{
            userService.modifyPassword(userDTO.getPassword());
            return new Result<>(200,"修改成功");
        }catch (Exception e) {
            return new Result<>(400, e.getMessage());
        }
    }
    @RequestMapping(value="/users/{id}",method = RequestMethod.DELETE)
    public Result<String> deleteUser(@PathVariable @Min(value=1,message = "编号不能小于1")
                                         Long id){
        try{
            userService.deleteUser(id);
            return new Result<>(200,"删除成功");
        } catch (Exception e) {
            return new Result<>(400,e.getMessage());
        }
    }
}
