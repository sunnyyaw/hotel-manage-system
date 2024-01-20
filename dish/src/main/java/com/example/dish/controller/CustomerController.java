package com.example.dish.controller;

import com.example.dish.common.PageUtils;
import com.example.dish.common.Query;
import com.example.dish.common.Result;
import com.example.dish.entity.Customer;
import com.example.dish.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @GetMapping("/customers")
    public PageUtils listCustomers(@RequestParam Map<String,Object> params){
        Query query = new Query(params);
        List<Customer> customers = customerService.listCustomers(query);
        int count = customerService.count(query);
        return new PageUtils(customers,count, query.getPageSize());
    }
    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable("id") Long id){
        return customerService.getCustomerById(id);
    }
    @PostMapping("/customers")
    public int addCustomer(@RequestBody Customer customer){
        return customerService.addCustomer(customer);
    }
    @DeleteMapping("/customers/{id}")
    public Result<String> deleteCustomer(@PathVariable Long id) throws Exception {
        customerService.deleteCustomerById(id);
        return Result.success("删除成功");
    }
}
