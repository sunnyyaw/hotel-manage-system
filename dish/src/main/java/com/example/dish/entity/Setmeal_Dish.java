package com.example.dish.entity;

import lombok.Data;

@Data
public class Setmeal_Dish {
    private Long id;
    private Long dishId;
    private Long setmealId;
    private Integer num;
}
