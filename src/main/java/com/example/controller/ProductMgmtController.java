package com.example.controller;

import com.example.dto.ProductRequest;
import com.example.service.ProductMgmtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Secured("ROLE_ADMIN")
@RestController
@RequestMapping("/api/mgmt/products")
@RequiredArgsConstructor
public class ProductMgmtController {
    private final ProductMgmtService productService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody ProductRequest productRequest) {
        productService.save(productRequest);
        return new ResponseEntity<>("Product has been saved.", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody ProductRequest product) {
        productService.update(id, product);
        return new ResponseEntity<>("Product has been updated", HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> refreshDate(@PathVariable int id) {
        productService.refreshDate(id);
        return new ResponseEntity<>("Product has been refreshed", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        productService.delete(id);
        return new ResponseEntity<>("Product has been deleted", HttpStatus.NO_CONTENT);
    }
}
