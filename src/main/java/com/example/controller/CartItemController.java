package com.example.controller;

import com.example.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {
    private final CartService cartService;

    @PostMapping("/{id}")
    public ResponseEntity<String> addProductToCart(@PathVariable int id) {
        cartService.saveCartItem(id);
        return new ResponseEntity<>("Product has been added to cart",
                HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable int id) {
        cartService.deleteCartItem(id);
        return new ResponseEntity<>("Product has been removed from cart",
                HttpStatus.NO_CONTENT);
    }
}
