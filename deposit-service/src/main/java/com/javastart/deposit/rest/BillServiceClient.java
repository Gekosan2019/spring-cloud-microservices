package com.javastart.deposit.rest;

import com.javastart.deposit.controller.dto.BillRequestDTO;
import com.javastart.deposit.controller.dto.BillResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "bill-service")
public interface BillServiceClient {

    @GetMapping("/bills/{billId}")
    ResponseEntity<BillResponseDTO> getBillById(@PathVariable("billId") Long billId);

    @PutMapping("/bills/{billId}")
    ResponseEntity<BillResponseDTO> updateBill(@PathVariable("billId") Long billId, BillRequestDTO billRequestDTO);

    @GetMapping("/bills/account/{accountId}")
    List<BillResponseDTO> getBillsByAccountId(@PathVariable("accountId") Long accountId);
}
