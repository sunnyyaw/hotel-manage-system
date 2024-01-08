package com.example.dish.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class BillFormDTO {
    private Long customerId;
    private List<OrderDTO> orders;
}
