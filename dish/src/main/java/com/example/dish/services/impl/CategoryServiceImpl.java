package com.example.dish.services.impl;

import com.example.dish.entity.Category;
import com.example.dish.entity.Dish;
import com.example.dish.mapper.CategoryMapper;
import com.example.dish.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<Category> getCategoriesByKeyword(String keyword) {
        return categoryMapper.getCategoriesByKeyword(keyword);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryMapper.getCategoryById(id);
    }

    @Override
    public int addCategory(Category category) {
        Category c = categoryMapper.getCategoryById(category.getId());
        if(Objects.isNull(c))
            return categoryMapper.addCategory(category);
        return categoryMapper.updateCategory(category);
    }

    @Override
    public int deleteCategoryById(Long id) {
        return categoryMapper.deleteCategoryById(id);
    }
}
