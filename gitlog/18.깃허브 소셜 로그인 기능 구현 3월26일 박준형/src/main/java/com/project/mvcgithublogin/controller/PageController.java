package com.project.mvcgithublogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {
    // 매칭분석 페이지로 이동
    @GetMapping("/matching")
    public String matching() {
        return "matching";
    }

    @GetMapping("/matching-detail")
    public String matchingDetail() {
        return "matching-detail";
    }

    // 프로필 페이지
    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
    // 프로필 수정 페이지
    @GetMapping("/profile-edit")
    public String profileEdit() {
        return "profile-edit";
    }
    //로그인
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    // 메인페이지
    @GetMapping("/index.html")
    public String indexHtml() {
        return "redirect:/";
    }
    // 공고보기 페이지
    @GetMapping("/jobs.html")
    public String jobsHtml() {
        return "redirect:/jobs";
    }
    // 매칭페이지
    @GetMapping("/matching.html")
    public String matchingHtml() {
        return "redirect:/matching";
    }

    @GetMapping("/matching-detail.html")
    public String matchingDetailHtml() {
        return "redirect:/matching-detail";
    }
    // 프로필 페이지
    @GetMapping("/profile.html")
    public String profileHtml() {
        return "redirect:/profile";
    }
    // 프로필 수정 페이지
    @GetMapping("/profile-edit.html")
    public String profileEditHtml() {
        return "redirect:/profile-edit";
    }
    // 로그인 페이지
    @GetMapping("/login.html")
    public String loginHtml() {
        return "redirect:/login";
    }
    // 회원가입 페이지
    @GetMapping("/signup.html")
    public String signupHtml() {
        return "redirect:/signup";
    }

    @GetMapping("/templates/{page}.html")
    public String legacyTemplates(@PathVariable String page) {
        switch (page) {
            case "index":
                return "redirect:/";
            case "jobs":
                return "redirect:/jobs";
            case "matching":
                return "redirect:/matching";
            case "profile":
                return "redirect:/profile";
            case "profile-edit":
                return "redirect:/profile-edit";
            case "login":
                return "redirect:/login";
            default:
                return "redirect:/";
        }
    }
}
