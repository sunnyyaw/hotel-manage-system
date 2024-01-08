package com.example.dish.controller;

import com.example.dish.entity.*;
import com.example.dish.result.Result;
import com.example.dish.services.Bill_DishService;
import com.example.dish.services.CategoryService;
import com.example.dish.services.CoverService;
import com.example.dish.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private CoverService coverService;

    @RequestMapping(value = "/dishes",method = RequestMethod.GET)
    public List<Dish> getAllDishes(@RequestParam(name="keyword",required = false)String keyword){
        return dishService.getDishesByKeyword(keyword);
    }
    @RequestMapping(value = "/dishDetails",method = RequestMethod.GET)
    public List<DishDetailDTO> getAllDishDetails(){
        return dishService.getAllDishDetails();
    }
    @RequestMapping(value = "/dishes/{id}",method = RequestMethod.GET)
    public Dish getDishById(@PathVariable("id") Long id){
        return dishService.getDishById(id);
    }
    @RequestMapping(value = "/dishes",method = RequestMethod.POST)
    public int addDish(@RequestBody Dish dish){
        return dishService.addOrUpdateDish(dish);
    }
    @RequestMapping(value = "/dishes/{id}",method = RequestMethod.DELETE)
    public int deleteDish(@PathVariable("id") Long id){
        return dishService.deleteDishById(id);
    }
    @RequestMapping(value = "/dishes/{id}",method = RequestMethod.PUT)
    public int updateDish(@PathVariable Long id,@RequestBody Dish dish){
        dish.setId(id);
        return dishService.updateDish(dish);
    }
    @RequestMapping(value = "/categories/{categoryId}/dishes",method = RequestMethod.GET)
    public List<Dish> getDishesByCategoryId(@PathVariable Long categoryId){
        return dishService.getDishesByCategoryId(categoryId);
    }

    @RequestMapping(value = "/bills/{id}/dishes",method = RequestMethod.GET)
    public Result<BillDetailDTO> getBillDishes(@PathVariable("id") Long billId){
        BillDetailDTO details = dishService.getDetailsByBillId(billId);
        return new Result<>(200, "OK", details);
    }

    @RequestMapping(value = "/covers",method = RequestMethod.POST)
    public String coversUpload(MultipartFile file) throws Exception{
        return coverService.coversUpload(file);
    }
}
