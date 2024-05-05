package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderCheckTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int providerCheckId;

    @Column
    private String storedFileName;

    @ManyToOne
    @JoinColumn(name = "id")
    IdTable id;
}
