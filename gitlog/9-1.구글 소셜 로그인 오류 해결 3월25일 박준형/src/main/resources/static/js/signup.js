(() => {
  const signupBtn = document.getElementById("signupBtn");
  const idInput = document.getElementById("signupEmail");
  const pwInput = document.getElementById("signupPassword");
  const nicknameInput = document.getElementById("signupNickname") || document.getElementById("signupName");

  const signup = async () => {
    const id = idInput?.value?.trim();
    const pw = pwInput?.value?.trim();
    const nickname = nicknameInput?.value?.trim();

    if (!id || !pw || !nickname) {
      alert("이메일, 비밀번호, 닉네임을 입력해주세요.");
      return;
    }

    try {
      const res = await fetch("/users/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin",
        body: JSON.stringify({ id, pw, nickname }),
      });

      const data = await res.json().catch(() => ({}));
      if (!res.ok) {
        throw new Error(data.message || "회원가입 실패");
      }

      alert("회원가입 완료! 로그인 페이지로 이동합니다.");
      window.location.href = "/login";
    } catch (err) {
      alert(`회원가입 실패: ${err.message}`);
    }
  };

  signupBtn?.addEventListener("click", signup);
})();
