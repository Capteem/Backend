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
public class PortfolioTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int portfolioId;

    @Column
    private String storedFileName;

    //서버에 저장할 날짜 디렉토리
    @Column
    private String dateBasedImagePath;

    @ManyToOne
    @JoinColumn(name="providerId")
    private ProviderTable providerId;

}
