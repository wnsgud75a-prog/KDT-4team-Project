package com.project.join.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

// 회원가입 요청 본문에서 전달받는 값을 담는 DTO
public class UserSignupRequest {

    // 로그인에 사용할 아이디
    @NotBlank(message = "아이디는 필수입니다.")
    @Size(max = 50, message = "아이디는 50자 이하여야 합니다.")
    private String id;

    // 회원가입 시 입력한 비밀번호
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 255, message = "비밀번호는 4자 이상이어야 합니다.")
    private String pw;

    // 화면에 표시할 닉네임
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 50, message = "닉네임은 50자 이하여야 합니다.")
    private String nickname;

    // 회원가입 시 함께 저장할 기술 스택 이름 목록
    private List<String> stackNames;

    public UserSignupRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
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

//    public String getPhone(){
//        return phone;
//    }
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
}
