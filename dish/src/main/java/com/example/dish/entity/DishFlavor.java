package com.example.dish.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DishFlavor {
    private Long id;
    private Long dishId;
    private String name;
    private String value;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
