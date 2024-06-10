package com.plog.demo.service.chat;

import com.plog.demo.dto.chat.ChatMessageDto;
import com.plog.demo.model.ChatMessageTable;
import com.plog.demo.model.ChatRoomTable;
import com.plog.demo.repository.ChatMessageTableRepository;
import com.plog.demo.repository.ChatRoomTableRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

    private final ChatRoomTableRepository chatRoomTableRepository;
    private final ChatMessageTableRepository chatMessageTableRepository;


    @Override
    public ChatMessageDto addChatMessage(ChatMessageDto chatMessageDto, int roodId){

        ChatRoomTable chatRoom = chatRoomTableRepository.findById(roodId)
                .orElseThrow(() -> new RuntimeException("존재하지 않은 채팅방 입니다."));


        ChatMessageTable chatMessage = ChatMessageTable.builder()
                .sender(chatMessageDto.getSender())
                .senderType(chatMessageDto.getSenderType())
                .message(chatMessageDto.getMessage())
                .sendDate(chatMessageDto.getSendDate())
                .chatRoomTable(chatRoom)
                .build();

        try{
            chatMessageTableRepository.save(chatMessage);
        }catch (Exception e){
            throw new RuntimeException("채팅 메시지 저장 중 에러", e);
        }

        return ChatMessageDto.builder()
                .roomId(roodId)
                .sender(chatMessage.getSender())
                .senderType(chatMessage.getSenderType())
                .sendDate(chatMessage.getSendDate())
                .message(chatMessage.getMessage())
                .build();
    }
}
