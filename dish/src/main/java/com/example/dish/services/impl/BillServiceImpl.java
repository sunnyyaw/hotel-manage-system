package com.example.dish.services.impl;

import com.example.dish.common.Query;
import com.example.dish.common.BillStatus;
import com.example.dish.entity.Bill;
import com.example.dish.entity.Bill_Dish;
import com.example.dish.mapper.BillMapper;
import com.example.dish.mapper.CustomerMapper;
import com.example.dish.services.BillService;
import com.example.dish.services.Bill_DishService;
import com.example.dish.services.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private Bill_DishService billDishService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private UserService userService;
    @Override
    public List<Bill> getAllBills() {
        return billMapper.getAllBills();
    }


    @Override
    public List<Bill> listBills(Query query){
        return billMapper.listBills(query).stream().peek(bill->{
            bill.setUser(userService.getUserById(bill.getUserId()));
            bill.setCustomer(customerMapper.getCustomerById(bill.getCustomerId()));
        }).toList();
    }
    @Override
    public int count(Query query){
        return billMapper.count(query);
    }

    @Override
    public List<Bill> getBillsByCustomerId(Long customerId) {
        return this.getAllBills().stream().filter(bill->
                Objects.equals(bill.getCustomerId(),customerId)).toList();
    }

    @Override
    public List<Bill> getBillsByUserId(Long userId) {
        return this.getAllBills().stream().filter(bill->
                Objects.equals(bill.getUserId(),userId)).toList();
    }

    @Override
    public List<Bill> getUserBills() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        Long userId =userService.getUserByUsername(username).getId();
        return this.getAllBills().stream().filter(bill->
                Objects.equals(bill.getUserId(),userId)).toList();
    }

    @Override
    public Bill getBillById(Long id) {
        return billMapper.getBillById(id);
    }


    @Override
    @Transactional
    public void saveBill(Bill bill) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        Long userId = userService.getUserByUsername(username).getId();
        bill.setStatus(BillStatus.IN_PROCESS.ordinal());
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
    public void completeBill(Long id)throws Exception {
        Bill bill = this.getBillById(id);
        if(Objects.isNull(bill))
            throw new Exception("账单不存在");
        if(bill.getStatus()!=null && bill.getStatus()!= BillStatus.IN_PROCESS.ordinal())
            throw new Exception("账单不能在已结算状态下结算");
        bill.setStatus(BillStatus.COMPLETED.ordinal());
        billMapper.updateBill(bill);
    }

    @Override
    public void cancelBill(Long id) throws Exception {
        Bill bill = this.getBillById(id);
        if(Objects.isNull(bill))
            throw new Exception("账单不存在");
        if(bill.getStatus()!=null && bill.getStatus()!= BillStatus.IN_PROCESS.ordinal())
            throw new Exception("账单不能在已撤销状态下撤销");
        bill.setStatus(BillStatus.CANCELED.ordinal());
        billMapper.updateBill(bill);
    }

    @Override
    @Transactional
    public void deleteBillById(Long id) throws Exception{
        billDishService.deleteBill_DishByBillId(id);
        billMapper.deleteBillById(id);
    }
}
