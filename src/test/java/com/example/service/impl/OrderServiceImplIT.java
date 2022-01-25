package com.example.service.impl;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderServiceImplIT {
//    @Autowired
//    private OrderDetailsRepository orderDetailsRepository;
//    @Autowired
//    private OrderItemRepository orderItemRepository;
//    @Autowired
//    private PaymentDetailsRepository paymentDetailsRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private CartRepository cartRepository;
//    @Autowired
//    private CartItemRepository cartItemRepository;
//    @Autowired
//    private OrderMapper orderMapper;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private OrderService orderService;
//
//    private User user;
//
//    @BeforeEach
//    void setup() {
//        orderService = new OrderServiceImpl(orderDetailsRepository, orderItemRepository, paymentDetailsRepository, productRepository, cartRepository, cartItemRepository, authService, orderMapper);
//        Address address = new Address("city", "street", "12-345");
//        user = userRepository.save(new User("testuser", "some_password", "first name", "last name", "email", "12312412412", Role.USER, address));
//        given(authService.getCurrentUser()).willReturn(user);
//    }
//
//    @Test
//    @WithMockUser
//    void getAll_shouldSucceed() {
//        initDb();
//        List<OrderDetailsRepresentation> orderDetails = orderService.getAll();
//
//        assertThat(orderDetails).hasSize(3);
//    }
//
//    private void initDb() {
//        List<OrderDetails> entities = List.of(new OrderDetails(user), new OrderDetails(user), new OrderDetails(user));
//        orderDetailsRepository.save(new OrderDetails(user));
//        orderDetailsRepository.saveAll(entities);
//    }

//    @Test
//    void getById_shouldSucceed() {
//
//    }
//
//    @Test
//    void orderCartItems_shouldSucceed() {
//
//    }
    //should fail ...
}