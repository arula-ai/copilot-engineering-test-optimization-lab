# Lab Action Guide – Angular/Jest

Follow these lean steps using the Critique-then-Create methodology. Use the custom agents in `.github/agents/` and prompts in `.github/prompts/` throughout.

## Quick Reference

| Stage | Primary Agents/Prompts | Core Artifacts / Commands |
| --- | --- | --- |
| 0 | Agent | `npm install`, `npm test`, `npm run test:coverage` |
| 1 | Test Critique Agent | `docs/TEST_ANALYSIS.md`, `.golden-examples/` |
| 2 | Test Create Agent + Prompts | `*.spec.ts` files, coverage improvement |
| 3 | Test Quality Gate Agent | `Jenkinsfile`, `sonar-project.properties`, `jest.config.ts` |
| 4 | Agent | Final validation, commit changes |

## Stage 0 – Environment Setup

- Agent Mode: `#runInTerminal npm install`
- Agent Mode: `#runInTerminal npm test` (verify tests run)
- Agent Mode: `#runInTerminal npm run test:coverage` (establish baseline)
- Agent Mode: `#runInTerminal open coverage/lcov-report/index.html` (review baseline coverage)
- Note: Record baseline coverage percentage for comparison

## Stage 1 – Coverage Gap Analysis (CRITIQUE Phase)

- **Test Critique Agent**: Analyze all services in `src/app/core/services/`
- **Test Critique Agent**: Identify anti-patterns (flaky tests, redundancies)
- **Test Critique Agent**: Document findings → saves to `docs/TEST_ANALYSIS.md`
- Alternatively, use **coverage-analysis prompt**: `/coverage-analysis`
- Agent Mode: Review `.golden-examples/` to understand available patterns
- Agent Mode: Verify `docs/TEST_ANALYSIS.md` was created with complete analysis

### Key Analysis Areas
- Payment service: HTTP error handling gaps
- User service: Redundant validation tests
- Order service: Flaky async tests
- Inventory service: Missing signal tests

## Stage 2 – Test Enhancement (CREATE Phase)

### Task 2.1 – Refactor Redundant Tests
- **Test Create Agent**: Reference `#file:docs/TEST_ANALYSIS.md` redundancy section
- **refactor-to-parameterized prompt**: `/refactor-to-parameterized` for user.service.spec.ts
- Agent Mode: Apply `test.each` pattern from `.golden-examples/parameterized-tests/`
- Agent Mode: `#runInTerminal npm test -- --testPathPattern=user.service` (verify refactoring)
- Target: 50%+ line reduction in validation tests

### Task 2.2 – Fix Flaky Tests
- **Test Create Agent**: Reference `#file:docs/TEST_ANALYSIS.md` anti-patterns section
- **fix-flaky-test prompt**: `/fix-flaky-test` for order.service.spec.ts
- Agent Mode: Apply `fakeAsync`/`tick` pattern from `.golden-examples/async-patterns/`
- Agent Mode: `#runInTerminal for i in {1..5}; do npm test -- --testPathPattern=order.service; done` (verify stability)

### Task 2.3 – Generate HTTP Error Tests
- **Test Create Agent**: Reference `#file:docs/TEST_ANALYSIS.md` HTTP gaps
- **generate-error-tests prompt**: `/generate-error-tests` for payment.service.spec.ts
- Agent Mode: Apply patterns from `.golden-examples/http-mocking/` and `.golden-examples/error-handling/`
- Agent Mode: `#runInTerminal npm test -- --testPathPattern=payment.service` (verify new tests)

### Task 2.4 – Generate Signal Tests
- **Test Create Agent**: Reference `#file:docs/TEST_ANALYSIS.md` signal gaps
- Agent Mode: Apply patterns from `.golden-examples/signal-testing/`
- Agent Mode: `#runInTerminal npm test -- --testPathPattern=inventory.service` (verify new tests)

### Task 2.5 – Generate Boundary Tests
- **generate-boundary-tests prompt**: `/generate-boundary-tests` for validators
- Agent Mode: Apply 7-point analysis from `.golden-examples/boundary-testing/`
- Agent Mode: `#runInTerminal npm test` (verify all tests pass)

### Verification
- Agent Mode: `#runInTerminal npm run test:coverage`
- Agent Mode: Compare coverage to Stage 0 baseline
- Target: Coverage improved by 15%+

## Stage 3 – Quality Gates & CI (Jenkins + SonarQube)

- **Test Quality Gate Agent**: Configure Jest coverage thresholds
- **Test Quality Gate Agent**: Generate `sonar-project.properties`
- **Test Quality Gate Agent**: Generate `Jenkinsfile` with SonarQube integration
- Agent Mode: Add Jest reporters for Jenkins (`jest-junit`, `jest-sonar-reporter`)
- Agent Mode: Update `package.json` with required devDependencies

### Configuration Files to Create
- `jest.config.ts` – coverage thresholds (60% global, 80% critical services)
- `sonar-project.properties` – SonarQube project configuration
- `Jenkinsfile` – Pipeline with test, coverage, and quality gate stages

### Verification
- Agent Mode: `#runInTerminal npm run test:coverage` (verify thresholds enforced)
- Agent Mode: `#runInTerminal cat sonar-project.properties` (verify SonarQube config)
- Agent Mode: `#runInTerminal cat Jenkinsfile` (verify pipeline structure)

## Stage 4 – Final Validation & Submission

- Agent Mode: `#runInTerminal npm run lint` (no errors)
- Agent Mode: `#runInTerminal npm test` (all tests pass)
- Agent Mode: `#runInTerminal npm run test:coverage` (thresholds met)
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
| `/refactor-to-parameterized` | Converting redundant tests to test.each |
| `/fix-flaky-test` | Diagnosing and fixing timing issues |
| `/generate-error-tests` | HTTP error and exception tests |
| `/generate-boundary-tests` | 7-point boundary value tests |

## Golden Examples Reference

| Pattern | Location | Use For |
| --- | --- | --- |
| Parameterized Tests | `.golden-examples/parameterized-tests/` | Redundancy reduction |
| HTTP Mocking | `.golden-examples/http-mocking/` | Service HTTP tests |
| Signal Testing | `.golden-examples/signal-testing/` | Angular 19 signals |
| Async Patterns | `.golden-examples/async-patterns/` | Flaky test fixes |
| Error Handling | `.golden-examples/error-handling/` | Exception tests |
| Boundary Testing | `.golden-examples/boundary-testing/` | Edge case tests |

## Workflow Loop

```
Test Critique Agent → docs/TEST_ANALYSIS.md → Test Create Agent → Test Quality Gate Agent
```

Each stage builds on the previous. The analysis file (`docs/TEST_ANALYSIS.md`) is the bridge between CRITIQUE and CREATE phases.
