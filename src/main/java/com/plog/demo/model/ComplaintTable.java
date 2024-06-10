package com.plog.demo.model;


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
public class ComplaintTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int complaintId;
    @Column
    private String complaintTitle;
    @Column
    private int complaintStatus;
    @Column
    private String complaintContent;
    @Column
    private String complaintDate;
    @Column
    private int complaintType;

    @Column
    private String complaintUuid;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id")
    private IdTable userId;

    @OneToOne
    @JoinColumn(name = "complaintAnswerId")
    private ComplaintAnswerTable complaintAnswerId;

}
