package com.javastart.bill.controller.mapper;

import com.javastart.bill.controller.dto.BillResponseDTO;
import com.javastart.bill.entity.Bill;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillMapper {

    BillResponseDTO billToBillResponseDTO(Bill bill);

}
