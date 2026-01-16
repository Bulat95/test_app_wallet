package com.example.demo.mapper;

import com.example.demo.dto.WalletDto;
import com.example.demo.entity.Wallet;

public class WalletMapper {
    public static WalletDto toWalletDto(Wallet wallet) {
        if (wallet == null) {
            return null;
        }
        return new WalletDto(
                wallet.getId(),
                wallet.getBalance()
        );
    }
}
