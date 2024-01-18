package com.example.dish.utils;

import com.example.dish.common.Status;
import com.example.dish.controller.BillController;
import com.example.dish.entity.Bill;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class BillModelAssembler implements RepresentationModelAssembler<Bill, EntityModel<Bill>> {

    @Override
    public @NotNull EntityModel<Bill> toModel(@NotNull Bill bill) {
        EntityModel<Bill> billModel = EntityModel.of(bill,
                linkTo(methodOn(BillController.class).getBillById(bill.getId())).withSelfRel(),
                linkTo(methodOn(BillController.class).listBills(new HashMap<>())).withRel("all")
        );
        if(bill.getStatus()==null || bill.getStatus()== Status.未结算) {
            try {
                billModel.add(
                        linkTo(methodOn(BillController.class).completeBill(bill.getId())).withRel("complete"),
                        linkTo(methodOn(BillController.class).cancelBill(bill.getId())).withRel("cancel"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return billModel;
    }
    public List<EntityModel<Bill>> toModel(List<Bill> bills) {
        return bills.stream().map(this::toModel).toList();
    }
}
