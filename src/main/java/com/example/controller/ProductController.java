package com.example.controller;

import com.example.dto.ProductRepresentation;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductRepresentation>> getAll(@RequestParam int page,
                                                              @RequestParam int size) {
        return ResponseEntity.ok(productService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductRepresentation> getById(@PathVariable int id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<ProductRepresentation>> getByName(@RequestParam String name,
                                                                 @RequestParam int page,
                                                                 @RequestParam int size) {
        return ResponseEntity.ok(productService.getByNamePaginated(name, page, size));
    }

    @GetMapping("/categories/{category}")
    public ResponseEntity<List<ProductRepresentation>> getByCategory(@PathVariable String category,
                                                                     @RequestParam int page,
                                                                     @RequestParam int size) {
        return ResponseEntity.ok(productService.getByCategoryPaginated(category, page, size));
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<List<ProductRepresentation>> getByUsername(@PathVariable String username,
                                                                     @RequestParam int page,
                                                                     @RequestParam int size) {
        return ResponseEntity.ok(productService.getAllByUsernamePaginated(username, page, size));
    }

}