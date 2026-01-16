package com.example.demo.dto;

import com.example.demo.entity.OperationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class WalletOperationDto {

    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;
}
