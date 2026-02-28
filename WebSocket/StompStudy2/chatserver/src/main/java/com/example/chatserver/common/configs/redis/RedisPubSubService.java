package com.example.chatserver.common.configs.redis;

import com.example.chatserver.chat.dto.ChatMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPubSubService implements MessageListener {

    private final StringRedisTemplate template;
    private final SimpMessageSendingOperations messageTemplate;

    // onMessage 메서드 구현, subscribe 시 동작
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
            messageTemplate.convertAndSend("/topic/" + chatMessageDto.getRoomId(), chatMessageDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // message : redis를 subscribe할 때 받아오는 실제 메시지
        // pattern : topic 이름의 패턴이 들어가있고, 현재는 사용하지 않지만 여러 동적 기능 구현 가능
    }

    public void publish(String channel, String message) {
        messageTemplate.convertAndSend(channel, message);
    }
}
