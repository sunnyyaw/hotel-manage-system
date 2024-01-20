package com.example.dish.mapper;

import com.example.dish.common.Query;
import com.example.dish.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CustomerMapper {
    List<Customer> getAllCustomers();
    List<Customer> listCustomers(Query query);
    int count(Query query);
    Customer getCustomerById(Long id);
    int addCustomer(Customer customer);
    int updateCustomer(Customer customer);
    int deleteCustomerById(Long id);
}
