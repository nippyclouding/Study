package com.example.chatserver.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class StompController {
    @MessageMapping("/{roomId}") // 클라이언트에서 특정 publish/roomId 형태로 메시지 발행 시 해당 요청을 수신
    @SendTo("/topic/{roomId}") // 해당 roomId에 메시지를 발행하여 구독 중인 클라이언트에게 메시지 전송
    // @DestinationVariable : @MessageMapping과 함께 사용, @PathVariable의 Stomp version
    public String sendMessage(@DestinationVariable Long roomId, String message) {
        log.info("{}", message);
        return message;
    }
    /*
    @SendTo : 클라이언트가 메시지 발행 -> 브로커가 roomId에 맞게 메시지 전달
    => roomId를 구독하는 클라이언트에게 메시지 전달
     */
}
