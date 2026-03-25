package com.project.mvcgithublogin.dto;

import java.util.List;

public class ChartCache {
    private List<TechChart> data;
    private long time;

    public ChartCache(List<TechChart> data) {
        this.data = data;
        this.time = System.currentTimeMillis();
    }

    public List<TechChart> getData() {
        return this.data;
    }

    public long getTime() {
        return this.time;
    }
}
