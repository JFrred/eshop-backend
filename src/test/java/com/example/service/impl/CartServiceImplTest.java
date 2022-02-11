package com.example.service.impl;

import com.example.mapper.CartMapper;
import com.example.model.Cart;
import com.example.model.CartItem;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.enums.ProductCategory;
import com.example.repository.CartItemRepository;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import com.example.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private AuthService authService;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private CartRepository cartRepository;

    private CartServiceImpl cartService;

    @Mock
    private User user;

    private Cart cart;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setup() {
        cartService = new CartServiceImpl(authService, cartRepository,
                cartItemRepository, productRepository, cartMapper);

        product = new Product("test product", "test description", ProductCategory.ROAD, 1);
        cart = new Cart(user);
        cart.setTotalPrice(100);
        cartItem = new CartItem(cart, product, 1);
        given(cartRepository.findByUser(any())).willReturn(Optional.of(cart));
    }

    @Test
    void saveCartItem_ShouldSucceed() {
        given(productRepository.findById(1)).willReturn(Optional.of(product));
        double totalPriceBefore = cart.getTotalPrice();

        cartService.saveCartItem(1, 2);

        double totalPriceAfter = cart.getTotalPrice();

        assertThat(totalPriceBefore).isEqualTo(100);
        assertThat(totalPriceAfter).isEqualTo(102);
    }

    @Test
    void deleteCartItem_ShouldSucceed() {
        given(cartItemRepository.findByCartAndProductId(any(), anyInt())).willReturn(Optional.of(cartItem));
        cartItem.setQuantity(10);
        double totalPriceBefore = cart.getTotalPrice();

        cartService.deleteCartItem(1, 4);

        double totalPriceAfter = cart.getTotalPrice();

        assertThat(cartItem.getQuantity()).isEqualTo(6);
        assertThat(totalPriceBefore).isEqualTo(100);
        assertThat(totalPriceAfter).isEqualTo(96);
    }

    @Test
    void deleteCartItem_WithQuantityToDeleteHigherThenTotalItemQuantity_ShouldThrowIAE() {
        cartItem.setQuantity(5);

        assertThrows(RuntimeException.class, () ->
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