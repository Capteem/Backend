package com.plog.demo.service.chat;

import com.plog.demo.dto.chat.ChatCreateRoomReqDto;
import com.plog.demo.dto.chat.ChatCreateRoomResDto;
import com.plog.demo.dto.chat.ChatMessageDto;
import com.plog.demo.dto.chat.ChatRoomDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.ChatMessageTable;
import com.plog.demo.model.ChatRoomTable;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.repository.ChatMessageTableRepository;
import com.plog.demo.repository.ChatRoomTableRepository;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.repository.ProviderTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService{

    private final ChatRoomTableRepository chatRoomTableRepository;
    private final ChatMessageTableRepository chatMessageTableRepository;
    private final IdTableRepository idTableRepository;
    private final ProviderTableRepository providerTableRepository;

    @Override
    public ChatCreateRoomResDto createRoom(ChatCreateRoomReqDto chatCreateRoomReqDto) throws CustomException {
        Optional<ChatRoomTable> savedChatRoom = chatRoomTableRepository
                .findByUserIdAndProviderId(chatCreateRoomReqDto.getUserId(), chatCreateRoomReqDto.getProviderId());

        if (savedChatRoom.isPresent()) throw new CustomException("이미 채팅방이 존재합니다.", HttpStatus.CONFLICT.value());

        ChatRoomTable chatRoomTable = ChatRoomTable.builder()
                .userId(chatCreateRoomReqDto.getUserId())
                .userNickName(chatCreateRoomReqDto.getUserNickName())
                .providerId(chatCreateRoomReqDto.getProviderId())
                .providerName(chatCreateRoomReqDto.getProviderName())
                .creationDate(chatCreateRoomReqDto.getCreationDate())
                .build();

        try {
            ChatRoomTable chatRoom = chatRoomTableRepository.save(chatRoomTable);
            return ChatCreateRoomResDto.builder()
                    .roomId(chatRoom.getChatRoomId())
                    .userId(chatRoom.getUserId())
                    .userNickName(chatRoom.getUserNickName())
                    .providerId(chatRoom.getProviderId())
                    .providerName(chatRoom.getProviderName())
                    .creationDate(chatRoom.getCreationDate())
                    .build();

        }catch (Exception e){
            throw new RuntimeException("채팅 방 생성중 에러 발생", e);
        }


    }

    @Override
    public void deleteRoom(int roomId) throws CustomException {

        ChatRoomTable chatRoom = chatRoomTableRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("채팅방이 존재하지 않습니다.", HttpStatus.NOT_FOUND.value()));

        try{
            chatRoomTableRepository.deleteById(chatRoom.getChatRoomId());
        }catch (Exception e){
            throw new RuntimeException("채팅 방 삭제중 에러 발생", e);
        }

    }

    @Override
    public List<ChatRoomDto> getByUserId(String userId) throws CustomException {
        IdTable idTable = idTableRepository.findById(userId)
                .orElseThrow(() -> new CustomException("유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND.value()));

        List<ChatRoomTable> chatRooms = chatRoomTableRepository.findAllByUserId(idTable.getId());

        if(chatRooms.isEmpty()) throw new CustomException("채팅방이 존재하지 않습니다.", HttpStatus.NOT_FOUND.value());

        return chatRooms.stream().map(
                chatRoom -> ChatRoomDto.builder()
                        .roomId(chatRoom.getChatRoomId())
                        .userId(chatRoom.getUserId())
                        .userNickName(chatRoom.getUserNickName())
                        .providerId(chatRoom.getProviderId())
                        .providerName(chatRoom.getProviderName())
                        .creationDate(chatRoom.getCreationDate())
                        .build()).toList();
    }

    @Override
    public List<ChatRoomDto> getByProviderId(int providerId) throws CustomException {
        ProviderTable provider = providerTableRepository.findById(providerId)
                .orElseThrow(() -> new CustomException("서비스가 존재하지 않습니다.", HttpStatus.NOT_FOUND.value()));

        List<ChatRoomTable> chatRooms = chatRoomTableRepository.findAllByProviderId(provider.getProviderId());

        if(chatRooms.isEmpty()) throw new CustomException("채팅방이 존재하지 않습니다.", HttpStatus.NOT_FOUND.value());

        return chatRooms.stream().map(
                chatRoom -> ChatRoomDto.builder()
                        .roomId(chatRoom.getChatRoomId())
                        .userId(chatRoom.getUserId())
                        .userNickName(chatRoom.getUserNickName())
                        .providerId(chatRoom.getProviderId())
                        .providerName(chatRoom.getProviderName())
                        .creationDate(chatRoom.getCreationDate())
                        .build()).toList();
    }

    @Override
    public List<ChatMessageDto> getChatMessages(int roomId) throws CustomException {
        ChatRoomTable chatRoom = chatRoomTableRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("채팅방이 존재하지 않습니다.", HttpStatus.NOT_FOUND.value()));

        List<ChatMessageTable> chatMessages = chatMessageTableRepository.findAllByChatRoomTable(chatRoom);

        if(chatMessages.isEmpty()) return null;

        return chatMessages.stream()
                .map(chatMessage -> ChatMessageDto.builder()
                        .roomId(roomId)
                        .message(chatMessage.getMessage())
                        .sender(chatMessage.getSender())
                        .senderType(chatMessage.getSenderType())
                        .sendDate(chatMessage.getSendDate())
                        .build()
                ).toList();
    }
}
