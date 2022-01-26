package com.example.controller;

import com.example.dto.ProductRepresentation;
import com.example.dto.ProductsResponse;
import com.example.exception.ProductNotFoundException;
import com.example.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    private final String PRODUCT_PATH = "/products";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private ProductRepresentation productRepresentation;
    private ProductRepresentation productRepresentation1;

    @BeforeEach
    void setup() {
        productRepresentation = new ProductRepresentation(1, "product name 0",
                "ELECTRONICS", "test description",
                "test-img-url", 65, String.valueOf(Instant.now()));
        productRepresentation1 = new ProductRepresentation(2, "product name 1",
                "SPORT", "test description",
                "test-img-url", 123, String.valueOf(Instant.now()));
    }

    private ProductsResponse mockedData() {
        List<ProductRepresentation> products = List.of(this.productRepresentation, productRepresentation1);
        return new ProductsResponse(products, products.size());
    }

    @Test
    void getAll_thenReturnListOfPaginatedProductViews() throws Exception {
        given(productService.getAll(0, 2)).willReturn(mockedData());

        String jsonResponse = mockMvc.perform(get(PRODUCT_PATH + "?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ProductsResponse actualResponse = objectMapper.readValue(jsonResponse, ProductsResponse.class);

        assertThat(actualResponse.getProducts()).hasSize(2);
        assertThat(actualResponse.getProducts().get(0).getName()).isEqualTo(productRepresentation.getName());
    }

    @Test
    void get_withValidId_thenStatusIsOk() throws Exception {
        given(productService.get(anyInt())).willReturn(productRepresentation);

        mockMvc.perform(get(PRODUCT_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("product name 0"))
                .andExpect(jsonPath("$.price").value(65));
    }

    @Test
    void get_withInvalidId_thenStatus404() throws Exception {
        String expectedBody = "Could not find product with id=1";
        given(productService.get(anyInt())).willThrow(new ProductNotFoundException(1));

        mockMvc.perform(get(PRODUCT_PATH + "/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedBody));

    }

    @Test
    void getByCategory_thenStatusIsOK() throws Exception {
        given(productService.getByCategoryPaginated("electronics", 0, 2))
                .willReturn(mockedData());

        String jsonResponse = mockMvc.perform(get(PRODUCT_PATH + "/categories/electronics?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ProductsResponse actualResponse = objectMapper.readValue(jsonResponse, ProductsResponse.class);

        assertThat(actualResponse.getProducts()).hasSize(2);
        assertThat(actualResponse.getProducts().get(0).getName()).isEqualTo(productRepresentation.getName());
    }

}