package com.project.join.repository;

import com.project.join.domain.User;
import com.project.join.domain.UserStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// USER_STACK 테이블을 조회하고 저장하는 JPA 저장소
public interface UserStackRepository extends JpaRepository<UserStack, Long> {

    // 사용자의 기존 기술 스택을 전부 삭제
    void deleteByUser(User user);

    // 사용자의 현재 기술 스택 목록 조회
    List<UserStack> findByUser(User user);

    // 변경한 기술 스택을 생성하고 저장하기 위해 현재 최대값 조회
    @Query("select coalesce(max(us.userStackId), 0) from UserStack us")
    Long findMaxUserStackId();
}
