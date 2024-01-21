package com.example.dish.entity;

import lombok.Data;

@Data
public class Category {
    private Long id;
    private Integer type;
    private Integer sort;
    private String name;

    private Integer association;
}
