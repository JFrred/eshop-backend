package com.example.mapper;

import com.example.dto.CartItemRepresentation;
import com.example.dto.CartRepresentation;
import com.example.model.Cart;
import com.example.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CartMapper {

    @Mapping(target = "items", expression = "java(getCartItemsRepresentation(cart))")
    public abstract CartRepresentation mapCartToRepresentation(Cart cart);

    public List<CartItemRepresentation> getCartItemsRepresentation(Cart cart) {
        return cart.getItems().stream()
                .map(this::mapItemSourceToRepresentation)
                .collect(Collectors.toList());
    }

    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(cartItem))")
    public abstract CartItemRepresentation mapItemSourceToRepresentation(CartItem cartItem);

    public double calculateTotalPrice(CartItem cartItem) {
        return cartItem.getQuantity() * cartItem.getProduct().getPrice();
    }
}
