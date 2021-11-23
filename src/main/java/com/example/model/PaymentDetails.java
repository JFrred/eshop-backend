package com.example.model;

import com.example.model.abstracts.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "payment_details")
public class PaymentDetails extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderDetails orderDetails;

    @Column(name = "amount")
    private int amount;

    @Column(name = "provider")
    private String provider;

    @Column(name = "status")
    private String status;

}
