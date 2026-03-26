package com.project.mvcgithublogin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mvcgithublogin.domain.User;
import com.project.mvcgithublogin.dto.CreateUserRequest;
import com.project.mvcgithublogin.dto.LoginRequest;
import com.project.mvcgithublogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
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

   @Value("${app.github.client-id:}")
   private String githubClientId;
   // 깃허브 로그인 클라이언트와 연동
   @Value("${app.github.client-secret:}")
   private String githubClientSecret;
   // 깃허브 연동 로그인시 다시 반환하는 페이지
   @Value("${app.github.redirect-uri:}")
   private String githubRedirectUri;

   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   // 회원가입시 받아야하는 정보
   public void signup(CreateUserRequest request) {
      String id = request.getNormalizedId();
      String pw = request.getPw() == null ? null : request.getPw().trim();
      String nickname = request.getNormalizedNickname();
      String stackName = request.getNormalizedStackName();

      validateLocalSignup(id, pw, nickname, stackName);
      ensureEmailNotTaken(id);

      // 회원가입 후 DB에 저장되는 값

      User user = new User();
      user.setId(id);
      user.setPw(passwordEncoder.encode(pw));
      user.setNickname(nickname);
      user.setStackName(stackName);
      user.setLoginType("LOCAL");
      user.setAuthKey("LOCAL_USER");
      userRepository.save(user);
   }

   // 로그인 확인
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

      // GitHub 소셜 로그인 계정 차단
      if ("GITHUB".equalsIgnoreCase(user.getLoginType())) {
         throw new IllegalArgumentException("소셜 로그인으로 가입한 계정입니다. GitHub 로그인을 사용해주세요.");
      }

      if (!passwordEncoder.matches(pw, user.getPw())) {
         throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
      }
      return user;
   }

   // (수정) : 구글 로그인 결과에 사용자 정보와 "신규 가입 여부"를 함께 담아 컨트롤러가 최초 로그인 분기를 할 수 있도록 변경
   public GoogleLoginResult loginWithGoogle(String credential, String expectedAudience) {
      if (credential == null || credential.isBlank()) {
         throw new IllegalArgumentException("구글 인증 정보가 비어 있습니다.");
      }
      if (expectedAudience == null || expectedAudience.isBlank()) {
         throw new IllegalStateException("app.google.client-id 설정이 필요합니다.");
      }

      GoogleTokenInfo tokenInfo = verifyGoogleToken(credential, expectedAudience);
      Optional<User> existingUser = userRepository.find(tokenInfo.email());
      if (existingUser.isPresent()) {
         // (수정) : 이미 가입된 구글 계정이면 신규 가입이 아니므로 false로 반환
         return new GoogleLoginResult(existingUser.get(), false);
      }

      User user = new User();
      user.setId(tokenInfo.email());
      user.setPw(passwordEncoder.encode(UUID.randomUUID().toString()));
      user.setNickname(tokenInfo.nickname());
      user.setLoginType("GOOGLE");
      user.setAuthKey(tokenInfo.subject());
      userRepository.save(user);

      User savedUser = userRepository.find(tokenInfo.email())
              .orElseThrow(() -> new IllegalStateException("구글 로그인 사용자 저장에 실패했습니다."));

      // (수정) : 방금 생성한 구글 계정은 프로필 추가 입력 화면으로 보내야 하므로 true로 반환

      return new GoogleLoginResult(savedUser, true);
   }

   // 깃허브 로그인 결과에 사용자 정보와 "신규 가입 여부"를 함께 담아 컨트롤러가 최초 로그인 분기를 할 수 있도록 변경
   public GitHubLoginResult loginWithGitHub(String code, String expectedAudience) {
      if (code == null || code.isBlank()) {
         throw new IllegalArgumentException("GitHub 인증정보가 비어있습니다.");
      }
      if (expectedAudience == null || expectedAudience.isBlank()) {
         throw new IllegalStateException("app.github.client-id 설정이 필요합니다.");
      }

      // 인가 코드를 access token으로 교환
      String accessToken = exchangeGitHubCodeForAccessToken(code);

      GitHubTokenInfo tokenInfo = verifyGitHubToken(accessToken, expectedAudience);
      Optional<User> existingUser = userRepository.find(tokenInfo.email());
      if (existingUser.isPresent()) {
         // 이미 가입된 깃허브 계정이면 신규 가입이 아니므로 false로 반환
         return new GitHubLoginResult(existingUser.get(), false);
      }

      // 가입시 받을 정보
      User user = new User();
      user.setId(tokenInfo.email());
      user.setPw(passwordEncoder.encode(UUID.randomUUID().toString()));
      user.setNickname(tokenInfo.nickname());
      user.setLoginType("GITHUB");
      user.setAuthKey(tokenInfo.subject());
      userRepository.save(user);

      User savedUser = userRepository.find(tokenInfo.email())
              .orElseThrow(() -> new IllegalStateException("깃허브 로그인 사용자 저장에 실패했습니다."));

      // 방금 생성한 깃허브 계정은 프로필 추가 입력 화면으로 보내야 하므로 true로 반환

      return new GitHubLoginResult(savedUser, true);
   }

   // GitHub 인가 코드를 access token으로 교환
   private String exchangeGitHubCodeForAccessToken(String code) {
      try {
         String requestBody =
                 "client_id=" + URLEncoder.encode(githubClientId, StandardCharsets.UTF_8) +
                         "&client_secret=" + URLEncoder.encode(githubClientSecret, StandardCharsets.UTF_8) +
                         "&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                         "&redirect_uri=" + URLEncoder.encode(githubRedirectUri, StandardCharsets.UTF_8);

         HttpRequest tokenRequest = HttpRequest.newBuilder()
                 .uri(URI.create("https://github.com/login/oauth/access_token"))
                 .header("Accept", "application/json")
                 .header("Content-Type", "application/x-www-form-urlencoded")
                 .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                 .build();

         HttpResponse<String> tokenResponse = httpClient.send(tokenRequest, HttpResponse.BodyHandlers.ofString());

         if (tokenResponse.statusCode() != 200) {
            throw new IllegalArgumentException("GitHub access token 발급에 실패했습니다.");
         }

         JsonNode tokenJson = objectMapper.readTree(tokenResponse.body());
         String accessToken = tokenJson.path("access_token").asText("");

         if (accessToken.isBlank()) {
            throw new IllegalArgumentException("GitHub access token을 가져오지 못했습니다.");
         }

         return accessToken;
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new IllegalStateException("GitHub access token 요청 중 인터럽트가 발생했습니다.", e);
      } catch (IOException e) {
         throw new IllegalStateException("GitHub access token 요청 중 오류가 발생했습니다.", e);
      }
   }

   // GitHub access token으로 사용자 정보와 이메일을 검증하는 메서드
   private GitHubTokenInfo verifyGitHubToken(String accessToken, String expectedAudience) {
      try {
         // GitHub 사용자 기본 정보 조회 요청

         HttpRequest userRequest = HttpRequest.newBuilder()
                 .uri(URI.create("https://api.github.com/user")) // HTTPS 주소 사용
                 .header("Authorization", "Bearer " + accessToken) // access token 전달
                 .header("Accept", "application/vnd.github+json") // GitHub API 응답 형식 지정
                 .header("X-GitHub-Api-Version", "2022-11-28") // GitHub API 버전 지정
                 .GET()
                 .build();

         // 사용자 정보 응답 받기

         HttpResponse<String> userResponse = httpClient.send(userRequest, HttpResponse.BodyHandlers.ofString());

         // 사용자 정보 조회 실패 시 예외 발생
         if (userResponse.statusCode() != 200) {
            throw new IllegalArgumentException("GitHub 사용자 정보 조회에 실패했습니다.");
         }

         // 사용자 정보 JSON 파싱

         JsonNode userJson = objectMapper.readTree(userResponse.body());
         String login = userJson.path("login").asText(""); // GitHub 로그인 아이디
         String name = userJson.path("name").asText("").trim(); // GitHub 이름
         String subject = userJson.path("id").asText(""); // GitHub 고유 사용자 ID

         // GitHub 이메일 목록 조회 요청

         HttpRequest emailRequest = HttpRequest.newBuilder()
                 .uri(URI.create("https://api.github.com/user/emails"))
                 .header("Authorization", "Bearer " + accessToken) // access token 전달
                 .header("Accept", "application/vnd.github+json") // GitHub API 응답 형식 지정
                 .header("X-GitHub-Api-Version", "2022-11-28") // GitHub API 버전 지정
                 .GET()
                 .build();

         // 이메일 정보 응답 받기

         HttpResponse<String> emailResponse = httpClient.send(emailRequest, HttpResponse.BodyHandlers.ofString());

         // 이메일 조회 실패 시 예외 발생

         if (emailResponse.statusCode() != 200) {
            throw new IllegalArgumentException("GitHub 이메일 정보 조회에 실패했습니다.");
         }

         // 이메일 목록 JSON 파싱
         JsonNode emails = objectMapper.readTree(emailResponse.body());
         String email = "";

         // primary + verified 이메일 찾기
         for (JsonNode item : emails) {
            boolean primary = item.path("primary").asBoolean(false); // 대표 이메일 여부
            boolean verified = item.path("verified").asBoolean(false); // 인증 완료 여부

            if (primary && verified) { // 대표 + 인증 이메일 저장
               email = item.path("email").asText("").trim().toLowerCase();
               break;
            }
         }

         // 검증된 이메일이 없으면 예외 발생
         if (email.isBlank()) {
            throw new IllegalArgumentException("GitHub에서 검증된 이메일을 가져오지 못했습니다.");
         }

         // 이름이 없으면 login 값을 닉네임으로 사용
         String nickname = name.isBlank() ? login : name;

         // 이메일, 닉네임, 식별값 묶어서 반환
         return new GitHubTokenInfo(email, nickname, subject);

      } catch (InterruptedException e) {
         // 스레드 인터럽트 상태 복구
         Thread.currentThread().interrupt();
         throw new IllegalStateException("GitHub 로그인 검증 중 인터럽트가 발생했습니다.", e);
      } catch (IOException e) {
         // 네트워크/JSON 처리 오류
         throw new IllegalStateException("GitHub 로그인 검증 중 오류가 발생했습니다.", e);
      }
   }

   private record GitHubTokenInfo(String email, String nickname, String subject) {
   }

   // 회원가입시 입력 할 목록

   private void validateLocalSignup(String id, String pw, String nickname, String stackName) {
      if (id == null || id.isBlank()) {
         throw new IllegalArgumentException("이메일을 입력해주세요.");
      }
      if (pw == null || pw.isBlank()) {
         throw new IllegalArgumentException("비밀번호를 입력해주세요.");
      }
      if (nickname == null || nickname.isBlank()) {
         throw new IllegalArgumentException("닉네임을 입력해주세요.");
      }
      // 회원가입시 기술스택 입력 - 260325
      if (stackName == null || stackName.isBlank()) {
         throw new IllegalArgumentException("기술 스택을 입력해주세요.");
      }
   }

   private void ensureEmailNotTaken(String id) {
      if (userRepository.find(id).isPresent()) {
         throw new IllegalArgumentException("이미 가입된 이메일입니다.");
      }
   }

   // 구글 토큰 검증확인
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

   // (수정) : 컨트롤러에서 구글 로그인 후 이동 경로를 판단할 수 있도록 사용자와 신규 가입 여부를 함께 전달하는 반환형입니다.
   public record GoogleLoginResult(User user, boolean isNewUser) {
   }

   // (수정) : 컨트롤러에서 깃허브 로그인 후 이동 경로를 판단할 수 있도록 사용자와 신규 가입 여부를 함께 전달하는 반환형입니다.
   public record GitHubLoginResult(User user, boolean isNewUser) {
   }
}
