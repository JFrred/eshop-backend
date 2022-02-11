package com.example.mapper;

import com.example.dto.CartItemRepresentation;
import com.example.dto.CartRepresentation;
import com.example.model.Cart;
import com.example.model.CartItem;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.enums.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CartMapperTest {

    @Autowired
    private CartMapperImpl cartMapper;

    @Mock
    private User user;

    private Cart cart;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setup() {
        product = new Product("pinarello dogma", "dogma f12", ProductCategory.ROAD, 9999);
        cartItem = new CartItem(cart, product, 2);
        cart = new Cart(user, 10, List.of(cartItem, cartItem));
    }

    @Test
    void testMapItemSourceToRepresentation() {
        CartItemRepresentation expected = new CartItemRepresentation(1, "img_url_here",
                product.getName(), product.getPrice(), 2, 19998);

        CartItemRepresentation actual = cartMapper.mapItemSourceToRepresentation(cartItem);

        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getTotalPrice()).isEqualTo(expected.getTotalPrice());
    }

    @Test
    void testMapCartToRepresentation() {
        CartRepresentation expected = new CartRepresentation(
                List.of(new CartItemRepresentation(), new CartItemRepresentation()), 10);
        CartRepresentation actual = cartMapper.mapCartToRepresentation(cart);

        assertThat(actual.getItems().size()).isEqualTo(expected.getItems().size());
        assertThat(actual.getTotalPrice()).isEqualTo(expected.getTotalPrice());
    }

    @Test
    void testCalculateTotalPrice() {
        double expected = 19998;
        double actual = cartMapper.calculateTotalPrice(cartItem);

        assertThat(actual).isEqualTo(expected);
    }
}