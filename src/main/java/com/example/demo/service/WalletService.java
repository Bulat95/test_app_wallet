package com.example.demo.service;

import com.example.demo.dto.WalletDto;
import com.example.demo.dto.WalletOperationDto;
import com.example.demo.entity.OperationType;
import com.example.demo.entity.Wallet;
import com.example.demo.entity.WalletOperation;
import com.example.demo.exception.NotEnoughFundsException;
import com.example.demo.exception.WalletNotFoundException;
import com.example.demo.mapper.WalletMapper;
import com.example.demo.mapper.WalletOperationMapper;
import com.example.demo.repository.WalletOperationRepository;
import com.example.demo.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletOperationRepository walletOperationRepository;
    @Value("${wallet.max-retries:10}")
    private int maxRetries;

    public WalletService(WalletRepository walletRepository,
                         WalletOperationRepository walletOperationRepository) {
        this.walletRepository = walletRepository;
        this.walletOperationRepository = walletOperationRepository;
    }

    @Transactional
    public WalletDto applyOperation(WalletOperationDto request) {
        int attempt = 0;
        while (true) {
            try {
                return doApplyOperation(request);
            } catch (OptimisticLockingFailureException ex) {
                attempt++;
                if (attempt >= maxRetries) {
                    throw ex;
                }
            }
        }
    }

    private WalletDto doApplyOperation(WalletOperationDto request) {
        UUID walletId = request.getWalletId();
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        BigDecimal amount = request.getAmount();
        OperationType type = request.getOperationType();

        if (type == OperationType.WITHDRAW &&
                wallet.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughFundsException("Not enough funds for withdraw");
        }

        BigDecimal newBalance = wallet.getBalance();
        if (type == OperationType.DEPOSIT) {
            newBalance = newBalance.add(amount);
        } else if (type == OperationType.WITHDRAW) {
            newBalance = newBalance.subtract(amount);
        }

        wallet.setBalance(newBalance);

        WalletOperation operation = WalletOperationMapper.fromRequestDto(request);
        walletOperationRepository.save(operation);

        wallet = walletRepository.save(wallet);
        return WalletMapper.toWalletDto(wallet);
    }


    @Transactional(readOnly = true)
    public WalletDto getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        return WalletMapper.toWalletDto(wallet);
    }
}
