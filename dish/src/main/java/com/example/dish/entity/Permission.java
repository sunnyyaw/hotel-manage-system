package com.example.dish.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Permission {
    private Long id;
    @NotBlank(message="接口地址不得为空")
    private String url;
    private String description;
}
