(() => {
  const nicknameEl = document.getElementById("profileNicknameView");
  const introEl = document.getElementById("profileIntroView");
  const stacksEl = document.getElementById("profileStacksView");

  const renderStacks = (names) => {
    if (!stacksEl) return;
    stacksEl.innerHTML = "";
    if (!names || names.length === 0) {
      stacksEl.textContent = "등록된 스택이 없습니다.";
      return;
    }
    names.forEach((name) => {
      const chip = document.createElement("span");
      chip.className = "stack-chip";
      chip.textContent = name;
      stacksEl.appendChild(chip);
    });
  };

  const loadProfile = async () => {
    try {
      const res = await fetch("/users/profile", { credentials: "same-origin" });
      if (!res.ok) return;
      const data = await res.json();
      if (!data.loggedIn) return;
      if (nicknameEl) nicknameEl.textContent = data.nickname || "-";
      if (introEl) introEl.textContent = data.intro || "-";
      renderStacks(data.stackNames || []);
    } catch {
      // ignore
    }
  };

  loadProfile();
})();
