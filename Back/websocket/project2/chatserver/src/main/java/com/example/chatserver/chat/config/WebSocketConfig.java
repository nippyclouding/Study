package com.example.chatserver.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final SimpleWebSocketHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/connect") // connect로 요청이 올 경우 등록된 핸들러가 처리
                .setAllowedOrigins("http://localhost:3000");
    }
    /*
        websocket을 처리할 핸들러를 등록하는 메서드
        websocket 요청이 /connect 엔드포인트로 들어오면 해당 핸들러가 처리한다.
        SecurityConfig 에서의 CORS 예외는 http 요청에 대한 예외
        websocket 프로토콜에 대한 요청에 대해서는 별도의 CORS 요청이 필요하다.
    */
}
