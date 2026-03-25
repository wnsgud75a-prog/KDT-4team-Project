(() => {
  const storageKey = "theme";
  const darkClass = "dark";

  const applyTheme = (mode) => {
    const isDark = mode === "dark";
    document.body.classList.toggle(darkClass, isDark);
    document.querySelectorAll("[data-theme-toggle]").forEach((btn) => {
      btn.setAttribute("aria-pressed", String(isDark));
      btn.textContent = isDark ? "라이트 모드" : "다크 모드";
    });
    localStorage.setItem(storageKey, mode);
  };

  const getInitialMode = () => {
    const saved = localStorage.getItem(storageKey);
    const prefersDark = window.matchMedia &&
      window.matchMedia("(prefers-color-scheme: dark)").matches;
    return saved || (prefersDark ? "dark" : "light");
  };

  const toggleTheme = () => {
    const next = document.body.classList.contains(darkClass) ? "light" : "dark";
    applyTheme(next);
  };

  document.addEventListener("click", (event) => {
    const target = event.target.closest("[data-theme-toggle]");
    if (!target) return;
    toggleTheme();
  });

  window.initThemeToggle = () => applyTheme(getInitialMode());
  window.initThemeToggle();
})();
