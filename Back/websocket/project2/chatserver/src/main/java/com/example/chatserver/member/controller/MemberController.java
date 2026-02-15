package com.example.chatserver.member.controller;

import com.example.chatserver.common.auth.JwtTokenProvider;
import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.dto.MemberListRespDto;
import com.example.chatserver.member.dto.MemberLoginReqDto;
import com.example.chatserver.member.dto.MemberSaveReqDto;
import com.example.chatserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    /*
    일반 객체 리턴은 스프링이 자동으로 JSON으로 변경 (Jackson 라이브러리 내장)
    ResponseEntity : JSON + 응답 코드 함께 반환
    => 상태 코드, 헤더 설정 등 정밀 제어가 가능하다.
     */

    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberSaveReqDto memberSaveReqDto) {
        Member member = memberService.create(memberSaveReqDto);
        return new ResponseEntity<>(member.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody MemberLoginReqDto memberLoginReqDto) {
        // DB 에서 email, password 검증
        Member loginMember = memberService.login(memberLoginReqDto);

        // 검증 - 일치할 경우 access 토큰 발행
        String jwtToken = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getRole().toString()); // 토큰 발행

        Map<String, Object> loginInfo = new HashMap<>(); // 반환할 json 객체
        loginInfo.put("id", loginMember.getId()); // 회원 pk
        loginInfo.put("token", jwtToken); // 회원 jwt 토큰

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> memberList() {
        List<MemberListRespDto> dtos = memberService.findAll();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
