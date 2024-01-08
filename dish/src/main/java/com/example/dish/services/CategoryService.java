package com.example.dish.services;

import com.example.dish.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoriesByKeyword(String keyword);
    Category getCategoryById(Long id);
    int addCategory(Category category);
    int deleteCategoryById(Long id);

}
