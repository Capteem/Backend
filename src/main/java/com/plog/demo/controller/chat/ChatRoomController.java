package com.plog.demo.controller.chat;

import com.plog.demo.dto.ErrorDto;
import com.plog.demo.dto.SuccessDto;
import com.plog.demo.dto.chat.*;
import com.plog.demo.dto.portfolio.PortfolioResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.chat.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "채팅 방 생성", description = "채팅방을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "채팅방 생성 성공",
                    content = @Content(schema = @Schema(implementation = ChatCreateRoomResDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "채팅방 이미 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping
    public ResponseEntity<ChatCreateRoomResDto> createChatRoom(@RequestBody ChatCreateRoomReqDto chatCreateRoomReqDto) throws CustomException {
        ChatCreateRoomResDto chatRoom = chatRoomService.createRoom(chatCreateRoomReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(chatRoom);
    }

    @Operation(summary = "채팅 방 유저 아이디로 조회", description = "채팅방을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "채팅방 조회 성공",
                    content = @Content(schema = @Schema(implementation = ChatRoomResponseDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "채팅방이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/user")
    public ResponseEntity<ChatRoomResponseDto> getChatRoomsByUserId(@RequestBody Map<String, String> userMap) throws CustomException {

        String userId = userMap.get("userId");
        List<ChatRoomDto> chatRoomDtos = chatRoomService.getByUserId(userId);

        ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder().chatRoomDtoList(chatRoomDtos).build();

        return ResponseEntity.status(HttpStatus.OK).body(chatRoomResponseDto);
    }

    @Operation(summary = "채팅 방 제공자 id로 조회", description = "채팅방을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "채팅방 조회 성공",
                    content = @Content(schema = @Schema(implementation = ChatRoomResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "채팅방이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/provider")
    public ResponseEntity<ChatRoomResponseDto> getChatRoomsByProviderId(@RequestBody Map<String, Integer> providerMap) throws CustomException {

        int providerId = providerMap.get("providerId");

        List<ChatRoomDto> chatRoomDtos = chatRoomService.getByProviderId(providerId);

        ChatRoomResponseDto chatRoomResponseDto = ChatRoomResponseDto.builder().chatRoomDtoList(chatRoomDtos).build();

        return ResponseEntity.status(HttpStatus.OK).body(chatRoomResponseDto);
    }

    @Operation(summary = "채팅 메시지 조회", description = "채팅 메시지를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "채팅 메시지 조회 성공",
                    content = @Content(schema = @Schema(implementation = ChatMessageResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "채팅방이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatMessageResponseDto> getMessages(@PathVariable int roomId) throws CustomException {
        List<ChatMessageDto> chatMessages = chatRoomService.getChatMessages(roomId);

        ChatMessageResponseDto chatMessageResponseDto = ChatMessageResponseDto.builder().chatMessageDtoList(chatMessages).build();

        return ResponseEntity.status(HttpStatus.OK).body(chatMessageResponseDto);
    }

    @Operation(summary = "채팅 방 삭제", description = "채팅방을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "채팅 방 삭제 성공",
                    content = @Content(schema = @Schema(implementation = SuccessDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "채팅방이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
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
