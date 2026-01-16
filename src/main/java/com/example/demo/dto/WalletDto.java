package com.example.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class WalletDto {
    private UUID walletId;
    private BigDecimal balance;

    public WalletDto(UUID walletId, BigDecimal balance) {
        this.walletId = walletId;
        this.balance = balance;
    }
}