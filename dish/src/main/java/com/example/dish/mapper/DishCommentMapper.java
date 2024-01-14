package com.example.dish.mapper;

import com.example.dish.entity.DishComment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DishCommentMapper {
    List<DishComment> getAllDishComments();
    DishComment getDishCommentById(Long id);
    void addDishComment(DishComment dishComment);
    void updateDishComment(DishComment dishComment);
    void deleteDishCommentById(Long id);
}
