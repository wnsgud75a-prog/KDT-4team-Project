package com.project.mvcgithublogin.service;

import com.project.mvcgithublogin.domain.User;
import com.project.mvcgithublogin.dto.CreateUserRequest;
import com.project.mvcgithublogin.dto.LoginRequest;
import com.project.mvcgithublogin.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
   private final UserRepository userRepository;
   private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   public void signup(CreateUserRequest request) {
      User user = new User();
      user.setId(request.getId());
      user.setPw(passwordEncoder.encode(request.getPw()));
      user.setNickname(request.getNickname());
      user.setLoginType("LOCAL");
      user.setAuthKey("LOCAL_USER");
      this.userRepository.save(user);
   }

   public User login(LoginRequest request) {
      User user = userRepository.find(request.getId())
              .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));

      if (!passwordEncoder.matches(request.getPw(), user.getPw())) {
         throw new IllegalArgumentException("비밀번호 오류");
      }
      return user;
   }
}
