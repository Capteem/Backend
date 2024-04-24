package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PaymentTable {

    @Id
    private String paymentId;

    @Column
    private int paymentPrice;

    @Column
    private int paymentAmount;

    @Column
    private int paymentTaxFreeAmount;

    @ManyToOne
    @JoinTable(name = "id")
    private IdTable userId;
}
