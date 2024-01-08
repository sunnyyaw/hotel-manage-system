package com.example.dish.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
@Data
public class Bill {
    private Long id;
    private Long customerId;
    private Timestamp genTime;
}
