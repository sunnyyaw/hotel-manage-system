package com.example.dish.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DishDetailDTO {
    private Long dishId;
    private String dishName;
    private String category;
    private BigDecimal unitPrice = BigDecimal.ZERO;
    private Integer num;
    private BigDecimal price = BigDecimal.ZERO;
}
