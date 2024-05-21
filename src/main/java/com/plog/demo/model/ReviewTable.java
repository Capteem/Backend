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
@Table(name = "reviewTable",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "providerId", "reviewDate"})},
        indexes = {@Index(columnList = "user_id")}
)
public class ReviewTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @Column
    private String reviewContent;

    @Column
    private int reviewScore;

    @Column
    private String userId;

    @Column
    private String userNickName;

    @Column
    private LocalDateTime reviewDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "providerId")
    private ProviderTable providerId;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "commentId")
    private CommentTable commentId;

}
