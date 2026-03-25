(() => {
  const saveBtn = document.getElementById("profileSaveAllBtn");
  const idInput = document.getElementById("profileId");
  const nickInput = document.getElementById("profileNickname");
  const introInput = document.getElementById("profileIntro");
  const pwInput = document.getElementById("profileNewPw");
  const stackInput = document.getElementById("profileStackInput");
  const stackAddBtn = document.getElementById("profileStackAdd");
  const stackList = document.getElementById("profileStacks");
  const stackStatus = document.getElementById("profileStackStatus");

  const stacks = [];

  const renderStacks = () => {
    if (!stackList) return;
    stackList.innerHTML = "";
    if (stacks.length === 0) {
      const empty = document.createElement("span");
      empty.className = "muted";
      empty.textContent = "등록된 스택이 없습니다.";
      stackList.appendChild(empty);
      return;
    }
    stacks.forEach((name, idx) => {
      const chip = document.createElement("span");
      chip.className = "stack-chip";
      chip.textContent = name;
      chip.style.cursor = "pointer";
      chip.title = "클릭해서 삭제";
      chip.addEventListener("click", () => {
        stacks.splice(idx, 1);
        renderStacks();
        if (stackStatus) stackStatus.textContent = "저장 전";
      });
      stackList.appendChild(chip);
    });
  };

  const addStack = () => {
    const name = stackInput?.value?.trim();
    if (!name) return;
    if (!stacks.includes(name)) {
      stacks.push(name);
      renderStacks();
      if (stackStatus) stackStatus.textContent = "저장 전";
    }
    if (stackInput) stackInput.value = "";
  };

  const saveProfile = async () => {
    const id = idInput?.value?.trim();
    const nickname = nickInput?.value?.trim();
    const intro = introInput?.value?.trim();
    const newPassword = pwInput?.value?.trim();

    if (!id) {
      alert("로그인 ID가 없습니다. 다시 로그인 해주세요.");
      return;
    }

    try {
      const res = await fetch("/users/profile", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin",
        body: JSON.stringify({
          id,
          nickname,
          intro,
          newPassword,
          stackNames: stacks,
        }),
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "저장 실패");
      }

      if (stackStatus) stackStatus.textContent = "저장 완료";
      alert("회원 정보와 스택이 저장되었습니다.");
      window.location.href = "/profile";
    } catch (err) {
      alert(`저장 실패: ${err.message}`);
    }
  };

  saveBtn?.addEventListener("click", saveProfile);
  stackAddBtn?.addEventListener("click", addStack);
  stackInput?.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      addStack();
    }
  });

  const loadProfile = async () => {
    try {
      const res = await fetch("/users/profile", { credentials: "same-origin" });
      if (!res.ok) return;
      const data = await res.json();
      if (!data.loggedIn) return;
      if (nickInput && data.nickname) nickInput.value = data.nickname;
      if (introInput && data.intro) introInput.value = data.intro;
      if (Array.isArray(data.stackNames)) {
        data.stackNames.forEach((name) => {
          if (name && !stacks.includes(name)) stacks.push(name);
        });
      }
      renderStacks();
    } catch {
      // ignore
    }
  };

  loadProfile();
})();
