# Test Optimization Lab

Hands-on lab for improving test coverage using GitHub Copilot with the Critique-then-Create methodology.

## Choose Your Lab

| Lab | Stack | Testing Framework | Guide |
|-----|-------|-------------------|-------|
| **Angular** | Angular 19, TypeScript | Jest | [LAB_ACTION_GUIDE.md](angular/LAB_ACTION_GUIDE.md) |
| **Java** | Java 17, Spring Boot 3.2 | JUnit 5, Mockito | [LAB_ACTION_GUIDE.md](java/LAB_ACTION_GUIDE.md) |

Both labs use the same e-commerce domain (payments, orders, users, inventory) with intentionally weak test coverage (~30-40%) for you to analyze and improve.

## What You'll Do

| Stage | Focus | Outcome |
|-------|-------|---------|
| **Stage 1** | Coverage Gap Analysis | `docs/TEST_ANALYSIS.md` with prioritized findings |
| **Stage 2** | Test Enhancement | Refactored tests, +15% coverage |
| **Stage 3** | Quality Gates | Jenkins pipeline with SonarQube integration |

## Quick Start

### Angular

```bash
cd angular
npm install
npm test
npm run test:coverage
open coverage/lcov-report/index.html
```

### Java

```bash
cd java
mvn clean test
mvn jacoco:report
open target/site/jacoco/index.html
```

## Custom Agents & Prompts

Located in `.github/agents/` and `.github/prompts/`:

| Agent/Prompt | Purpose |
|--------------|---------|
| **Test Critique Agent** | Analyze coverage gaps, identify anti-patterns |
| **Test Create Agent** | Generate tests from analysis |
| **Test Quality Gate Agent** | Configure Jenkins/SonarQube |
| `/coverage-analysis` | Quick coverage gap analysis |
| `/refactor-to-parameterized` | Convert to `test.each`/`@ParameterizedTest` |
| `/fix-flaky-test` | Diagnose timing issues |
| `/generate-error-tests` | HTTP error and exception tests |
| `/generate-boundary-tests` | 7-point boundary value tests |

## Issues to Find & Fix

| Issue Type | Angular File | Java File |
|------------|--------------|-----------|
| Weak coverage (~35%) | `payment.service.spec.ts` | `PaymentServiceTest.java` |
| Redundant tests | `user.service.spec.ts` | `UserServiceTest.java` |
| Flaky async tests | `order.service.spec.ts` | `OrderServiceTest.java` |
| Missing tests | `notification.service` | `NotificationService` |

## Golden Examples

Reference implementations in `.golden-examples/`:

- `parameterized-tests/` - `test.each` and `@ParameterizedTest` patterns
- `http-mocking/` - HttpClientTestingModule patterns (Angular)
- `mockito-mocking/` - BDD Mockito patterns (Java)
- `async-patterns/` - `fakeAsync`/`tick` and `CompletableFuture` tests
- `error-handling/` - Exception and HTTP error tests
- `boundary-testing/` - 7-point boundary analysis
- `signal-testing/` - Angular 19 signal tests

## Target Metrics

| Metric | Baseline | Target |
|--------|----------|--------|
| Line Coverage | ~30-40% | >75% |
| Branch Coverage | ~25-35% | >60% |
| Flaky Tests | Present | Fixed |
| Redundant Tests | Present | Parameterized |

## Prerequisites

**Angular:** Node.js 20+, npm 10+, VS Code with GitHub Copilot

**Java:** JDK 17+, Maven 3.9+, VS Code/IntelliJ with GitHub Copilot
