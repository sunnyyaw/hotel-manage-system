package com.example.dish.mapper;

import com.example.dish.common.Query;
import com.example.dish.entity.Setmeal_Dish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Setmeal_DishMapper {
    List<Setmeal_Dish> all(Query query);
    int count(Query query);
    Setmeal_Dish get(Long id);
    int add(Setmeal_Dish setmeal_dish);
    int update(Setmeal_Dish setmeal_dish);
    int delete(Long id);
    int deleteByQuery(Query query);
}
