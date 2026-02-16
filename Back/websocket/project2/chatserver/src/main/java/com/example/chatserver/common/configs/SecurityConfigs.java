package com.example.chatserver.common.configs;

import com.example.chatserver.common.auth.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfigs {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain checkFilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // 원칙적으로 같은 도메인, 포트번호 요청만 처리 가능하지만 예외적으로 요청 중 3000번 포트 요청은 받아들인다.
                .cors(cors->cors.configurationSource(configurationSource())) // filter에 cors 설정
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // http basic(보안 인증 방법) 비활성화 (토큰 기반 인증 처리를 하기 때문)
                // 특정 url 패턴에 대해서는 Authentication 객체를 요구하지 않는다.
                .authorizeHttpRequests(a -> a.requestMatchers(
                                // filter 적용하지 않을 api
                        "/member/create"
                                , "/member/doLogin"
                                , "/connect/**")

                        .permitAll()
                        .anyRequest()
                        .authenticated())
                // 세션 방식 사용하지 x
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // 요청의 token 검증, jwtAuthFilter로 진입
                .build();
    }

    @Bean
    CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 http 메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더값 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 url 패턴에 대해 cors 허용 설정
        return source;
    }

    @Bean // 회원가입 시 비밀번호 암호화, 로그인 시 암호화된 비밀번호와 입력값이 일치하는지 확인
    public PasswordEncoder makePassword(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // createDelegatingPasswordEncoder() : 기본값으로 BCrypt 적용
    }
}
