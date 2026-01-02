package WebSocket.websocket;


import WebSocket.websocket.dto.RequestDto;
import WebSocket.websocket.dto.ResponseDto;
import WebSocket.websocket.dto.ResponseSessionsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Controller
public class StompController {

    private final StompEventListener eventListener;

    private final SimpMessagingTemplate messagingTemplate;

    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledMap = new ConcurrentHashMap<>();



    private final TaskScheduler taskScheduler;

    public StompController(StompEventListener eventListener, SimpMessagingTemplate messagingTemplate, TaskScheduler taskScheduler) {
        this.eventListener = eventListener;
        this.messagingTemplate = messagingTemplate;
        this.taskScheduler = taskScheduler;
    }


    // @GetMapping 은 http 요청 처리, @MessageMapping 은 웹소켓 메시지 처리
    @MessageMapping("/hello") // /app/hello 로 요청 시 해당 매핑이 처리
    @SendTo("/topic/hello") // /topic/hello 를 구독한 클라이언트에게 발행
    public ResponseDto basic(RequestDto request) {
        log.info("request: {}", request);
        return new ResponseDto(request.getMessage().toUpperCase(), LocalDateTime.now());
    }

    @MessageMapping("/multi") // /app/multi 로 요청 시 해당 매핑이 처리
    @SendTo({"/topic/hello", "/topic/hello2"}) // 배열로 발행 가능
    public ResponseDto nulti(RequestDto request) {
        log.info("request: {}", request);

        return new ResponseDto(request.getMessage().toUpperCase(), LocalDateTime.now());
    }

    @MessageMapping("/hello1")
    @SendTo("/topic/hello")
    public ResponseDto annotations(Message<RequestDto> message, MessageHeaders headers, RequestDto request) {
        // RequestDto request 는 웹소켓 메시지 바디 부분에 대한 데이터만 확인 가능
        // MessageHeaders headers 는 웹소켓 메시지 헤더만 확인 가능
        // Message<RequestDto> message 는 웹소켓 메시지 바디와 함께 헤더에 대한 데이터도 확인 가능
        // Message<RequestDto> = MessageHeaders + RequestDto (body)
        log.info("message: {}", message);
        log.info("headers: {}", headers);
        log.info("request: {}", request);

        return new ResponseDto(request.getMessage().toUpperCase(), LocalDateTime.now());
    }

    @MessageMapping("/hello/{detail}")
    @SendTo("/topic/hello")
    public ResponseDto detail(@DestinationVariable("detail") String detail, RequestDto request) {
        // @DestinationVariable : @PathVariable 과 비슷한 역할, {detail}로 들어온 값을 String detail 에 담아 사용
        // ex : /app/hello/world
        log.info("detail: {}", detail);
        log.info("request: {}", request);

        return new ResponseDto("[" + detail + "]_" + request.getMessage().toUpperCase(), LocalDateTime.now());
    }

    @MessageMapping("/sessions")
    @SendToUser("/queue/sessions")
    public ResponseSessionsDto sessions(RequestDto request, MessageHeaders headers) {
        log.info("request: {}", request);
        Set<String> sessions = eventListener.getSessions();
        String sourceSessionId = headers.get("simpSessionId").toString();
        return new ResponseSessionsDto(sessions.size(), sessions.stream().toList(), sourceSessionId, LocalDateTime.now());
    }


    @MessageMapping("/code1")
    public void code1(RequestDto request) {
        log.info("request: {}", request);
        ResponseDto resDto = new ResponseDto(request.getMessage().toUpperCase(), LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/hello", resDto);
    }

    @MessageMapping("/code2")
    public void code2(RequestDto request, MessageHeaders headers) {
        log.info("request: {}", request);
        String sourceSessionId = headers.get("simpSessionId").toString();
        Set<String> sessions = eventListener.getSessions();

        ResponseSessionsDto resSessionsDto = new ResponseSessionsDto(sessions.size(), sessions.stream().toList(), sourceSessionId, LocalDateTime.now());

        messagingTemplate.convertAndSendToUser(sourceSessionId, "/queue/sessions", resSessionsDto, createHeaders(sourceSessionId));
    }

    private MessageHeaders createHeaders(@Nullable String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);

        if (sessionId != null) {
            headerAccessor.setSessionId(sessionId);
        }
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    @MessageMapping("/start")
    public void start(RequestDto request, MessageHeaders headers) {
        log.info("request: {}", request);
        String sourceSessionId = headers.get("simpSessionId").toString();

        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(() -> {
            Random random = new Random();
            int currentPrice = random.nextInt(100);
            messagingTemplate.convertAndSendToUser(sourceSessionId, "/queue/trade", currentPrice, createHeaders(sourceSessionId));
        }, Duration.ofSeconds(3));

        scheduledMap.put(sourceSessionId, scheduledFuture);
    }

    @MessageMapping("/stop")
    public void stop(RequestDto request, MessageHeaders headers) {
        log.info("request: {}", request);
        String sourceSessionId = headers.get("simpSessionId").toString();

        ScheduledFuture<?> scheduledFuture = scheduledMap.remove(sourceSessionId);
        scheduledFuture.cancel(true);
    }


    @MessageMapping("/exception")
    @SendTo("/topic/hello")
    public void exception(RequestDto request, MessageHeaders headers) throws Exception {
        log.info("request: {}", request);
        String message = request.getMessage();
        switch(message) {
            case "runtime":
                throw new RuntimeException();
            case "nullPointer":
                throw new NullPointerException();
            case "io":
                throw new IOException();
            case "exception":
                throw new Exception();
            default:
                throw new InvalidParameterException();
        }
    }

}