package com.example.dish.services;

import com.example.dish.entity.BillDetailDTO;
import com.example.dish.entity.Dish;
import com.example.dish.entity.DishComment;
import com.example.dish.entity.DishDetailDTO;
import com.example.dish.mapper.DishCommentMapper;
import com.example.dish.mapper.DishMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface DishService extends DishMapper, DishCommentMapper {
    List<Dish> getAllDishes() ;
    Dish getDishById(Long id) ;
    List<Dish> getDishesByBillId(Long billId) ;
    BillDetailDTO getDetailsByBillId(Long billId);
    List<Dish> getDishesByCategoryId(Long categoryId)  ;
    List<DishDetailDTO> getAllDishDetails()  ;
    List<DishComment> getDishCommentsByDishId(Long dishId);
    void saveDishComment(DishComment dishComment)throws Exception;
    void modifyDishComment(DishComment dishComment)throws Exception;
    void addDish(Dish dish);
    void saveDish(Dish dish)  ;
    void deleteDishById(Long id);
    void updateDish(Dish dish);
}
