# TrendBridge - 기술 스택 기반 취업 전략 플랫폼

개인 기술 역량과 채용 시장 데이터를 연결해 맞춤형 공고 탐색과 역량 분석을 돕는 취업 지원 웹서비스

![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-6DB33F?style=flat-square&logo=springboot)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F?style=flat-square&logo=thymeleaf)
![MyBatis](https://img.shields.io/badge/MyBatis-3.0.4-BF1E2E?style=flat-square)
![JPA](https://img.shields.io/badge/JPA-Hibernate-59666C?style=flat-square)
![Oracle](https://img.shields.io/badge/Oracle_DB-19c-F80000?style=flat-square&logo=oracle)
![Python](https://img.shields.io/badge/Python-Data_Analysis-3776AB?style=flat-square&logo=python)

## 📋 프로젝트 소개

**TrendBridge**는 IT 취업 준비생을 위한 기술 스택 기반 채용 분석 서비스입니다.  
단순히 채용 공고를 조회하는 데서 끝나는 것이 아니라, 사용자의 보유 기술 스택과 채용 공고 요구 기술을 비교해 현재 역량 수준과 부족한 부분을 확인할 수 있도록 설계했습니다.

### 주요 특징

- 🧩 **기술 스택 기반 매칭**: 보유 스택과 공고 요구 스택을 비교해 적합도 분석
- 📈 **기술 트렌드 차트**: 직무 카테고리별 시장 요구 기술 통계 제공
- 🔖 **북마크 기능**: 관심 공고를 저장하고 다시 확인 가능
- 👤 **프로필 관리**: 닉네임, 소개, 보유 스택, 비밀번호 수정 지원
- 🔐 **소셜 로그인 연동**: Google / GitHub OAuth 로그인 제공
- 🐙 **GitHub 스택 분석**: GitHub 계정 연동을 통해 기술 스택 후보 확인

## 🎯 서비스 목표

- 개인 기술 역량과 채용 시장 요구 간의 미스매치 해소
- 취업 준비생에게 부족한 기술과 추천 공고를 함께 제시
- 데이터 기반으로 보다 구체적인 취업 준비 방향 제공

## 🚀 시작하기

### 사전 요구사항

- JDK 21 이상
- Oracle DB 접속 가능 환경
- Gradle Wrapper 실행 가능 환경

### 실행 전 확인

`src/main/resources/application.properties`에서 아래 항목을 확인해야 합니다.

- 서버 포트
- Oracle DB 접속 정보
- Google OAuth 설정
- GitHub OAuth 설정

### 실행 방법

```bash
./gradlew bootRun
```

Windows에서는 아래 명령으로 실행할 수 있습니다.

```powershell
.\gradlew.bat bootRun
```

### 접속 주소

```text
http://localhost:8081
```

## 📁 프로젝트 구조

```text
4team-project/
├── src/
│   └── main/
│       ├── java/com/project/mvcgithublogin/
│       │   ├── config/         # Spring Security, OAuth 설정
│       │   ├── controller/     # 페이지 라우팅 및 API 엔드포인트
│       │   ├── dao/            # 차트/통계 조회용 DAO
│       │   ├── domain/         # 핵심 도메인 객체
│       │   ├── dto/            # 요청/응답 DTO
│       │   ├── model/          # 외부 연동 모델
│       │   ├── profile/        # 프로필 관련 JPA 엔티티/서비스
│       │   ├── repository/     # MyBatis Repository
│       │   ├── service/        # 비즈니스 로직
│       │   └── App.java        # 애플리케이션 시작점
│       └── resources/
│           ├── application.properties
│           ├── mapper/         # MyBatis XML Mapper
│           ├── static/
│           │   ├── css/
│           │   └── js/
│           └── templates/      # Thymeleaf 템플릿
├── build.gradle.kts
├── pom.xml
└── README.md
```

## 🗺️ 주요 페이지

| 경로 | 페이지 | 설명 |
|------|--------|------|
| `/` | 채용 공고 메인 | 카테고리별 공고 조회 |
| `/jobs` | 기술 트렌드 차트 | 기술 스택 통계 및 관련 공고 확인 |
| `/matching` | 매칭 분석 | 내 스택 기반 공고 적합도 분석 |
| `/matching-detail` | 세부 매칭 설정 | 목표 공고 상세 선택 |
| `/profile` | 프로필 | 사용자 정보 조회 |
| `/profile-edit` | 프로필 수정 | 소개, 스택, 비밀번호 변경 |
| `/bookmarks` | 북마크 | 저장한 공고 목록 확인 |
| `/login` | 로그인 | 일반 / 소셜 로그인 |
| `/signup` | 회원가입 | 일반 회원가입 |

## 🎯 주요 구현 내용

### 1. 회원 및 인증

- 일반 회원가입 / 로그인
- 세션 기반 로그인 상태 유지
- Google OAuth 로그인
- GitHub OAuth 로그인 및 계정 연동
- 회원탈퇴 처리

### 2. 채용 공고 조회

- 직무 카테고리별 채용 공고 분류
- 페이지네이션 기반 조회
- 기업명, 마감일, 지역, 기술 스택 등 상세 정보 제공

### 3. 매칭 분석

- 사용자 보유 스택 표시
- 선택 공고 기준으로 보유 / 부족 스택 구분
- 매칭 점수 계산
- 점수 개선 시뮬레이션
- 보유 스택 기반 추천 공고 제공

### 4. 기술 트렌드 차트

- 카테고리별 기술 수요 통계 시각화
- 특정 기술을 요구하는 공고 목록 제공

### 5. 프로필 / 북마크

- 닉네임, 자기소개, 기술 스택 수정
- GitHub 연동 동의 여부 저장
- 관심 공고 북마크 추가 / 삭제 / 조회

## 🐍 데이터 분석 및 Python 활용

기술 스택 트렌드 차트와 카테고리 분류를 위해 별도의 데이터 전처리 및 분석 과정을 진행했습니다.

### 1. 데이터 전처리 / 정규화

- 분석에 불필요한 컬럼과 결측 데이터를 제거
- 공고등록일이 없는 경우 지원마감일 기준 한 달 전으로 추정하여 생성
- 쉼표로 나열된 기술 스택 문자열을 분리하고 공백 제거
- 표기가 다른 동일 기술은 정규화 사전을 통해 하나의 명칭으로 통일

이를 통해 기술 스택 집계와 분류 정확도를 높일 수 있도록 처리했습니다.

### 2. 카테고리 분류

- 기술 스택 키워드가 많아 주요 키워드는 수동으로 분류
- 그 외 키워드는 규칙 기반으로 자동 분류
- 각 공고는 포함된 기술 스택을 기준으로 카테고리별 점수를 계산해 멀티 카테고리로 분류

이 과정을 통해 백엔드, 프론트엔드, AI/데이터, 인프라/보안, 모바일 등 직무 성격에 맞는 카테고리화를 수행했습니다.

### 3. 기술 스택 트렌드 분석

- 전처리된 기술 스택 데이터를 기준으로 기술별 등장 빈도 집계
- 전체 공고 대비 기술 스택 비율 계산
- 집계 결과를 기반으로 기술 트렌드 차트 구성

즉, 단순 공고 목록 제공이 아니라 시장에서 어떤 기술이 실제로 많이 요구되는지 시각적으로 확인할 수 있도록 분석 데이터를 활용했습니다.

## 🔌 주요 API

### 사용자

- `POST /users/signup`
- `POST /users/login`
- `POST /users/logout`
- `GET /users/me`
- `DELETE /users/me`

### 프로필

- `GET /users/profile`
- `PUT /users/profile`
- `PUT /users/profile/github-consent`

### 채용 공고

- `GET /api/jobs?categoryId={id}&page={page}&size={size}`

### 북마크

- `GET /api/bookmarks`
- `GET /api/bookmarks/ids`
- `POST /api/bookmarks/{postingId}`
- `DELETE /api/bookmarks/{postingId}`

### GitHub 연동

- `GET /api/github/stacks`

## 🛠️ 기술 스택

- **Backend**: Spring Boot, Spring MVC, Spring Security OAuth2 Client
- **Template Engine**: Thymeleaf
- **Database Access**: MyBatis, JPA
- **Database**: Oracle DB
- **Frontend**: HTML, CSS, JavaScript
- **Build Tool**: Gradle
- **Language**: Java 21
- **Data Analysis**: Python 기반 전처리 / 정규화 / 트렌드 분석

## 👥 팀원 역할

### 장상연

- 기획 총괄
- 프론트엔드 개발
- 백엔드(Java)
- GitHub 레포지토리 연동 및 기술 스택 분석 기능 구현

### 김홍현

- 백엔드(Java)
- 트렌드 차트 기능 구현
- 일반 로그인 기능 구현
- 로그아웃 및 회원 탈퇴 기능 구현

### 양선호

- 백엔드(Java / Python)
- DB 설계 및 문서화
- 채용공고 데이터 전처리 및 기술 스택 정규화
- 기술 스택 / 카테고리 분류 및 트렌드 분석
- 공고 목록 조회 및 즐겨찾기 기능 구현

### 박준형

- 백엔드(Java)
- 회원가입 및 계정관리(프로필 수정) 기능 구현
- Google, GitHub 소셜 로그인 기능 구현

## 🧱 데이터 접근 방식

이 프로젝트는 기능 특성에 따라 **MyBatis와 JPA를 혼합 사용**했습니다.

- **MyBatis**
  - 회원가입 / 로그인
  - 채용 공고 조회
  - 북마크 처리
  - 기술 스택 및 차트 데이터 조회

- **JPA**
  - 프로필 조회 / 수정
  - GitHub 연동 동의 상태 관리

## 💡 아키텍처 포인트

- 페이지 렌더링은 **Spring MVC + Thymeleaf**
- 동적 데이터 처리는 **REST API + JavaScript**
- 인증 상태는 **HttpSession** 기반으로 관리
- 일부 외부 연동은 **Google OAuth / GitHub OAuth / GitHub API** 활용

## 🚧 개선 포인트

1. 보안 설정 세분화 및 CSRF 정책 보완
2. 민감 정보 환경 변수 분리
3. 예외 처리 및 응답 포맷 공통화
4. 추천 / 매칭 로직 정교화
5. MyBatis와 JPA 사용 기준 정리

## 🤝 프로젝트 저장소

- GitHub Repository: [https://github.com/hap728/KDT-4team-Project](https://github.com/hap728/KDT-4team-Project)

## 📄 라이선스

이 프로젝트는 교육 및 학습 목적으로 제작되었습니다.

---

**Made for KDT Team Project**

*개인 기술 역량과 채용 데이터를 연결하는 취업 전략 지원 서비스*
