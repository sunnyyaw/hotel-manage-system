package com.example.dish.entity;

import lombok.Data;

@Data
public class Customer {
    private Long id;
    private String description;

    private Integer association;
}
