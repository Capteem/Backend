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
//@Table(uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"reservation_camera", "reservation_date"}),
//        @UniqueConstraint(columnNames = {"reservation_studio", "reservation_date"}),
//        @UniqueConstraint(columnNames = {"reservation_hair", "reservation_date"})
//})
public class ReservationTable {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int reservationId;

    @Column
    private int reservation_camera;

    @Column
    private int reservation_studio;

    @Column
    private int reservation_hair;

    @Column
    private LocalDateTime reservation_start_date;

    @Column
    private LocalDateTime reservation_end_date;

    @Column
    private String status;

    @ManyToOne
    @JoinColumn(name="id")
    private IdTable userId;

}
