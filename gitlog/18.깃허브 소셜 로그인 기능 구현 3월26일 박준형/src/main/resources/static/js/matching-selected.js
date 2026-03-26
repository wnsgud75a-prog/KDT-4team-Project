document.addEventListener("DOMContentLoaded", () => {
  const container = document.getElementById("selectedJobDetail");
  if (!container) return;

  const showEmpty = () => {
    container.innerHTML = '<div class="muted">선택된 공고가 없습니다.</div>';
  };

  fetch("/users/me", { credentials: "same-origin" })
    .then((res) => res.json())
    .then((data) => {
      if (!data?.loggedIn) {
        localStorage.removeItem("selectedMatchingJob");
        showEmpty();
        return;
      }

      const raw = localStorage.getItem("selectedMatchingJob");
      if (!raw) {
        showEmpty();
        return;
      }

      let job;
      try {
        job = JSON.parse(raw);
      } catch {
        showEmpty();
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

      const techStacks = job.techStack
        ? job.techStack
            .split(",")
            .map((stack) => `<span class="stack-chip">${stack.trim()}</span>`)
            .join("")
        : "";

      container.innerHTML = `
        <div class="job-card">
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
        </div>
      `;
    })
    .catch(() => {
      localStorage.removeItem("selectedMatchingJob");
      showEmpty();
    });
});
