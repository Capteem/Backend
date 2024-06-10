package com.plog.demo.dto.chat;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResponseDto {

    private List<ChatRoomDto> chatRoomDtoList;
}
