package com.example.chatserver.chat.config.stomp;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker // STOMP
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

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
    클라이언트는 /topic/구독할 roomId 만 수신하면 메시지를 받을 수 있다.
     */

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
    /*
    웹소켓 요청은 세큐리티의 인터셉터를 건너뛴다.
    문제점 : 세큐리티 요청을 건너뛰기 때문에 클라이언트 인증 불가 (로그인 정보 등 토큰 검증 불가)
    => configureClientInboundChannel 메서드를 통해 웹소켓 요청이 들어오기 전 전용 인터셉터로 요청을 가로챈다.
    사용자 요청 -> 세큐리티 필터 건너뛰고 -> 현재 메서드로 들어온다.

    웹소켓 요청 (connect, subscribe, disconnect) 시에는 http header 등 http 메시지를 넣을 수 있고
    이를 인터셉터를 통해 가로채어 토큰을 검증 가능 (이 외 일반적인 웹소켓 메시지 송수신은 http 메시지를 넣을 수 없다)
    connect : 가장 처음 클라이언트 - 서버 간 웹소켓 연결하는 시점 => http 정보를 넣을 수 있다.
    subscribe : 클라이언트가 특정 주제(topic/{roomId})를 수신하겠다고 서버에 알리는 시점 => http 정보를 넣을 수 있다.
    disconnect : TCP 연결을 해제하고 서버 자원을 정리하는 시점 => http 정보를 넣을 수 있다.
    */
}
