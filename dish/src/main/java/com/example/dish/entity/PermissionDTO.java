package com.example.dish.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PermissionDTO extends Permission{
    private Long id;
    @NotBlank(message="接口地址不得为空")
    private String url;
    private String description;
}
