package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemRepresentation {
    private String name;
    private double price;
    private int quantity;
    private double totalPrice;
}
