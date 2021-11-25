package com.example.controller;

import com.example.dto.ProductRequest;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductMgmtController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody ProductRequest productRequest) {
        int productId = productService.save(productRequest);

        return new ResponseEntity<>("Product has been saved with.",
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody ProductRequest product) {
        productService.update(id, product);
        return new ResponseEntity<>("Product has been updated", HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updatePrice(@PathVariable int id, @RequestBody double newPrice) {
        productService.updatePrice(id, newPrice);
        return new ResponseEntity<>("Product price has been updated", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        productService.delete(id);
        return new ResponseEntity<>("Product has been deleted", HttpStatus.NO_CONTENT);
    }
}
