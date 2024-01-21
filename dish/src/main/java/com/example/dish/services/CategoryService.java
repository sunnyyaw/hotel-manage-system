package com.example.dish.services;

import com.example.dish.common.Query;
import com.example.dish.entity.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    int count(Query query);
    List<Category> getCategories(Query query);
    Category getCategoryById(Long id) throws Exception;
    void addCategory(Category category)throws Exception;
    void deleteCategoryById(Long id)throws Exception;

}
