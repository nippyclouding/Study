package com.example.chatserver.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    // String은 추가 설정 없을 경우 DB에 varchar(255)로 생성
    private String name;
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING) // DB에 ENUM 타입으로 들어간다.
    @Builder.Default // Builder 패턴 - USER 기본값 적용
    private Role role = Role.USER; // 기본값 : USER



}
