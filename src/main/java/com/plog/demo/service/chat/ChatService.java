package com.plog.demo.service.chat;

import com.plog.demo.dto.chat.ChatMessageDto;

public interface ChatService {

    ChatMessageDto addChatMessage(ChatMessageDto chatMessageDto, int roodId);
}
