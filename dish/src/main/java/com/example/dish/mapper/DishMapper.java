package com.example.dish.mapper;

import com.example.dish.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DishMapper {
    List<Dish> getAllDishes();
    Dish getDishById(Long id);
    List<Dish> getDishesByKeyword(String keyword);
    List<Dish> getDishesByCategoryId(Long categoryId);
    int addDish(Dish dish);
    int deleteDishById(Long id);
    int updateDish(Dish dish);
}
