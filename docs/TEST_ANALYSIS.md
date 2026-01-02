# Test Analysis

## Baseline Metrics (Stage 0)
- Test suites: 12 total (6 passed, 6 failed)
- Tests: 50 passed, 50 total
- Coverage (summary):
  - % Stmts: 34.4
  - % Branch: 31.97
  - % Funcs: 19.01
  - % Lines: 37.86

Notes: Several test suites in `.golden-examples/` fail at runtime because they import local example services that do not exist in that folder. These failing golden-example tests block a clean `npm test` run and should be addressed (exclude golden examples from Jest or provide the referenced example modules).

---

## Priority Findings (per `src/app/core/services`)

- **PaymentService (`payment.service.ts`)**
  - Missing tests for HTTP error handling (4xx, 5xx) and retry/fallback code paths.
  - Coverage: many uncovered lines in network/error branches (see coverage summary). Recommend adding mocked HTTP tests using `HttpClientTestingModule` and testing error responses, timeouts, and header handling.

- **UserService (`user.service.ts`)**
  - Tests include many near-duplicate validation cases; redundancy could be reduced with parameterized tests (`test.each`).
  - Coverage gaps in less common validation branches and edge input sizes (very long emails, internationalized inputs).

- **OrderService (`order.service.ts`)**
  - Contains async behavior and polling/refresh semantics; tests show occasional flakiness in CI-like runs.
  - Use `fakeAsync`/`tick` patterns and ensure `flush()`/`discardPeriodicTasks()` are used where appropriate.

- **InventoryService (`inventory.service.ts`)**
  - No tests present (0% coverage for this file). It exposes signals/computed behaviors that require explicit signal testing.
  - Add signal initialization, mutation, and computed assertions following the signal-testing golden example.

---

## Anti-patterns & Other Observations
- Golden-example tests in `.golden-examples/` are executed by Jest and fail due to missing local example modules; treat these as documentation-only examples or relocate/exclude them from the test runner.
- Some component tests and feature files have 0% coverage (components under `app/features/*`) and should be audited for missing tests.
- Several validator edge-cases are untested; apply boundary testing (7-point analysis) to validators.

## Recommended Next Steps (Stage 2 targets)
1. Exclude `.golden-examples/` from Jest test runs (via `testMatch`/`testPathIgnorePatterns`) or add minimal example modules so golden specs pass.
2. Implement parameterized tests for `user.service.spec.ts` to reduce duplication.
3. Convert flaky `order.service.spec.ts` timers to `fakeAsync`/`tick` patterns and run the test multiple times to confirm stability.
4. Add HTTP-error tests for `payment.service.spec.ts` using `HttpTestingController` for 400/401/403/404/500 responses.
5. Add signal tests for `inventory.service.ts` following `.golden-examples/signal-testing/`.
6. Re-run `npm test` and `npm run test:coverage` and capture improved metrics.

## File Reference Audit
| Referenced Path | Exists |
|---|---:|
| src/app/core/services/payment.service.ts | ✅ |
| src/app/core/services/user.service.ts    | ✅ |
| src/app/core/services/order.service.ts   | ✅ |
| src/app/core/services/inventory.service.ts | ✅ |
| .golden-examples/                         | ✅ (contains specs that currently fail) |
