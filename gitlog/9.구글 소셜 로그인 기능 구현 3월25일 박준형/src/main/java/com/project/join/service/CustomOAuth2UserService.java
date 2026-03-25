package com.project.join.service;

import com.project.join.domain.User;
import com.project.join.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 구글 로그인 성공 후 사용자 정보를 DB에 저장하거나 갱신하는 서비스
    // 여기부터 구글에서 내려준 사용자 정보를 받아 우리 USERS 테이블과 연결
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // 구글 쪽에서 내려주는 기본 정보(email, name)를 꺼낸다
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String loginId = email != null ? email : UUID.randomUUID() + "@google.local";
        String nickname = (name != null && !name.isBlank()) ? name : loginId;

        // DB에 같은 아이디가 있으면 닉네임과 로그인 타입을 최신 값으로 갱신한다.
        User user = userRepository.findByLoginId(loginId)
                .map(existing -> {
                    existing.setNickname(nickname);
                    existing.setLoginType("GOOGLE");
                    existing.setAuthkey(loginId);
                    return existing;
                })
                // 처음 로그인한 구글 사용자라면 USERS 테이블에 새로 저장한다.
                .orElseGet(() -> User.builder()
                        .id(loginId)
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .nickname(nickname)
                        .loginType("GOOGLE")
                        .authkey(loginId)
                        .build());

        // 여기부터 구글 로그인 사용자 정보를 실제 DB에 저장
        userRepository.save(user);

        // 스프링 시큐리티가 로그인 세션을 만들 수 있도록 OAuth2User를 그대로 반환한다.
        return new DefaultOAuth2User(
                oauth2User.getAuthorities(),
                attributes,
                "email"
        );
    }
}
