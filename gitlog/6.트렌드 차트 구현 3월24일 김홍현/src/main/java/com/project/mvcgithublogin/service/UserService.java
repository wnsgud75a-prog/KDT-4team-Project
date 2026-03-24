package com.project.mvcgithublogin.service;

import com.project.mvcgithublogin.domain.User;
import com.project.mvcgithublogin.dto.CreateUserRequest;
import com.project.mvcgithublogin.dto.LoginRequest;
import com.project.mvcgithublogin.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
   private final UserRepository userRepository;

   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   public void signup(CreateUserRequest request) {
      User user = new User();
      user.setId(request.getId());
      user.setPw(request.getPw());
      user.setNickname(request.getNickname());
      user.setLoginType("LOCAL");
      user.setAuthKey("LOCAL_USER");
      this.userRepository.save(user);
   }

   public User login(LoginRequest request) {
      User user = userRepository.find(request.getId(), request.getPw())
              .orElseThrow(()-> new IllegalArgumentException("해당 사용자 없음"));

      if(!user.getPw().equals(request.getPw())) {
         throw new IllegalArgumentException("비밀번호 오류");
      }
      return user;
   }

   public void delete(String id) {
      userRepository.delete(id);
   }
}
