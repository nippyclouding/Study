package com.example.chatserver.chat.service;

import com.example.chatserver.chat.domain.ChatMessage;
import com.example.chatserver.chat.domain.ChatParticipant;
import com.example.chatserver.chat.domain.ChatRoom;
import com.example.chatserver.chat.domain.ReadStatus;
import com.example.chatserver.chat.dto.ChatMessageDto;
import com.example.chatserver.chat.dto.ChatRoomListResDto;
import com.example.chatserver.chat.dto.MyChatListResDto;
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
        // 새로운 메시지가 저장될 때 해당 채팅방의 모든 참여자를 대상으로 이 메시지에 대한 초기 읽음 상태 레코드를 발신자는 '읽음', 수신자는 '읽지 않음'으로 설정
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
        Member member = findMemberFromSecurityEmail();

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

        // 2. 본인 회원 객체 조회
        Member member = findMemberFromSecurityEmail();

        // 1:1 채팅 & 그룹 채팅의 구분 기준 : '인원 수' 가 아닌 DB 컬럼의 'isGroupChat' 이다.
        if (chatRoom.getIsGroupChat().equals("N")) {
            throw new IllegalArgumentException("그룹 채팅이 아닙니다.");
        }

        // 3. ChatParticipant 객체 생성, 저장, 참여자 검증 필요
        Optional<ChatParticipant> participant =
                chatParticipantRepository.findByChatRoomAndMember(chatRoom, member);

        // 3-1. 참여자 검증 : 해당 roomId 에서 이미 참여자인 상태인지 검증
        if (!participant.isPresent()) {
            addParticipantToRoom(chatRoom, member);
        }
    }

    public List<ChatMessageDto> getChatHistory(Long roomId) {
        // 특정 room에 대한 message 조회, 검증 - 1:1 채팅에서 해당 roomId 에 속하지 않은 유저가 접근 시도 가능
        // 검증 - 요청이 해당 채팅방의 참여자가 아닐 경우

        // 1. roomId로 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

        // 2. 본인 회원 객체 조회
        Member member = findMemberFromSecurityEmail();

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

    public void messageRead(Long roomId) {
        // 1. roomId로 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

        // 2. 본인 회원 객체 조회
        Member member = findMemberFromSecurityEmail();

        // 모든 메시지 읽음 처리
        List<ReadStatus> readStatuses = readStatusRepository.findByChatRoomAndMember(chatRoom, member);
        for (ReadStatus r : readStatuses) {
            // JPA 트랜잭션 속에셔 동작 => 변경 감지 (dirty checking), 별도 update 쿼리가 필요 x
            r.updateIsRead(true);
        }
    }


    public List<MyChatListResDto> getMyChatRooms() {
        // 1. 방을 생성한 회원 조회 (본인 회원 객체 조회)
        Member member = findMemberFromSecurityEmail();

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByMember(member);
        List<MyChatListResDto> dtos = new ArrayList<>();
        for (ChatParticipant c : chatParticipants) {
            Long count = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(c.getChatRoom(), member);
            MyChatListResDto dto = MyChatListResDto.builder()
                    .roomId(c.getChatRoom().getId())
                    .roomName(c.getChatRoom().getName())
                    .isGroupChat(c.getChatRoom().getIsGroupChat())
                    .unReadCount(count)
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }


    /*
    채팅을 나갈 때 - 이전 채팅 내역, 읽음 여부는 삭제 x, soft delete
    1. 참여자 participant 객체 삭제
    2. 모든 참여자가 나갔을 때 hard delete : 채팅방, 메시지, 읽음 여부 모두 삭제
     */
    public void leaveGroupChatRoom(Long roomId) {
        // 1. roomId로 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot be found"));

        // 2. 본인 회원 객체 조회
        Member member = findMemberFromSecurityEmail();

        // 1:1 채팅일 경우
        if (chatRoom.getIsGroupChat().equals("N")) {
            throw new IllegalArgumentException("단체 채팅방이 아닙니다.");
        }

        ChatParticipant c = chatParticipantRepository.findByChatRoomAndMember(chatRoom, member)
                .orElseThrow(() -> new EntityNotFoundException("참여자를 찾을 수 없습니다."));

        // 참여자 participant 객체 삭제
        chatParticipantRepository.delete(c);

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);

        // 모든 참여자가 나갔을 때 : cascade 옵션으로 채팅방, 메시지, 읽음 여부 모두 삭제
        if (chatParticipants.isEmpty()) {
            chatRoomRepository.delete(chatRoom);
        }
    }

    public Long getOrCreatePrivateRoom(Long otherMemberId) {

        Member member = findMemberFromSecurityEmail();
        Member otherMember = findMemberFromSecurityId(otherMemberId);

        /* JPQL로 Join 쿼리를 통해 데이터를 조회
        select chatRoom         (JPQL은 객체를 대상)
        from chat_participant a
        join chat_participant b
          on a.room_id = b.room_id
        where a.member_id = {member.getId()}
          and b.member_id = {otherMember.getId()};
          and a.chatRoom.isGroupChat = 'N'

          + 해당 room_id의 방이 단체 채팅이 아닌 경우에 조회 : isGroupChat = "N"
         */

        /* [chat_participant]
        participant_id      room_id        member_id
        1                   1              1
        2                   1              2
        3                   2              1
        4                   2              3
        5                   3              1
        6                   3              4
         */

        /*
        participant_id      room_id        member_id       participant_id     room_id     other_member_id
        1                   1              1               2                  1           2

        room_id가 같은 행들끼리 join(self join) 후 파라미터로 넣은 member_id, other_member_id가 join 행 결과값 데이터와 일치하는지 확인
        일치하는 데이터가 있다면 해당 room_id 리턴
        일치하는 데이터가 없다면 새로운 채팅방 생성
         */

        // 본인과 상대방이 1:1 채팅에 이미 참여하고 있을 경우 해당 roomId 리턴
        Optional<ChatRoom> chatRoom = chatParticipantRepository.findExistingPrivateRoom(member.getId(), otherMember.getId());
        if (chatRoom.isPresent()) {
            return chatRoom.get().getId();
        }

        // 1:1 채팅방이 없을 경우 기존 채팅방 개설, 두 사람 모두 참여자로 새롭게 추가
        ChatRoom newRoom = ChatRoom.builder()
                .isGroupChat("N")
                .name(member.getName() + "-" + otherMember.getName())
                .build();
        chatRoomRepository.save(newRoom);

        // 본인과 상대방을 참여자로 모두 추가
        addParticipantToRoom(newRoom, member);
        addParticipantToRoom(newRoom, otherMember);

        return newRoom.getId();
    }

    private void addParticipantToRoom(ChatRoom chatRoom, Member member) {
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

    private Member findMemberFromSecurityId(Long otherMemberId) {
        Member otherMember = memberRepository.findById(otherMemberId).orElseThrow(
                () -> new EntityNotFoundException
                        ("member cannot be found")
        ); // id로 상대방 회원 조회

        return otherMember;
    }

    private Member findMemberFromSecurityEmail() {
        // 세큐리티 컨텍스트 홀더 속에서 email을 조회
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName(); // Authentication 객체로 email 조회

        // email로 회원 객체 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException
                        ("member cannot be found")
        );
        return member; // 해당 메서드를 실행하는 본인 객체 조회
    }
}