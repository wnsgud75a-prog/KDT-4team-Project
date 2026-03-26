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

    // 구글 클라이언트 id 받는 값
    @Value("${app.google.client-id:}")
    private String googleClientId;
    // 깃허브 클라이언트 id 받는 값
    @Value("${app.github.client-id:}")
    private String githubClientId;

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
        try {
            userService.signup(request);
            return ResponseEntity.ok(Map.of("message", "회원가입이 완료되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
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
                    .body(Map.of("message", e.getMessage()));
        }
    }
    // 구글 로그인 요청을 처리하는 API - 260325
    @PostMapping("/users/google")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> googleLogin(@RequestBody Map<String, String> payload, HttpSession session) {
        // 프론트에서 전달한 구글 credential을 이용해 로그인 처리
        try {
            // (수정) : 사용자 객체와 신규 가입 여부를 함께 받아서, 구글 최초 로그인만 별도로 분기하도록 함
            UserService.GoogleLoginResult result  = userService.loginWithGoogle(payload.get("credential"), googleClientId);
            User user = result.user();
            // 로그인 성공 시 세션에 사용자 아이디 저장
            session.setAttribute("loginUser", user.getId());

            // 사용자가 소셜로그인 진행시, 기술스택 미입력 여부를 확인하여 프로필에 추가 입력하도록 상태 확인

            // (수정) : 스택 입력 여부가 아니라 "이번 로그인에서 신규 생성된 계정인지"를 기준으로 프로필 입력 이동 여부를 결정
            boolean needsProfileSetup = result.isNewUser();
                    //user.getStackName() == null || user.getStackName().isBlank();

            // 로그인 성공 시 세션에 사용자 아이디 저장
            // (수정) : 프론트에서 /profile-edit 이동 여부를 판단할 수 있도록 needsProfileSetup 값을 응답에 포함
            return ResponseEntity.ok(Map.of("message", "구글 로그인 성공", "id", user.getId(), "needsProfileSetup", needsProfileSetup));
        } catch (IllegalArgumentException e) {
            // 인증 실패 시 401 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            // 서버 내부 오류 발생 시 500 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // 예상하지 못한 예외도 JSON 메시지로 내려서 프론트에서 원인을 확인
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage() == null ? "구글 로그인 처리 중 예외 발생" : e.getMessage()));
        }
    }

    // GitHub에 로그인 요청하는 API - 260326
    @PostMapping("/users/github")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> githubLogin(@RequestBody Map<String, String> payload, HttpSession session) {
        // 프론트에서 전달한 GitHub access token 또는 code를 이용해 로그인 처리
        try {
            // (수정) : 사용자 객체와 신규 가입 여부를 함께 받아서, 깃허브 최초 로그인만 별도로 분기하도록 함
            UserService.GitHubLoginResult result = userService.loginWithGitHub(payload.get("code"), githubClientId);
            User user = result.user();

            // 로그인 성공시 세션에 사용자 ID 저장
            session.setAttribute("loginUser", user.getId());

            // (수정) : 스택 입력 여부가 아니라 "이번 로그인에서 신규 생성된 계정인지"를 기준으로 프로필 입력 이동 여부를 결정
            boolean needsProfileSetup = result.isNewUser();

            // 로그인 성공시 프론트에서 /profile-edit 이동여부 판단하도록 needsProfileSetup에 응답 포함
            return ResponseEntity.ok(Map.of("message","GitHub 로그인 성공", "id", user.getId(), "needsProfileSetup", needsProfileSetup));
        } catch (IllegalArgumentException e) {
            // 인증 실패 시 401 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            // 서버 내부 오류 발생 시 500 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // 예상하지 못한 예외도 JSON 메시지로 내려서 프론트에서 원인을 확인
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage() == null ? "GitHub 로그인 처리 중 예외 발생" : e.getMessage()));
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
