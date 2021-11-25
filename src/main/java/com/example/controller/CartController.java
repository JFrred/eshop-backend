package com.example.controller;

import com.example.dto.CartRepresentation;
import com.example.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/{id}")
    public ResponseEntity<String> addProduct(@PathVariable int id,
                                             @RequestParam int quantity) {
        cartService.saveCartItem(id, quantity);
        return new ResponseEntity<>("Product has been added to cart",
                HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id,
                                                @RequestParam int quantity) {
        cartService.deleteCartItem(id, quantity);
        return new ResponseEntity<>("Product has been removed from cart",
                HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllProducts() {
        cartService.deleteAllCartItems();
        return new ResponseEntity<>("Products have been removed from cart",
                HttpStatus.NO_CONTENT);
    }

}
