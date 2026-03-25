(() => {
  const myStacksEl = document.getElementById("myStacks");
  const stackInput = document.getElementById("stackInput");
  const addStackBtn = document.getElementById("addStack");
  const suggestionBox = document.getElementById("stackSuggestions");

  const myStacks = [];
  let stackCatalog = [];

  const renderStacks = () => {
    if (!myStacksEl) return;
    myStacksEl.innerHTML = "";
    if (myStacks.length === 0) {
      const empty = document.createElement("span");
      empty.className = "muted";
      empty.textContent = "등록된 스택이 없습니다.";
      myStacksEl.appendChild(empty);
      return;
    }

    myStacks.forEach((name, idx) => {
      const chip = document.createElement("span");
      chip.className = "stack-chip";
      chip.textContent = name;
      chip.style.cursor = "pointer";
      chip.title = "클릭해서 삭제";
      chip.addEventListener("click", () => {
        myStacks.splice(idx, 1);
        renderStacks();
        renderSuggestions(stackInput?.value || "");
      });
      myStacksEl.appendChild(chip);
    });
  };

  const hideSuggestions = () => {
    if (!suggestionBox) return;
    suggestionBox.hidden = true;
    suggestionBox.innerHTML = "";
  };

  const addStack = (rawName) => {
    const name = String(rawName || "").trim();
    if (!name) return;
    if (!myStacks.includes(name)) {
      myStacks.push(name);
      renderStacks();
    }
    if (stackInput) {
      stackInput.value = "";
      stackInput.focus();
    }
    hideSuggestions();
  };

  const renderSuggestions = (keyword) => {
    if (!suggestionBox) return;

    const normalized = String(keyword || "").trim().toLowerCase();
    if (!normalized) {
      hideSuggestions();
      return;
    }

    const matched = stackCatalog
      .filter((name) => name.toLowerCase().includes(normalized) && !myStacks.includes(name))
      .slice(0, 8);

    if (matched.length === 0) {
      hideSuggestions();
      return;
    }

    suggestionBox.hidden = false;
    suggestionBox.innerHTML = "";

    matched.forEach((name) => {
      const button = document.createElement("button");
      button.type = "button";
      button.className = "stack-suggestion-item";
      button.textContent = name;
      button.addEventListener("click", () => addStack(name));
      suggestionBox.appendChild(button);
    });
  };

  const loadProfileStacks = async () => {
    try {
      const res = await fetch("/users/profile", { credentials: "same-origin" });
      if (!res.ok) return;
      const data = await res.json();
      if (!data.loggedIn) return;
      (data.stackNames || []).forEach((name) => {
        if (name && !myStacks.includes(name)) {
          myStacks.push(name);
        }
      });
      renderStacks();
    } catch {
      renderStacks();
    }
  };

  const loadStackCatalog = async () => {
    try {
      const res = await fetch("/api/stacks", { credentials: "same-origin" });
      if (!res.ok) return;
      const data = await res.json();
      stackCatalog = [...new Set((data || []).map((item) => item.stackName).filter(Boolean))];
    } catch {
      stackCatalog = [];
    }
  };

  addStackBtn?.addEventListener("click", () => addStack(stackInput?.value));
  stackInput?.addEventListener("input", (e) => renderSuggestions(e.target.value));
  stackInput?.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      const firstSuggestion = suggestionBox?.querySelector(".stack-suggestion-item");
      if (firstSuggestion && !suggestionBox.hidden) {
        addStack(firstSuggestion.textContent);
        return;
      }
      addStack(stackInput?.value);
    }
    if (e.key === "Escape") {
      hideSuggestions();
    }
  });

  document.addEventListener("click", (e) => {
    if (!suggestionBox || !stackInput) return;
    if (e.target === stackInput || suggestionBox.contains(e.target)) return;
    hideSuggestions();
  });

  Promise.all([loadProfileStacks(), loadStackCatalog()]).then(() => {
    renderStacks();
  });
})();
