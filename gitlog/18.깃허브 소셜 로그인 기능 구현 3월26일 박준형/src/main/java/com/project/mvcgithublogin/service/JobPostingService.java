package com.project.mvcgithublogin.service;

import com.project.mvcgithublogin.domain.JobPosting;
import com.project.mvcgithublogin.domain.JobPostingPageResponse;
import com.project.mvcgithublogin.repository.JobPostingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;

    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    public List<JobPosting> findAll() {
        return jobPostingRepository.findAll();
    }

    public List<String> findCompanyNames() {
        return jobPostingRepository.findCompanyNames();
    }

    public JobPostingPageResponse getJobsByCategory(int categoryId, int page, int size) {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 10;
        }

        int totalCount = jobPostingRepository.countByCategoryId(categoryId);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        int startRow = (page - 1) * size + 1;
        int endRow = page * size;

        List<JobPosting> jobs = jobPostingRepository.findByCategoryIdWithPaging(categoryId, startRow, endRow);

        return new JobPostingPageResponse(jobs, page, size, totalCount, totalPages);
    }
}
