package com.example.dish.services;

import com.example.dish.common.Query;
import com.example.dish.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listCategories(Query query);
    int count(Query query);
    Category getCategoryById(Long id) throws Exception;
    void addCategory(Category category)throws Exception;
    void deleteCategoryById(Long id)throws Exception;

}
