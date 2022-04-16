package com.javastart.deposit.rest;

import com.javastart.deposit.controller.dto.AccountResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service")
public interface AccountServiceClient {

    @GetMapping("/accounts/{accountId}")
    ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable("accountId") Long accountId);

}
