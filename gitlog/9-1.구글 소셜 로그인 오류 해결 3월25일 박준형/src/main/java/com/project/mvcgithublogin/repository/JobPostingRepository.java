package com.project.mvcgithublogin.repository;

import com.project.mvcgithublogin.domain.JobPosting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobPostingRepository {
    List<JobPosting> findAll();

    List<String> findCompanyNames();

    List<JobPosting> findByCategoryIdWithPaging(@Param("categoryId") int categoryId,
                                                @Param("startRow") int startRow,
                                                @Param("endRow") int endRow);

    int countByCategoryId(@Param("categoryId") int categoryId);
}
