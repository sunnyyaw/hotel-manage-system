package com.example.dish.services;


import com.example.dish.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long id);
    int addCustomer(Customer customer);
    int deleteCustomerById(Long id);
}
