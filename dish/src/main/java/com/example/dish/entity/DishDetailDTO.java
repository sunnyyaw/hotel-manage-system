package com.example.dish.entity;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class DishDetailDTO {
    private Long dishId;
    private String dishName;
    private String category;
    private BigDecimal unitPrice = BigDecimal.ZERO;
    private Integer num;
    private BigDecimal price = BigDecimal.ZERO;
}
