package com.javastart.bill.controller;

import com.javastart.bill.controller.dto.BillRequestDTO;
import com.javastart.bill.controller.dto.BillResponseDTO;
import com.javastart.bill.controller.mapper.BillMapper;
import com.javastart.bill.entity.Bill;
import com.javastart.bill.exception.BillNotFoundException;
import com.javastart.bill.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BillController {

    private final BillService billService;
    private final BillMapper billMapper;

    @Autowired
    public BillController(BillService billService, BillMapper billMapper) {
        this.billService = billService;
        this.billMapper = billMapper;
    }

    @GetMapping("/{billId}")
    public ResponseEntity<?> getBill(@PathVariable Long billId) {
        try {
            Bill bill = billService.getBillById(billId);
            return ResponseEntity.status(HttpStatus.OK).body(billMapper.billToBillResponseDTO(bill));
        } catch (BillNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<Long> createBill(@RequestBody  BillRequestDTO billRequestDTO){
        Long billId = billService.createBill(billRequestDTO.getAccountId(), billRequestDTO.getAmount(), billRequestDTO.getIsDefault(),
                billRequestDTO.getOverdraftEnabled());
        return ResponseEntity.status(HttpStatus.OK).body(billId);
    }

    @PutMapping("/{billId}")
    public ResponseEntity<BillResponseDTO> updateBill(@PathVariable Long billId, @RequestBody BillRequestDTO billRequestDTO) {
        Bill updateBill = billService.updateBill(billId, billRequestDTO.getAccountId(), billRequestDTO.getAmount(), billRequestDTO.getIsDefault(),
                billRequestDTO.getOverdraftEnabled());
        return ResponseEntity.status(HttpStatus.OK).body(billMapper.billToBillResponseDTO(updateBill));
    }

    @DeleteMapping("/{billId}")
    public ResponseEntity<?> deleteBill(@PathVariable Long billId) {
        try{
            Bill deleteBill = billService.deleteBill(billId);
            return ResponseEntity.status(HttpStatus.OK).body(billMapper.billToBillResponseDTO(deleteBill));
        } catch (BillNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

}
