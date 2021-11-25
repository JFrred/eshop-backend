package com.example.service.impl;

import com.example.model.CartItem;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.enums.ProductCategory;
import com.example.model.Cart;
import com.example.repository.ProductRepository;
import com.example.repository.CartRepository;
import com.example.repository.CartItemRepository;
import com.example.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class CartServiceImplTest {
    @Mock
    private CartRepository sessionRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private AuthService authService;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private Product product;
    private CartItem cartItem;
    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        product = new Product("test product", "test description", ProductCategory.ELECTRONICS, 1d);
        user = new User();
        cart = new Cart(user);
        cart.setTotalPrice(100);
        cartItem = new CartItem(cart, product, 1);
        given(sessionRepository.findByUser(any())).willReturn(Optional.of(cart));
        given(productRepository.findById(1)).willReturn(Optional.of(product));
        given(cartItemRepository.save(any())).willReturn(cartItem);
    }

    @Test
    void saveCartItem_ShouldSucceed() {
        double totalPriceBefore = cart.getTotalPrice();

        cartService.saveCartItem(1, 2);

        double totalPriceAfter = cart.getTotalPrice();

        assertThat(totalPriceBefore).isEqualTo(100);
        assertThat(totalPriceAfter).isEqualTo(102);
    }

    @Test
    void deleteCartItem_ShouldSucceed() {
        cartItem.setQuantity(10);
        double totalPriceBefore = cart.getTotalPrice();
        given(cartItemRepository.findById(1)).willReturn(Optional.of(cartItem));

        cartService.deleteCartItem(1, 4);

        double totalPriceAfter = cart.getTotalPrice();

        assertThat(cartItem.getQuantity()).isEqualTo(6);
        assertThat(totalPriceBefore).isEqualTo(100);
        assertThat(totalPriceAfter).isEqualTo(96);
    }

    @Test
    void deleteCartItem_WithQuantityToDeleteHigherThenTotalItemQuantity_ShouldThrowIAE() {
        cartItem.setQuantity(5);
        given(cartItemRepository.findById(1)).willReturn(Optional.of(cartItem));

        assertThrows(IllegalArgumentException.class, () ->
                cartService.deleteCartItem(1, 10));
    }

    @Test
    void deleteAllCartItems_ShouldSucceed() {
        double totalPriceBefore = cart.getTotalPrice();

        cartService.deleteAllCartItems();

        double totalPriceAfter = cart.getTotalPrice();

        assertThat(totalPriceBefore).isEqualTo(100);
        assertThat(totalPriceAfter).isZero();
    }
}