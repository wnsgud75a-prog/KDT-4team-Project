package com.project.mvcgithublogin.controller;

import com.project.mvcgithublogin.domain.User;
import com.project.mvcgithublogin.dto.CreateUserRequest;
import com.project.mvcgithublogin.dto.LoginRequest;
import com.project.mvcgithublogin.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;

    @Value("${app.google.client-id:}")
    private String googleClientId;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute CreateUserRequest request) {
        userService.signup(request);
        return "redirect:/signup";
    }

    @PostMapping("/users/signup")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> signupJson(@RequestBody CreateUserRequest request) {
        try {
            userService.signup(request);
            return ResponseEntity.ok(Map.of("message", "회원가입이 완료되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/users/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> loginJson(@RequestBody LoginRequest request, HttpSession session) {
        try {
            User user = userService.login(request);
            session.setAttribute("loginUser", user.getId());
            return ResponseEntity.ok(Map.of("message", "로그인 성공", "id", user.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }
    // 구글 로그인 요청을 처리하는 API
    @PostMapping("/users/google")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> googleLogin(@RequestBody Map<String, String> payload, HttpSession session) {
        // 프론트에서 전달한 구글 credential을 이용해 로그인 처리
        try {
            User user = userService.loginWithGoogle(payload.get("credential"), googleClientId);
            // 로그인 성공 시 세션에 사용자 아이디 저장
            session.setAttribute("loginUser", user.getId());
            // 로그인 성공 시 세션에 사용자 아이디 저장
            return ResponseEntity.ok(Map.of("message", "구글 로그인 성공", "id", user.getId()));
        } catch (IllegalArgumentException e) {
            // 인증 실패 시 401 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            // 서버 내부 오류 발생 시 500 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/users/me")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> me(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.ok(Map.of("loggedIn", false));
        }
        return ResponseEntity.ok(Map.of("loggedIn", true, "id", loginUser));
    }

    @PostMapping("/users/logout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "로그아웃 완료"));
    }

    @GetMapping("/users")
    public String home() {
        return "index";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
