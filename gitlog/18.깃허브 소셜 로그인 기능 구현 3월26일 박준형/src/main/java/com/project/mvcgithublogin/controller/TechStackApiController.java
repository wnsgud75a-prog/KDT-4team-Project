package com.project.mvcgithublogin.controller;

import com.project.mvcgithublogin.domain.TechStack;
import com.project.mvcgithublogin.service.TechStackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TechStackApiController {
    private final TechStackService techStackService;

    public TechStackApiController(TechStackService techStackService) {
        this.techStackService = techStackService;
    }

    @GetMapping("/api/stacks")
    public List<TechStack> stacks(@RequestParam(value = "categoryId", required = false) Integer categoryId) {
        if (categoryId == null) {
            return techStackService.findAllStacks();
        }
        return techStackService.findByCategoryId(categoryId);
    }
}
