package com.project.join.service;

import com.project.join.domain.User;
import com.project.join.domain.UserStack;
import com.project.join.dto.UserProfileRequest;
import com.project.join.repository.UserRepository;
import com.project.join.repository.UserStackRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

// 회원 관련 비즈니스 로직을 처리하는 서비스
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserStackRepository userStackRepository;

    // 회원가입과 비밀번호 변경 시 비밀번호를 암호화하기 위한 객체
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, UserStackRepository userStackRepository) {
        this.userRepository = userRepository;
        this.userStackRepository = userStackRepository;
    }

    // 로그인 아이디 중복 여부를 확인
    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    // 비밀번호를 암호화한 뒤 회원 정보 저장
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // 프로필 수정 시 닉네임과 기술 스택을 함께 저장
    @Transactional
    public User updateProfile(UserProfileRequest request) {
        // 1. 로그인 아이디로 수정할 사용자를 찾기
        User user = userRepository.findByLoginId(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 사용자입니다."));

        // 2. USERS 테이블에서는 닉네임 같은 기본 프로필 값을 수정
        user.updateProfile(request.getNickname(),request.getIntro());

        // 비밀번호 변경란에 값이 들어오면 암호화해서 PW 컬럼도 함께 변경
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        }

        // 3. USER_STACK 테이블의 기존 기술 스택은 모두 지우고
        //    사용자가 새로 보낸 stackIds 값으로 다시 저장
        userStackRepository.deleteByUser(user);

        long nextId = userStackRepository.findMaxUserStackId() + 1L;
        if (request.getStackIds() != null) {
            for (Long stackId : request.getStackIds()) {
                UserStack userStack = new UserStack();
                userStack.setUserStackId(nextId++);
                userStack.setUser(user);
                userStack.setStackId(stackId);
                userStackRepository.save(userStack);
            }
        }

        return user;
    }

    // 저장된 기술 스택을 다시 조회해서 응답에 사용
    @Transactional(readOnly = true)
    public List<Long> getUserStackIds(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 사용자입니다."));

        List<UserStack> stacks = userStackRepository.findByUser(user);
        if (stacks == null || stacks.isEmpty()) {
            return Collections.emptyList();
        }

        return stacks.stream()
                .map(UserStack::getStackId)
                .toList();
    }
}
