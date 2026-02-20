#  Event-Driven UFC Matchup Risk & Transaction Intelligence Platform

> **Prediction X | Risk Quantification O**  
> 이 프로젝트는 “누가 이긴다”를 단정하지 않는다.  
> 대신 **이 경기가 얼마나 불확실하고 위험한지**, 그리고 **그 위험의 근거가 무엇인지**를 정량화한다.

---

##  Project Goal

- 단순 승패 예측 모델 X
- **Matchup Risk / Uncertainty / Volatility** 중심 분석 O
- 이벤트 기반(Event-Driven) 구조
- 리스크 점수 + 근거(breakdown)를 함께 제공
- 이상 거래(FDS) 및 오즈 변동 분석까지 확장 가능한 구조

---

## 🏗 Overall Pipeline (A → F)

A. Fighter & Fight Data  
B. Recency Weighted Stats  
C. Style Matchup Matrix  
D. RiskScore & Uncertainty  
E. Probability & Odds Engine  
F. FDS (Fraud / Abnormal Transaction Detection)

---

# Phase 0 — Project Setup & Big Picture

## Project Overview & Concept Definition

### What we define
- X “누가 이긴다”
- O “얼마나 리스크가 큰 경기인가”

### Key Ideas
- 리스크는 **확률 + 불확실성 + 변동성**의 결합
- 결과가 아니라 **위험의 구조**를 설명하는 시스템
- 모델 결과는 항상 **설명 가능해야 함**



---

##Architecture Concept

### Architecture Principles
- **Core Engine**
    - 계산, 모델, 리스크 로직
- **Platform Layer**
    - API, Persistence, External Interface

### Why this split?
- 계산 로직을 API/DB와 분리
- 모델 실험과 운영 코드의 결합 방지
- 확장성 (ML, LLM, FDS)

### Deliverables
- 아키텍처 개념 다이어그램


---

##  Tech Stack & Project Initialization

### Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 (in-memory, dev)
- Gradle
- Lombok

### Project Initialization
- Spring Initializr 기반 프로젝트 생성
- IntelliJ IDEA 설정
- 빈 프로젝트 기동 확인

### Configuration
- `application.yml` 사용
- 환경별 설정 확장 가능 구조

### Deliverables
- 빈 Spring Boot 프로젝트
- 기본 설정 완료

---

# Phase 1 — Domain Model & DB Design

##  Fighter & WeightClassBaseline

### Fighter
- 파이터의 **정체성 엔티티**
- 이름, 체급, 상태 정보
- 계산 로직 X

### WeightClassBaseline
- 체급별 기준 데이터
- 평균 피니시율 / 판정율
- 변동성 지수(volatilityIndex)

### Design Decisions
- 체급당 baseline은 **단 하나**
- `weight_class`에 UNIQUE 제약 적용

### Deliverables
- `Fighter` Entity
- `WeightClassBaseline` Entity

---

##  Fight & FightHistory

### Fight
- 경기 자체 (이벤트)
- 날짜, 체급, 파이터 매칭
- 시간 개념 X

### FightHistory
- 경기 시점 스냅샷
- 경기 전 / 후 / 모델 재계산 시점 기록
- **시간 개념은 여기만 존재**

### Relationship
- Fight 1 : N FightHistory
- 기록은 늘어나고, 경기는 변하지 않음

### Deliverables
- `Fight` Entity
- `FightHistory` Entity
- Repository 설계

---

## FighterStyleProfile

### Purpose
- 파이터의 **현재 스타일 스탯**
- 타격 / 레슬링 / 그래플링 수치

### Stored Metrics
- Striking: SLpM, Accuracy, Defense
- Wrestling: TD Avg, Accuracy, Defense
- Grappling: Submission Avg

### Design
- Fighter 1 : 1 StyleProfile
- 퍼센트/확률은 `0.0 ~ 1.0` 범위로 저장

### Deliverables
- `FighterStyleProfile` Entity
- `FighterStyleProfileRepository`

---

##  ERD Consolidation

### Final ERD Entities
- Fighter
- FighterStyleProfile
- WeightClassBaseline
- Fight
- FightHistory

### Relationship Summary
- Fighter 1 ── 1 FighterStyleProfile
- Fighter 1 ── N Fight
- Fight 1 ── N FightHistory
- WeightClassBaseline 1 ── N Fight

### Key Design Principles
- FK는 항상 N 쪽에 위치
- 시간 개념은 History로 분리
- 기준 데이터(Baseline)는 DB에서 유니크 보장

### Outcome
- 전체 데이터 흐름 고정
- Day8 이후 계산 로직의 기반 완성

---

## Status 

- Domain model 확정
- DB 구조 고정
- 계산/모델링을 위한 데이터 토대 완료

---
# Phase 2 — Recency Weighted Stats

## 최근 N경기 추출 로직 & 가중치 테이블 정의

### 목표
- “최근 경기일수록 더 중요하다”는 개념을 **구조와 정책으로 고정**


### 구현 요약
- **RecencyWeightPolicy**
  - 최근 경기 가중치 정책 정의
  - 예: `[1.00, 0.85, 0.70, 0.55, 0.40]`
  - 불변 객체(`final`, `List.copyOf`)
- **RecentFightsPort (Core)**
  - fighter 기준 최근 N경기 조회 계약
  - DB/JPA 의존 제거
- **RecencyFightSelector**
  - “최근 경기 선택” 정책 담당
  - Port + WeightPolicy 결합
- **JPA Adapter / FightRepository**
  - `redCorner / blueCorner` 기준 경기 조회
  - `fightDate DESC` 정렬
  - `PageRequest`로 N개 제한

### 핵심 포인트
- “최근 경기”는 쿼리가 아닌 **도메인 정책**
- Core ↔ Platform 분리 구조 확립

---

##  RecencyAdjustedStats 계산

### 목표
- 최근 폼(Form)을 **숫자로 계산**

### 구현 요약
- **RecencyAdjustedStats**
  - 계산 결과 값 객체
  - `totalFights`, `weightedWinScore`
- **RecencyStatsService / Impl**
  - Selector로 최근 경기 조회
  - index 기반 가중치 적용
  - 승리 경기만 가중 합산

### 예시
```text
최근 5경기 결과: 승/패/승/무/승
가중치 적용 → weightedWinScore = 2.10

```
---
##Recency Stats 검증 & JSON 표현

###  목표
- Day 9에서 구현한 **RecencyAdjustedStats 계산 로직**을  
  **단위 테스트로 검증**
- 계산 결과가 **JSON으로 안정적으로 표현 가능함**을 확인

---

### 구현 내용

#### 1. 단위 테스트 (RecencyStatsServiceImplTest)
- Spring 컨테이너 없이 순수 JUnit 테스트
- DB 접근 X (Fake `RecentFightsPort` 사용)
- Mockito로 `Fight`, `Fighter` mock 생성

**검증 케이스**
- 전부 승리 → 모든 가중치 합산
- 승/패/무 혼합 → 승리한 경기만 가중치 반영

---

#### 2. 가중치 계산 검증
- index 0 = 가장 최근 경기
- 가중치 정책:
  ```text
  [1.00, 0.85, 0.70, 0.55, 0.40]

---
# 🟦 Phase 3 — Style Matchup & Clash Engine (Day 11 ~ Day 15)

Phase 3의 목적은 단순한 승패 예측이 아니다.

> **두 파이터의 스타일이 어떻게 맞부딪히고,  
그 충돌이 경기의 변동성과 리스크를 어떻게 만들어내는지**  
를 정량적으로 설명하는 엔진을 구축한다.

이 Phase에서는 아래와 같은 순서로 계산 레이어를 쌓는다.

1. 타격 우위 (Striking Advantage)
2. 레슬링 우위 (Wrestling Advantage)
3. 그래플 / 서브미션 위협 (Grapple Threat)
4. 스타일 충돌 (Style Clash)
5. 체급 기준선 보정 (Weight Baseline Adjustment)

모든 점수는 **A 파이터 기준 상대 비교 점수**로 계산되며,
부호는 방향(+/-), 절대값은 강도를 의미한다.

---
# 🟦 Phase 4 — RiskScore & Uncertainty

> **“누가 이긴다”가 아니라  
> “이 경기는 얼마나 위험한가”를 수치로 만든다.**

Phase 4에서는 경기 결과 예측이 아니라,  
**예측 자체가 얼마나 불안정하고 리스크가 큰지**를 정량화한다.

이 단계의 핵심은
- 데이터 신뢰도
- 결말 방식의 변동성
- 최근 폼 변화
  를 **서로 다른 관점의 리스크로 분리한 뒤**,  
  하나의 RiskScore로 통합하는 것이다.

---

##  Uncertainty Score

###  목표
> **이 경기의 데이터를 얼마나 믿을 수 있는가?**

### 핵심 아이디어
- 표본 수가 적으면 불확실
- 경기력 분산이 크면 불확실
- 최근 경기 비중이 낮으면 불확실

### 주요 요소
- `dataSparsity` : 최근 경기 수 부족 리스크
- `statVariance` : 퍼포먼스 분산 리스크
- `recencyCoverage` : 최근성 커버리지 부족 리스크

### 산출물
- `UncertaintyScore`
- `UncertaintyBreakdown`
- `UncertaintyCalculator`


---

## Finish Volatility

###  목표
> **이 경기는 어떤 방식으로 끝날지 얼마나 요동치는가?**

### 핵심 아이디어
- KO / SUB / DEC 분포가 고를수록 위험
- KO와 SUB가 동시에 열려 있으면 폭발적
- 판정보다 피니시 비중이 높을수록 변동성 증가

### 주요 요소
- `finishRate` : KO + SUB 비중
- `entropy` : 결말 방식 혼합도
- `mismatch` : KO ↔ SUB 상성 충돌

### 산출물
- `FinishVolatilityScore`
- `FinishVolatilityBreakdown`
- `FinishVolatilityCalculator`

> 엔트로피는 “정보 이론”이 아니라  
> **“어떻게 끝날지 갈피를 못 잡겠는가”를 수치화한 도구**다.

---

##  Recency Shift

###  목표
> **이 선수는 지금 ‘변곡점’에 있는가?**

### 핵심 아이디어
- 방향(+/−)이 아니라 **변화의 크기**가 리스크
- 급상승 / 급하락 모두 위험
- 최근 기복 증가 역시 위험 신호

### 주요 요소
- `recentVsBaselineDelta` : 최근 평균 vs 장기 평균 차이
- `trendSlope` : 최근 경기 흐름의 기울기
- `volatilitySpike` : 최근 분산 급증 여부

### 산출물
- `RecencyShiftScore`
- `RecencyShiftBreakdown`
- `RecencyShiftDetector`

> RecencyShift는  
> **“이 선수를 지금 기준으로 평가해도 되는가?”**에 대한 질문이다.

---

## RiskScore Engine

### 목표
> **여러 리스크 신호를 하나의 판단으로 통합한다.**

### 입력 요소
- `baseAdvantageRisk` : Day11~15 상성 결과를 리스크 관점으로 변환
- `uncertaintyRisk` : Day16 결과
- `finishVolatilityRisk` : Day17 결과
- `recencyShiftRisk` : Day18 결과

### 핵심 아이디어
- 각 리스크는 **서로 다른 원인**
- 가중치는 정책(Policy)로 분리
- breakdown을 통해 “왜 위험한지” 설명 가능

### 산출물
- `RiskScore`
- `RiskBreakdown`
- `RiskScoreEngine`
- `RiskScorePolicy`

---

## Risk Result API

### 목표
> **RiskScore를 외부에 노출하는 API 계약 확정**

### API
```http
GET /fights/{fightId}/risk

```
---
# UFC Matchup Risk Platform
## Rule-Based Probability Prediction & Context Assembly

### 목표



- 단순 승/패 예측 x
- **확률 기반 예측 + 신뢰도 + 판단 근거(reason)** 제공 0
- 기존  계산한 리스크 요소들을
  **하나의 컨텍스트(Context)** 로 묶어
  **룰 기반 확률 엔진**과 연결

즉,  
👉 *“이 경기 결과가 왜 불확실한지 / 왜 50%로 수렴하는지”* 를  
**설명 가능한 방식으로 표현**하는 단계다.

---

