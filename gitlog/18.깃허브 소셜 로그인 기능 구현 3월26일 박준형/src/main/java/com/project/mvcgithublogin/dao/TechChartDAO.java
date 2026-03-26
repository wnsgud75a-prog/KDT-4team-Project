package com.project.mvcgithublogin.dao;

import com.project.mvcgithublogin.dto.ChartCache;
import com.project.mvcgithublogin.dto.TechChart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TechChartDAO {
    @Autowired
    private DataSource dataSource;
    private Map<Integer, ChartCache> cache = new HashMap();
    private final long EXPIRE_TIME = 1209600000L;

    public List<TechChart> getTechStats(int categoryId) {
        if (this.cache.containsKey(categoryId)) {
            ChartCache cached = (ChartCache)this.cache.get(categoryId);
            long now = System.currentTimeMillis();
            if (now - cached.getTime() < 1209600000L) {
                System.out.println("Cache : 1");
                return cached.getData();
            }

            System.out.println("Cache : 0");
        }

        System.out.println("DB : 1");
        List<TechChart> list = new ArrayList();
        String sql = "SELECT stack_name, cnt, percent FROM tech_cal WHERE category_id = ? ORDER BY percent DESC";

        try (
                Connection conn = this.dataSource.getConnection();
                PreparedStatement psmt = conn.prepareStatement(sql);
        ) {
            psmt.setInt(1, categoryId);
            ResultSet rs = psmt.executeQuery();

            while(rs.next()) {
                TechChart chart = new TechChart();
                chart.setStackname(rs.getString("stack_name"));
                chart.setCnt(rs.getInt("cnt"));
                chart.setPercent(rs.getDouble("percent"));
                list.add(chart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.cache.put(categoryId, new ChartCache(list));
        return list;
    }
}
