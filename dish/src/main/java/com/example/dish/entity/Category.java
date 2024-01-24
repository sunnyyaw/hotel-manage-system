package com.example.dish.entity;

import lombok.Data;

import java.util.List;

@Data
public class Category {
    private Long id;
    private Integer type;
    private Integer sort;
    private String name;

    private List<Dish> dishList;
    private List<Setmeal> setmealList;
}
