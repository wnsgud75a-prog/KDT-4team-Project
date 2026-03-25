package com.project.join.config;

import com.project.join.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    // 새로 추가된 OAuth2 사용자 서비스를 연결하는 생성자
    // 여기부터 구글 로그인 후 사용자 정보를 처리할 서비스 연결
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 여기부터 구글 로그인 시작 주소와 로그인 화면 접근 허용
                        .requestMatchers("/", "/users/signup", "/login", "/login.html", "/error", "/css/**", "/js/**", "/oauth2/**", "/login/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 구글 로그인 성공 후 사용자 정보를 DB에 연결하기 위해 커스텀 서비스를 사용
                .oauth2Login(oauth -> oauth
                        // 여기부터 구글에서 받은 사용자 정보를 CustomOAuth2UserService에서 직접 저장
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                );

        return http.build();
    }
}
