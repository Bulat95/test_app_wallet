package com.example.demo.service;

import com.example.demo.repository.WalletOperationRepository;
import com.example.demo.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletOperationService {
    @Autowired
    private WalletOperationRepository walletOperationRepository;

}
