package com.example.chatserver.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    // 요청, 응답 모두 사용되는 dto
    private String message;
    private String senderEmail;
}
