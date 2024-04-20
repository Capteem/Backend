package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int providerId;
    @Column
    private String providerName;
    @Column
    private String providerArea;
    @Column
    private String providerSubArea;
    @Column
    private String providerDetailArea;
    @Column
    private String providerPrice;
    @Column
    private String providerPhoneNum;
    @Column
    private int providerType;
    @Column
    private int providerStatus;


    @OneToOne
    @JoinColumn(name = "userId")
    private IdTable userId;
}
