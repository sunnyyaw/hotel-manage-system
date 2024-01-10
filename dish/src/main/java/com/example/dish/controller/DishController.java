package com.example.dish.controller;

import com.example.dish.entity.*;
import com.example.dish.result.Result;
import com.example.dish.services.CoverService;
import com.example.dish.services.DishService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private CoverService coverService;

    @RequestMapping(value = "/dishes",method = RequestMethod.GET)
    public List<Dish> getAllDishes(@RequestParam(name="keyword",required = false)String keyword) {
        return dishService.getDishesByKeyword(keyword);
    }
    @RequestMapping(value = "/dishDetails",method = RequestMethod.GET)
    public List<DishDetailDTO> getAllDishDetails() {
        return dishService.getAllDishDetails();
    }
    @RequestMapping(value = "/dishes/{id}",method = RequestMethod.GET)
    public Dish getDishById(@PathVariable("id") Long id) {
        return dishService.getDishById(id);
    }
    @RequestMapping(value = "/dishes",method = RequestMethod.POST)
    public void addDish(@RequestBody Dish dish) {
        dishService.saveDish(dish);
    }
    @RequestMapping(value = "/dishes/{id}",method = RequestMethod.DELETE)
    public void deleteDish(@PathVariable("id") Long id){
        dishService.deleteDishById(id);
    }
    @RequestMapping(value = "/dishes/{id}",method = RequestMethod.PUT)
    public void updateDish(@PathVariable Long id,@RequestBody Dish dish){
        dish.setId(id);
        dishService.updateDish(dish);
    }
    @RequestMapping(value = "/categories/{categoryId}/dishes",method = RequestMethod.GET)
    public List<Dish> getDishesByCategoryId(@PathVariable Long categoryId) {
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
