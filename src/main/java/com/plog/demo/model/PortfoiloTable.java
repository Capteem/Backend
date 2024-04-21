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
public class PortfoiloTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int portfoiloId;

    @Column
    private String portfoiloTitle;

    @ManyToOne
    @JoinColumn(name="providerId")
    private ProviderTable providerId;

}
