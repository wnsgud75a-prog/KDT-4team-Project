(() => {
  const authArea = document.getElementById("authArea");

  const renderLoggedOut = () => {
    if (!authArea) return;
    authArea.innerHTML = '<a class="auth-btn secondary" href="login.html">로그인</a><a class="auth-btn" href="signup.html">회원가입</a>';
  };

  const renderLoggedIn = () => {
    if (!authArea) return;
    authArea.innerHTML = '<a class="auth-btn" href="profile.html">프로필</a><button id="logoutBtn" class="auth-btn secondary" type="button">로그아웃</button>';
    const profileBtn = authArea.querySelector('a[href="profile.html"]');
    const current = location.pathname.split("/").pop() || "index.html";
    if (profileBtn && current === "profile.html") {
      profileBtn.classList.add("active");
    }
    const logoutBtn = document.getElementById("logoutBtn");
    logoutBtn?.addEventListener("click", async () => {
      try {
        await fetch("/users/logout", { method: "POST", credentials: "same-origin" });
      } finally {
        window.location.href = "index.html";
      }
    });
  };

  if (!authArea) return;

  const setActiveNav = () => {
    const current = location.pathname.split("/").pop() || "index.html";
    document.querySelectorAll(".nav a").forEach((link) => {
      const href = link.getAttribute("href");
      if (href === current) {
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
      }
    })
    .catch(() => {
      renderLoggedOut();
    });
})();
