package com.example.dish.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Setmeal {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private String cover;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Category category;
    private List<Setmeal_Dish> setmeal_dishList;
    private List<Dish> dishList;
}
