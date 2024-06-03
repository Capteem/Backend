package com.plog.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessageTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatId;

    @Column
    private String sender;

    @Column
    private String senderType;

    @Column
    private String message;

    @Column
    private LocalDateTime sendDate;

    @ManyToOne
    @JoinColumn(name = "chatRoomId")
    private ChatRoomTable chatRoomTable;
}
