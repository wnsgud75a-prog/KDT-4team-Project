package com.project.join.service;

import com.project.join.domain.User;
import com.project.join.dto.UserProfileRequest;
import com.project.join.dto.UserSignupRequest;
import com.project.join.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

// 회원 관련 비즈니스 로직을 처리하는 서비스
@Service
public class UserService {

    private final UserRepository userRepository;

    // 회원가입과 비밀번호 변경 시 비밀번호를 암호화하기 위한 객체
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 로그인 아이디 중복 여부를 확인
    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    // 비밀번호를 암호화한 뒤 회원 정보 저장
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // 회원가입 시 USERS 테이블에 기본 정보와 stackName 문자열을 함께 저장
    @Transactional
    public User signup(UserSignupRequest request) {
        return save(
                User.builder()
                        .id(request.getId())
                        .password(request.getPw())
                        .nickname(request.getNickname())
                        .loginType("GENERAL")
                        .authkey(java.util.UUID.randomUUID().toString())
                        .stackName(joinStackNames(request.getStackNames()))
                        .build()
        );
    }

    // 프로필 수정 시 닉네임, 소개, 기술 스택 이름을 함께 저장
    @Transactional
    public User updateProfile(UserProfileRequest request) {
        User user = userRepository.findByLoginId(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 사용자입니다."));

        user.updateProfile(
                request.getNickname(),
                request.getIntro(),
                joinStackNames(request.getStackNames())
        );

        // 비밀번호 변경란에 값이 들어오면 암호화해서 PW 컬럼도 함께 변경
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        }

        return user;
    }

    // 저장된 기술 스택 이름 문자열을 다시 목록으로 분리해서 응답에 사용
    @Transactional(readOnly = true)
    public List<String> getUserStackNames(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 사용자입니다."));

        if (user.getStackName() == null || user.getStackName().isBlank()) {
            return Collections.emptyList();
        }

        return List.of(user.getStackName().split("\\s*,\\s*"));
    }

    // 여러 기술 스택 이름을 쉼표 문자열로 합쳐서 USERS.STACK_NAME 컬럼에 저장
    private String joinStackNames(List<String> stackNames) {
        if (stackNames == null || stackNames.isEmpty()) {
            return null;
        }

        return String.join(", ", stackNames);
    }
}
