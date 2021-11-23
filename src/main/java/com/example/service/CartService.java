package com.example.service;

import com.example.model.CartItem;
import com.example.model.ShoppingSession;

public interface ShoppingSessionService {
    ShoppingSession get();

    CartItem addItem(int productId);

    void removeItem(int productId);
}
