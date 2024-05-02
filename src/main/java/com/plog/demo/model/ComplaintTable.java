package com.plog.demo.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
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

    @ManyToOne
    @JoinColumn(name = "id")
    private IdTable userId;

    public ComplaintTable(int complaintId, String complaintTitle, int complaintStatus, String complaintContent, String complaintDate, IdTable userId) {
        this.complaintId = complaintId;
        this.complaintTitle = complaintTitle;
        this.complaintStatus = complaintStatus;
        this.complaintContent = complaintContent;
        this.complaintDate = complaintDate;
        this.userId = userId;
    }
}
