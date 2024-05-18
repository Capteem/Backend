package com.plog.demo.dto.Provider;

import lombok.*;

import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderReservationDto {

    private int reservationId;
    private String providerName;
    private int providerType;
    private int reservationStatus;
    private LocalDateTime reservationStartTime;
    private LocalDateTime reservationEndTime;
}
