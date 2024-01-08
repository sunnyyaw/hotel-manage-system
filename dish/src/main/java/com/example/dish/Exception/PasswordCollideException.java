package com.example.dish.Exception;

public class PasswordCollideException extends Exception{
    public PasswordCollideException(){
        super("密码不能和当前密码相同");
    }
}
