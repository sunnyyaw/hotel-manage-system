package com.example.dish.controller;

import com.example.dish.common.PageUtils;
import com.example.dish.common.Query;
import com.example.dish.entity.*;
import com.example.dish.common.Result;
import com.example.dish.dto.BillDetailDTO;
import com.example.dish.dto.DishDetailDTO;
import com.example.dish.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/dishes")
    public PageUtils listDishes(@RequestParam Map<String,Object> params) {
        Query query = new Query(params);
        List<Dish> dishes = dishService.getAllDishes(query);
        int count = dishService.count(query);
        return new PageUtils(dishes,count,query.getPageSize());
    }
    @GetMapping("/dishDetails")
    public List<DishDetailDTO> getAllDishDetails() {
        return dishService.getAllDishDetails();
    }
    @GetMapping("/dishes/{id}")
    public Result<Dish> getDishById(@PathVariable("id") Long id)throws Exception {
        return Result.success("查询成功",dishService.get(id));
    }
    @PostMapping("/dishes")
    public Result<String> addDish(@RequestBody Dish dish) {
        dishService.saveDish(dish);
        return Result.success("保存成功");
    }
    @DeleteMapping("/dishes/{id}")
    public Result<String> deleteDish(@PathVariable("id") Long id) throws Exception {
        dishService.delete(id);
        return Result.success("删除成功");
    }
    @PutMapping( "/dishes")
    public Result<String> updateDish(@RequestBody Dish dish)throws Exception{
        dishService.updateDish(dish);
        return Result.success("修改成功");
    }
    @PutMapping("/dishesBatch")
    public Result<String> updateDishBatch(@RequestBody List<Dish> dishList)throws Exception{
        dishService.updateDishBatch(dishList);
        return Result.success("批量更新成功");
    }

    @GetMapping("/bills/{id}/dishes")
    public Result<BillDetailDTO> getBillDishes(@PathVariable("id") Long billId){
        BillDetailDTO details = dishService.getDetailsByBillId(billId);
        return Result.success("查询成功", details);
    }
    @GetMapping("/dishes/{id}/comments")
    public Result<List<DishComment>> getDishComments(@PathVariable("id") Long dishId)throws Exception{
        List<DishComment> dishComments = dishService.getDishCommentsByDishId(dishId);
        return Result.success( "查询成功", dishComments);
    }
    @PostMapping("/dishes/{id}/comments")
    public Result<String> saveDishComment(@PathVariable("id") Long dishId,
                                          @RequestBody DishComment dishComment) throws Exception {
        dishComment.setDishId(dishId);
        dishService.saveDishComment(dishComment);
        return Result.success("保存成功");
    }
    @PutMapping("/dishes/{dishId}/comments/{id}")
    public Result<String> updateDishComment(@PathVariable("dishId") Long dishId,
                                          @PathVariable("id")Long id,
                                          @RequestBody DishComment dishComment) throws Exception {
        dishComment.setId(id);
        dishComment.setDishId(dishId);
        dishService.modifyDishComment(dishComment);
        return Result.success("修改成功");
    }
    @DeleteMapping("/dishes/{dishId}/comments/{id}")
    public Result<String> deleteDishComment(@PathVariable("id")Long id){
        dishService.deleteDishCommentById(id);
        return Result.success("删除成功");
    }

}
