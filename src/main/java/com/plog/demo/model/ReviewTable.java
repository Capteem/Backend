package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @Column
    private String reviewContext;

    @Column
    private int reviewScore;

    @Column
    private LocalDateTime reviewDate;

    @ManyToOne
    @JoinColumn(name = "providerId")
    private ProviderTable providerTable;

}
