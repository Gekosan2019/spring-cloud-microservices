package com.javastart.deposit.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long depositId;

    private Long billId;

    private BigDecimal amount;

    private OffsetDateTime creationDate;

    private String email;

    public Deposit(Long billId, BigDecimal amount, OffsetDateTime creationDate, String email) {
        this.billId = billId;
        this.amount = amount;
        this.creationDate = creationDate;
        this.email = email;
    }

}
