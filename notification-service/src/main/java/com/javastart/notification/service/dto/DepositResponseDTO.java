package com.javastart.notification.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class DepositResponseDTO {

    private BigDecimal amount;

    private String email;
}
