package com.project.mvcgithublogin.repository;

import com.project.mvcgithublogin.domain.TechStack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TechStackRepository {
    List<TechStack> findByCategoryId(@Param("categoryId") int categoryId);
    List<TechStack> findAllStacks();
}
