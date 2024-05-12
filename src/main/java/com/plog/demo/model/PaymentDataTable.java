package com.plog.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor
@Builder
public class PaymentDataTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentDataId;

    private String created_at;
    private String approved_at;

    private int total;
    private int vat;
    private int discount;
    private int point;

    private String kakaopayPurchaseCorp;

    @ManyToOne
    @JoinColumn(name = "id")
    private IdTable idTable;
}
