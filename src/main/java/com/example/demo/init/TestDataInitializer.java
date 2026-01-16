package com.example.demo.init;

import com.example.demo.entity.Wallet;
import com.example.demo.repository.WalletRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class TestDataInitializer {

    private final WalletRepository walletRepository;

    public TestDataInitializer(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }
//    данные для провери
    @PostConstruct
    public void init() {
        if (walletRepository.count() > 0) {
            return;
        }

        Wallet wallet1 = new Wallet(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                new BigDecimal("1000.00")
        );
        Wallet wallet2 = new Wallet(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                new BigDecimal("500.00")
        );

        walletRepository.save(wallet1);
        walletRepository.save(wallet2);
    }
}
