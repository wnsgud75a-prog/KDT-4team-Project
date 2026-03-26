package com.project.mvcgithublogin.dto;

public class TechChart {
    private String stackname;
    private int cnt;
    private double percent;
    private int categoryId;

    public String getStackname() {
        return stackname;
    }

    public void setStackname(String stackname) {
        this.stackname = stackname;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getCategoryId() {
        return categoryId;

    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
