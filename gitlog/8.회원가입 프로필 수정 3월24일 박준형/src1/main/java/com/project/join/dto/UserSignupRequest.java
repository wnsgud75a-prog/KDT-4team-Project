package com.project.join.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    // 휴대폰 번호는 선택값으로 처리
    @Pattern(regexp = "^$|^01[0-9]{8,9}$", message = "휴대폰번호 형식이 올바르지 않습니다.")
    private String phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
