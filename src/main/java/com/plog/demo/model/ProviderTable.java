package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@RequiredArgsConstructor
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
    private int providerPrice;
    @Column
    private String providerPhoneNum;
    @Column
    private int providerTypeInfo;
    @Column
    private int providerType;
    @Column
    private int providerStatus;
    @Column
    private String providerRepPhoto;
    @Column
    private String providerRepPhotoPath;



    @ManyToOne
    @JoinColumn(name = "Id")
    private IdTable userId;
}
