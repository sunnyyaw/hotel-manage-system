package com.example.dish.result;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result(int code) {
        this.code = code;
    }
    public Result(int code,String message) {
        this.code = code;
        this.message = message;
    }
    public Result(int code,String message,T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
