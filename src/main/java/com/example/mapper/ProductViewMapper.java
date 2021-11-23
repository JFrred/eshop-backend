package com.example.mapper;

import com.example.dto.ProductRequest;
import com.example.dto.ProductRepresentation;
import com.example.exception.IllegalProductCategoryNameException;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.enums.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductViewMapper {
    @Mapping(target = "lastModified", source = "product.modifiedAt")
    ProductRepresentation mapSourceToView(Product product);


    @Mapping(target = "category", expression = "java(getProductCategory(request.getCategory()))")
    @Mapping(target = "seller", source = "user")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "modifiedAt", expression = "java(java.time.Instant.now())")
    Product mapProductDtoToSource(ProductRequest request, User user);

    default ProductCategory getProductCategory(String categoryName) {
        try {
            return ProductCategory.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalProductCategoryNameException();
        }
    }

}
