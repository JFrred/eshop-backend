package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartRepresentation {
    private List<CartItemRepresentation> items;
    private double totalPrice;
}
