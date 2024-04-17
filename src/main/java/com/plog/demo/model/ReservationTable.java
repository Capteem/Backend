package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Builder
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
    private Date reservation_date;

    @Column
    private String status;

    @ManyToOne
    @JoinColumn(name="id")
    private IdTable userId;

    public ReservationTable(int reservationId, int reservation_camera, int reservation_studio, int reservation_hair, Date reservation_date,String status, IdTable userId) {
        this.reservationId = reservationId;
        this.reservation_camera = reservation_camera;
        this.reservation_studio = reservation_studio;
        this.reservation_hair = reservation_hair;
        this.reservation_date = reservation_date;
        this.userId = userId;
        this.status = status;
    }
}
