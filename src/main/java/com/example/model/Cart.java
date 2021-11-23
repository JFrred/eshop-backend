package com.example.model;

import com.example.model.abstracts.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shopping_cart")
public class Cart extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_price")
    private double totalPrice;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "cart",
            cascade = CascadeType.ALL)
    private List<CartItem> items;

    public Cart(User user) {
        this.user = user;
    }
}
