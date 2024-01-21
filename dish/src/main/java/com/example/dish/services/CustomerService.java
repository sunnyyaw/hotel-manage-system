package com.example.dish.services;


import com.example.dish.common.Query;
import com.example.dish.entity.Customer;

import java.util.List;

public interface CustomerService {
    int count(Query query);
    List<Customer> getCustomers(Query query);
    Customer getCustomerById(Long id);
    int addCustomer(Customer customer);
    void deleteCustomerById(Long id)throws Exception;
}
