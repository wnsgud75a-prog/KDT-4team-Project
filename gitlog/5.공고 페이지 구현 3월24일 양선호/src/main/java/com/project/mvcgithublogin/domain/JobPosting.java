package com.project.mvcgithublogin.domain;

import lombok.Data;

@Data
public class JobPosting {
    private Long postingId;
    private String companyName;
    private String postedDate;
    private String deadline;
    private String region;
    private String employmentType;
    private String education;
    private String jobPosition;
    private String techStack;
    private String salary;
    private String postingUrl;
}