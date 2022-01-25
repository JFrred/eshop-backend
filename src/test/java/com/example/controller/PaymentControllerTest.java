package com.example.controller;

import com.example.dto.PaymentRepresentation;
import com.example.model.OrderDetails;
import com.example.model.PaymentDetails;
import com.example.model.User;
import com.example.model.embeddable.Address;
import com.example.model.enums.PaymentStatus;
import com.example.model.enums.PaymentType;
import com.example.model.enums.Role;
import com.example.repository.OrderDetailsRepository;
import com.example.repository.PaymentDetailsRepository;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    //    @Autowired
//    private PaymentService paymentService;
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${jwt}")
    String token;

    PaymentDetails paymentDetails;

    @BeforeEach
    void init() {
        User user = userRepository.save(new User("testuser", "password", "firstname", "lastnaem", "email", "acc_num",
                Role.USER, new Address("city", "street", "postal")));
        OrderDetails orderDetails = orderDetailsRepository.save(new OrderDetails(user));
        orderDetails.setTotalPrice(100);
        orderDetailsRepository.save(orderDetails);

        paymentDetails = paymentDetailsRepository.save(
                new PaymentDetails(
                        orderDetails, PaymentType.TRANSFER, PaymentStatus.PENDING));
    }

    @Test
    void get_shouldSucceed_withStatus200() throws Exception {
        String response = mockMvc.perform(get("/api/payments/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        PaymentRepresentation paymentRepresentation = objectMapper.readValue(response, PaymentRepresentation.class);

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