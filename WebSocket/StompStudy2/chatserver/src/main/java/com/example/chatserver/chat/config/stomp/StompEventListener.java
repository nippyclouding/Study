package com.example.chatserver.chat.config.stomp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class StompEventListener {
    private final Set<String> set = ConcurrentHashMap.newKeySet();

    @EventListener
    public void connectHandle(SessionConnectEvent event) {
        // SessionConnectEvent : 커넥션이 발생했을 때 이벤트 리스너의 connectHandle가 동작한다.
        StompHeaderAccessor wrap = StompHeaderAccessor.wrap(event.getMessage());
        set.add(wrap.getSessionId());

        log.info("connect session Id : {}", wrap.getSessionId());
        log.info("total session set : {}", set.size());
    }

    @EventListener
    public void disconnectHandle(SessionDisconnectEvent event) {
        // SessionDisconnectEvent : 커넥션이 종료되었을 때 이벤트 리스너의 disconnectHandle가 동작한다.
        StompHeaderAccessor wrap = StompHeaderAccessor.wrap(event.getMessage());
        set.remove(wrap.getSessionId());

        log.info("disconnect session Id : {}", wrap.getSessionId());
        log.info("total session set : {}", set.size());

    }
}
