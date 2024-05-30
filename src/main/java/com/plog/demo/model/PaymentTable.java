package com.plog.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

    @Column
    private int paymentStatus;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id")
    private IdTable userId;
}
