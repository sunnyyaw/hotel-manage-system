package com.example.dish.services.impl;

import com.example.dish.common.Query;
import com.example.dish.entity.Customer;
import com.example.dish.mapper.BillMapper;
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
    @Autowired
    private BillMapper billMapper;

    @Override
    public int count(Query query) {
        return customerMapper.count(query);
    }

    @Override
    public List<Customer> getCustomers(Query query) {
        return customerMapper.listCustomers(query).stream().peek(customer->{
            Query query1 = new Query();
            query1.put("customerId",customer.getId());
            customer.setAssociation(billMapper.count(query1));
        }).toList();
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
    public void deleteCustomerById(Long id) throws Exception{
        if(customerMapper.getCustomerById(id)==null)
            throw new Exception("餐桌不存在");
        Query query = new Query();
        query.put("customerId",id);
        int count = billMapper.count(query);
        if(count > 0)
            throw new Exception("有账单与该餐桌关联，不得删除");
        customerMapper.deleteCustomerById(id);
    }
}
