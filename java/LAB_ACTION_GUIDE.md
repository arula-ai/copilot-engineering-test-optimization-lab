# Lab Action Guide – Java/Spring Boot

Follow these lean steps using the Critique-then-Create methodology. Use the custom agents in `.github/agents/` and prompts in `.github/prompts/` throughout.

## Quick Reference

| Stage | Primary Agents/Prompts | Core Artifacts / Commands |
| --- | --- | --- |
| 0 | Agent | `mvn clean compile`, `mvn test`, `mvn jacoco:report` |
| 1 | Test Critique Agent | `docs/TEST_ANALYSIS.md`, `.golden-examples/` |
| 2 | Test Create Agent + Prompts | `*Test.java` files, coverage improvement |
| 3 | Test Quality Gate Agent | `Jenkinsfile`, `pom.xml` (SonarQube), JaCoCo thresholds |
| 4 | Agent | Final validation, commit changes |

## Stage 0 – Environment Setup

- Agent Mode: `#runInTerminal mvn clean compile` (verify build)
- Agent Mode: `#runInTerminal mvn test` (verify tests run)
- Agent Mode: `#runInTerminal mvn jacoco:report` (generate baseline coverage)
- Agent Mode: `#runInTerminal open target/site/jacoco/index.html` (review baseline coverage)
- Note: Record baseline coverage percentage for comparison

## Stage 1 – Coverage Gap Analysis (CRITIQUE Phase)

- **Test Critique Agent**: Analyze all services in `com.example.lab.service`
- **Test Critique Agent**: Identify anti-patterns (flaky tests, redundancies)
- **Test Critique Agent**: Document findings → saves to `docs/TEST_ANALYSIS.md`
- Alternatively, use **coverage-analysis prompt**: `/coverage-analysis`
- Agent Mode: Review `.golden-examples/` to understand available patterns
- Agent Mode: Verify `docs/TEST_ANALYSIS.md` was created with complete analysis

### Key Analysis Areas
- PaymentService: Only CREDIT_CARD tested, missing other payment methods
- UserService: Redundant validation tests (not parameterized)
- OrderService: Flaky async tests (Thread.sleep, no timeout)
- NotificationService: Missing test file entirely

## Stage 2 – Test Enhancement (CREATE Phase)

### Task 2.1 – Refactor Redundant Tests
- **Test Create Agent**: Reference `#file:docs/TEST_ANALYSIS.md` redundancy section
- **refactor-to-parameterized prompt**: `/refactor-to-parameterized` for UserServiceTest.java
- Agent Mode: Apply `@ParameterizedTest` pattern from `.golden-examples/parameterized-tests/`
- Agent Mode: `#runInTerminal mvn test -Dtest=UserServiceTest` (verify refactoring)
- Target: 50%+ line reduction using @CsvSource/@MethodSource

### Task 2.2 – Fix Flaky Tests
- **Test Create Agent**: Reference `#file:docs/TEST_ANALYSIS.md` anti-patterns section
- **fix-flaky-test prompt**: `/fix-flaky-test` for OrderServiceTest.java
- Agent Mode: Apply CompletableFuture patterns from `.golden-examples/async-patterns/`
- Agent Mode: `#runInTerminal for i in {1..5}; do mvn test -Dtest=OrderServiceTest; done` (verify stability)

### Task 2.3 – Generate Mockito Tests
- **Test Create Agent**: Reference `#file:docs/TEST_ANALYSIS.md` coverage gaps
- Agent Mode: Apply BDD Mockito patterns from `.golden-examples/mockito-mocking/`
- Agent Mode: Use `@EnumSource(PaymentMethod.class)` for all payment methods
- Agent Mode: `#runInTerminal mvn test -Dtest=PaymentServiceTest` (verify new tests)

### Task 2.4 – Generate Exception Tests
- **Test Create Agent**: Reference `#file:docs/TEST_ANALYSIS.md` exception gaps
- **generate-error-tests prompt**: `/generate-error-tests` for PaymentService
- Agent Mode: Apply patterns from `.golden-examples/error-handling/`
- Agent Mode: Verify exception messages and error codes

### Task 2.5 – Generate Boundary Tests
- **generate-boundary-tests prompt**: `/generate-boundary-tests` for validators
- Agent Mode: Apply 7-point analysis from `.golden-examples/boundary-testing/`
- Agent Mode: Document boundaries as constants in test class

### Verification
- Agent Mode: `#runInTerminal mvn clean test jacoco:report`
- Agent Mode: Compare coverage to Stage 0 baseline
- Target: Coverage improved by 15%+

## Stage 3 – Quality Gates & CI (Jenkins + SonarQube)

- **Test Quality Gate Agent**: Configure JaCoCo coverage thresholds in pom.xml
- **Test Quality Gate Agent**: Add SonarQube Maven plugin and profile
- **Test Quality Gate Agent**: Generate `Jenkinsfile` with SonarQube integration
- Agent Mode: Configure Surefire for parallel test execution
- Agent Mode: Add Failsafe for integration tests

### Configuration Updates
- `pom.xml` – JaCoCo thresholds (60% LINE, 50% BRANCH)
- `pom.xml` – SonarQube profile (`-Psonar`)
- `pom.xml` – Surefire parallel execution
- `Jenkinsfile` – Pipeline with test, coverage, and quality gate stages

### Verification
- Agent Mode: `#runInTerminal mvn clean verify` (verify thresholds enforced)
- Agent Mode: `#runInTerminal mvn help:effective-pom | grep -A20 jacoco` (verify JaCoCo config)
- Agent Mode: `#runInTerminal cat Jenkinsfile` (verify pipeline structure)

## Stage 4 – Final Validation & Submission

- Agent Mode: `#runInTerminal mvn clean compile` (no errors)
- Agent Mode: `#runInTerminal mvn test` (all tests pass)
- Agent Mode: `#runInTerminal mvn verify` (thresholds met)
- Agent Mode: Review `docs/TEST_ANALYSIS.md` for completeness
- Agent Mode: Commit changes with meaningful message
- Agent Mode: Push branch and open PR if required

## Agent & Prompt Reference

| Agent/Prompt | When to Use |
| --- | --- |
| **Test Critique Agent** | Stage 1 – analyzing coverage gaps, anti-patterns |
| **Test Create Agent** | Stage 2 – generating tests from analysis |
| **Test Quality Gate Agent** | Stage 3 – Jenkins/SonarQube configuration |
| `/coverage-analysis` | Quick coverage gap analysis |
| `/refactor-to-parameterized` | Converting to @ParameterizedTest |
| `/fix-flaky-test` | Diagnosing and fixing timing issues |
| `/generate-error-tests` | Exception and error path tests |
| `/generate-boundary-tests` | 7-point boundary value tests |

## Golden Examples Reference

| Pattern | Location | Use For |
| --- | --- | --- |
| Parameterized Tests | `.golden-examples/parameterized-tests/` | @ParameterizedTest patterns |
| Mockito Mocking | `.golden-examples/mockito-mocking/` | BDD style mocking |
| Repository Testing | `.golden-examples/repository-testing/` | @DataJpaTest patterns |
| Async Patterns | `.golden-examples/async-patterns/` | CompletableFuture tests |
| Error Handling | `.golden-examples/error-handling/` | Exception tests |
| Boundary Testing | `.golden-examples/boundary-testing/` | Edge case tests |

## Workflow Loop

```
Test Critique Agent → docs/TEST_ANALYSIS.md → Test Create Agent → Test Quality Gate Agent
```

Each stage builds on the previous. The analysis file (`docs/TEST_ANALYSIS.md`) is the bridge between CRITIQUE and CREATE phases.
