package com.plog.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
public class CommentTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int CommentId;

    @Column
    private String CommentContent;

    @Column
    private LocalDateTime CommentDate;

    @OneToOne
    @JoinColumn(name="reviewId")
    private ReviewTable reviewId;
}
