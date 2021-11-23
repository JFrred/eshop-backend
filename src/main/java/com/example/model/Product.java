package com.example.model;

import com.example.model.abstracts.BaseEntity;
import com.example.model.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @NotEmpty(message = "Product name must not be empty")
    @Max(value = 30, message = "Product name must be not longer than 30 characters")
    @Column(name = "name")
    private String name;

    @Min(value = 10, message = "Description must be longer then 10 characters")
    @Max(value = 255, message = "Description must be shorter than 255 characters")
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Min(value = 0, message = "Price cannot be negative")
    @Column(name = "price")
    private double price;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id")
    private User seller;

    public Product(String name, String description, ProductCategory category, double price) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
    }

}
