package com.plog.demo.model;

import com.plog.demo.dto.Provider.ProviderDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
public class WorkdateTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workdateId;

    @Column
    private LocalDateTime workdate;

    @ManyToOne
    @JoinColumn(name="providerId")
    private ProviderTable providerId;

}
