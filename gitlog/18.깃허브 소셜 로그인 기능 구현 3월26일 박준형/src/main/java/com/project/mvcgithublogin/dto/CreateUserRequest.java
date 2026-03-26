package com.project.mvcgithublogin.dto;

public class CreateUserRequest {
   private String id;
   private String pw;
   private String nickname;
   private String stackName;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getPw() {
      return this.pw;
   }

   public void setPw(String pw) {
      this.pw = pw;
   }

   public String getNickname() {
      return this.nickname;
   }

   public void setNickname(String nickname) {
      this.nickname = nickname;
   }

   // 기술스택 추가

   public String getStackName() {
      return this.stackName;
   }
   public void setStackName(String stackName) {
      this.stackName = stackName;
   }



   // ID 공백 제거, 소문자 변환
   public String getNormalizedId() {
      return this.id == null ? null : this.id.trim().toLowerCase();
   }
   // 닉네임 공백 제거
   public String getNormalizedNickname() {
      return this.nickname == null ? null : this.nickname.trim();
   }
   // 기술스택 공백 제거
   public String getNormalizedStackName() {
      return this.stackName == null ? null : this.stackName.trim();
   }
}
