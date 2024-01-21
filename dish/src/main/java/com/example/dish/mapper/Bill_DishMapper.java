package com.example.dish.mapper;

import com.example.dish.common.Query;
import com.example.dish.entity.Bill_Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface Bill_DishMapper {
    List<Bill_Dish> listBill_Dishes(Query query);
    int count(Query query);
    List<Bill_Dish> getAllBill_Dish();
    Bill_Dish getBill_DishById(@Param("billId") Long billId,@Param("dishId") Long dishId);
    List<Bill_Dish> getDishesByBillId(Long billId);
    int addBill_Dish(Bill_Dish bill_dish);
    int deleteBill_Dish(Bill_Dish bill_dish);
    void deleteBill_DishByBillId(Long billId);
}
