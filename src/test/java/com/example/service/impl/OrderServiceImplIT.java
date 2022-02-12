package com.example.service.impl;

import com.example.controller.OrderController;
import com.example.controller.handler.GlobalExceptionHandler;
import com.example.dto.OrderDetailsRepresentation;
import com.example.dto.OrderItemRequest;
import com.example.dto.OrderRequest;
import com.example.dto.OrderViewResponse;
import com.example.model.User;
import com.example.model.enums.PaymentType;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@Sql(value = "/db/populateDb.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) //TODO: get rid of it
public class OrderServiceImplIT {

    private final String PATH = "/api/orders";
    /**
     * ID of the user from /db/populateDb.sql
     */
    private static final int USER_ID = 1;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderController orderController;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private Authentication mockedAuthentication;
    @Mock
    private SecurityContext mockedSecurityContext;
    @Mock
    private User mockedUser;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void getAll_shouldFail_status401() throws Exception {
        SecurityContextHolder.setContext(mockedSecurityContext);
        given(mockedSecurityContext.getAuthentication()).willReturn(mockedAuthentication);
        mockAuthentication(mockedUser);

        mockMvc.perform(get(PATH))
                .andExpect(status().isForbidden());
    }

    @Test
    void orderSelectedFromCart_shouldFail_status401() throws Exception {
        SecurityContextHolder.setContext(mockedSecurityContext);
        given(mockedSecurityContext.getAuthentication()).willReturn(mockedAuthentication);
        mockAuthentication(mockedUser);

        mockMvc.perform(get(PATH)
                        .content(objectMapper.writeValueAsString(new OrderRequest())))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void orderSelectedFromCart_shouldSucceed_status201() throws Exception {
        List<OrderItemRequest> orderItems = List.of(
                new OrderItemRequest(1, 1),
                new  OrderItemRequest(2, 2));
        OrderRequest orderRequest = new OrderRequest(orderItems,
                "fullname", "email", "city", "street",
                "postalCode", PaymentType.TRANSFER.getValue());

        final User user = userRepository.findById(USER_ID).get();
        final String fakeToken = "fakeToken";

        mockAuthentication(user);

        mockMvc.perform(post(PATH)
                        .header(HttpHeaders.AUTHORIZATION, fakeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void getAll_shouldSucceed_status200() throws Exception {
        final User user = userRepository.findById(USER_ID).get();
        final String fakeToken = "fakeToken";

        mockAuthentication(user);

        String responseJson = mockMvc.perform(get(PATH)
                        .header(HttpHeaders.AUTHORIZATION, fakeToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        OrderViewResponse orderDetailsResponse = objectMapper.readValue(responseJson, OrderViewResponse.class);

        List<OrderDetailsRepresentation> orders = orderDetailsResponse.getOrders();
        assertThat(orders).hasSize(1);
    }

    private void mockAuthentication(User user) {
        SecurityContextHolder.setContext(mockedSecurityContext);
        given(mockedSecurityContext.getAuthentication()).willReturn(mockedAuthentication);
        given(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).willReturn(user);
    }

}