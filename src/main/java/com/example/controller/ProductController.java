package com.example.controller;

import com.example.dto.ProductView;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    //todo: return basic product view
    @GetMapping
    public ResponseEntity<List<ProductView>> getAll(@RequestParam int page,
                                                    @RequestParam int size) {
//        List<ProductView> products = productService.getAll(Integer.parseInt(page), Integer.parseInt(size));
        List<ProductView> products = productService.getAll(page, size);
        log.info("page=" + page + ", size=" + size);
        return ResponseEntity.ok(products);
    }

    //todo: return detailed product view
    @GetMapping("/by-name")
    public ResponseEntity<List<ProductView>> getByName(@RequestParam String name,
                                                       @RequestParam int page,
                                                       @RequestParam int size) {
        return ResponseEntity.ok(productService.getByNamePaginated(name, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductView> getById(@PathVariable int id) {
        ProductView product = productService.get(id);
        log.info("product: " + product);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/categories/{category}")
    public ResponseEntity<List<ProductView>> getByCategory(@PathVariable String category,
                                                           @RequestParam int page,
                                                           @RequestParam int size) {
        List<ProductView> products = productService.getByCategoryPaginated(category, page, size);
        log.info("items count: " + products.size() + " => page=" + page + ", size=" + size);

        return ResponseEntity.ok(products);
    }

    //todo: refactor products component to not need this method
    @GetMapping("/count/{categoryName}")
    public int countProducts(@PathVariable String categoryName) {
        int productsNumber = productService.countCategoryProducts(categoryName);
        log.info("count : " + productsNumber);
        return productsNumber;
    }
}