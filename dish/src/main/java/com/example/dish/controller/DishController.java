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
        return Result.success("查询成功",dishService.getDishById(id));
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
        return Result.success("查询成功", details);
    }
    @RequestMapping(value = "/dishes/{id}/comments",method = RequestMethod.GET)
    public Result<List<DishComment>> getDishComments(@PathVariable("id") Long dishId){
        List<DishComment> dishComments = dishService.getDishCommentsByDishId(dishId);
        return Result.success( "查询成功", dishComments);
    }
    @RequestMapping(value = "/dishes/{id}/comments",method = RequestMethod.POST)
    public Result<String> saveDishComment(@PathVariable("id") Long dishId,
                                          @RequestBody DishComment dishComment) throws Exception {
        dishComment.setDishId(dishId);
        dishService.saveDishComment(dishComment);
        return Result.success("保存成功");
    }
    @RequestMapping(value = "/dishes/{dishId}/comments/{id}",method = RequestMethod.PUT)
    public Result<String> updateDishComment(@PathVariable("dishId") Long dishId,
                                          @PathVariable("id")Long id,
                                          @RequestBody DishComment dishComment) throws Exception {
        dishComment.setId(id);
        dishComment.setDishId(dishId);
        dishService.modifyDishComment(dishComment);
        return Result.success("修改成功");
    }
    @RequestMapping(value = "/dishes/{dishId}/comments/{id}",method = RequestMethod.DELETE)
    public Result<String> deleteDishComment(@PathVariable("id")Long id){
        dishService.deleteDishCommentById(id);
        return Result.success("删除成功");
    }

    @RequestMapping(value = "/covers",method = RequestMethod.POST)
    public String coversUpload(MultipartFile file) throws Exception{
        return coverService.coversUpload(file);
    }
}
