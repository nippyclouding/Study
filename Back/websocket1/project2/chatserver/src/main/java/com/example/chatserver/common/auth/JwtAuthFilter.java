package com.example.chatserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends GenericFilter {

    @Value("${jwt.secretKey}")
    private String secretKey;

    // 사용자가 요청을 할 때 해당 요청 속 token을 검증하는 로직
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String token = req.getHeader("Authorization");

        try {
            if (token != null) { // token == null : filterChain.doFilter (현재 필터 종료, 요청한 곳으로 다시 간다.(

                if (!token.substring(0, 7).equals("Bearer ")) {
                    // Postman 테스트 시 Auth - Auth Type : Bearer Token 지정 시 자동으로 토큰 앞에 Bearer가 붙는다.
                    throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
                }

                String jwtToken = token.substring(7); // 7번째 인덱스부터 끝까지

                Claims claims = Jwts.parserBuilder() // claims = payload, Authentication 객체를 생성하기 위해 claims 추출
                        .setSigningKey(secretKey) // secretKey 를 다시 넣어 사용자의 payload, header 부분을 결합
                        .build()                  // => 다시 암호화를 해서 현재 서버가 생성한 토큰이 맞는지 검증
                        .parseClaimsJws(jwtToken) // 토큰 검증
                        .getBody(); // 검증이 끝나면 payload 부분을 꺼낸다 (email, role)

                // Authentication 객체 생성
                // Authentication 객체는 SecurityContextHolder 속 SecurityContext 안에 들어간다.
                List<GrantedAuthority> authorities = new ArrayList<>(); // 권한
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));

                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(servletRequest, servletResponse); // 원래 요청으로 돌아간다.
        } catch (Exception e) {
            // 예외 발생 시 사용자에게 응답 return
            e.printStackTrace();
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            resp.setContentType("application/json");
            resp.getWriter().write("invalid token");
        }



    }
}
