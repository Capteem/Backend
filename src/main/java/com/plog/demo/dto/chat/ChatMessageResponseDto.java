package com.plog.demo.dto.chat;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponseDto {

    private List<ChatMessageDto> chatMessageDtoList;
}
