package com.example.controller;

import com.example.model.CartItem;
import com.example.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingSessionController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartItem> add(@RequestParam int userId, @RequestParam int productId) {
        return new ResponseEntity<>(cartService.add(userId, productId), HttpStatus.CREATED);
    }
}
