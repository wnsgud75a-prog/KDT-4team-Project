package com.project.join.dto;

import java.util.List;

// 프로필 수정 요청 본문에서 받는 DTO
public class UserProfileRequest {

    private String id;
    private String nickname;
    // 사용자가 선택한 기술 스택 ID 목록
    private List<Long> stackIds;
    // 프로필 수정 화면에서 새 비밀번호를 함께 보낼 때 사용하는 값 (비밀번호 수정)
    private String newPassword;
    // 프로필 수정시 한줄 소개를 위한 값
    private String intro;


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

    public List<Long> getStackIds() {
        return stackIds;
    }

    public void setStackIds(List<Long> stackIds) {
        this.stackIds = stackIds;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

}
