package com.project.mvcgithublogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JobPostingController {
    public JobPostingController() {
    }

    @GetMapping({"/", "/index"})
    public String jobs() {
        return "index";
    }
}
