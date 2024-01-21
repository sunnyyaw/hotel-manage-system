package com.example.dish.mapper;

import com.example.dish.common.Query;
import com.example.dish.entity.Dish;
import com.example.dish.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DishFlavorMapper {
    List<DishFlavor> all(Query query);
    int count(Query query);
    void add(DishFlavor dishFlavor);
    void update(DishFlavor dishFlavor);
    void delete(Long id);

}
