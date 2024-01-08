package com.example.dish.controller;

import com.example.dish.entity.Category;
import com.example.dish.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @RequestMapping(value="/categories",method = RequestMethod.GET)
    List<Category> getCategoriesByKeyword(@RequestParam(name="keyword",required = false)String keyword){
        return categoryService.getCategoriesByKeyword(keyword);
    }
    @RequestMapping(value="/categories",method = RequestMethod.POST)
    int addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }
    @RequestMapping(value="/categories/{id}",method = RequestMethod.DELETE)
    int deleteCategory(@PathVariable("id")Long id){
        return categoryService.deleteCategoryById(id);
    }
}
