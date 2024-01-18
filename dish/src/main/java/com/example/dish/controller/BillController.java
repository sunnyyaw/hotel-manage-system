package com.example.dish.controller;

import com.example.dish.common.PageUtils;
import com.example.dish.common.Query;
import com.example.dish.entity.Bill;
import com.example.dish.entity.Bill_Dish;
import com.example.dish.common.Result;
import com.example.dish.services.BillService;
import com.example.dish.services.Bill_DishService;
import com.example.dish.utils.BillModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取账单信息
     */
    @RequestMapping(value = "/bills",method = RequestMethod.GET)
    public PageUtils listBills(@RequestParam Map<String,Object> params){
        Query query = new Query(params);
        List<EntityModel<Bill>> bills = billModelAssembler.toModel(billService.listBills(query));
        int count = billService.count(query);
        return new PageUtils(bills,count,query.getPageSize());
    }
    @RequestMapping(value = "/customers/{id}/bills",method = RequestMethod.GET)
    public List<EntityModel<Bill>> getBillsByCustomerId(@PathVariable("id") Long customerId){
        return billModelAssembler.toModel(billService.getBillsByCustomerId(customerId));
    }
    @RequestMapping(value = "/users/{id}/bills",method = RequestMethod.GET)
    public List<EntityModel<Bill>> getBillsByUserId(@PathVariable("id") Long userId){
        return billModelAssembler.toModel(billService.getBillsByUserId(userId));
    }
    @RequestMapping(value = "/userBills",method = RequestMethod.GET)
    public List<EntityModel<Bill>> getUserBills(){
        return billModelAssembler.toModel(billService.getUserBills());
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
    public Result<String> completeBill(@PathVariable("id") Long id) throws Exception {
        billService.completeBill(id);
        return Result.success("结算成功");
    }
    @RequestMapping(value="/bills/{id}/cancel",method = RequestMethod.POST)
    public Result<String> cancelBill(@PathVariable("id") Long id) throws Exception {
        billService.cancelBill(id);
        return Result.success("撤销成功");
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
