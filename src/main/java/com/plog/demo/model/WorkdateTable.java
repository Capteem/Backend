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
@Table(indexes = {
        @Index(columnList = "providerId")
})
public class WorkdateTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workDateId;

    @Column
    private String workDate;

    @Column
    private String workTime;

    @ManyToOne
    @JoinColumn(name="providerId")
    private ProviderTable providerId;

}
