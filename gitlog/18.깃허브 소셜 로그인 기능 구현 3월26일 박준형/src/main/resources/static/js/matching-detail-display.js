document.addEventListener("DOMContentLoaded", () => {
  const summaryEl = document.getElementById("matchingDetailSummary");
  const listEl = document.getElementById("matchingDetailList");

  if (!summaryEl || !listEl) return;

  const raw = localStorage.getItem("matchingDetailResult");
  if (!raw) {
    summaryEl.textContent = "선택된 조건이 없습니다.";
    return;
  }

  let data;
  try {
    data = JSON.parse(raw);
  } catch {
    summaryEl.textContent = "선택된 조건이 없습니다.";
    return;
  }

  const { major, minor, jobs, totalCount } = data || {};
  if (!major || !Array.isArray(jobs)) {
    summaryEl.textContent = "선택된 조건이 없습니다.";
    return;
  }

  const minorDisplay = minor && minor !== "" ? minor : "전체";
  const total = typeof totalCount === "number" ? totalCount : jobs.length;
  summaryEl.textContent = `선택: ${major} / ${minorDisplay} (총 ${total}개)`;
  listEl.innerHTML = "";

  if (jobs.length === 0) {
    listEl.innerHTML = '<div class="card" style="padding:10px 14px;">표시할 공고가 없습니다.</div>';
    return;
  }

  const formatDate = (dateString) => {
    if (!dateString) return "-";
    return String(dateString).split(" ")[0];
  };

  const formatEmploymentType = (text) => {
    if (!text) return "-";
    return String(text).replace(/\n/g, " / ");
  };

  jobs.forEach((job) => {
    const techStacks = job.techStack
      ? job.techStack
          .split(",")
          .map((stack) => `<span class="stack-chip">${stack.trim()}</span>`)
          .join("")
      : "";

    const card = document.createElement("div");
    card.className = "job-card";
    card.innerHTML = `
      <div class="job-card-inner">
        <div class="job-company">${job.companyName ?? "-"}</div>
        <div class="job-main-row">
          <div class="job-title-area">
            <div class="job-title">${job.jobPosition ?? "-"}</div>
            <div class="job-stack-list">${techStacks}</div>
            <div class="job-link-row">
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
    listEl.appendChild(card);
  });
});
