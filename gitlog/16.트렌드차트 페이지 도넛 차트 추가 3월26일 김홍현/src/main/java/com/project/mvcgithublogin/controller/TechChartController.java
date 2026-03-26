package com.project.mvcgithublogin.controller;

import com.project.mvcgithublogin.dao.TechChartDAO;
import com.project.mvcgithublogin.dto.JobPostingbyTech;
import com.project.mvcgithublogin.dto.TechChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TechChartController {
    @Autowired
    private TechChartDAO techdao;

    @GetMapping("/jobs")
    public String jobs(
            @RequestParam(value = "cat", defaultValue = "1") int cat,
            Model model) {

        List<TechChart> list = techdao.getTechStats(cat);

        model.addAttribute("list", list);
        model.addAttribute("selectedCat", cat);

        model.addAttribute("techMap", techdao.getTechCategoryMap());

        List<TechChart> categoryList = techdao.getCategoryStats();

        model.addAttribute("categoryList", categoryList);
        return "jobs";
    }

    @GetMapping("/jobs/by-tech")
    @ResponseBody
    public List<JobPostingbyTech> getJobsByTech(
            @RequestParam String stackname,
            @RequestParam int cat) {

        return techdao.getJobsByTech(stackname, cat);
    }
}
