package com.project.mvcgithublogin.service;

import com.project.mvcgithublogin.domain.TechStack;
import com.project.mvcgithublogin.repository.TechStackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechStackService {
    private final TechStackRepository techStackRepository;

    public TechStackService(TechStackRepository techStackRepository) {
        this.techStackRepository = techStackRepository;
    }

    public List<TechStack> findByCategoryId(int categoryId) {
        return techStackRepository.findByCategoryId(categoryId);
    }

    public List<TechStack> findAllStacks() {
        return techStackRepository.findAllStacks();
    }
}
