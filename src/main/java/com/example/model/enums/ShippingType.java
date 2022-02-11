package com.example.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShippingType {
    SELF_PICKUP(0),
    PICKUP_AT_THE_POINT(10),
    DELIVERY_BY_COURIER(15);

    private final double price;
}
