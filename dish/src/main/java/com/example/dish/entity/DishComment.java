package com.example.dish.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DishComment {
    private Long id;
    private Long dishId;
    private Long userId;
    private Integer rate;
    private String comment;
    private Date time;

    private User user;
}
