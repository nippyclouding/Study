package com.example.chatserver.chat.controller;

import com.example.chatserver.chat.dto.ChatMessageDto;
import com.example.chatserver.chat.service.ChatService;
import com.example.chatserver.common.configs.redis.RedisPubSubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class StompController {

    // 1. messageMapping(수신), sendTo(송신) 한 번에 처리 : 유연성이 떨어진다.
//    @MessageMapping("/{roomId}") // 클라이언트에서 특정 publish/roomId 형태로 메시지 발행 시 해당 요청을 수신
//    @SendTo("/topic/{roomId}") // 해당 roomId에 메시지를 발행하여 구독 중인 클라이언트에게 메시지 전송
//    // @DestinationVariable : @MessageMapping과 함께 사용, @PathVariable의 Stomp version
//    public String sendMessage(@DestinationVariable Long roomId, String message) {
//        log.info("{}", message);
//        return message;
//    }

    private final SimpMessageSendingOperations messageTemplate;
    private final ChatService chatService;
    private final RedisPubSubService redisPubSubService;

    // 2. messageMapping만 활용 (sendTo 사용 x) : 유연성 증가
    @MessageMapping("/{roomId}") // 클라이언트에서 특정 publish/roomId 형태로 메시지 발행 시 해당 요청을 수신
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageDto chatMessageDto) throws JsonProcessingException { // modelAttribute가 아니라 @Payload로 객체 등 값을 컨트롤러에서 받는다.

        log.info("message : {}, sender : {}",
                chatMessageDto.getMessage(), chatMessageDto.getSenderEmail());

        chatService.saveMessage(roomId, chatMessageDto); // 메시지를 DB에 저장

        // messageTemplate.convertAndSend("/topic/" + roomId, chatMessageDto);

        // redis pub sub 으로 동작, redisPubSubService 에 해당 코드 작성
        // 서버로 pub sub 을 하기 위해서는 아래 코드를 주석 처리, 위 코드 주석을 해제하면 된다.

        chatMessageDto.setRoomId(roomId);

        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(chatMessageDto);
        redisPubSubService.publish("chat", message);
    }
}
