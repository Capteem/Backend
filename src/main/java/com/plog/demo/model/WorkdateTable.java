package com.plog.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.plog.demo.dto.Provider.ProviderDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "providerId")
})
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class WorkdateTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workDateId;

    @Column
    private String workDate;

    @Column
    private String workDay;

    @Column
    private String workTime;

    @ManyToOne
    @JoinColumn(name="providerId")
    private ProviderTable providerId;

}
