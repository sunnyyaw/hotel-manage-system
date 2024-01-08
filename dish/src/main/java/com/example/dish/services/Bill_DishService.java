package com.example.dish.services;

import com.example.dish.entity.Bill_Dish;

import java.util.List;

public interface Bill_DishService {
    List<Bill_Dish> getAllBill_Dish();
    Bill_Dish getBill_DishById(Long billId,Long dishId);
    List<Bill_Dish> getBill_DishByBillId(Long billId);
    int addBill_Dish(Bill_Dish bill_dish);
    int deleteBill_Dish(Bill_Dish bill_dish);
}
