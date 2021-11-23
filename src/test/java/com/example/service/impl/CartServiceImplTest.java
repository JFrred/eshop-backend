package com.example.service.impl;

import com.example.model.CartItem;
import com.example.model.Product;
import com.example.model.enums.ProductCategory;
import com.example.model.Cart;
import com.example.repository.ProductRepository;
import com.example.repository.CartRepository;
import com.example.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class CartServiceImplTest {
    @Mock
    CartRepository sessionRepository;
    @Mock
    CartItemRepository cartItemRepository;
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    CartServiceImpl service;

    private Cart cart;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        product = new Product("test product", "test description", ProductCategory.ELECTRONICS, 1.23);
        cart = new Cart();
        cartItem = new CartItem(cart, product, 1);
        given(sessionRepository.findByUser(any())).willReturn(Optional.of(cart));
        given(productRepository.findById(anyInt())).willReturn(Optional.of(product));
        given(cartItemRepository.save(any())).willReturn(cartItem);
    }

//    @Test
//    void add_ProductToEmptyShoppingSession_ThenSaveNewCartItem() {
//        assert cart.getTotalPrice() == 0;
//        CartItem actualCartItem = service.add(1, 1);
//
//        assertThat(actualCartItem.getProduct()).isEqualTo(product);
//        assertThat(actualCartItem.getSession()).isEqualTo(cart);
//    }
//
//    @Test
//    void remove_FromNotEmptyShoppingSession_ThenOk() {
//        given(cartItemRepository.findBySessionAndProductId(any(), anyInt()))
//                .willReturn(Optional.of(cartItem));
//
//        service.remove(1, 1);
//
//        verify(cartItemRepository).delete(cartItem);
//    }
//
//    @Test
//    void remove_FromEmptyShoppingSession_ThenThrow() {
//        Exception exception = assertThrows(ProductNotFoundException.class,
//                () -> service.remove(1, 1));
//
//        assertThat(exception).isInstanceOf(ProductNotFoundException.class);
//    }

}