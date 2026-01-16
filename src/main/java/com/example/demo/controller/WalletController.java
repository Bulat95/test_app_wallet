package com.example.demo.controller;

import com.example.demo.dto.WalletDto;
import com.example.demo.dto.WalletOperationDto;
import com.example.demo.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/wallet")
    public ResponseEntity<WalletDto> changeBalance(@RequestBody WalletOperationDto request) {
        WalletDto response = walletService.applyOperation(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<WalletDto> getBalance(@PathVariable("walletId") UUID walletId) {
        WalletDto response = walletService.getBalance(walletId);
        return ResponseEntity.ok(response);
    }
}
