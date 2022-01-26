package com.example.controller;

import com.example.dto.ProductRequest;
import com.example.service.ProductMgmtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductMgmtControllerTest {
    private static final String PATH = "/api/mgmt/products";
    private static final String PATH_WITH_ID = PATH + "/{id}";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductMgmtService productService;

    @Test
    void unauthenticatedPostRequest_thenStatus401() throws Exception {
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthenticatedPutRequest_thenStatus401() throws Exception {
        int productId = 1;

        mockMvc.perform(put(PATH_WITH_ID, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthenticatedPatchRequest_thenStatus401() throws Exception {
        int productId = 1;
        mockMvc.perform(patch(PATH_WITH_ID, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthenticatedDeleteRequest_thenStatus401() throws Exception {
        int productId = 1;

        mockMvc.perform(delete(PATH_WITH_ID, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void unauthorizedPostRequest_RoleUSER__thenStatus403() throws Exception {
        ProductRequest productRequest = new ProductRequest();

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void unauthorizedPutRequest_RoleUSER_thenStatus403() throws Exception {
        int productId = 1;
        ProductRequest productRequest = new ProductRequest();

        mockMvc.perform(put(PATH_WITH_ID, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void unauthorizedPatchRequest_RoleUSER_thenStatus403() throws Exception {
        int productId = 1;
        double newPrice = 9.99;

        mockMvc.perform(patch(PATH_WITH_ID, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPrice)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void unauthorizedDeleteRequest_RoleUSER_thenStatus403() throws Exception {
        int productId = 1;

        mockMvc.perform(delete(PATH_WITH_ID, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="admin", roles = {"ADMIN"})
    void authorizedPostRequest_RoleADMIN_thenStatus201() throws Exception {
        ProductRequest productRequest = new ProductRequest();

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="admin", roles = {"ADMIN"})
    void authorizedPutRequest_RoleADMIN_thenStatus204() throws Exception {
        int productId = 1;
        ProductRequest productRequest = new ProductRequest();

        mockMvc.perform(put(PATH_WITH_ID, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username="admin", roles = {"ADMIN"})
    void authorizedPatchPriceRequest_RoleADMIN_thenStatus204() throws Exception {
        int productId = 1;
        double newPrice = 9.99;

        mockMvc.perform(patch(PATH_WITH_ID, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPrice)))
                .andExpect(status().isNoContent());
    }


    @Test
    @WithMockUser(username="admin", roles = {"ADMIN"})
    void authorizedDeleteRequest_RoleADMIN_thenStatus204() throws Exception {
        int productId = 1;

        mockMvc.perform(delete(PATH_WITH_ID, productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}