package com.plog.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenTable {

    @Id
    private String refreshToken;

    @Column
    private String accessToken;

    @Column
    private String userId;
}
