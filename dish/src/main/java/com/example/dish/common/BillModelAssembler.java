package com.example.dish.common;

import com.example.dish.common.BillStatus;
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
    public @NotNull EntityModel<Bill> toModel(@NotNull Bill bill){
        EntityModel<Bill> billModel = null;
        try {
            billModel = EntityModel.of(bill,
                    linkTo(methodOn(BillController.class).getBillById(bill.getId())).withSelfRel(),
                    linkTo(methodOn(BillController.class).listBills(new HashMap<>())).withRel("all")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(bill.getStatus()==null || bill.getStatus()== BillStatus.IN_PROCESS.ordinal()) {
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
