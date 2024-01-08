package com.example.dish.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Role {
    private Long id;
    private String roleName;
}
