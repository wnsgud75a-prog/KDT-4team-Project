
# 핵심 프로젝트
# TrendBridge - 기술 스택 기반 취업 전략 플랫폼
> **[박준형] 개인 포트폴리오용 저장소** (팀 프로젝트 기반)

## 👤 My Role & Contribution (나의 역할)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) 
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
**핵심 구현 사항:**
- **소셜 로그인 시스템 구축**: Google 및 GitHub OAuth2 API를 연동하여 편의성 증대
- **사용자 관리 및 프로필 시스템**: JPA를 활용한 회원 정보 수정 및 기술 스택 관리 기능 개발
- **보안 설정**: Spring Security를 활용한 접근 제어 및 세션 관리

---

## 🎯 내가 구현한 상세 기능
### 1. 소셜 로그인 연동 (OAuth 2.0)
- Google 및 GitHub API를 활용한 인증 프로세스 구현
- 사용자 동의 기반의 기술 스택 데이터 추출 로직 설계

### 2. 회원 및 프로필 관리 (JPA 활용)
- MyBatis와 JPA를 병행 사용하는 환경에서 엔티티 매핑 및 비즈니스 로직 구현
- 사용자 기술 스택의 동적 변경 및 DB 반영 처리

---

# 실전 프로젝트

## TrendBridge - 인지 훈련 및 돌봄 연계 플랫폼

고령 사용자 대상 인지 훈련 서비스를 중심으로, 보호자와 수급자 관리, 훈련 진행, 결과 리포트 조회까지 연결한 웹 프로젝트입니다.  
단순 문제 풀이에 그치지 않고, 음성 응답 기반 분석 파이프라인을 연동해 반응 시간, 반복어 비율, 발화 길이, 문항별 수행 결과를 종합적으로 확인할 수 있도록 구성했습니다.

## 프로젝트 개요

- 보호자/수급자 중심의 인지 훈련 관리 웹 서비스 구현
- 음성 기반 응답 분석 파이프라인 연동
- 훈련 결과 리포트 및 추이 데이터 제공
- 수급자 등록, 수정, 상세 조회 등 관리 기능 제공
- 카카오 로그인 기반 사용자 인증 처리

## My Role & Contribution

- Spring Boot 기반 백엔드 구조 및 주요 화면 연동 작업 참여
- 수급자 관리 기능 구현
- 훈련 결과 조회 및 리포트 관련 기능 연동
- 음성 분석 서버(FastAPI)와 Spring 서비스 간 API 연결 구조 반영
- 인지 문항 응답 결과를 리포트 형태로 가공하는 흐름 정리
- 프로젝트 기능별 화면-API-DB 흐름 점검 및 통합 테스트 진행

## 주요 기능

### 1. 사용자 인증
- Spring Security + OAuth2 Client 기반 로그인 처리
- Kakao 소셜 로그인 연동

### 2. 수급자 관리
- 수급자 등록 / 수정 / 상세 조회 / 목록 관리
- 보호자 기준 수급자 정보 관리 기능 제공

### 3. 인지 훈련
- 훈련 문항 진행
- 문제 응답 데이터 수집
- 훈련 상태 및 결과 확인

### 4. 음성 분석 연동
- 음성 응답을 별도 Python(FastAPI) 분석 서버로 전달
- STT 및 발화 분석 결과를 백엔드 서비스에서 수신
- 반응 시간, 반복어 비율, 발화 길이, 문항별 점수 기반 결과 활용

### 5. 결과 리포트
- 수행 결과 요약 리포트 제공
- 문항 유형별 점수 및 추이 데이터 조회
- 사용자/보호자가 결과를 직관적으로 확인할 수 있는 구조 구성

## Tech Stack

### Backend
<p>
  <img src="https://skillicons.dev/icons?i=java,spring,gradle" />
</p>

### Frontend
<p>
  <img src="https://skillicons.dev/icons?i=html,css,js" />
</p>

### Database / Auth
<p>
  <img src="https://skillicons.dev/icons?i=mysql" />
  <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white" />
  <img src="https://img.shields.io/badge/Kakao_OAuth-FEE500?style=for-the-badge&logo=kakaotalk&logoColor=000000" />
</p>

### AI / Analysis
<p>
  <img src="https://skillicons.dev/icons?i=python,fastapi" />
  <img src="https://img.shields.io/badge/STT-Whisper%20%7C%20Speech%20Analysis-4B8BBE?style=for-the-badge" />
  <img src="https://img.shields.io/badge/LLM_Scoring-OpenAI_API-412991?style=for-the-badge&logo=openai&logoColor=white" />
</p>

### Tools
<p>
  <img src="https://skillicons.dev/icons?i=git,github,idea,vscode" />
</p>

## Architecture Summary

- `Spring Boot`
  - 사용자 인증, 수급자 관리, 훈련 진행, 리포트 조회 등 웹 서비스 담당
- `FastAPI`
  - 음성 분석 파이프라인 처리 담당
- `MariaDB`
  - 사용자, 수급자, 훈련 결과, 리포트 데이터 저장
- `Thymeleaf + JavaScript`
  - 화면 렌더링 및 사용자 인터랙션 처리

## 프로젝트에서 집중한 포인트

- 웹 서비스 기능과 음성 분석 기능을 분리해 역할을 명확히 구성
- Java 백엔드와 Python 분석 서버 간 연결 구조를 실제 서비스 흐름에 맞게 설계
- 단순 정답 여부가 아니라 발화 특성을 포함한 결과 확인 흐름에 집중
- 보호자/수급자 관점에서 관리와 결과 확인이 쉬운 구조를 목표로 구현

## 프로젝트 회고

이번 실전 프로젝트에서는  
단순 CRUD 구현을 넘어서, `웹 서비스 + 인증 + 외부 분석 서버 연동 + 결과 리포트 제공`까지 이어지는 구조를 실제로 다뤄볼 수 있었습니다.  
특히 Spring Boot와 Python 기반 분석 서버를 연결하면서 서비스 간 역할 분리, API 통신, 결과 가공 흐름을 실무에 가깝게 경험한 점이 가장 큰 배움이었습니다.
