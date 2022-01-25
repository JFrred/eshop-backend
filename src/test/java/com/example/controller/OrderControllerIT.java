package com.example.controller;

import com.example.dto.OrderItemRequest;
import com.example.dto.OrderRequest;
import com.example.model.Cart;
import com.example.model.CartItem;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.enums.PaymentType;
import com.example.model.enums.ProductCategory;
import com.example.repository.CartItemRepository;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@TestPropertySource(
//        locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIT {
    private final String PATH = "/api/orders";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;

//    @MockBean //TODO: when mocked it works, because users authorities aren't verified
    @Autowired
    private AuthService authService;

    OrderRequest orderRequest;
    double totalPrice;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        User user = userRepository.save(new User());
        Cart cart = cartRepository.save(new Cart(user));
        user.setCart(cart);

//        given(authService.getCurrentUser()).willReturn(user);

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

        OrderItemRequest orderItemRequest1 = new OrderItemRequest(1, 1);
        OrderItemRequest orderItemRequest2 = new OrderItemRequest(2, 2);
        orderRequest = new OrderRequest(List.of(orderItemRequest1, orderItemRequest2),
                "fullname", "email", "city", "street",
                "postalCode", PaymentType.TRANSFER.getValue());
    }

    @Test
    void getAll_shouldFail_status401() throws Exception {
        mockMvc.perform(get(PATH))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getAll_shouldSucceed_status200() throws Exception {
        String fakeToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImFkbWluIjpmYWxzZSwiaXNzIjoiR3JvYm1laWVyIFNvbHV0aW9ucyBHbWJIIiwiaWF0IjoxNjQzMTI4MzE1LCJleHAiOjE2NDMxNTcxMTV9.eUUFnfXiVYXAKlcjCnej4L3ZTSpXpfbd2EBy6lPeiqgeKDNrkd7P6Hw4aiHbDZP3r2RVGQOp7yrPnmps0w0hDg";
        mockMvc.perform(get(PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + fakeToken))
                .andExpect(status().isOk());
    }

    @Test
    void orderSelectedFromCart_shouldFail_status401() throws Exception {

        mockMvc.perform(get(PATH)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void orderSelectedFromCart_shouldSucceed_status201() throws Exception {
        String fakeToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsIm5hbWUiOiJ0ZXN0dXNlciIsImlhdCI6MTUxNjIzOTAyMn0.QMjnn27bVbRgAkkqHopl5uPfR-umiH1QFkeXS2rVnPo";

        mockMvc.perform(post(PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + fakeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

}



















