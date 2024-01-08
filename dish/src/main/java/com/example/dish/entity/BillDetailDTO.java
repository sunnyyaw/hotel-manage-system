package com.example.dish.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class BillDetailDTO {
    private List<DishDetailDTO> dishDetail;
    private BigDecimal totalPrice;
}
