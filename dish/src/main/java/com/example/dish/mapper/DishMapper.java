package com.example.dish.mapper;

import com.example.dish.entity.Dish;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DishMapper {
    List<Dish> getAllDishes() ;
    Dish getDishById(Long id) ;
    List<Dish> getDishesByKeyword(String keyword) ;
    List<Dish> getDishesByCategoryId(Long categoryId) ;
    void addDish(Dish dish);
    void deleteDishById(Long id);
    void updateDish(Dish dish);
}
