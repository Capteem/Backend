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
public class ComplaintAnswerTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int complaintAnswerId;

    @Column
    private String complaintAnswerContent;

    @Column
    private String complaintAnswerDate;

    @OneToOne
    @JoinColumn(name = "complaintId")
    private ComplaintTable complaintId;

}
