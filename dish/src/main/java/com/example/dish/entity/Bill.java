package com.example.dish.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class Bill {
    private Long id;
    private Long customerId;
    private Long userId;
    private Timestamp genTime;
    private List<Bill_Dish> orders;
    private Customer customer;
    private User user;
}
