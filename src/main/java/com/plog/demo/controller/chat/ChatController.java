package com.plog.demo.controller.chat;


import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.chat.ChatMessageDto;
import com.plog.demo.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/{roomId}") //전송
    @SendTo("/room/{roomId}") //구독
    public ChatMessageDto chat(@DestinationVariable int roomId, ChatMessageDto chatMessageDto){

        chatService.addChatMessage(chatMessageDto, roomId);;

        return ChatMessageDto.builder()
                .roomId(roomId)
                .sender(chatMessageDto.getSender())
                .message(chatMessageDto.getMessage())
                .senderType(chatMessageDto.getSenderType())
                .sendDate(chatMessageDto.getSendDate())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    public void runtimeExceptionHandler(Exception e){
        log.error("runtimeExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .msg("메시지 전송 중 오류가 발생하였습니다.")
                .build();

        messagingTemplate.convertAndSend("/room/errors", errorDto);
    }

    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e){
        log.error("runtimeExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .msg("메시지 전송 중 오류가 발생하였습니다.")
                .build();

        messagingTemplate.convertAndSend("/room/errors", errorDto);
    }

}
