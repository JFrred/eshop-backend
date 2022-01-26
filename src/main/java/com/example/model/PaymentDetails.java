package com.example.model;

import com.example.model.abstracts.BaseEntity;
import com.example.model.enums.PaymentStatus;
import com.example.model.enums.PaymentType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_details")
public class PaymentDetails extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderDetails orderDetails;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public PaymentDetails(PaymentType type, PaymentStatus status) {
        this.type = type;
        this.status = status;
    }

    @Override
    public String toString() {
        return "PaymentDetails{" +
                "orderDetails=" + orderDetails +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
