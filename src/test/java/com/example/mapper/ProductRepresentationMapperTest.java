package com.example.mapper;

import com.example.dto.ProductRepresentation;
import com.example.dto.ProductRequest;
import com.example.exception.IllegalProductCategoryNameException;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.enums.ProductCategory;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ProductRepresentationMapperTest {

    @Autowired
    private ProductViewMapperImpl mapper;

    @Mock
    private User user;

    @Test
    void mapSourceToView() {
        Product product = new Product("test product", "description",
                ProductCategory.E_BIKE, 1.23);
        ProductRepresentation productRepresentation = mapper.mapSourceToView(product);

        assertThat(productRepresentation.getName()).isEqualTo(product.getName());
        assertThat(productRepresentation.getCategory()).isEqualToIgnoringCase(product.getCategory().getValue());
    }

    @Test
    void mapProductDtoToSource() {
        ProductRequest productRequest = new ProductRequest("prod","desc",
                "E_BIKE","imgUrl", 1234);

        Product product = mapper.mapProductDtoToSource(productRequest, user);

        assertThat(product.getCategory()).isEqualTo(ProductCategory.E_BIKE);
    }


    @Test
    void getProductCategory_ShouldSucceed() {
        String validCategoryName = ProductCategory.MOUNTAIN.getValue();
        ProductCategory productCategory = mapper.getProductCategory(validCategoryName);

        assertThat(productCategory).isInstanceOf(ProductCategory.class);
        assertThat(productCategory.getValue()).isEqualTo(validCategoryName);
    }

    @Test
    void getProductCategory_ShouldFail() {
        String invalidCategoryName = "INVALID_CATEGORY";
        IllegalProductCategoryNameException exception = assertThrows(IllegalProductCategoryNameException.class,
                () -> mapper.getProductCategory(invalidCategoryName));

        assertThat(exception).isInstanceOf(IllegalProductCategoryNameException.class);
    }

}