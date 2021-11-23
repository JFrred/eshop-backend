package com.example.service.impl;

import com.example.dto.ProductRepresentation;
import com.example.dto.ProductRequest;
import com.example.exception.ProductNotFoundException;
import com.example.model.Product;
import com.example.mapper.ProductViewMapper;
import com.example.model.enums.ProductCategory;
import com.example.repository.ProductRepository;
import com.example.service.AuthService;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductViewMapper productViewMapper;
    private final AuthService authService;

    @Override
    public ProductRepresentation get(int id) {
        return productRepository.findById(id)
                .stream().map(productViewMapper::mapSourceToView).findAny()
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public List<ProductRepresentation> getAll(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size))
                .stream().map(productViewMapper::mapSourceToView)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductRepresentation> getByNamePaginated(String productName, int page, int size) {
        String trimmed = productName.trim();

        return productRepository.findAll(PageRequest.of(page, size))
                .stream().filter(p -> StringUtils.containsIgnoreCase(p.getName(), trimmed))
                .map(productViewMapper::mapSourceToView)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductRepresentation> getAllByUsernamePaginated(String username, int page, int size) {
        String trimmedUsername = username.trim();
        return productRepository.findAll(PageRequest.of(page, size))
                .filter(p -> StringUtils.containsIgnoreCase(p.getSeller().getUsername(), trimmedUsername))
                .stream().map(productViewMapper::mapSourceToView)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductRepresentation> getByCategoryPaginated(String categoryName, int page, int size) {
        String trimmedUpperCaseCategory = categoryName.trim().toUpperCase();
        ProductCategory category = productViewMapper.getProductCategory(trimmedUpperCaseCategory);

        return productRepository.findAll(PageRequest.of(page, size))
                .filter(p -> p.getCategory() == category)
                .stream().map(productViewMapper::mapSourceToView)
                .collect(Collectors.toList());
    }

    @Override
    public int save(ProductRequest productRequest) {
        Product product = productViewMapper.mapProductDtoToSource(
                productRequest, authService.getCurrentUser());
        return productRepository.save(product).getId();
    }

    @Override
    @Transactional
    public int update(int id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        product.setName(request.getName());
        product.setDescription(request.getName());
        product.setCategory(productViewMapper.getProductCategory(request.getCategory()));
        product.setPrice(request.getPrice());

        return id;
    }

    @Override
    @Transactional
    public int updatePrice(int id, double newPrice) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        product.setPrice(newPrice);

        return id;
    }

    @Override
    public void delete(int id) {
        productRepository.deleteById(id);
    }
}
