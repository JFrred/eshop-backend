package com.example.controller;

import com.example.dto.OrderDetailsResponse;
import com.example.dto.OrderItemRequest;
import com.example.dto.OrderRequest;
import com.example.model.*;
import com.example.model.embeddable.Address;
import com.example.model.embeddable.BillingAddress;
import com.example.model.enums.PaymentStatus;
import com.example.model.enums.PaymentType;
import com.example.model.enums.ProductCategory;
import com.example.model.enums.Role;
import com.example.repository.*;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderControllerIT {
    private final String PATH = "/api/orders";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Value("${jwt}")
    String token;

    OrderRequest orderRequest;
    double totalPrice;

    @BeforeEach
    public void setup() {
        User user = userRepository.save(new User("testuser", "password", "firstname", "lastnaem", "email", "acc_num", Role.USER, new Address("city", "street", "postal")));

        Cart cart = cartRepository.save(new Cart(user));
        user.setCart(cart);

        Product product1 = new Product("specialized stumpljumper", "desc for trek madone", ProductCategory.MOUNTAIN, 1000d);
        Product product2 = new Product("pinarello dogma", "desc for pinarello dogma", ProductCategory.ROAD, 2000d);
        productRepository.saveAll(List.of(product1, product2));

        CartItem cartItem1 = cartItemRepository.save(new CartItem(cart, product1, 1));
        CartItem cartItem2 = cartItemRepository.save(new CartItem(cart, product2, 2));
        List<CartItem> cartItems = List.of(cartItem1, cartItem2);

        totalPrice = cartItem1.getQuantity() * cartItem1.getProduct().getPrice() +
                cartItem2.getQuantity() * cartItem2.getProduct().getPrice();
        cart.setItems(cartItems);
        cart.setTotalPrice(totalPrice);


        OrderDetails orderDetails = new OrderDetails(user);
        orderDetails.setTotalPrice(totalPrice);
        orderDetails.setBillingAddress(new BillingAddress("fullname", "email", user.getAddress()));
        orderDetailsRepository.save(orderDetails);

        PaymentDetails paymentDetails = paymentDetailsRepository.save(new PaymentDetails(orderDetails, PaymentType.TRANSFER, PaymentStatus.PENDING));

        OrderItem orderItem1 = orderItemRepository.save(new OrderItem(orderDetails, product1, 1));
        OrderItem orderItem2 = orderItemRepository.save(new OrderItem(orderDetails, product2, 2));
        orderDetails.setOrderItems(List.of(orderItem1, orderItem2));


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
        String json = mockMvc.perform(get(PATH)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        OrderDetailsResponse orderDetailsResponse =
                objectMapper.readValue(json, OrderDetailsResponse.class);

        System.out.println("list size: " + orderDetailsResponse.getOrderDetails().size());

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