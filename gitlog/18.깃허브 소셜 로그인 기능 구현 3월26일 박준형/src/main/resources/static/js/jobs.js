document.addEventListener("DOMContentLoaded", () => {
  const buttons = document.querySelectorAll("#jobCategoryTabs .tab-btn");
  const jobList = document.getElementById("jobList");
  const pagination = document.getElementById("pagination");

  let currentCategoryId = 2;
  let currentPage = 1;
  const pageSize = 10;

  const formatDate = (dateString) => {
    if (!dateString) return "-";
    return String(dateString).split(" ")[0];
  };

  const formatEmploymentType = (text) => {
    if (!text) return "-";
    return String(text).replace(/\n/g, " / ");
  };

  const renderJobs = (jobs) => {
    jobList.innerHTML = "";

    if (!jobs || jobs.length === 0) {
      jobList.innerHTML = '<div class="card" style="padding:16px;">해당 카테고리에 공고가 없습니다.</div>';
      return;
    }

    jobs.forEach((job) => {
      const techStacks = job.techStack
        ? job.techStack
            .split(",")
            .map((stack) => `<span class="stack-chip">${stack.trim()}</span>`)
            .join("")
        : "";

      const card = document.createElement("div");
      card.className = "job-card";

      const rawUrl = job.postingUrl ?? "";
      const safeUrl = rawUrl
        ? (rawUrl.startsWith("http://") || rawUrl.startsWith("https://") ? rawUrl : `http://${rawUrl}`)
        : "#";

      card.innerHTML = `
        <div class="job-card-inner">
          <div class="job-company">${job.companyName ?? "-"}</div>

          <div class="job-main-row">
            <div class="job-title-area">
              <div class="job-title">${job.jobPosition ?? "-"}</div>
              <div class="job-stack-list">${techStacks}</div>
              <div class="job-link-row">
                <a href="${safeUrl}" target="_blank" rel="noopener" class="job-link-btn">공고 바로가기</a>
              </div>
            </div>

            <div class="job-info-area">
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

      jobList.appendChild(card);
    });
  };

  const renderPagination = (pageInfo) => {
    pagination.innerHTML = "";

    if (!pageInfo || pageInfo.totalPages <= 1) {
      return;
    }

    const { currentPage, totalPages } = pageInfo;

    const createButton = (label, page, disabled = false, active = false) => {
      const button = document.createElement("button");
      button.textContent = label;
      button.className = "page-btn";

      if (active) {
        button.classList.add("active");
      }

      if (disabled) {
        button.disabled = true;
      } else {
        button.addEventListener("click", () => {
          loadJobs(currentCategoryId, page);
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

  const loadJobs = async (categoryId, page = 1) => {
    try {
      const response = await fetch(`/api/jobs?categoryId=${categoryId}&page=${page}&size=${pageSize}`);
      if (!response.ok) {
        throw new Error("공고 조회 실패");
      }

      const data = await response.json();

      currentCategoryId = categoryId;
      currentPage = page;

      renderJobs(data.jobs);
      renderPagination(data);
    } catch (error) {
      console.error(error);
      jobList.innerHTML = '<div class="card" style="padding:16px;">공고를 불러오지 못했습니다.</div>';
      pagination.innerHTML = "";
    }
  };

  buttons.forEach((button) => {
    button.addEventListener("click", () => {
      buttons.forEach((btn) => btn.classList.remove("active"));
      button.classList.add("active");

      const categoryId = Number(button.dataset.categoryId);
      loadJobs(categoryId, 1);
    });
  });

  const defaultButton = document.querySelector("#jobCategoryTabs .tab-btn.active");
  if (defaultButton) {
    loadJobs(Number(defaultButton.dataset.categoryId), 1);
  }
});
