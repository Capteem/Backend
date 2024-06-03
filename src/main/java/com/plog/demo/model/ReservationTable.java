package com.plog.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
    private boolean reservation_camera_confirm;
    @Column
    private boolean reservation_studio_confirm;
    @Column
    private boolean reservation_hair_confirm;

    @Column
    private int status;

    @Column
    private int amount;

    @OneToOne
    private PaymentTable tid;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="id")
    private IdTable userId;

}
