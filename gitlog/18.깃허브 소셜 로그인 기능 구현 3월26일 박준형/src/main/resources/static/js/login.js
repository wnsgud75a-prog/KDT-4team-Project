(() => {
  const loginBtn = document.getElementById("loginBtn");
  const idInput = document.getElementById("loginEmail");
  const pwInput = document.getElementById("loginPassword");
  // 화면에 보이는 커스텀 Google 버튼.
  const googleBtn = document.getElementById("loginGoogle");
  // Google SDK가 실제 로그인용 버튼을 렌더링하는 숨김 영역
  const googleButtonWrap = document.getElementById("googleLoginButton");
  // 서버 설정값을 메타 태그로 받아 GitHub SDK 초기화에 사용
  const googleClientId = document.querySelector('meta[name="google-client-id"]')?.content?.trim();
  const githubBtn = document.getElementById("loginGithub");
  let googleInitialized = false;
  let googleInitAttempts = 0;
  let googleInitTimer = null;
  const MAX_GOOGLE_INIT_ATTEMPTS = 20;

  // 일반 로그인

  const login = async () => {
    const id = idInput?.value?.trim();
    const pw = pwInput?.value?.trim();

    if (!id || !pw) {
      alert("아이디와 비밀번호를 입력해주세요.");
      return;
    }

    try {
      const res = await fetch("/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin",
        body: JSON.stringify({ id, pw }),
      });

      const data = await res.json().catch(() => ({}));
      if (!res.ok) {
        throw new Error(data.message || "로그인 실패");
      }

      window.location.href = "/";
    } catch (err) {
      alert(`로그인 실패: ${err.message}`);
    }
  };

  // Google에서 전달한 credential을 우리 서버로 넘겨 세션 로그인을 완료
  const loginWithGoogle = async (credential) => {
    try {
      const res = await fetch("/users/google", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin",
        body: JSON.stringify({ credential }),
      });

      // 로그인 실패시 뜨는 메시지 출력
      // (수정) : 서버가 JSON 대신 HTML/문자열 에러를 내려도 실제 오류 내용을 확인할 수 있게 응답 본문을 먼저 문자열로 읽음
      const rawText = await res.text();
      let data = {};

      // (수정) : JSON 파싱이 실패하면 원문 텍스트를 message로 보존해서 alert에 실제 오류가 보이도록 처리
      try {
        data = rawText ? JSON.parse(rawText) : {};
      } catch (e) {
        data = { message: rawText };
      }

      if (!res.ok) {
        throw new Error(data.message || "구글 로그인 실패");
      }
      // 구글 소셜 로그인 성공 후, 프로필 수정창으로 이동하게하는 기능을 추가
      // (수정) : 서버에서 최초 구글 가입 여부를 함께 내려주므로, 첫 가입자는 메인 대신 프로필 수정 페이지로 이동
      window.location.href = data.needsProfileSetup ? "/profile-edit" : "/";
    } catch (err) {
      alert(`구글 로그인 실패: ${err.message}`);
    }
  };

  // 깃허브 소셜 로그인 - 260326

  const params = new URLSearchParams(window.location.search);
  const code = params.get("code");

  // Github에서 전달한 credential을 우리 서버로 넘겨 세션 로그인을 완료

  if (code) {
    fetch("/users/github", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "same-origin",
      body: JSON.stringify({ code }),
    })
        .then(async (res) => {
          const data = await res.json();
          if (!res.ok) {
            throw new Error(data.message || "GitHub 로그인 실패");
          }
          // 깃허브 소셜 로그인 성공 후, 프로필 수정창으로 이동하게하는 기능을 추가
          // 서버에서 최초 깃허브 가입 여부를 함께 내려주므로, 첫 가입자는 메인 대신 프로필 수정 페이지로 이동

          window.location.href = data.needsProfileSetup ? "/profile-edit" : "/";
        })
        .catch((err) => alert(err.message));
  }





  // Google SDK 준비 상태를 확인하고, 숨겨진 실제 로그인 버튼을 한 번만 생성
  const initGoogleLogin = () => {
    if (!googleBtn || !googleButtonWrap) {
      return;
    }

    if (googleInitialized) {
      return;
    }

    if (!googleClientId) {
      googleBtn.disabled = true;
      googleBtn.title = "Google Client ID 설정이 필요합니다.";
      return;
    }

    if (!window.google?.accounts?.id) {
      googleBtn.disabled = true;
      googleBtn.title = "구글 로그인 버튼을 준비 중입니다.";

      // 외부 SDK가 늦게 로드되는 경우를 대비해 잠시 후 다시 초기화
      if (googleInitAttempts < MAX_GOOGLE_INIT_ATTEMPTS) {
        googleInitAttempts += 1;
        window.clearTimeout(googleInitTimer);
        googleInitTimer = window.setTimeout(initGoogleLogin, 300);
      }
      return;
    }

    window.google.accounts.id.initialize({
      client_id: googleClientId,
      callback: ({ credential }) => loginWithGoogle(credential),
    });

    // 실제 Google 버튼은 숨겨진 영역에 렌더링하고, 클릭은 커스텀 버튼이 대신 위임
    googleButtonWrap.innerHTML = "";
    window.google.accounts.id.renderButton(googleButtonWrap, {
      theme: "outline",
      size: "large",
      shape: "pill",
      width: googleButtonWrap.offsetWidth || 320,
      text: "signin_with",
    });

    googleInitialized = true;
    googleBtn.disabled = false;
    googleBtn.title = "";
  };

  loginBtn?.addEventListener("click", login);

  const handleKey = (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      login();
    }
  };

  idInput?.addEventListener("keydown", handleKey);
  pwInput?.addEventListener("keydown", handleKey);

  googleBtn?.addEventListener("click", () => {
    initGoogleLogin();

    if (!googleInitialized) {
      alert("구글 로그인 준비가 아직 완료되지 않았습니다.");
      return;
    }

    // Google SDK가 만든 실제 버튼을 대신 눌러서 표준 인증 흐름을 그대로 사용
    const renderedButton = googleButtonWrap.querySelector('div[role="button"]');
    if (renderedButton instanceof HTMLElement) {
      renderedButton.click();
      return;
    }

    window.google?.accounts?.id?.prompt();
  });

  // GitHub 로그인 버튼 클릭 시 GitHub 인증 페이지로 이동

  githubBtn?.addEventListener("click", () => {
    const clientId = "Ov23liBtn82Yb3qpnvms";
    const redirectUri = encodeURIComponent("http://localhost:8081/login");
    const scope = encodeURIComponent("read:user user:email");

    window.location.href =
        `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}`;
  });


  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initGoogleLogin, { once: true });
  } else {
    initGoogleLogin();
  }

  window.addEventListener("load", initGoogleLogin, { once: true });
})();
