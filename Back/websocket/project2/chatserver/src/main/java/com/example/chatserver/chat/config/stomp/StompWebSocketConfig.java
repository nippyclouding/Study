package com.example.chatserver.chat.config.stomp;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect") // connect로 요청이 들어올 경우
                .setAllowedOrigins("http://localhost:3000") // CORS - 3000번 포트 요청 허용
                .withSockJS(); // sockJS 라이브러리 이용: 웹소켓 통신에서 ws://가 아닌 http:// 엔드포인트를 사용할 수 있게 한다.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/publish");  // 메시지를 발행 시 /publish 접두어가 자동 추가
        registry.enableSimpleBroker("/topic"); // 메시지를 수신 시 /topic 접두어가 자동 추가
    }

    /*
    /publish 로 시작하는 url 패턴으로 메시지 발행 시 @Controller 객체의 @MessageMapping 메서드로 라우팅

     */
}
