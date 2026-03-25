package com.project.mvcgithublogin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mvcgithublogin.domain.User;
import com.project.mvcgithublogin.dto.CreateUserRequest;
import com.project.mvcgithublogin.dto.LoginRequest;
import com.project.mvcgithublogin.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
   private final UserRepository userRepository;
   private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
   private final HttpClient httpClient = HttpClient.newHttpClient();
   private final ObjectMapper objectMapper = new ObjectMapper();

   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   public void signup(CreateUserRequest request) {
      String id = request.getNormalizedId();
      String pw = request.getPw() == null ? null : request.getPw().trim();
      String nickname = request.getNormalizedNickname();

      validateLocalSignup(id, pw, nickname);
      ensureEmailNotTaken(id);

      User user = new User();
      user.setId(id);
      user.setPw(passwordEncoder.encode(pw));
      user.setNickname(nickname);
      user.setLoginType("LOCAL");
      user.setAuthKey("LOCAL_USER");
      userRepository.save(user);
   }

   public User login(LoginRequest request) {
      String id = request.getId() == null ? null : request.getId().trim().toLowerCase();
      String pw = request.getPw() == null ? null : request.getPw().trim();

      if (id == null || id.isBlank() || pw == null || pw.isBlank()) {
         throw new IllegalArgumentException("이메일과 비밀번호를 입력해주세요.");
      }

      User user = userRepository.find(id)
              .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

      if ("GOOGLE".equalsIgnoreCase(user.getLoginType())) {
         throw new IllegalArgumentException("소셜 로그인으로 가입한 계정입니다. Google 로그인을 사용해주세요.");
      }

      if (!passwordEncoder.matches(pw, user.getPw())) {
         throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
      }
      return user;
   }

   public User loginWithGoogle(String credential, String expectedAudience) {
      if (credential == null || credential.isBlank()) {
         throw new IllegalArgumentException("구글 인증 정보가 비어 있습니다.");
      }
      if (expectedAudience == null || expectedAudience.isBlank()) {
         throw new IllegalStateException("app.google.client-id 설정이 필요합니다.");
      }

      GoogleTokenInfo tokenInfo = verifyGoogleToken(credential, expectedAudience);
      Optional<User> existingUser = userRepository.find(tokenInfo.email());
      if (existingUser.isPresent()) {
         return existingUser.get();
      }

      User user = new User();
      user.setId(tokenInfo.email());
      user.setPw(passwordEncoder.encode(UUID.randomUUID().toString()));
      user.setNickname(tokenInfo.nickname());
      user.setLoginType("GOOGLE");
      user.setAuthKey(tokenInfo.subject());
      userRepository.save(user);

      return userRepository.find(tokenInfo.email())
              .orElseThrow(() -> new IllegalStateException("구글 로그인 사용자 저장에 실패했습니다."));
   }

   private void validateLocalSignup(String id, String pw, String nickname) {
      if (id == null || id.isBlank()) {
         throw new IllegalArgumentException("이메일을 입력해주세요.");
      }
      if (pw == null || pw.isBlank()) {
         throw new IllegalArgumentException("비밀번호를 입력해주세요.");
      }
      if (nickname == null || nickname.isBlank()) {
         throw new IllegalArgumentException("닉네임을 입력해주세요.");
      }
   }

   private void ensureEmailNotTaken(String id) {
      if (userRepository.find(id).isPresent()) {
         throw new IllegalArgumentException("이미 가입된 이메일입니다.");
      }
   }

   private GoogleTokenInfo verifyGoogleToken(String credential, String expectedAudience) {
      try {
         String encodedCredential = URLEncoder.encode(credential, StandardCharsets.UTF_8);
         HttpRequest request = HttpRequest.newBuilder()
                 .uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=" + encodedCredential))
                 .GET()
                 .build();

         HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
         if (response.statusCode() != 200) {
            throw new IllegalArgumentException("구글 토큰 검증에 실패했습니다.");
         }

         JsonNode json = objectMapper.readTree(response.body());
         String audience = json.path("aud").asText("");
         String email = json.path("email").asText("").trim().toLowerCase();
         boolean emailVerified = Boolean.parseBoolean(json.path("email_verified").asText("false"));
         String subject = json.path("sub").asText("");
         String name = json.path("name").asText("").trim();

         if (!expectedAudience.equals(audience)) {
            throw new IllegalArgumentException("허용되지 않은 구글 클라이언트입니다.");
         }
         if (!emailVerified || email.isBlank()) {
            throw new IllegalArgumentException("검증된 구글 이메일이 필요합니다.");
         }

         String nickname = name.isBlank() ? email.substring(0, email.indexOf("@")) : name;
         return new GoogleTokenInfo(email, nickname, subject);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new IllegalStateException("구글 로그인 검증 중 인터럽트가 발생했습니다.", e);
      } catch (IOException e) {
         throw new IllegalStateException("구글 로그인 검증 중 오류가 발생했습니다.", e);
      }
   }

   private record GoogleTokenInfo(String email, String nickname, String subject) {
   }
}
