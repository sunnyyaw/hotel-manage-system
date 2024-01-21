package com.example.dish.controller;

import com.example.dish.common.PageUtils;
import com.example.dish.common.Query;
import com.example.dish.common.Result;
import com.example.dish.entity.Bill;
import com.example.dish.entity.Category;
import com.example.dish.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/categories")
    public PageUtils getCategories(@RequestParam Map<String,Object> params){
        Query query = new Query(params);
        List<Category> categories = categoryService.getCategories(query);
        int count = categoryService.count(query);
        return new PageUtils(categories,count,query.getPageSize());
    }
    @GetMapping("/categories/{id}")
    public Result<Category> getCategoryById(@PathVariable Long id) throws Exception {
        return Result.success("查询成功",categoryService.getCategoryById(id));
    }
    @PostMapping("/categories")
    public Result<String> addCategory(@RequestBody Category category) throws Exception {
        categoryService.addCategory(category);
        return Result.success("保存成功");
    }
    @DeleteMapping("/categories/{id}")
    public Result<String> deleteCategory(@PathVariable("id")Long id) throws Exception {
        categoryService.deleteCategoryById(id);
        return Result.success("删除成功");
    }
}
