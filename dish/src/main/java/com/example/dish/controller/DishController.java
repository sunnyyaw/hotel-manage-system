package com.example.dish.controller;

import com.example.dish.entity.*;
import com.example.dish.common.Result;
import com.example.dish.services.CoverService;
import com.example.dish.services.DishService;
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
    public List<Dish> getAllDishes() {
        return dishService.getAllDishes();
    }
    @RequestMapping(value = "/dishDetails",method = RequestMethod.GET)
    public List<DishDetailDTO> getAllDishDetails() {
        return dishService.getAllDishDetails();
    }
    @RequestMapping(value = "/dishes/{id}",method = RequestMethod.GET)
    public Result<Dish> getDishById(@PathVariable("id") Long id) {
        return new Result<>(200,"OK",dishService.getDishById(id));
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
    @RequestMapping(value = "/dishes/{id}/comments",method = RequestMethod.GET)
    public Result<List<DishComment>> getDishComments(@PathVariable("id") Long dishId){
        List<DishComment> dishComments = dishService.getDishCommentsByDishId(dishId);
        return new Result<>(200, "OK", dishComments);
    }
    @RequestMapping(value = "/dishes/{id}/comments",method = RequestMethod.POST)
    public Result<String> saveDishComment(@PathVariable("id") Long dishId,
                                          @RequestBody DishComment dishComment){
        try {
            dishComment.setDishId(dishId);
            dishService.saveDishComment(dishComment);
            return new Result<>(200, "保存成功");
        }catch (Exception e){
            return new Result<>(500,e.getMessage());
        }
    }
    @RequestMapping(value = "/dishes/{dishId}/comments/{id}",method = RequestMethod.PUT)
    public Result<String> updateDishComment(@PathVariable("dishId") Long dishId,
                                          @PathVariable("id")Long id,
                                          @RequestBody DishComment dishComment){
        try {
            dishComment.setId(id);
            dishComment.setDishId(dishId);
            dishService.modifyDishComment(dishComment);
            return new Result<>(200, "修改成功");
        }catch (Exception e){
            return new Result<>(500,e.getMessage());
        }
    }
    @RequestMapping(value = "/dishes/{dishId}/comments/{id}",method = RequestMethod.DELETE)
    public Result<String> deleteDishComment(@PathVariable("id")Long id){
        try {
            dishService.deleteDishCommentById(id);
            return new Result<>(200, "删除成功");
        }catch (Exception e){
            return new Result<>(500,e.getMessage());
        }
    }

    @RequestMapping(value = "/covers",method = RequestMethod.POST)
    public String coversUpload(MultipartFile file) throws Exception{
        return coverService.coversUpload(file);
    }
}
