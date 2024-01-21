package com.example.dish.services.impl;

import com.example.dish.common.Query;
import com.example.dish.entity.*;
import com.example.dish.dto.BillDetailDTO;
import com.example.dish.dto.DishDetailDTO;
import com.example.dish.mapper.*;
import com.example.dish.services.DishService;
import com.example.dish.services.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {
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
    public int count(Query query) {
        return dishMapper.count(query);
    }

    @Override
    public List<Dish> getAllDishes(Query query){
        return dishMapper.listDishes(query).stream().peek(dish->{
            Query query1 = new Query();
            query1.put("dishId",dish.getId());
            dish.setBillNum(billDishMapper.count(query1));
            double score = dishCommentMapper.getAllDishComments().stream()
                    .filter(dishComment -> Objects.equals(dishComment.getDishId(),dish.getId()))
                    .mapToInt(DishComment::getRate).summaryStatistics().getAverage();
            dish.setAverageScore(String.format("%.2f",score));
        }).toList();
    }

    @Override
    public Dish getDishById(Long id){;
        return dishMapper.getDishById(id);
    }

    @Override
    public Dish get(Long id) throws Exception {
        Dish dish = dishMapper.getDishById(id);
        if(dish==null)
            throw new Exception("菜品不存在");
        double score = dishCommentMapper.getAllDishComments().stream()
                .filter(dishComment -> Objects.equals(dishComment.getDishId(),dish.getId()))
                .mapToInt(DishComment::getRate).summaryStatistics().getAverage();
        dish.setAverageScore(String.format("%.2f",score));
        return dishMapper.getDishById(id);
    }

    @Override
    public void delete(Long id) throws Exception {
        if(dishMapper.getDishById(id)==null)
            throw new Exception("菜品不存在");
        Query query = new Query();
        query.put("dishId",id);
        int count = billDishMapper.count(query);
        if(count > 0)
            throw new Exception("有账单与此菜品关联，不得删除");
        dishMapper.deleteDishById(id);
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
    public List<DishDetailDTO> getAllDishDetails() {
        List<Dish> dishes = dishMapper.getAllDishes();
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
    public List<DishComment> getDishCommentsByDishId(Long dishId){
        List<DishComment> dishComments =
                dishCommentMapper.getAllDishComments().stream().filter(dishComment ->
                Objects.equals(dishId,dishComment.getDishId())).toList();
        dishComments.forEach(dishComment ->
            dishComment.setUser(userService.getUserById(dishComment.getUserId())));
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
        dishCommentMapper.addDishComment(dishComment);
    }

    @Override
    public void modifyDishComment(DishComment dishComment) throws Exception {
        if(Objects.isNull(dishComment.getDishId())||
        Objects.isNull(dishComment.getId())||
        Objects.isNull(dishMapper.getDishById(dishComment.getId())))
            throw new Exception("评论不存在!");
        dishCommentMapper.updateDishComment(dishComment);
    }

    @Override
    public void saveDish(Dish dish) {
        Dish d = this.getDishById(dish.getId());
        if(Objects.isNull(d))
            dishMapper.addDish(dish);
        dishMapper.updateDish(dish);
    }

    @Override
    public void updateDish(Dish dish) {
        dishMapper.updateDish(dish);
    }

    @Override
    public void deleteDishCommentById(Long id) {
        dishCommentMapper.deleteDishCommentById(id);
    }
}
