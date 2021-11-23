package com.example.service;

import com.example.dto.CartRepresentation;

public interface CartService {
    CartRepresentation get();

    int saveCartItem(int productId);

    void deleteCartItem(int productId);
}
