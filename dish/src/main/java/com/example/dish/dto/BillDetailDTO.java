package com.example.dish.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class BillDetailDTO {
    private List<DishDetailDTO> dishDetail;
    private BigDecimal totalPrice;
}
