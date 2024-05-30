package com.plog.demo.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class CommentTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int CommentId;

    @Column
    private String CommentContent;

    @Column
    private LocalDateTime CommentDate;

}
