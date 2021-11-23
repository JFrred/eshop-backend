package com.example.service.impl;

import com.example.dto.CartRepresentation;
import com.example.exception.ProductNotFoundException;
import com.example.mapper.CartMapper;
import com.example.model.CartItem;
import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartItemRepository;
import com.example.repository.ProductRepository;
import com.example.repository.CartRepository;
import com.example.service.AuthService;
import com.example.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final AuthService authService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Override
    public CartRepresentation get() {
        Cart cart = findCartByUser();
        if (cart.getItems() == null)
            throw new RuntimeException("Your cart is empty");
        return cartMapper.mapCartToRepresentation(cart);
    }

    private Cart findCartByUser() {
        return cartRepository.findByUser(authService.getCurrentUser())
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    @Transactional
    public int saveCartItem(int productId) {
        Cart cart = findCartByUser();
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException(productId));

        cart.setTotalPrice(cart.getTotalPrice() + product.getPrice());

        return cartItemRepository.save(new CartItem(cart, product, 1)).getId();
    }

    @Override
    public void deleteCartItem(int itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("No cart item with id=" + itemId));
        Cart cart = findCartByUser();
        cart.setTotalPrice(cart.getTotalPrice() - cartItem.getProduct().getPrice());

        cartItemRepository.delete(cartItem);
    }

}
