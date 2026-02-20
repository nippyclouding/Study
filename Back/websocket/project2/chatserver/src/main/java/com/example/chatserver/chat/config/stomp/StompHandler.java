package com.example.chatserver.chat.config.stomp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        //connect, send, subscribe, disconnect 요청 전에 반드시 핸들러 속 해당 메서드가 먼저 동작한다.
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("connect 요청 시 유효성 검증 시작");
            String bearerToken = accessor.getFirstNativeHeader("Authorization"); // 토큰을 꺼낸다.
            String token = bearerToken.substring(7);

            // 토큰 검증
            Claims claims = Jwts.parserBuilder() // claims = payload, Authentication 객체를 생성하기 위해 claims 추출
                    .setSigningKey(secretKey) // secretKey 를 다시 넣어 사용자의 payload, header 부분을 결합
                    .build()                  // => 다시 암호화를 해서 현재 서버가 생성한 토큰이 맞는지 검증
                    .parseClaimsJws(token) // 토큰 검증
                    .getBody(); // 검증이 끝나면 payload 부분을 꺼낸다 (email, role)
            log.info("connect 요청 시 유효성 검증 완료");
        }

        if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            log.info("subscribe 요청 시 유효성 검증 시작");
            String bearerToken = accessor.getFirstNativeHeader("Authorization"); // 토큰을 꺼낸다.
            String token = bearerToken.substring(7);

            // 토큰 검증
            Claims claims = Jwts.parserBuilder() // claims = payload, Authentication 객체를 생성하기 위해 claims 추출
                    .setSigningKey(secretKey) // secretKey 를 다시 넣어 사용자의 payload, header 부분을 결합
                    .build()                  // => 다시 암호화를 해서 현재 서버가 생성한 토큰이 맞는지 검증
                    .parseClaimsJws(token) // 토큰 검증
                    .getBody(); // 검증이 끝나면 payload 부분을 꺼낸다 (email, role)
            log.info("subscribe 요청 시 유효성 검증 완료");
        }

        return message;
    }
}
