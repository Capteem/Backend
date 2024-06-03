package com.plog.demo.controller.chat;

import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.SuccessDto;
import com.plog.demo.dto.chat.ChatCreateRoomReqDto;
import com.plog.demo.dto.chat.ChatCreateRoomResDto;
import com.plog.demo.dto.chat.ChatMessageDto;
import com.plog.demo.dto.chat.ChatRoomDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.chat.ChatRoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ch")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "ChatRoom", description = "채팅방 API")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ChatCreateRoomResDto> createChatRoom(@RequestBody ChatCreateRoomReqDto chatCreateRoomReqDto) throws CustomException {
        ChatCreateRoomResDto chatRoom = chatRoomService.createRoom(chatCreateRoomReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(chatRoom);
    }

    @PostMapping("/user")
    public ResponseEntity<Map<String, List<ChatRoomDto>>> getChatRoomsByUserId(@RequestBody Map<String, String> userMap) throws CustomException {

        String userId = userMap.get("userId");
        List<ChatRoomDto> chatRoomDtos = chatRoomService.getByUserId(userId);

        Map<String, List<ChatRoomDto>> chatRoomMap = new HashMap<>();

        chatRoomMap.put("chatRoomList", chatRoomDtos);

        return ResponseEntity.status(HttpStatus.OK).body(chatRoomMap);
    }

    @PostMapping("/provider")
    public ResponseEntity<Map<String, List<ChatRoomDto>>> getChatRoomsByProviderId(@RequestBody Map<String, Integer> providerMap) throws CustomException {

        int providerId = providerMap.get("providerId");

        List<ChatRoomDto> chatRoomDtos = chatRoomService.getByProviderId(providerId);

        Map<String, List<ChatRoomDto>> chatRoomMap = new HashMap<>();

        chatRoomMap.put("chatRoomList", chatRoomDtos);

        return ResponseEntity.status(HttpStatus.OK).body(chatRoomMap);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Map<String, List<ChatMessageDto>>> getMessages(@PathVariable int roomId) throws CustomException {
        List<ChatMessageDto> chatMessages = chatRoomService.getChatMessages(roomId);

        Map<String, List<ChatMessageDto>> chatMessageMap = new HashMap<>();

        chatMessageMap.put("chatMessageList", chatMessages);

        return ResponseEntity.status(HttpStatus.OK).body(chatMessageMap);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<SuccessDto> deleteChatRoom(@PathVariable int roomId) throws CustomException {
        chatRoomService.deleteRoom(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessDto.builder().message("채팅 방 삭제 성공").build());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> customExceptionHandler(CustomException e){
        log.warn("customExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(e.getResultCode())
                .msg(e.getMessage())
                .build();

        return ResponseEntity.status(e.getResultCode()).body(errorDto);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> runtimeExceptionHandler(Exception e){
        log.error("runtimeExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .resultCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .msg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
