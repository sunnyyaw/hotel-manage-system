package com.example.dish.mapper;

import com.example.dish.common.Query;
import com.example.dish.entity.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BillMapper {
    List<Bill> getAllBills();
    List<Bill> listBills(Query query);
    Bill getBillById(Long id);
    int count(Query query);
    void updateBill(Bill bill);
    void addBill(Bill bill);
    void deleteBillById(Long id);
}
