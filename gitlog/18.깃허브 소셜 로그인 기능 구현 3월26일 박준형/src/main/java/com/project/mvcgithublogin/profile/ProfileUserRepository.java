package com.project.mvcgithublogin.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileUserRepository extends JpaRepository<ProfileUser, Long> {

    @Query("select case when count(u) > 0 then true else false end from ProfileUser u where u.id = :loginId")
    boolean existsByLoginId(@Param("loginId") String loginId);

    @Query("select u from ProfileUser u where u.id = :loginId")
    Optional<ProfileUser> findByLoginId(@Param("loginId") String loginId);
}
