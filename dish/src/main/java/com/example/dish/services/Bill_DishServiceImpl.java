package com.example.dish.services;

import com.example.dish.entity.Bill_Dish;
import com.example.dish.mapper.Bill_DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class Bill_DishServiceImpl implements  Bill_DishService{
    @Autowired
    private Bill_DishMapper billDishMapper;
    @Override
    public List<Bill_Dish> getAllBill_Dish() {
        return billDishMapper.getAllBill_Dish();
    }

    @Override
    public Bill_Dish getBill_DishById(Long billId, Long dishId) {
        return billDishMapper.getBill_DishById(billId,dishId);
    }

    @Override
    public List<Bill_Dish> getBill_DishByBillId(Long billId) {
        return billDishMapper.getDishesByBillId(billId);
    }

    @Override
    public int addBill_Dish(Bill_Dish bill_dish) {
        return billDishMapper.addBill_Dish(bill_dish);
    }

    @Override
    public int deleteBill_Dish(Bill_Dish bill_dish) {
        return billDishMapper.deleteBill_Dish(bill_dish);
    }
}
