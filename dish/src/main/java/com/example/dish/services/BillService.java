package com.example.dish.services;

import com.example.dish.entity.Bill;
import com.example.dish.entity.BillFormDTO;

import java.util.List;

public interface BillService {
    List<Bill> getAllBills();
    List<Bill> getBillsByCustomerId(Long customerId);

    Bill getBillById(Long id);
    int addBill(BillFormDTO billForm);
    int deleteBillById(Long id);
}
