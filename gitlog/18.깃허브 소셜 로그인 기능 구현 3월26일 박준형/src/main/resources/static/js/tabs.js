(() => {
  const tabGroups = document.querySelectorAll(".tabs");
  if (tabGroups.length === 0) return;

  const jobData = {
    backend: [
      "백엔드 개발자 (Java/Spring)",
      "백엔드 개발자 (Kotlin)",
      "서버 엔지니어",
      "플랫폼 백엔드",
      "API 백엔드"
    ],
    frontend: [
      "프론트엔드 개발자 (React)",
      "프론트엔드 개발자 (Vue)",
      "웹 UI 엔지니어",
      "모바일 웹 퍼블리셔",
      "디자인 시스템 엔지니어"
    ],
    "ai-data": [
      "머신러닝 엔지니어",
      "데이터 엔지니어",
      "데이터 분석가",
      "NLP 엔지니어",
      "컴퓨터비전 엔지니어"
    ],
    infra: [
      "클라우드 엔지니어 (AWS)",
      "DevOps 엔지니어",
      "SRE",
      "인프라 자동화",
      "보안/네트워크 엔지니어"
    ]
  };

  const trendData = {
    backend: ["Spring", "Java", "JPA", "MySQL", "Redis", "Kafka", "JUnit", "Docker", "Kubernetes", "AWS"],
    frontend: ["React", "TypeScript", "Next.js", "Redux", "Tailwind", "Vite", "HTML", "CSS", "Jest", "Cypress"],
    "ai-data": ["Python", "Pandas", "TensorFlow", "PyTorch", "Scikit-learn", "SQL", "Spark", "Airflow", "MLflow", "FastAPI"],
    infra: ["AWS", "Docker", "Kubernetes", "Terraform", "Linux", "Nginx", "Prometheus", "Grafana", "Ansible", "GitHub Actions"]
  };

  const renderList = (target, items) => {
    if (!target) return;
    target.innerHTML = "";
    items.forEach((text) => {
      const card = document.createElement("div");
      card.className = "card";
      card.style.padding = "10px 14px";
      card.style.marginBottom = "8px";
      card.textContent = text;
      target.appendChild(card);
    });
  };

  tabGroups.forEach((group) => {
    const buttons = Array.from(group.querySelectorAll(".tab-btn"));
    if (buttons.length === 0) return;

    const isJobsPage = document.getElementById("jobList");
    const isTrendPage = document.getElementById("trendTop10List");

    const applyCategory = (category) => {
      if (isJobsPage) {
        renderList(isJobsPage, jobData[category] || []);
      }
      if (isTrendPage) {
        renderList(isTrendPage, trendData[category] || []);
      }
    };

    buttons.forEach((btn) => {
      btn.addEventListener("click", () => {
        buttons.forEach((b) => b.classList.remove("active"));
        btn.classList.add("active");
        const category = btn.getAttribute("data-category");
        if (category) applyCategory(category);
      });
    });

    const defaultBtn = buttons.find((b) => b.classList.contains("active")) || buttons[0];
    const defaultCategory = defaultBtn.getAttribute("data-category");
    if (defaultCategory) applyCategory(defaultCategory);
  });
})();
