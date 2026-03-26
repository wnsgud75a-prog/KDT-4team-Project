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

    @Column(name = "INTRO")
    private String intro;

//    @Column(name = "PHONE")
//    private String phone;

    @Column(name = "STACK_NAME")
    private String stackName;

    // 회원가입할 때 회원 객체를 편하게 생성하기 위한 빌더 생성자
    @Builder
    public User(Long userNo, String id, String password, String nickname, String loginType, String authkey,
                String intro, String stackName)//,
                //String phone)
    {
        this.userNo = userNo;
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.loginType = loginType;
        this.authkey = authkey;
        this.intro = intro;
        this.stackName = stackName;
//        this.phone = phone;
    }

    // 프로필 수정 시 닉네임, 자기소개, 기술스택 문자열을 변경
    public void updateProfile(String nickname, String intro, String stackName) {
        this.nickname = nickname;
        this.intro = intro;
        this.stackName = stackName;
    }

    // 변경한 비밀번호를 암호화된 값으로 교체
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
