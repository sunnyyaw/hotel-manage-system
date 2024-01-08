package com.example.dish.Exception;

public class UserExistException extends Exception{
    public UserExistException(){
        super("用户已存在");
    }
}
