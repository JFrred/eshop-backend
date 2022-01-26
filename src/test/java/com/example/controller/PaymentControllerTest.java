package com.example.controller;

import com.example.dto.PaymentRepresentation;
import com.example.model.enums.PaymentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "/db/populateDb.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt}")
    String token;

    @Test
    void get_shouldSucceed_withStatus200() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/payments/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        assertThat(jsonResponse).isNotEmpty();

        PaymentRepresentation paymentRepresentation =
                objectMapper.readValue(jsonResponse, PaymentRepresentation.class);

        assertThat(paymentRepresentation.getTotalPrice()).isEqualTo(100);
        assertThat(paymentRepresentation.getPaymentType()).isEqualTo(PaymentType.TRANSFER.getValue());
    }

    @Test
    void get_shouldFail_withStatus401() throws Exception {
        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void get_shouldFail_withStatus404() throws Exception {
        mockMvc.perform(get("/api/payments/99")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound());
    }
}