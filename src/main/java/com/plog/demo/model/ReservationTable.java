package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;

import java.time.LocalDateTime;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(columnList = "reservation_camera"),
        @Index(columnList = "reservation_studio"),
        @Index(columnList = "reservation_hair")
})
public class ReservationTable {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int reservationId;

    @Column
    private int reservation_camera;

    @Column
    private String reservationCameraName;

    @Column
    private int reservation_studio;

    @Column
    private String reservationStudioName;

    @Column
    private int reservation_hair;

    @Column
    private String reservationHairName;

    @Column
    private LocalDateTime reservation_start_date;

    @Column
    private LocalDateTime reservation_end_date;

    @Column
    private int status;

    @Column
    private int amount;

    @ManyToOne
    @JoinColumn(name="id")
    private IdTable userId;

}
