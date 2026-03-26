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

// 회원가입 요청과 프로필 수정 요청을 처리하는 컨트롤러
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 API
    // 1. 중복 아이디 확인
    // 2. User 엔티티 저장
    // 3. USERS.STACK_NAME 컬럼에 기술 스택 이름 저장
    @PostMapping("/users/signup")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody UserSignupRequest request) {
        if (userService.existsByLoginId(request.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "이미 사용 중인 아이디입니다."));
        }

        try {
            User savedUser = userService.signup(request);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "회원가입이 완료되었습니다.",
                            "userNo", savedUser.getUserNo(),
                            "id", savedUser.getId(),
                            "stackNames", request.getStackNames()
                    )
            );
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "이미 사용 중인 아이디입니다."));
        }
    }

    // 프로필 수정 API
    // 1. 로그인 아이디로 사용자 조회
    // 2. 닉네임, 자기소개, 기술 스택 이름 수정
    // 3. 비밀번호 변경란에 값이 있으면 비밀번호도 함께 변경
    @PutMapping("/users/profile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody UserProfileRequest request) {
        try {
            User updatedUser = userService.updateProfile(request);
            List<String> savedStackNames = userService.getUserStackNames(updatedUser.getId());

            return ResponseEntity.ok(
                    Map.of(
                            "message", "프로필 수정이 완료되었습니다.",
                            "userNo", updatedUser.getUserNo(),
                            "id", updatedUser.getId(),
                            "nickname", updatedUser.getNickname(),
                            "intro", updatedUser.getIntro(),
                            "stackNames", savedStackNames
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
