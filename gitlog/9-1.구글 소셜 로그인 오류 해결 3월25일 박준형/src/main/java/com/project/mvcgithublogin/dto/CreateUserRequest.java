package com.project.mvcgithublogin.dto;

public class CreateUserRequest {
   private String id;
   private String pw;
   private String nickname;

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

   public String getNormalizedId() {
      return this.id == null ? null : this.id.trim().toLowerCase();
   }

   public String getNormalizedNickname() {
      return this.nickname == null ? null : this.nickname.trim();
   }
}
