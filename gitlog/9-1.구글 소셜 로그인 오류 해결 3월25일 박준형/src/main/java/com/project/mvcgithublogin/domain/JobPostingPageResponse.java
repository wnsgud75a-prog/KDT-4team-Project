package com.project.mvcgithublogin.domain;

import java.util.List;

public class JobPostingPageResponse {
    private final List<JobPosting> jobs;
    private final int currentPage;
    private final int size;
    private final int totalCount;
    private final int totalPages;

    public JobPostingPageResponse(List<JobPosting> jobs, int currentPage, int size, int totalCount, int totalPages) {
        this.jobs = jobs;
        this.currentPage = currentPage;
        this.size = size;
        this.totalCount = totalCount;
        this.totalPages = totalPages;
    }

    public List<JobPosting> getJobs() {
        return jobs;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getSize() {
        return size;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
