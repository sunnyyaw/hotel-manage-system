package com.example.dish.entity;

import com.example.dish.common.BillStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;


@Data
public class Bill {
    private Long id;
    private Long customerId;
    private Long userId;
    private Timestamp genTime;
    private Integer status;

    private List<Bill_Dish> orders;
    private Customer customer;
    private User user;
}
