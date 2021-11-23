package com.example.controller;

import com.example.dto.ProductRepresentation;
import com.example.exception.ProductNotFoundException;
import com.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIT {
    private final String PRODUCT_PATH = "/products";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductRepresentation productRepresentation;
    private ProductRepresentation productRepresentation1;

    @BeforeEach
    void setup() {
        productRepresentation = new ProductRepresentation(
                "product name 0", "ELECTRONICS", 65, Instant.now());
        productRepresentation1 = new ProductRepresentation(
                "product name 1", "SPORT", 123, Instant.now());
    }

    @Test
    void getAll_thenReturnListOfPaginatedProductViews() throws Exception {
        given(productService.getAll(0, 2)).willReturn(mockedData());

        mockMvc.perform(get(PRODUCT_PATH + "?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[1].name").value("product name 1"));
    }

    @Test
    void getByName_thenReturnListOf2DTOsWithStatusIsOk() throws Exception {
        given(productService.getByNamePaginated("keyboard", 0, 2)).willReturn(mockedData());
        mockMvc.perform(get(PRODUCT_PATH + "/by-name?name=keyboard&page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("product name 0"))
                .andExpect(jsonPath("$[1].name").value("product name 1"));

    }

    private List<ProductRepresentation> mockedData() {
        return List.of(productRepresentation, productRepresentation1);
    }

    @Test
    void getByName_thenReturnEmptyListWithStatusIsOk() throws Exception {
        given(productService.getByNamePaginated("keyboard", 0, 2)).willReturn(Collections.emptyList());

        mockMvc.perform(get(PRODUCT_PATH + "/by-name?name=giraffe&page=0&size=1"))
                .andExpect(content().string("[]"))
                .andExpect(status().isOk());
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

        mockMvc.perform(get(PRODUCT_PATH + "/categories/electronics?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getByUsername_thenStatusIsOK() throws Exception {
        given(productService.getAllByUsernamePaginated("test_username", 0, 2))
                .willReturn(mockedData());
        mockMvc.perform(get(PRODUCT_PATH + "/users/test_username?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}