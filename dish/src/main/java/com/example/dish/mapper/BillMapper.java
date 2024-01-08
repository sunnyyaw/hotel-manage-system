package com.example.dish.mapper;

import com.example.dish.entity.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BillMapper {
    List<Bill> getAllBills();
    List<Bill> getBillsByCustomerId(Long customerId);
    Bill getBillById(Long id);
    int addBill(Bill bill);
    int deleteBillById(Long id);
}
