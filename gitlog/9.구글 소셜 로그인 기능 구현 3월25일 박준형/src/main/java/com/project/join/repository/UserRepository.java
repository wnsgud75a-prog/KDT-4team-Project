package com.project.join.repository;

import com.project.join.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// USERS 테이블을 조회하고 저장하는 JPA 저장소
public interface UserRepository extends JpaRepository<User, Long> {

    // 회원가입 시 입력한 아이디가 이미 존재하는지 중복 확인
    @Query("select case when count(u) > 0 then true else false end from User u where u.id = :loginId")
    boolean existsByLoginId(@Param("loginId") String loginId);

    // 프로필 수정이나 추가 조회 시 로그인 아이디로 사용자 조회
    @Query("select u from User u where u.id = :loginId")
    Optional<User> findByLoginId(@Param("loginId") String loginId);
}
