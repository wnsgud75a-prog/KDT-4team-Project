(() => {
  const signupBtn = document.getElementById("signupBtn");
  const idInput = document.getElementById("signupEmail");
  const pwInput = document.getElementById("signupPassword");
  const nameInput = document.getElementById("signupName");
  const birthInput = document.getElementById("signupBirth");
  const phoneInput = document.getElementById("signupPhone");

  const signup = async () => {
    const id = idInput?.value?.trim();
    const pw = pwInput?.value?.trim();
    const nickname = nameInput?.value?.trim();
    const birth = birthInput?.value?.trim();
    const phone = phoneInput?.value?.trim();

    if (!id || !pw) {
      alert("아이디와 비밀번호를 입력해주세요.");
      return;
    }

    try {
      const res = await fetch("/users/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin",
        body: JSON.stringify({ id, pw, nickname, birth, phone }),
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "회원가입 실패");
      }

      alert("회원가입 완료! 로그인 페이지로 이동합니다.");
      window.location.href = "login.html";
    } catch (err) {
      alert(`회원가입 실패: ${err.message}`);
    }
  };

  signupBtn?.addEventListener("click", signup);
})();
