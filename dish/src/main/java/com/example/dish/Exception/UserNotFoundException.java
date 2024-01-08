package com.example.dish.Exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(){
        super("用户不存在");
    }
}
