package com.project.join.controller;

import com.project.join.domain.User;
import com.project.join.dto.UserProfileRequest;
import com.project.join.dto.UserSignupRequest;
import com.project.join.service.UserService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// 회원가입 요청과 프로필 수정 요청을 처리하는 컨트롤러
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 API
    // 1. 중복 아이디 확인
    // 2. User 엔티티 생성
    // 3. 서비스에서 암호화 후 저장
    @PostMapping("/users/signup")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody UserSignupRequest request) {
        if (userService.existsByLoginId(request.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "이미 사용 중인 아이디입니다."));
        }

        try {
            User savedUser = userService.save(
                    User.builder()
                            .id(request.getId())
                            .password(request.getPw())
                            .nickname(request.getNickname())
                            .phone(request.getPhone())
                            .loginType("GENERAL")
                            .authkey(UUID.randomUUID().toString())
                            .build()
            );

            // 회원가입 성공 시 저장된 사용자 번호와 아이디를 응답으로 반환
            return ResponseEntity.ok(
                    Map.of(
                            "message", "회원가입이 완료되었습니다.",
                            "userNo", savedUser.getUserNo(),
                            "id", savedUser.getId()
                    )
            );
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "이미 사용 중인 아이디입니다."));
        }
    }

    // 프로필 수정 API
    // 1. 로그인 아이디로 사용자 조회
    // 2. 닉네임 수정
    // 3. 비밀번호 변경란에 값이 있으면 비밀번호도 함께 변경
    // 4. 한줄 소개란 추가
    // 5. USER_STACK 테이블에 기술 스택 다시 저장
    @PutMapping("/users/profile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody UserProfileRequest request) {
        try {
            User updatedUser = userService.updateProfile(request);

            // 저장된 기술 스택을 다시 조회해서 응답으로 보여줌
            List<Long> savedStackIds = userService.getUserStackIds(updatedUser.getId());

            return ResponseEntity.ok(
                    Map.of(
                            "message", "프로필 수정이 완료되었습니다.",
                            "userNo", updatedUser.getUserNo(), // 사용자의 ID번호(가입순서)
                            "id", updatedUser.getId(), // 사용자의 ID
                            "nickname", updatedUser.getNickname(), // 닉네임
                            "intro", updatedUser.getIntro(), // 한줄 소개 (메모)
                            "stackIds", savedStackIds // 추가 할 기술 스택
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
