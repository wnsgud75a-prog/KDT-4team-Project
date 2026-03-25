(() => {
  const majorEl = document.getElementById("majorCategory");
  const minorEl = document.getElementById("minorCategory");
  const applyBtn = document.getElementById("applyCategory");
  const companyList = document.getElementById("companyList");
  const pagination = document.getElementById("matchingPagination");
  const jobCountEl = document.getElementById("jobCount");

  if (!majorEl || !minorEl || !applyBtn || !companyList) return;

  const pageSize = 10;
  let currentPage = 1;
  let currentMajor = "";
  let currentMinor = "";
  let filteredJobs = [];

  const categoryMap = {
    "백엔드": 2,
    "프론트엔드": 1,
    "인공지능/데이터": 3,
    "인프라/보안": 4,
    "모바일": 5,
  };

  const renderOptions = (select, items, placeholder) => {
    select.innerHTML = "";
    const ph = document.createElement("option");
    ph.value = "";
    ph.textContent = placeholder;
    ph.selected = true;
    select.appendChild(ph);
    items.forEach((item) => {
      const opt = document.createElement("option");
      opt.value = item;
      opt.textContent = item;
      select.appendChild(opt);
    });
  };

  const formatDate = (dateString) => {
    if (!dateString) return "-";
    return String(dateString).split(" ")[0];
  };

  const formatEmploymentType = (text) => {
    if (!text) return "-";
    return String(text).replace(/\n/g, " / ");
  };

  const renderJobs = (jobs) => {
    companyList.innerHTML = "";
    if (!jobs || jobs.length === 0) {
      companyList.innerHTML = "<p class=\"muted\">표시할 공고가 없습니다.</p>";
      return;
    }
    jobs.forEach((job) => {
      const techStacks = job.techStack
        ? job.techStack
            .split(",")
            .map((stack) => `<span class=\"stack-chip\">${stack.trim()}</span>`)
            .join("")
        : "";

      const card = document.createElement("div");
      card.className = "job-card";
      card.innerHTML = `
        <div class=\"job-card-inner\">
          <div class=\"job-company\">${job.companyName ?? "-"}</div>
          <div class=\"job-main-row\">
            <div class=\"job-title-area\">
              <div class=\"job-title\">${job.jobPosition ?? "-"}</div>
              <div class=\"job-stack-list\">${techStacks}</div>
            </div>
            <div class=\"job-info-area\">
              <div><strong>연봉:</strong> ${job.salary ?? "-"}</div>
              <div><strong>학력:</strong> ${job.education ?? "-"}</div>
              <div><strong>근무지역:</strong> ${job.region ?? "-"}</div>
              <div><strong>고용형태:</strong> ${formatEmploymentType(job.employmentType)}</div>
              <div><strong>등록일:</strong> ${formatDate(job.postedDate)}</div>
              <div><strong>마감일:</strong> ${formatDate(job.deadline)}</div>
            </div>
          </div>
        </div>
      `;
      card.addEventListener("click", (event) => {
        if (event.target.closest("a")) return;
        localStorage.setItem("selectedMatchingJob", JSON.stringify(job));
        window.location.href = "/matching";
      });
      companyList.appendChild(card);
    });
  };

  const renderPagination = () => {
    if (!pagination) return;
    pagination.innerHTML = "";
    const totalPages = Math.max(1, Math.ceil(filteredJobs.length / pageSize));
    if (totalPages <= 1) return;

    const createButton = (label, page, disabled = false, active = false) => {
      const button = document.createElement("button");
      button.textContent = label;
      button.className = "page-btn";
      if (active) button.classList.add("active");
      if (disabled) {
        button.disabled = true;
      } else {
        button.addEventListener("click", () => {
          currentPage = page;
          renderPage();
        });
      }
      return button;
    };

    const createEllipsis = () => {
      const span = document.createElement("span");
      span.className = "page-ellipsis";
      span.textContent = "...";
      return span;
    };

    pagination.appendChild(
      createButton("이전", currentPage - 1, currentPage === 1)
    );

    const pages = [];
    pages.push(1);

    const startPage = Math.max(2, currentPage - 2);
    const endPage = Math.min(totalPages - 1, currentPage + 2);

    if (startPage > 2) {
      pages.push("ellipsis-start");
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }

    if (endPage < totalPages - 1) {
      pages.push("ellipsis-end");
    }

    if (totalPages > 1) {
      pages.push(totalPages);
    }

    const uniquePages = [];
    for (const p of pages) {
      if (!uniquePages.includes(p)) {
        uniquePages.push(p);
      }
    }

    uniquePages.forEach((p) => {
      if (p === "ellipsis-start" || p === "ellipsis-end") {
        pagination.appendChild(createEllipsis());
      } else {
        pagination.appendChild(
          createButton(String(p), p, false, p === currentPage)
        );
      }
    });

    pagination.appendChild(
      createButton("다음", currentPage + 1, currentPage === totalPages)
    );
  };

  const renderPage = () => {
    const startIdx = (currentPage - 1) * pageSize;
    const pageJobs = filteredJobs.slice(startIdx, startIdx + pageSize);
    renderJobs(pageJobs);
    renderPagination();
    if (jobCountEl) {
      jobCountEl.textContent = `총 ${filteredJobs.length}건`;
    }
  };

  const normalize = (value) => String(value || "").toLowerCase().trim();
  const hasExactStack = (techStack, target) => {
    if (!techStack || !target) return false;
    const targetNorm = normalize(target);
    return techStack
      .split(",")
      .map((s) => normalize(s))
      .some((s) => s === targetNorm);
  };

  const fetchJobs = async (categoryId) => {
    const response = await fetch(`/api/jobs?categoryId=${categoryId}&page=1&size=200`);
    if (!response.ok) {
      throw new Error("공고 조회 실패");
    }
    const data = await response.json();
    return data.jobs || [];
  };

  const fetchMinorOptions = async (categoryId) => {
    const response = await fetch(`/api/stacks?categoryId=${categoryId}`);
    if (!response.ok) {
      throw new Error("소분류 조회 실패");
    }
    const data = await response.json();
    return (data || []).map((item) => item.stackName).filter(Boolean);
  };

  renderOptions(majorEl, Object.keys(categoryMap), "대분류 선택");
  renderOptions(minorEl, [], "소분류 선택");
  renderJobs([]);

  majorEl.addEventListener("change", async () => {
    const major = majorEl.value;
    const categoryId = categoryMap[major];
    renderJobs([]);

    if (!categoryId) {
      renderOptions(minorEl, [], "소분류 선택");
      minorEl.disabled = false;
      return;
    }

    renderOptions(minorEl, [], "불러오는 중...");
    minorEl.disabled = true;
    try {
      const minors = await fetchMinorOptions(categoryId);
      renderOptions(minorEl, minors, "소분류 선택");
      minorEl.disabled = false;
    } catch {
      renderOptions(minorEl, [], "소분류 선택");
      minorEl.disabled = false;
    }
  });

  applyBtn.addEventListener("click", async () => {
    const major = majorEl.value;
    const minor = minorEl.value;
    if (!major) {
      renderJobs([]);
      return;
    }
    const categoryId = categoryMap[major];
    if (!categoryId) {
      renderJobs([]);
      return;
    }

    companyList.innerHTML = "<p class=\"muted\">공고를 불러오는 중...</p>";
    try {
      const jobs = await fetchJobs(categoryId);
      currentMajor = major;
      currentMinor = minor || "";
      filteredJobs = minor
        ? jobs.filter((job) => hasExactStack(job.techStack, minor))
        : jobs;
      currentPage = 1;
      renderPage();
    } catch {
      companyList.innerHTML = "<p class=\"muted\">공고를 불러오지 못했습니다.</p>";
    }
  });
})();
