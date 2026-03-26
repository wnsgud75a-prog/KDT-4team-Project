package com.project.mvcgithublogin.dto;

import java.util.List;

public class ProfileUserProfileRequest {

    private String id;
    private String nickname;
    private List<String> stackNames;
    private String newPassword;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
