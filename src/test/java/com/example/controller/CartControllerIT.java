package com.example.controller;

import com.example.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerIT {
    private final String CART_PATH = "/api/cart/";
    private final String CART_PATH_WITH_ID = "/api/cart/{id}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    void addProduct_WithUnauthorizedRequest_Status401() throws Exception {
        mockMvc.perform(post(CART_PATH)
                        .param("quantity", "1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteProduct_WithUnauthorizedRequest_Status401() throws Exception {
        mockMvc.perform(delete(CART_PATH_WITH_ID, 1)
                        .param("quantity", "2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteAllProducts_WithUnauthorizedRequest_Status401() throws Exception {
        mockMvc.perform(delete(CART_PATH + "/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void addProduct_WithAuthorizedRequest_Status202() throws Exception {
        mockMvc.perform(post(CART_PATH_WITH_ID, 1)
                        .param("quantity", "3"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Product has been added to cart"));
    }

    @Test
    @WithMockUser
    void deleteProducts_WithAuthorizedRequest_Status204() throws Exception {
        mockMvc.perform(delete(CART_PATH_WITH_ID, 1)
                .param("quantity", "4"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Product has been removed from cart"));
    }


    @Test
    @WithMockUser
    void deleteAllProducts_WithUnauthorizedRequest_Status204() throws Exception {
        mockMvc.perform(delete(CART_PATH + "/all"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Products have been removed from cart"));
    }


}