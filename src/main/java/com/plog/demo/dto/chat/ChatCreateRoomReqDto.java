package com.plog.demo.dto.chat;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatCreateRoomReqDto {

    private String userId;

    private String userNickName;

    private int providerId;

    private String providerName;

    private LocalDateTime creationDate;
}
