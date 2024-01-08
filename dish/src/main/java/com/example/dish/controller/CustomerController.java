package com.example.dish.controller;

import com.example.dish.entity.Customer;
import com.example.dish.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @RequestMapping(value = "/customers",method = RequestMethod.GET)
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }
    @RequestMapping(value = "/customers/{id}",method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("id") Long id){
        return customerService.getCustomerById(id);
    }
    @RequestMapping(value = "/customers",method = RequestMethod.POST)
    public int addCustomer(@RequestBody Customer customer){
        return customerService.addCustomer(customer);
    }
    @RequestMapping(value = "/customers/{id}",method = RequestMethod.DELETE)
    public int deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomerById(id);
    }
}
