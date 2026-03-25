package com.project.mvcgithublogin.dto;

public class TechChart {
    private String stackname;
    private int cnt;
    private double percent;

    public String getStackname() {
        return this.stackname;
    }

    public void setStackname(String stackname) {
        this.stackname = stackname;
    }

    public int getCnt() {
        return this.cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public double getPercent() {
        return this.percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
