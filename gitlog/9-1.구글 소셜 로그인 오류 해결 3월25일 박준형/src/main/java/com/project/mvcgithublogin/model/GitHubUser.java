package com.project.mvcgithublogin.model;

public class GitHubUser {

    private final String login;
    private final String name;
    private final String email;
    private final String avatarUrl;
    private final String profileUrl;

    public GitHubUser(String login, String name, String email, String avatarUrl, String profileUrl) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.profileUrl = profileUrl;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
}
