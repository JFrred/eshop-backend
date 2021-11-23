package com.example.controller;

import com.example.dto.CartRepresentation;
import com.example.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartRepresentation> get() {
        return ResponseEntity.ok(cartService.get());
    }
}
