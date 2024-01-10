package com.example.dish.services;

import com.example.dish.entity.BillDetailDTO;
import com.example.dish.entity.Dish;
import com.example.dish.entity.DishDetailDTO;
import com.example.dish.mapper.DishMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface DishService extends DishMapper {
    List<Dish> getAllDishes() ;
    Dish getDishById(Long id) ;
    List<Dish> getDishesByBillId(Long billId) ;
    BillDetailDTO getDetailsByBillId(Long billId);
    List<Dish> getDishesByKeyword(String keyword)  ;
    List<Dish> getDishesByCategoryId(Long categoryId)  ;
    List<DishDetailDTO> getAllDishDetails()  ;
    void addDish(Dish dish);
    void saveDish(Dish dish)  ;
    void deleteDishById(Long id);
    void updateDish(Dish dish);
}
