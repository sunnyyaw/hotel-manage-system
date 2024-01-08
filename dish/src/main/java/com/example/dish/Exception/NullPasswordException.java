package com.example.dish.Exception;

public class NullPasswordException extends Exception{
    public NullPasswordException(){
        super("密码不能为空");
    }
}
