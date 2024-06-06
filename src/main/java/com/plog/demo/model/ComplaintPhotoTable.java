package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintPhotoTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int complaintPhotoId;

    private String StoreFileName;

    private String complaintUuid;

}
