package com.example.dish.services;

import com.example.dish.common.Query;
import com.example.dish.dto.BillDetailDTO;
import com.example.dish.entity.Dish;
import com.example.dish.entity.DishComment;
import com.example.dish.dto.DishDetailDTO;

import java.util.List;

public interface DishService {
    List<Dish> getAllDishes(Query query) ;
    int count(Query query);
    Dish getDishById(Long id);
    Dish get(Long id)throws Exception;
    void delete(Long id) throws Exception;
    BillDetailDTO getDetailsByBillId(Long billId);
    List<DishDetailDTO> getAllDishDetails()  ;
    List<DishComment> getDishCommentsByDishId(Long dishId);
    void saveDishComment(DishComment dishComment)throws Exception;
    void modifyDishComment(DishComment dishComment)throws Exception;
    void deleteDishCommentById(Long id);
    void saveDish(Dish dish);
    void updateDish(Dish dish);
}
