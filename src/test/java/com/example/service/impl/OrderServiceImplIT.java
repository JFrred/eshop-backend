package com.example.service.impl;

import com.example.dto.OrderDetailsResponse;
import com.example.dto.OrderItemRequest;
import com.example.dto.OrderRequest;
import com.example.model.enums.PaymentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = "/db/populateDb.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class OrderServiceImplIT {
    private final String PATH = "/api/orders";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt}")
    String token;

    OrderRequest orderRequest;

    @BeforeEach
    public void setup() {
        OrderItemRequest orderItemRequest1 = new OrderItemRequest(1, 1);
        OrderItemRequest orderItemRequest2 = new OrderItemRequest(2, 2);
        List<OrderItemRequest> orderItems = List.of(orderItemRequest1, orderItemRequest2);
        orderRequest = new OrderRequest(orderItems,
                "fullname", "email", "city", "street",
                "postalCode", PaymentType.TRANSFER.getValue());
    }

    @Test
    void getAll_shouldFail_status401() throws Exception {
        mockMvc.perform(get(PATH))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAll_shouldSucceed_status200() throws Exception {
        String responseJson = mockMvc.perform(get(PATH)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertThat(responseJson).isNotEmpty();

        OrderDetailsResponse orderDetailsResponse = new OrderDetailsResponse();

        assertThat(orderDetailsResponse.getOrderDetails()).hasSize(1);
        assertThat(orderDetailsResponse.getOrderDetails().get(0).getItems()).hasSize(2);
    }

    @Test
    void orderSelectedFromCart_shouldFail_status401() throws Exception {
        mockMvc.perform(get(PATH)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void orderSelectedFromCart_shouldSucceed_status201() throws Exception {
        mockMvc.perform(post(PATH)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

}