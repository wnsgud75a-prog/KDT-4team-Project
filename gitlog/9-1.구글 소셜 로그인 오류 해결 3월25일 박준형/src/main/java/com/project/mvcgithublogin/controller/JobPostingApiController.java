package com.project.mvcgithublogin.controller;

import com.project.mvcgithublogin.domain.JobPostingPageResponse;
import com.project.mvcgithublogin.service.JobPostingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobPostingApiController {

    private final JobPostingService jobPostingService;

    public JobPostingApiController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @GetMapping("/api/jobs")
    public JobPostingPageResponse getJobsByCategory(
            @RequestParam("categoryId") int categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return jobPostingService.getJobsByCategory(categoryId, page, size);
    }
}
