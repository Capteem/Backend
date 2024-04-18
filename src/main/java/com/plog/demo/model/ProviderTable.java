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
    private String providerId;
    @Column
    private String providerName;
    @Column
    private String providerAddress;
    @Column
    private String providerPrice;

    @OneToOne
    @JoinColumn(name = "userId")
    private IdTable userId;
}
