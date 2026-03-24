package com.project.mvcgithublogin.controller;

import com.project.mvcgithublogin.domain.User;
import com.project.mvcgithublogin.dto.CreateUserRequest;
import com.project.mvcgithublogin.dto.LoginRequest;
import com.project.mvcgithublogin.service.UserService;
import jakarta.servlet.http.HttpSession;
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
    public UserController(UserService userService){
        this.userService = userService;
    }

    // 폼 방식 회원가입 (페이지 리다이렉트용)
    @PostMapping("/signup")
    public String signup(@ModelAttribute CreateUserRequest request) {
        userService.signup(request);
        return "redirect:/signup";
    }

    // JSON 회원가입 (프론트 fetch용)
    @PostMapping("/users/signup")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> signupJson(@RequestBody CreateUserRequest request) {
        userService.signup(request);
        return ResponseEntity.ok(Map.of("message", "회원가입 완료"));
    }

    // JSON 로그인 (프론트 fetch용)
    @PostMapping("/users/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> loginJson(@RequestBody LoginRequest request, HttpSession session) {
        try {
            User user = userService.login(request);
            session.setAttribute("loginUser", user.getId());
            return ResponseEntity.ok(Map.of("message", "로그인 성공", "id", user.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "아이디 또는 비밀번호가 올바르지 않습니다."));
        }
    }

    // 로그인 상태 확인
    @GetMapping("/users/me")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> me(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.ok(Map.of("loggedIn", false));
        }
        return ResponseEntity.ok(Map.of("loggedIn", true, "id", loginUser));
    }

    // 로그아웃
    @PostMapping("/users/logout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "로그아웃"));
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
