package com.example.dish.services;

import com.example.dish.entity.Bill;
import com.example.dish.entity.BillFormDTO;
import com.example.dish.entity.Bill_Dish;
import com.example.dish.entity.OrderDTO;
import com.example.dish.mapper.BillMapper;
import com.example.dish.mapper.Bill_DishMapper;
import com.example.dish.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class BillServiceImpl implements BillService{
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private Bill_DishMapper bill_dishMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public List<Bill> getAllBills() {
        return billMapper.getAllBills();
    }

    @Override
    public List<Bill> getBillsByCustomerId(Long customerId) {
        return billMapper.getBillsByCustomerId(customerId);
    }

    @Override
    public Bill getBillById(Long id) {
        return billMapper.getBillById(id);
    }


    @Override
    @Transactional
    public int addBill(BillFormDTO billForm) {
        Bill bill = new Bill();
        bill.setCustomerId(billForm.getCustomerId());
        bill.setGenTime(new Timestamp(System.currentTimeMillis()));
        billMapper.addBill(bill);
        Long billId = bill.getId();
        List<OrderDTO> orders = billForm.getOrders();
        orders.forEach(order->{
            if(order.getNum()<=0)return;
            Bill_Dish bill_dish = new Bill_Dish();
            bill_dish.setBillId(billId);
            bill_dish.setDishId(order.getDishId());
            bill_dish.setNum(order.getNum());
            bill_dishMapper.addBill_Dish(bill_dish);
        });
        return orders.size();
    }

    @Override
    @Transactional
    public int deleteBillById(Long id) {
        bill_dishMapper.deleteBill_DishByBillId(id);
        return billMapper.deleteBillById(id);
    }
}
