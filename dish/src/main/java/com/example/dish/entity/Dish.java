package com.example.dish.entity;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
public class Dish extends RepresentationModel<Dish> {
    private Long id;
    private String dishName;
    private Long categoryId;
    private String description;
    private BigDecimal unitPrice;
    private String cover;
    private Category category;
    private String averageScore;
}
