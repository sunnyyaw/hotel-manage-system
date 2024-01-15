package com.example.dish.services;

import com.example.dish.entity.Bill;
import com.example.dish.entity.Bill_Dish;
import com.example.dish.entity.OrderDTO;
import com.example.dish.mapper.BillMapper;
import com.example.dish.mapper.Bill_DishMapper;
import com.example.dish.mapper.CustomerMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService{
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private Bill_DishService billDishService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private DishService dishService;
    @Override
    public List<Bill> getAllBills() {
        return billMapper.getAllBills().stream().peek(bill->{
            bill.setUser(userService.getUserById(bill.getUserId()));
            bill.setCustomer(customerMapper.getCustomerById(bill.getCustomerId()));
        }).toList();
    }

    @Override
    public List<Bill> getBillsByCustomerId(Long customerId) {
        return this.getAllBills().stream().filter(bill->
                Objects.equals(bill.getCustomerId(),customerId)).toList();
    }

    @Override
    public Bill getBillById(Long id) {
        Optional<Bill> bill = this.getAllBills().stream().filter(b->
                Objects.equals(b.getId(),id)).findFirst();
        return bill.orElse(null);
    }


    @Override
    @Transactional
    public void saveBill(Bill bill) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        Long userId = userService.getUserByUsername(username).getId();
        bill.setUserId(userId);
        bill.setGenTime(new Timestamp(System.currentTimeMillis()));
        billMapper.addBill(bill);
        Long billId = bill.getId();
        List<Bill_Dish> orders = bill.getOrders();
        orders.forEach(order->{
            if(order.getNum()<=0)return;
            order.setBillId(billId);
            billDishService.addBill_Dish(order);
        });
    }

    @Override
    @Transactional
    public void deleteBillById(Long id) {
        billDishService.deleteBill_DishByBillId(id);
        billMapper.deleteBillById(id);
    }
}
