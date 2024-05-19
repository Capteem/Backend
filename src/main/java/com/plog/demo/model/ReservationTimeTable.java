package com.plog.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class ReservationTimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int timeId;

    private String reservationDate;
    private String reservationTime;

    @ManyToOne
    @JoinColumn(name = "providerId")
    private ProviderTable providerId;

}
