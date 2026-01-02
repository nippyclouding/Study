package WebSocket.websocket;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP 동작 어노테이션
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트의 전송 경로가 /app 으로 시작하는 메시지들은 모두 컨트롤러의 @MessageMapping이 처리
        config.setApplicationDestinationPrefixes("/app");

        // 메시지 브로커가 /topic, /queue를 구독한 클라이언트에 전달 (/topic/hello ..)
        // Simple Broker는 메시지를 조작하지 않고 그대로 구독자들에게 자동 포워딩
        // setApplicationDestinationPrefixes 과 다르게 컨트롤러를 거치지 않고 바로 포워딩
        config.enableSimpleBroker("/topic", "/queue");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 서버 엔드포인트와 클라이언트 엔드포인트가 반드시 동일해야 한다. (/endPointTest)
        // registry.addEndpoint("/endPointTest");                    // websocket

        // .withSockJS() : WebSocket 불가 시 대안 사용 (long polling, HTTP Streaming 등)
        registry.addEndpoint("/endPointTest").withSockJS();       // sockjs
    }

}