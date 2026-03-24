(function () {
  const storageKey = "theme";
  const darkClass = "dark";
  const toggle = document.querySelector("[data-theme-toggle]");
  const body = document.body;

  if (!toggle || !body) return;

  const applyTheme = (mode) => {
    const isDark = mode === "dark";
    body.classList.toggle(darkClass, isDark);
    toggle.setAttribute("aria-pressed", String(isDark));
    toggle.textContent = isDark ? "라이트 모드" : "다크 모드";
    localStorage.setItem(storageKey, mode);
  };

  const saved = localStorage.getItem(storageKey);
  const prefersDark = window.matchMedia &&
    window.matchMedia("(prefers-color-scheme: dark)").matches;
  const initial = saved || (prefersDark ? "dark" : "light");

  applyTheme(initial);

  toggle.addEventListener("click", () => {
    const next = body.classList.contains(darkClass) ? "light" : "dark";
    applyTheme(next);
  });
})();

