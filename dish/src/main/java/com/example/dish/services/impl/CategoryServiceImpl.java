package com.example.dish.services.impl;

import com.example.dish.common.Query;
import com.example.dish.entity.Category;
import com.example.dish.mapper.CategoryMapper;
import com.example.dish.mapper.DishMapper;
import com.example.dish.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public int count(Query query) {
        return categoryMapper.count(query);
    }

    @Override
    public List<Category> getCategories(Query query) {
        return categoryMapper.listCategories(query);
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {
        Category category = categoryMapper.getCategoryById(id);
        if(category == null)
            throw new Exception("类别不存在");
        return categoryMapper.getCategoryById(id);
    }

    @Override
    public void addCategory(Category category) throws Exception{
        Category c = categoryMapper.getCategoryById(category.getId());
        int result = 0;
        if(Objects.isNull(c))
            result = categoryMapper.addCategory(category);
        else result = categoryMapper.updateCategory(category);
        if(result==0)throw new Exception("保存失败");
    }

    @Override
    public void deleteCategoryById(Long id)throws Exception {
        if(categoryMapper.getCategoryById(id) == null)
            throw new Exception("找不到类别");
        Query query = new Query();
        query.put("categoryId",id);
        int count = dishMapper.count(query);
        if(count > 0)
            throw new Exception("类别有菜品关联，不能删除");
        categoryMapper.deleteCategoryById(id);
    }
}
