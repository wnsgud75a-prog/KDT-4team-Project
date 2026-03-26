package com.project.mvcgithublogin.dao;

import com.project.mvcgithublogin.dto.ChartCache;
import com.project.mvcgithublogin.dto.JobPostingbyTech;
import com.project.mvcgithublogin.dto.TechChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TechChartDAO {

    @Autowired
    private DataSource dataSource;

    // 캐시 선언
    private Map<Integer, ChartCache> cache = new HashMap<>();

    // 2주 (밀리초)
    private final long EXPIRE_TIME = 1000L * 60 * 60 * 24 * 14;

    public List<TechChart> getTechStats(int categoryId) {

        // 캐시 확인
        if (cache.containsKey(categoryId)) {
            ChartCache cached = cache.get(categoryId);

            long now = System.currentTimeMillis();

            // 유효 시간 체크
            if (now - cached.getTime() < EXPIRE_TIME) {
                System.out.println("Cache : 1");
                return cached.getData();
            } else {
                System.out.println("Cache : 0");
            }
        }

        // DB 조회
        System.out.println("Chart DB : 1");

        List<TechChart> list = new ArrayList<>();

        String sql = "SELECT stack_name, cnt, percent " +
                "FROM tech_cal " +
                "WHERE category_id = ? " +
                "ORDER BY percent DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)) {

            psmt.setInt(1, categoryId);
            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                TechChart chart = new TechChart();
                chart.setStackname(rs.getString("stack_name"));
                chart.setCnt(rs.getInt("cnt"));
                chart.setPercent(rs.getDouble("percent"));
                list.add(chart);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 캐시에 저장
        cache.put(categoryId, new ChartCache(list));

        return list;
    }

    private String getCategoryName(int categoryId) {
        switch (categoryId) {
            case 1: return "프론트엔드";
            case 2: return "백엔드";
            case 3: return "AI/데이터";
            case 4: return "인프라/보안";
            case 5: return "모바일";
            case 6: return "기타";
            default: return "Unknown";
        }
    }

    public List<TechChart> getCategoryStats() {

        int CATEGORY_ALL_KEY = -1;

        // 캐시 확인
        if (cache.containsKey(CATEGORY_ALL_KEY)) {
            ChartCache cached = cache.get(CATEGORY_ALL_KEY);

            long now = System.currentTimeMillis();

            if (now - cached.getTime() < EXPIRE_TIME) {
                System.out.println("Category Cache : 1");
                return (List<TechChart>) cached.getData();
            } else {
                System.out.println("Category Cache : 0");
            }
        }

        System.out.println("Category DB : 1");

        List<TechChart> list = new ArrayList<>();

        String sql =
                "SELECT category_id, SUM(cnt) AS total_cnt, " +
                        "ROUND(SUM(cnt) * 100.0 / SUM(SUM(cnt)) OVER (), 2) AS percent " +
                        "FROM tech_cal " +
                        "GROUP BY category_id " +
                        "ORDER BY total_cnt DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)) {

            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                TechChart chart = new TechChart();

                int categoryId = rs.getInt("category_id");

                // 카테고리 이름 매핑
                chart.setCategoryId(categoryId);
                chart.setStackname(getCategoryName(categoryId));

                chart.setCnt(rs.getInt("total_cnt"));
                chart.setPercent(rs.getDouble("percent"));

                list.add(chart);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        cache.put(CATEGORY_ALL_KEY, new ChartCache(list));

        return list;
    }

    public List<JobPostingbyTech> getJobsByTech(String stackname, int categoryId) {

        List<JobPostingbyTech> list = new ArrayList<>();

        String sql = "SELECT \n" +
                "    jp.company_name,\n" +
                "    jp.job_position,\n" +
                "    jp.tech_stack,\n" +
                "    jp.posting_url,\n" +
                "    jp.salary,\n" +
                "    jp.education,\n" +
                "    jp.region,\n" +
                "    jp.employment_type,\n" +
                "    jp.posted_date,\n" +
                "    jp.deadline,\n" +
                "    ts.category_id,\n" +
                "    TRIM(REGEXP_SUBSTR(jp.tech_stack, '[^,]+', 1, l.lvl)) AS tech\n" +
                "FROM job_posting jp\n" +
                "CROSS JOIN (\n" +
                "    SELECT LEVEL AS lvl FROM dual CONNECT BY LEVEL <= 10\n" +
                ") l\n" +
                "JOIN tech_stack ts\n" +
                "    ON TRIM(REGEXP_SUBSTR(jp.tech_stack, '[^,]+', 1, l.lvl)) = ts.stack_name\n" +
                "WHERE \n" +
                "    REGEXP_SUBSTR(jp.tech_stack, '[^,]+', 1, l.lvl) IS NOT NULL\n" +
                "    AND TRIM(REGEXP_SUBSTR(jp.tech_stack, '[^,]+', 1, l.lvl)) = ?\n" +
                "    AND jp.posted_date >= ADD_MONTHS(SYSDATE, -4)" +
                "ORDER BY jp.posted_date DESC"; // SYSDATE, 뒷 부분이 최근 (4)달 이내 데이터 조회 조건

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)) {

            psmt.setString(1, stackname);
            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                JobPostingbyTech job = new JobPostingbyTech();
                job.setCompanyName(rs.getString("company_name"));
                job.setJobPosition(rs.getString("job_position"));
                job.setTechStack(rs.getString("tech_stack"));
                job.setPostingURL(rs.getString("posting_url"));

                job.setSalary(rs.getString("salary"));
                job.setEducation(rs.getString("education"));
                job.setRegion(rs.getString("region"));
                job.setEmploymentType(rs.getString("employment_type"));
                job.setPostedDate(rs.getString("posted_date"));
                job.setDeadline(rs.getString("deadline"));

                job.setCategoryId(rs.getInt("category_id"));
                list.add(job);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Map<String, Integer> getTechCategoryMap() {

        Map<String, Integer> map = new HashMap<>();

        String sql = "SELECT stack_name, category_id FROM tech_stack";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)) {

            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                map.put(rs.getString("stack_name"), rs.getInt("category_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
