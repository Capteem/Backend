package com.plog.demo.service.chat;

import com.plog.demo.dto.chat.ChatCreateRoomReqDto;
import com.plog.demo.dto.chat.ChatCreateRoomResDto;
import com.plog.demo.dto.chat.ChatMessageDto;
import com.plog.demo.dto.chat.ChatRoomDto;
import com.plog.demo.exception.CustomException;

import java.util.List;

public interface ChatRoomService {

    ChatCreateRoomResDto createRoom(ChatCreateRoomReqDto chatCreateRoomReqDto) throws CustomException;

    void deleteRoom(int roomId) throws CustomException;

    List<ChatRoomDto> getByUserId(String userId) throws CustomException;

    List<ChatRoomDto> getByProviderId(int providerId) throws CustomException;

    List<ChatMessageDto> getChatMessages(int roomId) throws CustomException;
}
