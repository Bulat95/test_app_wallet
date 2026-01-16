package com.example.demo.mapper;

import com.example.demo.dto.WalletOperationDto;
import com.example.demo.entity.WalletOperation;

import java.math.BigDecimal;

public class WalletOperationMapper {

    public static WalletOperation fromRequestDto(WalletOperationDto dto) {
        if (dto == null) {
            return null;
        }

        BigDecimal amount = dto.getAmount();
        return new WalletOperation(
                dto.getWalletId(),
                dto.getOperationType(),
                amount
        );
    }
}
