package com.example.dish.entity;

import lombok.Data;

@Data
public class Bill_Dish {
    private Long billId;
    private Long dishId;
    private Integer num;
}
