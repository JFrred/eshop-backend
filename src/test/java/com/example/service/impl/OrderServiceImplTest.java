package com.example.service.impl;

import com.example.dto.OrderItemRequest;
import com.example.dto.OrderRequest;
import com.example.mapper.OrderMapper;
import com.example.model.enums.PaymentType;
import com.example.repository.*;
import com.example.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderDetailsRepository orderDetailsRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private PaymentDetailsRepository paymentDetailsRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private AuthService authService;
    @Mock
    private OrderMapper orderMapper;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setup() {
        orderService = new OrderServiceImpl(orderDetailsRepository, orderItemRepository, paymentDetailsRepository,
                productRepository, cartRepository, cartItemRepository, authService, orderMapper);
    }

    @Test
    void orderCartItems_itemListIsNull_shouldFail() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setPaymentType(PaymentType.TRANSFER.getValue());
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.orderCartItems(orderRequest));

        String expectedMessage = "Products not selected";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage).containsIgnoringCase(expectedMessage);
    }

    @Test
    void orderCartItems_itemListIsEmpty_shouldFail() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setPaymentType(PaymentType.TRANSFER.getValue());
        orderRequest.setItems(Collections.emptyList());
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.orderCartItems(orderRequest));

        String expectedMessage = "Products not selected";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage).containsIgnoringCase(expectedMessage);
    }

    @Test
    void orderCartItems_paymentTypeIsNull_shouldFail() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItems(List.of(new OrderItemRequest()));
        Exception exception = assertThrows(RuntimeException.class,
                () -> orderService.orderCartItems(orderRequest));

        String expectedMessage = "Payment type must not be null";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage).containsIgnoringCase(expectedMessage);
    }

    @Test
    void orderCartItems_paymentTypeEmpty_shouldFail() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItems(List.of(new OrderItemRequest()));
        orderRequest.setPaymentType("");
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.orderCartItems(orderRequest));

        String expectedMessage = "Invalid payment type";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage).containsIgnoringCase(expectedMessage);
    }

}