package com.example.chatserver.chat.service;

import com.example.chatserver.chat.domain.ChatMessage;
import com.example.chatserver.chat.domain.ChatParticipant;
import com.example.chatserver.chat.domain.ChatRoom;
import com.example.chatserver.chat.domain.ReadStatus;
import com.example.chatserver.chat.dto.ChatMessageDto;
import com.example.chatserver.chat.dto.ChatRoomListResDto;
import com.example.chatserver.chat.repository.ChatMessageRepository;
import com.example.chatserver.chat.repository.ChatParticipantRepository;
import com.example.chatserver.chat.repository.ChatRoomRepository;
import com.example.chatserver.chat.repository.ReadStatusRepository;
import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MemberRepository memberRepository;

    public void saveMessage(Long roomId, ChatMessageDto chatMessageDto) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot found"));

        // 보낸 사람(sender) 조회
        Member sender = memberRepository.findByEmail(chatMessageDto.getSenderEmail()).orElseThrow(() -> new EntityNotFoundException("room cannot found"));

        // chatMessage 저장 (연관관계 - chatRoom, member을 가지고 있기 때문에 상단에서 먼저 조회)
        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(sender)
                .content(chatMessageDto.getMessage())
                .build();
        chatMessageRepository.save(message);

        // 사용자별로 읽음 여부 저장 : chatroom 속 chatParticipant list를 조회 후 각 참여자별로 읽음 여부 저장
        // 발송자는 메시지를 보낸 뒤 바로 읽음 처리
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for (ChatParticipant c : chatParticipants) {
            ReadStatus readStatus = ReadStatus.builder()
                    .chatRoom(chatRoom)
                    .member(c.getMember())
                    .chatMessage(message)
                    .isRead(c.getMember().equals(sender))
                    .build();

            readStatusRepository.save(readStatus);
        }

    }

    public void createGroupRoom(String roomName) {

        // 1. 방을 생성한 회원 조회
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName(); // Authentication 객체로 email 조회

        Member member = memberRepository.findByEmail(email).orElseThrow(
                        () -> new EntityNotFoundException
                                ("member cannot be found")
        ); // email로 방을 생성한 회원 조회

        // 2. 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .name(roomName)
                .isGroupChat("Y")
                .build();
        chatRoomRepository.save(chatRoom);

        // 3. 채팅 참여자로 개설자를 추가
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

    public List<ChatRoomListResDto> getGroupChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findByIsGroupChat("Y");
        List<ChatRoomListResDto> dtos = new ArrayList<>();

        for (ChatRoom c : chatRooms) {
            ChatRoomListResDto dto = ChatRoomListResDto
                    .builder()
                    .roomId(c.getId())
                    .roomName(c.getName())
                    .build();
            dtos.add(dto);
        }

        return dtos;
    }

    public void addParticipantToGroupChat(Long roomId) {
        // 1. roomId로 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

        // 2. Authentication 객체 조회
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName(); // Authentication 객체로 email 조회

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException
                        ("member cannot be found")
        ); // email로 방을 생성한 회원 조회

        // 3. ChatParticipant 객체 생성, 저장, 참여자 검증 필요
        Optional<ChatParticipant> participant =
                chatParticipantRepository.findByChatRoomAndMember(chatRoom, member);

        // 3-1. 참여자 검증 : 해당 roomId 에서 이미 참여자인 상태인지 검증
        if (!participant.isPresent()) {
            ChatParticipant chatParticipant = ChatParticipant.builder()
                    .member(member)
                    .chatRoom(chatRoom)
                    .build();

            chatParticipantRepository.save(chatParticipant);
        }
    }

    public List<ChatMessageDto> getChatHistory(Long roomId) {
        // 특정 room에 대한 message 조회, 검증 - 1:1 채팅에서 해당 roomId 에 속하지 않은 유저가 접근 시도 가능
        // 검증 - 요청이 해당 채팅방의 참여자가 아닐 경우

        // 1. roomId로 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

        // 2. Authentication 객체 조회
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName(); // Authentication 객체로 email 조회

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException
                        ("member cannot be found")
        ); // email로 방을 생성한 회원 조회


        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        // 엔티티 양방향 설계 => chatRoom.getChatParticipants() 로 조회도 가능

        boolean check = false;
        for (ChatParticipant c : chatParticipants) {
            if (c.getMember().equals(member)) {
                check = true;
            }
        }

        if (!check) {
            throw new IllegalArgumentException("본인이 속하지 않은 채팅방입니다.");
        }

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByCreatedTimeAsc(chatRoom);
        List<ChatMessageDto> dtos = new ArrayList<>();
        for (ChatMessage m : chatMessages) {
            ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                    .message(m.getContent())
                    .senderEmail(m.getMember().getEmail()) // member.getEmail()이 아님
                    .build();
            dtos.add(chatMessageDto);
        }

        return dtos;
    }
}