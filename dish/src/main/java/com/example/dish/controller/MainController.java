package com.example.dish.controller;

import com.example.dish.entity.Bill;
import com.example.dish.entity.BillFormDTO;
import com.example.dish.entity.Bill_Dish;
import com.example.dish.services.BillService;
import com.example.dish.services.Bill_DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {
    @Autowired
    private BillService billService;
    @Autowired
    private Bill_DishService billDishService;
    @RequestMapping("/index")
    public String hello(){
        return "你好";
    }
    @RequestMapping(value = "/bills",method = RequestMethod.GET)
    public List<Bill> getAllBills(){
        return billService.getAllBills();
    }
    @RequestMapping(value = "/customers/{id}/bills",method = RequestMethod.GET)
    public List<Bill> getBillsByCustomerId(@PathVariable("id") Long customerId){
        return billService.getBillsByCustomerId(customerId);
    }
    @RequestMapping(value = "/bills/{id}",method = RequestMethod.GET)
    public Bill getBillById(@PathVariable("id") Long id){
        return billService.getBillById(id);
    }
    @RequestMapping(value = "/bills",method = RequestMethod.POST)
    public int addBill(@RequestBody BillFormDTO billForm){
        return billService.addBill(billForm);
    }
    @RequestMapping(value = "/bills/{id}",method = RequestMethod.DELETE)
    public int deleteBill(@PathVariable Long id){
        return billService.deleteBillById(id);
    }
    @RequestMapping(value = "/bills_dishes",method = RequestMethod.GET)
    public List<Bill_Dish> getAllBill_Dish(){
        return billDishService.getAllBill_Dish();
    }
    @RequestMapping(value = "/bills_dishes",method = RequestMethod.POST)
    public int addBill_Dish(@RequestBody Bill_Dish bill_dish){return billDishService.addBill_Dish(bill_dish);}
    @RequestMapping(value = "/bill_dishes",method = RequestMethod.DELETE)
    public int deleteBill_Dish(@RequestBody Bill_Dish bill_dish){
        return billDishService.deleteBill_Dish(bill_dish);
    }
}
