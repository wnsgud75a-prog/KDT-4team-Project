package com.project.join.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// USERS 테이블과 매핑되는 회원 엔티티
@Entity
@Table(name = "USERS")
@NoArgsConstructor
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeqGenerator")
    @SequenceGenerator(name = "userSeqGenerator", sequenceName = "SEQ_USERS", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long userNo;

    @Column(name = "ID", nullable = false, unique = true)
    private String id;

    @Column(name = "PW", nullable = false)
    private String password;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "LOGIN_TYPE", nullable = false)
    private String loginType;

    @Column(name = "AUTH_KEY", nullable = false)
    private String authkey;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "INTRO")
    private String intro;

    // 회원가입할 때 회원 객체를 편하게 생성하기 위한 빌더 생성자
    @Builder
    public User(Long userNo, String id, String password, String nickname, String loginType, String authkey,
                String phone,  String intro) {
        this.userNo = userNo;
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.loginType = loginType;
        this.authkey = authkey;
        this.phone = phone;
        this.intro = intro;
    }

    // 프로필 수정 시 현재는 닉네임 변경 및 한줄 소개 추가
    public void updateProfile(String nickname, String intro) {
        this.nickname = nickname;
        this.intro = intro;
    }

    // 변경된 비밀번호를 암호화된 값으로 교체
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }


}
