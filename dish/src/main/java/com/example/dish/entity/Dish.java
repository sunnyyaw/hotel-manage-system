package com.example.dish.entity;

import lombok.*;

import java.math.BigDecimal;


@Data
public class Dish{
    private Long id;
    private String dishName;
    private Long categoryId;
    private String description;
    private BigDecimal unitPrice;
    private String cover;
    private Integer status;

    private Category category;
    private Integer billNum;
    private String averageScore;
}
