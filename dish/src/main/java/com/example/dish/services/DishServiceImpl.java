package com.example.dish.services;

import com.example.dish.entity.*;
import com.example.dish.mapper.Bill_DishMapper;
import com.example.dish.mapper.CategoryMapper;
import com.example.dish.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService{
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private Bill_DishMapper billDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<Dish> getAllDishes() {
        return dishMapper.getAllDishes();
    }

    @Override
    public Dish getDishById(Long id) {
        return dishMapper.getDishById(id);
    }

    @Override
    public List<Dish> getDishesByBillId(Long billId) {
        List<Bill_Dish> bill_dishes = billDishMapper.getDishesByBillId(billId);
        return bill_dishes.stream().map(
                bill_dish -> dishMapper.getDishById(bill_dish.getDishId())
        ).collect(Collectors.toList());
    }

    @Override
    public BillDetailDTO getDetailsByBillId(Long billId) {
        List<Bill_Dish> bill_dishes = billDishMapper.getDishesByBillId(billId);
        List<DishDetailDTO> dishDetails = bill_dishes.stream().map(bill_dish -> {
            DishDetailDTO dishDetail = new DishDetailDTO();
            Dish dish = dishMapper.getDishById(bill_dish.getDishId());
            Category category = categoryMapper.getCategoryById(dish.getCategoryId());
            int num = bill_dish.getNum();
            BigDecimal price = dish.getUnitPrice().multiply(BigDecimal.valueOf(num));
            dishDetail.setDishId(dish.getId());
            dishDetail.setDishName(dish.getDishName());
            if(!Objects.isNull(category))
                dishDetail.setCategory(category.getName());
            dishDetail.setUnitPrice(dish.getUnitPrice());
            dishDetail.setNum(num);
            dishDetail.setPrice(price);
            return dishDetail;
        }).toList();
        BigDecimal totalPrice = dishDetails.stream()
                .reduce(new DishDetailDTO(),(result,item)->{
                    DishDetailDTO dishDetail = new DishDetailDTO();
                    BigDecimal price = result.getPrice().add(item.getPrice());
                    dishDetail.setPrice(price);
                    return dishDetail;
                }).getPrice();
        BillDetailDTO billDetail = new BillDetailDTO();
        billDetail.setDishDetail(dishDetails);
        billDetail.setTotalPrice(totalPrice);
        return billDetail;
    }

    @Override
    public List<Dish> getDishesByKeyword(String keyword) {
        if(Objects.isNull(keyword) || keyword.isEmpty())
            return dishMapper.getAllDishes();
        return dishMapper.getDishesByKeyword(keyword);
    }

    @Override
    public List<Dish> getDishesByCategoryId(Long categoryId) {
        return dishMapper.getDishesByCategoryId(categoryId);
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
    public int addDish(Dish dish) {
        return dishMapper.addDish(dish);
    }

    @Override
    public int addOrUpdateDish(Dish dish) {
        Dish d = dishMapper.getDishById(dish.getId());
        if(Objects.isNull(d))
            return dishMapper.addDish(dish);
        return dishMapper.updateDish(dish);
    }

    @Override
    public int deleteDishById(Long id) {
        return dishMapper.deleteDishById(id);
    }

    @Override
    public int updateDish(Dish dish) {
        return dishMapper.updateDish(dish);
    }
}
