package com.example.dish.services;

import com.example.dish.common.Query;
import com.example.dish.entity.Bill;

import java.util.List;

public interface BillService {
    List<Bill> getAllBills();
    List<Bill> listBills(Query query)throws Exception;
    List<Bill> getBillsByCustomerId(Long customerId);
    List<Bill> getBillsByUserId(Long userId);
    List<Bill> getUserBills();
    int count(Query query);

    Bill getBillById(Long id);
    void saveBill(Bill bill);
    void completeBill(Long id)throws Exception;
    void cancelBill(Long id)throws Exception;
    void deleteBillById(Long id) throws Exception;
}
