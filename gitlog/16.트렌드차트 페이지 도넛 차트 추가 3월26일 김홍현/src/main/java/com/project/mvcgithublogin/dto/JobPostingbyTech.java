package com.project.mvcgithublogin.dto;

public class JobPostingbyTech {
    private String companyname;
    private String jobposition;
    private String techstack;
    private String postingurl;

    private String salary;
    private String education;
    private String region;
    private String employmenttype;
    private String posteddate;
    private String deadline;
    private int categoryId;

    public String getCompanyName() { return companyname; }
    public void setCompanyName(String companyname) { this.companyname = companyname; }

    public String getJobPosition() { return jobposition; }
    public void setJobPosition(String company) { this.jobposition = company; }

    public String getTechStack() { return techstack; }
    public void setTechStack(String techstack) { this.techstack = techstack; }

    public String getPostingURL() { return postingurl; }
    public void setPostingURL(String postingurl) { this.postingurl = postingurl; }


    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getEmploymentType() { return employmenttype; }
    public void setEmploymentType(String employmenttype) { this.employmenttype = employmenttype; }

    public String getPostedDate() { return posteddate; }
    public void setPostedDate(String posteddate) { this.posteddate = posteddate; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}
