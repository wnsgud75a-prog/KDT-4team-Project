package com.project.join.dto;

import java.util.List;

// 프로필 수정 요청 본문에서 받는 DTO
public class UserProfileRequest {

    private String id;
    private String nickname;
    private String intro;
    // 사용자가 선택한 기술 스택 이름 목록
    private List<String> stackNames;
    // 프로필 수정 화면에서 새 비밀번호를 함께 보낼 때 사용하는 값
    private String newPassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<String> getStackNames() {
        return stackNames;
    }

    public void setStackNames(List<String> stackNames) {
        this.stackNames = stackNames;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
