package com.example.dish.services;

import com.example.dish.entity.Bill;

import java.util.List;

public interface BillService {
    List<Bill> getAllBills();
    List<Bill> getBillsByCustomerId(Long customerId);

    Bill getBillById(Long id);
    void saveBill(Bill bill);
    void deleteBillById(Long id);
}
