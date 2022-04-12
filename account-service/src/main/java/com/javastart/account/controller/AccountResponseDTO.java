package com.javastart.account.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class AccountResponseDTO {

    private Long accountId;

    private String name;

    private String phone;

    private String email;

    private List<Long> bills;

    private OffsetDateTime creationDate;

}
