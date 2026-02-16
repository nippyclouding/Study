package com.example.chatserver.chat.config.simpleWebsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//@Component Stomp를 위해 주석 처리
@Slf4j
public class SimpleWebSocketHandler extends TextWebSocketHandler {
    // 웹소켓 연결 요청이 /connect로 들어왔을 때 요청을 처리할 객체

    // 메모리에서 관리하는 사용자들의 세션
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 연결이 되었을 때 (연결 등록 이후) : 사용자 세션을 메모리에 등록
        sessions.add(session);
        log.info("Connected : {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 사용자에게 메시지를 전달
        String payload = message.getPayload();
        log.info("received message : {}", payload);

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) { // 메시지를 받을 수 있다면
                s.sendMessage(new TextMessage(payload)); // set의 모든 사용자 session 들에게 메시지 전달
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결이 끝났을 때 (연결 종료 이후) : 사용자 세션을 메모리에서 삭제
        sessions.remove(session);
        log.info("disconnected : {}", session.getId());
    }
}
