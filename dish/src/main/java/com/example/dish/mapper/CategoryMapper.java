package com.example.dish.mapper;

import com.example.dish.common.Query;
import com.example.dish.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {
    List<Category> listCategories(Query query);
    int count(Query query);
    Category getCategoryById(Long id);
    int addCategory(Category category);
    int updateCategory(Category category);
    int deleteCategoryById(Long id);
}
