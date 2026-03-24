(() => {
  const majorEl = document.getElementById("majorCategory");
  const minorEl = document.getElementById("minorCategory");
  const applyBtn = document.getElementById("applyCategory");
  const companyList = document.getElementById("companyList");

  if (!majorEl || !minorEl || !applyBtn || !companyList) return;

  const data = {
    "백엔드": {
      "Java": ["브릿지랩", "스카이포지", "데이터허브"],
      "Python": ["인플로우", "오토그립", "데이터스"],
      "Node.js": ["옵스메이트", "마크코어", "브릿지랩"]
    },
    "프론트엔드": {
      "React": ["클라우드코어", "트렌드퓨리", "캔버스캠퍼"],
      "Vue": ["오토그립", "캔버스캠퍼", "프론트라인"],
      "Svelte": ["미스트림", "프론트라인", "스파이럴"]
    },
    "인공지능/데이터": {
      "머신러닝": ["인플로우", "지아이텍스", "인사이트팜"],
      "컴퓨터비전": ["비전마크", "지아이텍스", "센서마인"],
      "NLP": ["언어브릿지", "인사이트팜", "텍스트허브"]
    },
    "인프라": {
      "AWS": ["옵스메이트", "클라우드코어", "스택스카이"],
      "Kubernetes": ["스택스카이", "옵스메이트", "인프라월드"],
      "Linux": ["인프라월드", "옵스메이트", "서버마일"]
    }
  };

  const renderOptions = (select, items, placeholder) => {
    select.innerHTML = "";
    const ph = document.createElement("option");
    ph.value = "";
    ph.textContent = placeholder;
    ph.disabled = true;
    ph.selected = true;
    select.appendChild(ph);
    items.forEach((item) => {
      const opt = document.createElement("option");
      opt.value = item;
      opt.textContent = item;
      select.appendChild(opt);
    });
  };

  const renderCompanies = (companies) => {
    companyList.innerHTML = "";
    if (!companies || companies.length === 0) {
      companyList.innerHTML = "<p class=\"muted\">표시할 회사가 없습니다.</p>";
      return;
    }
    companies.forEach((name) => {
      const card = document.createElement("div");
      card.className = "card";
      card.style.padding = "10px 14px";
      card.style.marginBottom = "8px";
      card.textContent = name;
      companyList.appendChild(card);
    });
  };

  renderOptions(majorEl, Object.keys(data), "대분류 선택");
  renderOptions(minorEl, [], "소분류 선택");
  renderCompanies([]);

  majorEl.addEventListener("change", () => {
    const major = majorEl.value;
    const minors = major ? Object.keys(data[major]) : [];
    renderOptions(minorEl, minors, "소분류 선택");
    renderCompanies([]);
  });

  applyBtn.addEventListener("click", () => {
    const major = majorEl.value;
    const minor = minorEl.value;
    if (!major || !minor) {
      renderCompanies([]);
      return;
    }
    renderCompanies(data[major][minor] || []);
  });
})();
