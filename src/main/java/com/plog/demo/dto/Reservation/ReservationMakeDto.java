package com.plog.demo.dto.Reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReservationMakeDto {

    private String userId;
    private int reservationId;
    private int provider_studio;
    private int provider_camera;
    private int provider_hair;
    private Date reservation_date;

}
