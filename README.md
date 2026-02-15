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
