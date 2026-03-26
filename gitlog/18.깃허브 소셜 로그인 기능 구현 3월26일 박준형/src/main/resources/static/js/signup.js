(() => {
  const signupBtn = document.getElementById("signupBtn");
  const idInput = document.getElementById("signupEmail");
  const pwInput = document.getElementById("signupPassword");
  const nicknameInput = document.getElementById("signupNickname") || document.getElementById("signupName");
  const stackInput = document.getElementById("signupStackName"); // 기술스택 추가

  // 기술스택 값 입력 추가
  const signup = async () => {
    const id = idInput?.value?.trim();
    const pw = pwInput?.value?.trim();
    const nickname = nicknameInput?.value?.trim();
    const stackName = stackInput?.value?.trim();
    // id,pw,nickname,stackName 입력 및 미입력시 해당 메시지 출력
    if (!id || !pw || !nickname || stackName) {
      alert("이메일, 비밀번호, 닉네임, 기술스택을 입력해주세요.");
      return;
    }

    try {
      const res = await fetch("/users/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin",
        body: JSON.stringify({ id, pw, nickname, stackName }),
      });
      // 회원가입 실패시 메시지 출력
      const data = await res.json().catch(() => ({}));
      if (!res.ok) {
        throw new Error(data.message || "회원가입 실패");
      }
      // 회원가입 완료시 메시지 출력
      alert("회원가입 완료! 로그인 페이지로 이동합니다.");
      window.location.href = "/login";
    } catch (err) {
      alert(`회원가입 실패: ${err.message}`);
    }
  };
  // 회원가입 버튼 클릭시 회원가입 페이지로 이동
  signupBtn?.addEventListener("click", signup);
})();
