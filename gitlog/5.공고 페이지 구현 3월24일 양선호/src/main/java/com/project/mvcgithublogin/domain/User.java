package com.project.mvcgithublogin.domain;

public class User {
   private Long userId;
   private String id;
   private String pw;
   private String nickname;
   private String loginType;
   private String authKey;

   public Long getUserId() {
      return this.userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

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

   public String getLoginType() {
      return this.loginType;
   }

   public void setLoginType(String loginType) {
      this.loginType = loginType;
   }

   public String getAuthKey() {
      return this.authKey;
   }

   public void setAuthKey(String authKey) {
      this.authKey = authKey;
   }
}
