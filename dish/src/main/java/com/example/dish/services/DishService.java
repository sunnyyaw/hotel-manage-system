package com.example.dish.services;

import com.example.dish.entity.BillDetailDTO;
import com.example.dish.entity.Dish;
import com.example.dish.entity.DishDetailDTO;

import java.util.List;
import java.util.Map;

public interface DishService {
    List<Dish> getAllDishes();
    Dish getDishById(Long id);
    List<Dish> getDishesByBillId(Long billId);
    BillDetailDTO getDetailsByBillId(Long billId);
    List<Dish> getDishesByKeyword(String keyword);
    List<Dish> getDishesByCategoryId(Long categoryId);
    List<DishDetailDTO> getAllDishDetails();
    int addDish(Dish dish);
    int addOrUpdateDish(Dish dish);
    int deleteDishById(Long id);
    int updateDish(Dish dish);
}
