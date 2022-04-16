package com.javastart.deposit.controller.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DepositResponseDTO {

    private BigDecimal amount;

    private String email;

}
