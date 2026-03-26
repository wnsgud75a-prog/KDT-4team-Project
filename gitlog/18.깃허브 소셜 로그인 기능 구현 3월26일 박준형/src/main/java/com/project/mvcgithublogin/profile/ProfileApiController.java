package com.project.mvcgithublogin.profile;

import com.project.mvcgithublogin.dto.ProfileUserProfileRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@RestController
public class ProfileApiController {
    private final ProfileUserService profileUserService;

    public ProfileApiController(ProfileUserService profileUserService) {
        this.profileUserService = profileUserService;
    }

    @PutMapping("/users/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody ProfileUserProfileRequest request) {
        try {
            ProfileUser updatedUser = profileUserService.updateProfile(request);
            List<String> savedStackNames = profileUserService.getUserStackNames(updatedUser.getId());
            // 프로필 수정 완료시 메시지 출력
            return ResponseEntity.ok(
                    Map.of(
                            "message", "프로필 수정이 완료되었습니다.",
                            "userNo", updatedUser.getUserNo(),
                            "id", updatedUser.getId(),
                            "nickname", updatedUser.getNickname(),
                            "intro", updatedUser.getIntro() == null ? "" : updatedUser.getIntro(), // 프로필 수정시 자기소개를 비워도 저장 실패 오류가 안뜨도록 null값 추가
                            "stackNames", savedStackNames
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/users/profile")
    public ResponseEntity<Map<String, Object>> getProfile(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.ok(Map.of("loggedIn", false));
        }
        // 프로필 수정란
        // 수정목록 : 닉네임(null이 있으므로 공백으로 입력 가능),자기소개,기술스택
        ProfileUser user = profileUserService.getByLoginId(loginUser.toString());
        List<String> stackNames = profileUserService.getUserStackNames(user.getId());

        return ResponseEntity.ok(
                Map.of(
                        "loggedIn", true,
                        "id", user.getId(),
                        "nickname", user.getNickname(),
                        "intro", user.getIntro() == null?"":user.getIntro(),
                        "stackNames", stackNames
                )
        );
    }
}
