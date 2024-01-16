package com.example.dish.services;

import com.example.dish.entity.Bill;

import java.util.List;

public interface BillService {
    List<Bill> getAllBills();
    List<Bill> listAllBills();
    List<Bill> getBillsByCustomerId(Long customerId);
    List<Bill> getBillsByUserId(Long userId);
    List<Bill> getUserBills();

    Bill getBillById(Long id);
    void saveBill(Bill bill);
    void completeBill(Long id)throws Exception;
    void cancelBill(Long id)throws Exception;
    void deleteBillById(Long id);
}
