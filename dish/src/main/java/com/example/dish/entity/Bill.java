package com.example.dish.entity;

import com.example.dish.common.Status;
import lombok.Data;

import java.sql.Timestamp;
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
    private Status status;
}
