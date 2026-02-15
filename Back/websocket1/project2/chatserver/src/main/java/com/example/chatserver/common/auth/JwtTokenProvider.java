package com.example.chatserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

import static java.util.Base64.getDecoder;

@Component
public class JwtTokenProvider {
    // jwt 토큰 생성 클래스
    private final String secretKey;
    private final int expiration;
    private Key SECRET_KEY; // application.yml 에서 등록된 secretKey, expiration 으로 생성되는 암호화 KEY

    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey,
                            @Value("${jwt.expiration}") int expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;

        /*
        인코딩된 secretKey를 디코딩, HS512 암호화 알고리즘을 이용해 secretKey를 암호화한다.
        Y2hhdHNlcnZlcnNlY3JldGFjY2Vzc3Rva2VuY2hhdHNlcnZlcnNlY3JldGFjY2Vzc3Rva2VuY2hhdHNlcnZlcnNlY3JldGFjY2Vzc3Rva2Vu 값을
        chatserversecretaccesstokenchatserversecretaccesstokenchatserversecretaccesstoken 로 디코딩한 뒤
        디코딩 값을 HS512 알고리즘으로 암호화한 값을 SECRET_KEY 필드에 저장
        */
        this.SECRET_KEY = new SecretKeySpec(getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName());
    }

    public String createToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email); // jwt 토큰의 Payload (직접 데이터를 설정하는 부분, key = email)
        claims.put("role", role);
        // jwt Payload(claims) 에 email, role이 들어간다.

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims) // Payload 설정
                .setIssuedAt(now) // 토큰 발행 시간 설정
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000L)) // 만료 일자, ms 단위로 설정
                .signWith(SECRET_KEY) // 암호화된 키로 서명
                .compact();
        return token;
    }
}
