package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatRoomTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatRoomId;

    @Column
    private String userId;

    @Column
    private String userNickName;

    @Column
    private int providerId;

    @Column
    private String providerName;

    @Column
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "chatRoomTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageTable> chatMessages;
}
