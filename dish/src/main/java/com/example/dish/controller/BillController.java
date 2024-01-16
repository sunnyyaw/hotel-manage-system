package com.example.dish.controller;

import com.example.dish.entity.Bill;
import com.example.dish.entity.Bill_Dish;
import com.example.dish.common.Result;
import com.example.dish.services.BillService;
import com.example.dish.services.Bill_DishService;
import com.example.dish.utils.BillModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class BillController {
    @Autowired
    private BillModelAssembler billModelAssembler;
    @Autowired
    private BillService billService;
    @Autowired
    private Bill_DishService billDishService;
    @RequestMapping("/index")
    public String hello(){
        return "你好";
    }
    @RequestMapping(value = "/bills",method = RequestMethod.GET)
    public List<EntityModel<Bill>> getAllBills(){
        return billService.listAllBills().stream().map(
                bill -> billModelAssembler.toModel(bill)
        ).toList();
    }
    @RequestMapping(value = "/customers/{id}/bills",method = RequestMethod.GET)
    public List<EntityModel<Bill>> getBillsByCustomerId(@PathVariable("id") Long customerId){
        return billService.getBillsByCustomerId(customerId).stream().map(
                bill -> billModelAssembler.toModel(bill)
        ).toList();
    }
    @RequestMapping(value = "/users/{id}/bills",method = RequestMethod.GET)
    public List<EntityModel<Bill>> getBillsByUserId(@PathVariable("id") Long userId){
        return billService.getBillsByUserId(userId).stream().map(
                bill -> billModelAssembler.toModel(bill)
        ).toList();
    }
    @RequestMapping(value = "/userBills",method = RequestMethod.GET)
    public List<EntityModel<Bill>> getUserBills(){
        return billService.getUserBills().stream().map(
                bill -> billModelAssembler.toModel(bill)
        ).toList();
    }
    @RequestMapping(value = "/bills/{id}",method = RequestMethod.GET)
    public EntityModel<Bill> getBillById(@PathVariable("id") Long id){
        return billModelAssembler.toModel(billService.getBillById(id));
    }
    @RequestMapping(value = "/bills",method = RequestMethod.POST)
    public void addBill(@RequestBody Bill bill){
        billService.saveBill(bill);
    }
    @RequestMapping(value="/bills/{id}/complete",method = RequestMethod.POST)
    public Result<String> completeBill(@PathVariable("id") Long id){
        try{
            billService.completeBill(id);
            return new Result<>(HttpStatus.OK.value(),"结算成功");
        }catch(Exception e){
            return new Result<>(HttpStatus.METHOD_NOT_ALLOWED.value(),e.getMessage());
        }
    }
    @RequestMapping(value="/bills/{id}/cancel",method = RequestMethod.POST)
    public Result<String> cancelBill(@PathVariable("id") Long id){
        try{
            billService.cancelBill(id);
            return new Result<>(HttpStatus.OK.value(),"撤销成功");
        }catch(Exception e){
            return new Result<>(HttpStatus.METHOD_NOT_ALLOWED.value(),e.getMessage());
        }
    }
    @RequestMapping(value = "/bills/{id}",method = RequestMethod.DELETE)
    public void deleteBill(@PathVariable Long id){
        billService.deleteBillById(id);
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
