package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @Column
    private String reviewContent;

    @Column
    private int reviewScore;

    @Column
    private LocalDateTime reviewDate;

    @ManyToOne
    @JoinColumn(name = "providerId")
    private ProviderTable providerId;

    @OneToOne
    @JoinColumn(name = "commentId")
    private CommentTable comment;

}
