package com.example.exception;

public class PaymentNotFound extends RuntimeException{
    public PaymentNotFound(String message) {
        super(message);
    }
}
