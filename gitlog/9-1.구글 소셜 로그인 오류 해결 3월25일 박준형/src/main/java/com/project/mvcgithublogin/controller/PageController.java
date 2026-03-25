package com.project.mvcgithublogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/matching")
    public String matching() {
        return "matching";
    }

    @GetMapping("/matching-detail")
    public String matchingDetail() {
        return "matching-detail";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/profile-edit")
    public String profileEdit() {
        return "profile-edit";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/index.html")
    public String indexHtml() {
        return "redirect:/";
    }

    @GetMapping("/jobs.html")
    public String jobsHtml() {
        return "redirect:/jobs";
    }

    @GetMapping("/matching.html")
    public String matchingHtml() {
        return "redirect:/matching";
    }

    @GetMapping("/matching-detail.html")
    public String matchingDetailHtml() {
        return "redirect:/matching-detail";
    }

    @GetMapping("/profile.html")
    public String profileHtml() {
        return "redirect:/profile";
    }

    @GetMapping("/profile-edit.html")
    public String profileEditHtml() {
        return "redirect:/profile-edit";
    }

    @GetMapping("/login.html")
    public String loginHtml() {
        return "redirect:/login";
    }

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
