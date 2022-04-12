package com.javastart.account.service;

import com.javastart.account.entity.Account;
import com.javastart.account.exception.AccountNotFoundException;
import com.javastart.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId).
                orElseThrow(() -> new AccountNotFoundException("Unable to find account with id: " + accountId));
    }

    public Account createAccount(String name, String email, String phone, List<Long> bills) {
        Account newAccount = new Account(name, email, phone, OffsetDateTime.now(), bills);
        accountRepository.save(newAccount);
        return newAccount;
    }

    public Account updateAccount(Long accountId, String name, String email, String phone, List<Long> bills) {
        Account updateAccount = new Account();
        updateAccount.setAccountId(accountId);
        updateAccount.setBills(bills);
        updateAccount.setEmail(email);
        updateAccount.setName(name);
        updateAccount.setPhone(phone);
        return accountRepository.save(updateAccount);
    }

    public Account deleteAccount(Long accountId) {
        Account deleteAccount = getAccountById(accountId);
        accountRepository.deleteById(accountId);
        return deleteAccount;
    }
}
