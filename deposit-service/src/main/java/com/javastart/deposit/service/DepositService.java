package com.javastart.deposit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.deposit.controller.dto.AccountResponseDTO;
import com.javastart.deposit.controller.dto.BillRequestDTO;
import com.javastart.deposit.controller.dto.BillResponseDTO;
import com.javastart.deposit.controller.dto.DepositResponseDTO;
import com.javastart.deposit.entity.Deposit;
import com.javastart.deposit.exception.DepositServiceException;
import com.javastart.deposit.repository.DepositRepository;
import com.javastart.deposit.rest.AccountServiceClient;
import com.javastart.deposit.rest.BillServiceClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class DepositService {

    private static final String TOPIC_EXCHANGE_DEPOSIT = "js.deposit.notify.exchange";
    private static final String ROUTING_KEY_DEPOSIT = "js.key.deposit";


    private final DepositRepository depositRepository;
    private final AccountServiceClient accountServiceClient;
    private final BillServiceClient billServiceClient;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public DepositService(DepositRepository depositRepository, AccountServiceClient accountServiceClient,
                          BillServiceClient billServiceClient, RabbitTemplate rabbitTemplate) {
        this.depositRepository = depositRepository;
        this.accountServiceClient = accountServiceClient;
        this.billServiceClient = billServiceClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public DepositResponseDTO deposit(Long accountId, Long billId, BigDecimal amount) {
        if (accountId == null && billId == null){
            throw new DepositServiceException("Account is null and Bill is null");
        }
        if (billId != null) {
            ResponseEntity<BillResponseDTO> responseEntity = billServiceClient.getBillById(billId);
            BillResponseDTO billResponseDTO = responseEntity.getBody();
            BillRequestDTO billRequestDTO = createBillRequestDTO(amount, billResponseDTO);
            billServiceClient.updateBill(billId, billRequestDTO);

            ResponseEntity<AccountResponseDTO> accountResponseDTOResponseEntity = accountServiceClient.getAccountById(billResponseDTO.getAccountId());
            AccountResponseDTO accountResponseDTO = accountResponseDTOResponseEntity.getBody();
            depositRepository.save(new Deposit(billId, amount, OffsetDateTime.now(), accountResponseDTO.getEmail()));

            return createDepositResponseDTOResponseEntity(amount, accountResponseDTO);
        }

        ResponseEntity<BillResponseDTO> responseEntity = getDefaultBill(accountId);
        BillResponseDTO defaultBill = responseEntity.getBody();
        BillRequestDTO billRequestDTO = createBillRequestDTO(amount, defaultBill);
        billServiceClient.updateBill(defaultBill.getBillId(), billRequestDTO);
        ResponseEntity<AccountResponseDTO> accountResponseDTOResponseEntity = accountServiceClient.getAccountById(accountId);
        AccountResponseDTO currentAccount = accountResponseDTOResponseEntity.getBody();
        depositRepository.save(new Deposit(defaultBill.getBillId(), amount, defaultBill.getCreationDate(), currentAccount.getEmail()));

        return createDepositResponseDTOResponseEntity(amount,currentAccount);
    }

    private DepositResponseDTO createDepositResponseDTOResponseEntity(BigDecimal amount, AccountResponseDTO accountResponseDTO) {
        DepositResponseDTO depositResponseDTO = new DepositResponseDTO(amount, accountResponseDTO.getEmail());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_DEPOSIT, ROUTING_KEY_DEPOSIT, objectMapper.writeValueAsString(depositResponseDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new DepositServiceException("Can't send message to RabbitMQ");
        }
        return depositResponseDTO;
    }

    private BillRequestDTO createBillRequestDTO(BigDecimal amount, BillResponseDTO billResponseDTO) {
        BillRequestDTO billRequestDTO = new BillRequestDTO();
        billRequestDTO.setAccountId(billResponseDTO.getAccountId());
        billRequestDTO.setIsDefault(billResponseDTO.getIsDefault());
        billRequestDTO.setOverdraftEnabled(billResponseDTO.getOverdraftEnabled());
        billRequestDTO.setCreationDate(billResponseDTO.getCreationDate());
        billRequestDTO.setAmount(billResponseDTO.getAmount().add(amount));
        return billRequestDTO;
    }

    private ResponseEntity<BillResponseDTO> getDefaultBill(Long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(billServiceClient.getBillsByAccountId(accountId).stream().filter(BillResponseDTO::getIsDefault).
                findAny().orElseThrow(() -> new DepositServiceException("Not Found default bill for account: " + accountId)));
    }


}
