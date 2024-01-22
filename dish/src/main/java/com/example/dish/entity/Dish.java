package com.example.dish.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Data
public class Dish{
    private Long id;
    private String dishName;
    private Long categoryId;
    private String description;
    private BigDecimal unitPrice;
    private String cover;
    private Integer status;

    private List<DishFlavor> dishFlavorList;
    private Category category;
    private Integer billNum;
    private String averageScore;
}
