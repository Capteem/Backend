package com.plog.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authId;

    private String email;

    private int auth;

    private LocalDateTime createdAt;
}
