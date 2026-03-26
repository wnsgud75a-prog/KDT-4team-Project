package com.project.mvcgithublogin.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
public class ProfileUser {

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

    @Column(name = "STACK_NAME")
    private String stackName;

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getAuthkey() {
        return authkey;
    }

    public void setAuthkey(String authkey) {
        this.authkey = authkey;
    }


    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getStackName() {
        return stackName;
    }

    public void setStackName(String stackName) {
        this.stackName = stackName;
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
