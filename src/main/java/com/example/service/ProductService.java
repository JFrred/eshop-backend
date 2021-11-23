package com.example.service;

import com.example.dto.ProductRequest;
import com.example.dto.ProductRepresentation;

import java.util.List;

public interface ProductService {
    ProductRepresentation get(int productId);

    List<ProductRepresentation> getAll(int page, int size);

    List<ProductRepresentation> getAllByUsernamePaginated(String username, int page, int size);

    List<ProductRepresentation> getByCategoryPaginated(String categoryName, int page, int size);

    List<ProductRepresentation> getByNamePaginated(String productName, int page, int size);

    int save(ProductRequest product);

    int update(int id, ProductRequest productRequest);

    int updatePrice(int productId, double newPrice);

    void delete(int productId);

}
