package com.example.dish.services;

import com.example.dish.entity.*;
import com.example.dish.mapper.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService{
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private Bill_DishMapper billDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishCommentMapper dishCommentMapper;
    @Autowired
    private UserService userService;
    @Override
    public List<Dish> getAllDishes(){
        return dishMapper.getAllDishes();
    }

    @Override
    public Dish getDishById(Long id){
        return dishMapper.getDishById(id);
    }

    @Override
    public List<Dish> getDishesByBillId(Long billId){
        List<Bill_Dish> bill_dishes = billDishMapper.getDishesByBillId(billId);
        return bill_dishes.stream().map(
                bill_dish -> this.getDishById(bill_dish.getDishId())
        ).collect(Collectors.toList());
    }

    @Override
    public BillDetailDTO getDetailsByBillId(Long billId) {
        List<Bill_Dish> bill_dishes = billDishMapper.getDishesByBillId(billId);
        List<DishDetailDTO> dishDetails = bill_dishes.stream().map(bill_dish -> {
            Dish dish = this.getDishById(bill_dish.getDishId());
            return DishDetailDTO.builder().dishId(dish.getId())
                    .dishName(dish.getDishName())
                    .category(Optional.ofNullable(categoryMapper.getCategoryById(dish.getCategoryId()))
                            .map(Category::getName).orElse(null))
                    .unitPrice(dish.getUnitPrice())
                    .num(bill_dish.getNum())
                    .price(dish.getUnitPrice().multiply(BigDecimal.valueOf(bill_dish.getNum())))
                    .build();
        }).toList();
        BigDecimal totalPrice = dishDetails.stream().map(DishDetailDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add
                );
        BillDetailDTO billDetail = new BillDetailDTO();
        billDetail.setDishDetail(dishDetails);
        billDetail.setTotalPrice(totalPrice);
        return billDetail;
    }

    @Override
    public List<Dish> getDishesByKeyword(String keyword)  {
        if(Objects.isNull(keyword) || keyword.isEmpty())
            return this.getAllDishes();
        return this.getDishesByKeyword(keyword);
    }

    @Override
    public List<Dish> getDishesByCategoryId(Long categoryId)  {
        return this.getAllDishes().stream().filter(dish->
            Objects.equals(dish.getCategoryId(),categoryId)).toList();
    }

    @Override
    public List<DishDetailDTO> getAllDishDetails() {
        List<Dish> dishes = this.getAllDishes();
        return dishes.stream().map(dish -> {
            Category category = categoryMapper.getCategoryById(dish.getCategoryId());
            DishDetailDTO dishDetail = new DishDetailDTO();
            dishDetail.setDishId(dish.getId());
            dishDetail.setDishName(dish.getDishName());
            if(!Objects.isNull(category))
                dishDetail.setCategory(category.getName());
            dishDetail.setUnitPrice(dish.getUnitPrice());
            return dishDetail;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DishComment> getDishCommentsByDishId(Long dishId) {
        List<DishComment> dishComments =
                this.getAllDishComments().stream().filter(dishComment ->
                Objects.equals(dishId,dishComment.getDishId())).toList();
        dishComments.forEach(dishComment ->{
                dishComment.setUser(
                        userService.getUserById(dishComment.getUserId()));
                if(dishComment.getTime()!=null)
                    dishComment.setFormattedTime(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(dishComment.getTime()));
        });
        return dishComments;
    }

    @Override
    public void saveDishComment(DishComment dishComment)throws Exception {
        if(Objects.isNull(dishComment.getDishId())||
        Objects.isNull(this.getDishById(dishComment.getDishId())))
            throw new Exception("餐品不存在!");
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        dishComment.setUserId(userService.getUserByUsername(username).getId());
        dishComment.setTime(new Date(System.currentTimeMillis()));
        this.addDishComment(dishComment);
    }

    @Override
    public void modifyDishComment(DishComment dishComment) throws Exception {
        if(Objects.isNull(dishComment.getDishId())||
        Objects.isNull(dishComment.getId())||
        Objects.isNull(this.getDishById(dishComment.getId())))
            throw new Exception("评论不存在!");
        this.updateDishComment(dishComment);
    }

    @Override
    public void addDish(Dish dish) {
        dishMapper.addDish(dish);
    }

    @Override
    public void saveDish(Dish dish) {
        Dish d = this.getDishById(dish.getId());
        if(Objects.isNull(d))
            this.addDish(dish);
        this.updateDish(dish);
    }

    @Override
    public void deleteDishById(Long id) {
        dishMapper.deleteDishById(id);
    }

    @Override
    public void updateDish(Dish dish) {
        dishMapper.updateDish(dish);
    }

    @Override
    public List<DishComment> getAllDishComments() {
        return dishCommentMapper.getAllDishComments();
    }

    @Override
    public DishComment getDishCommentById(Long id) {
        return dishCommentMapper.getDishCommentById(id);
    }

    @Override
    public void addDishComment(DishComment dishComment) {
        dishCommentMapper.addDishComment(dishComment);
    }

    @Override
    public void updateDishComment(DishComment dishComment) {
        dishCommentMapper.updateDishComment(dishComment);
    }

    @Override
    public void deleteDishCommentById(Long id) {
        dishCommentMapper.deleteDishCommentById(id);
    }
}
