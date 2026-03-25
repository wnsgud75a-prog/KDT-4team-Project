(() => {
  const authArea = document.getElementById("authArea");

  const renderLoggedOut = () => {
    if (!authArea) return;
    authArea.innerHTML = '<a class="auth-btn secondary" href="/login">로그인</a><a class="auth-btn" href="/signup">회원가입</a>';
    const profileNav = document.querySelector('.nav a[data-page="profile"]');
    if (profileNav) {
      profileNav.style.display = "none";
    }
    window.initThemeToggle?.();
  };

  const renderLoggedIn = () => {
    if (!authArea) return;
    authArea.innerHTML = '<a class="auth-btn" href="/profile">프로필</a><button id="logoutBtn" class="auth-btn secondary" type="button">로그아웃</button>';
    const profileNav = document.querySelector('.nav a[data-page="profile"]');
    if (profileNav) {
      profileNav.style.display = "none";
    }
    window.initThemeToggle?.();
    const logoutBtn = document.getElementById("logoutBtn");
    logoutBtn?.addEventListener("click", async () => {
      try {
        await fetch("/users/logout", { method: "POST", credentials: "same-origin" });
      } finally {
        localStorage.removeItem("selectedMatchingJob");
        window.location.href = "/";
      }
    });
  };

  if (!authArea) return;

  const setActiveNav = () => {
    const currentPath = location.pathname || "/";
    document.querySelectorAll(".nav a").forEach((link) => {
      const href = link.getAttribute("href") || "";
      const normalizedHref = href.startsWith("/") ? href : `/${href}`;
      if (normalizedHref === currentPath) {
        link.classList.add("active");
      } else {
        link.classList.remove("active");
      }
    });
  };

  setActiveNav();

  fetch("/users/me", { credentials: "same-origin" })
    .then((res) => res.json())
    .then((data) => {
      if (data?.loggedIn) {
        renderLoggedIn();
      } else {
        renderLoggedOut();
        const protectedPage = document.body?.dataset?.protected === "true";
        if (protectedPage) {
          window.location.href = "/login";
        }
      }
    })
    .catch(() => {
      renderLoggedOut();
      const protectedPage = document.body?.dataset?.protected === "true";
      if (protectedPage) {
        window.location.href = "/login";
      }
    });
})();
