package com.javastart.account.controller;

import com.javastart.account.controller.mapper.AccountMapper;
import com.javastart.account.entity.Account;
import com.javastart.account.exception.AccountNotFoundException;
import com.javastart.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/accountId")
    public ResponseEntity<?> getAccount(@PathVariable Long accountId){
        try {
            Account account = accountService.getAccountById(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(accountMapper.accountToAccountResponseDTO(account));
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        Account account = accountService.createAccount(accountRequestDTO.getName(), accountRequestDTO.getEmail(),
                accountRequestDTO.getPhone(), accountRequestDTO.getBills());
        return ResponseEntity.status(HttpStatus.OK).body(accountMapper.accountToAccountResponseDTO(account));
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable Long accountId, @RequestBody AccountRequestDTO accountRequestDTO) {
        Account account = accountService.updateAccount(accountId, accountRequestDTO.getName(), accountRequestDTO.getEmail(),
                accountRequestDTO.getPhone(), accountRequestDTO.getBills());
        return ResponseEntity.status(HttpStatus.OK).body(accountMapper.accountToAccountResponseDTO(account));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
        try {
            Account account = accountService.deleteAccount(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(accountMapper.accountToAccountResponseDTO(account));
        }catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }
}
