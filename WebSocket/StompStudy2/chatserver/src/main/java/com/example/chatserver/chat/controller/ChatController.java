package com.example.chatserver.chat.controller;

import com.example.chatserver.chat.dto.ChatMessageDto;
import com.example.chatserver.chat.dto.ChatRoomListResDto;
import com.example.chatserver.chat.dto.MyChatListResDto;
import com.example.chatserver.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 그룹 채팅방 개설, DB에 그룹 채팅 여부 컬럼이 존재한다.
    @PostMapping("/room/group/create")
    public ResponseEntity<?> createGroupRoom(@RequestParam String roomName) {
        chatService.createGroupRoom(roomName);
        return ResponseEntity.ok().build();
    }

    // 그룹 채팅방 목록 조회
    @GetMapping("/room/group/list")
    public ResponseEntity<?> getGroupChatRooms() {
        List<ChatRoomListResDto> chatRooms = chatService.getGroupChatRooms();
        return new ResponseEntity<>(chatRooms,HttpStatus.OK);
    }

    @PostMapping("/room/group/{roomId}/join")
    public ResponseEntity<?> joinGroupChatRoom(@PathVariable Long roomId) {
        // 그룹 채팅방에 참여자 참여, 참여 대상은 Authentication 객체로 구분
        // -> 컨트롤러에서 파라미터로 전달하지 않는 대신 서비스에서 Authentication 객체 조회
        chatService.addParticipantToGroupChat(roomId);

        return ResponseEntity.ok().build();
    }

    // 이전 메시지 조회
    @GetMapping("/history/{roomId}")
    public ResponseEntity<?> getChatHistory(@PathVariable Long roomId) {
        List<ChatMessageDto> dtos = chatService.getChatHistory(roomId);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /* 채팅 읽음 처리
    방법 1 : 사용자가 메시지를 보내는 시점에 해당 방을 listen 중인 다른 구독자들을 읽음 처리
    => 웹소켓 세션을 직접 관리해야 한다
    메시지를 보내는 시점에 '서버의 메모리에 어떤 세션들이 메시지를 구독하고 있는지'

    방법 2 : disconnect 시점 (화면을 끄거나 라우트(다른 화면) 이동 시) : 현재까지 받은 메시지를 모두 읽음 처리
    메시지들을 하나씩 보내는 시점에 읽음 처리 하는 것이 아닌, disconnect가 발생하는 시점에
    현재까지 받은 모든 메시지를 읽음 처리로 변경
    ex : 단체 채팅에서 A가 메시지를 여러 개 보낼 때
    A는 메시지를 보내는 시점에 바로 계속 읽음 처리된다.
    단체 채팅에 들어와있는 B는 실제로는 메시지를 계속 읽고 있지만 DB 에서는 읽음 처리가 진행되지 않는다.
    B가 disconnect 할 때 해당 방의 모든 메시지들을 읽음 처리로 변경한다.
     */
    @PostMapping("/room/{roomId}/read") // 방법 2로 채팅 읽음 처리
    public ResponseEntity<?> messageRead(@PathVariable Long roomId) {
        chatService.messageRead(roomId);

        return ResponseEntity.ok().build();
    }

    // 내 채팅방 목록 조회 : roomId, roomName,
    // 읽지 않은 메시지 개수, 그룹 채팅 여부 (1:1 채팅이라면 화면에 나가기 버튼이 보여지지 않는다)
    @GetMapping("/my/rooms")
    public ResponseEntity<?> getMyChatRooms() {
        List<MyChatListResDto> dtos = chatService.getMyChatRooms();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // 채팅방 나가기
    // 1:1 채팅방은 나가기가 필요 X, 그룹 채팅방 나가기
    @DeleteMapping("/room/group/{roomId}/leave")
    public ResponseEntity<?> leaveGroupChatRoom(@PathVariable Long roomId) {
        chatService.leaveGroupChatRoom(roomId);

        return ResponseEntity.ok().build();
    }
}
