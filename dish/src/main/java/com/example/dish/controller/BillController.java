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

    /**
     * 获取账单信息
     */
    @GetMapping("/bills")
    public PageUtils listBills(@RequestParam Map<String,Object> params)throws Exception{
        Query query = new Query(params);
        List<EntityModel<Bill>> bills = billModelAssembler.toModel(billService.listBills(query));
        int count = billService.count(query);
        return new PageUtils(bills,count,query.getPageSize());
    }
    @GetMapping(value = "/customers/{id}/bills")
    public List<EntityModel<Bill>> getBillsByCustomerId(@PathVariable("id") Long customerId){
        return billModelAssembler.toModel(billService.getBillsByCustomerId(customerId));
    }
    @GetMapping("/users/{id}/bills")
    public List<EntityModel<Bill>> getBillsByUserId(@PathVariable("id") Long userId){
        return billModelAssembler.toModel(billService.getBillsByUserId(userId));
    }
    @GetMapping("/userBills")
    public List<EntityModel<Bill>> getUserBills(){
        return billModelAssembler.toModel(billService.getUserBills());
    }
    @GetMapping("/bills/{id}")
    public EntityModel<Bill> getBillById(@PathVariable("id") Long id){
        return billModelAssembler.toModel(billService.getBillById(id));
    }
    @PostMapping("/bills")
    public void addBill(@RequestBody Bill bill){
        billService.saveBill(bill);
    }
    @PostMapping("/bills/{id}/complete")
    public Result<String> completeBill(@PathVariable("id") Long id) throws Exception {
        billService.completeBill(id);
        return Result.success("结算成功");
    }
    @PostMapping("/bills/{id}/cancel")
    public Result<String> cancelBill(@PathVariable("id") Long id) throws Exception {
        billService.cancelBill(id);
        return Result.success("撤销成功");
    }
    @DeleteMapping("/bills/{id}")
    public Result<String> deleteBill(@PathVariable Long id) throws Exception {
        billService.deleteBillById(id);
        return Result.success("删除成功");
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
