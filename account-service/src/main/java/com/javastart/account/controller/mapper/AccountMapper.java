package com.javastart.account.controller.mapper;

import com.javastart.account.controller.dto.AccountResponseDTO;
import com.javastart.account.entity.Account;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface AccountMapper {

    AccountResponseDTO accountToAccountResponseDTO(Account account);

}
