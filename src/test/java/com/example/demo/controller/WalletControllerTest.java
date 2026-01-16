package com.example.demo.controller;

import com.example.demo.dto.WalletDto;
import com.example.demo.exception.NotEnoughFundsException;
import com.example.demo.exception.WalletNotFoundException;
import com.example.demo.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Test
    void changeBalance_deposit_success() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletDto response = new WalletDto(walletId, new BigDecimal("100.00"));
        when(walletService.applyOperation(any())).thenReturn(response);

        String requestJson = """
            {
              "walletId": "%s",
              "operationType": "DEPOSIT",
              "amount": 100.00
            }
            """.formatted(walletId);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(100.00));
    }

    @Test
    void getBalance_success() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletDto response = new WalletDto(walletId, new BigDecimal("50.00"));
        when(walletService.getBalance(walletId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(50.00));
    }
    @Test
    void changeBalance_walletNotFound() throws Exception {
        UUID walletId = UUID.randomUUID();

        when(walletService.applyOperation(any()))
                .thenThrow(new WalletNotFoundException(walletId));

        String requestJson = """
        {
          "walletId": "%s",
          "operationType": "DEPOSIT",
          "amount": 100.00
        }
        """.formatted(walletId);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("WALLET_NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void changeBalance_notEnoughFunds() throws Exception {
        UUID walletId = UUID.randomUUID();

        when(walletService.applyOperation(any()))
                .thenThrow(new NotEnoughFundsException("Not enough funds for withdraw"));

        String requestJson = """
        {
          "walletId": "%s",
          "operationType": "WITHDRAW",
          "amount": 1000.00
        }
        """.formatted(walletId);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("NOT_ENOUGH_FUNDS"))
                .andExpect(jsonPath("$.message").value("Not enough funds for withdraw"));
    }

    @Test
    void changeBalance_invalidJson() throws Exception {
        String invalidJson = "{ invalid-json }";

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST_BODY"))
                .andExpect(jsonPath("$.message").exists());
    }
}
