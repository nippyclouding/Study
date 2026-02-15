package com.example.chatserver.member.service;

import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.dto.MemberListRespDto;
import com.example.chatserver.member.dto.MemberLoginReqDto;
import com.example.chatserver.member.dto.MemberSaveReqDto;
import com.example.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(MemberSaveReqDto memberSaveReqDto){
        // 이미 가입되어있는 이메일 검증
        if (memberRepository.findByEmail(memberSaveReqDto.getEmail()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
        Member newMember = Member.builder()
                .name(memberSaveReqDto.getName())
                .email(memberSaveReqDto.getEmail())
                .password(passwordEncoder.encode(memberSaveReqDto.getPassword())) // 비밀번호 Bcrypt 암호화
                .build();
        Member savedMember = memberRepository.save(newMember);

        return savedMember;
    }

    public Member login(MemberLoginReqDto memberLoginReqDto) {
        Member findMember = memberRepository.findByEmail(memberLoginReqDto.getEmail()).orElseThrow(()->new EntityNotFoundException("존재하지 않는 email 입니다."));

        if (!passwordEncoder.matches(memberLoginReqDto.getPassword(), findMember.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        return findMember;
    }

    public List<MemberListRespDto> findAll() {
        List<Member> members = memberRepository.findAll();
        List<MemberListRespDto> memberListRespDtos = new ArrayList<>();

        for (Member m : members) {
            MemberListRespDto dto = new MemberListRespDto();

            dto.setId(m.getId());
            dto.setEmail(m.getEmail());
            dto.setName(m.getName());

            memberListRespDtos.add(dto);
        }

        return memberListRespDtos;
    }

}
