package com.plog.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.plog.demo.dto.workdate.TestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
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

    @OneToMany(mappedBy = "providerId", fetch = FetchType.EAGER)
    private List<WorkdateTable> workdateTableList;

    @ManyToOne
    @JoinColumn(name = "Id")
    private IdTable userId;
}
