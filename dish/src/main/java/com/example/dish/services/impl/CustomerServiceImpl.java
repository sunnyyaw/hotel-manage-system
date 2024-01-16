package com.example.dish.services.impl;

import com.example.dish.entity.Customer;
import com.example.dish.mapper.CustomerMapper;
import com.example.dish.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public List<Customer> getAllCustomers() {
        return customerMapper.getAllCustomers();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerMapper.getCustomerById(id);
    }

    @Override
    public int addCustomer(Customer customer) {
        Customer c = customerMapper.getCustomerById(customer.getId());
        if(Objects.isNull(c)){
            return customerMapper.addCustomer(customer);
        }
        return customerMapper.updateCustomer(customer);
    }

    @Override
    public int deleteCustomerById(Long id) {
        return customerMapper.deleteCustomerById(id);
    }
}
