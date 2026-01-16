package com.example.demo.exception;

public class NotEnoughFundsException extends RuntimeException {

    public NotEnoughFundsException(String message) {
        super(message);
    }

    public NotEnoughFundsException() {
        super("Not enough funds for withdraw");
    }
}
