---
description: "Diagnose and fix flaky tests by identifying timing issues, shared state, and non-deterministic patterns"
---

# Fix Flaky Test

Diagnose the root cause of test flakiness and apply deterministic patterns.

## Prerequisites

Reference the anti-patterns in `#file:docs/TEST_ANALYSIS.md` to identify flaky tests.

## Common Flaky Patterns & Fixes

### Angular/Jest

| Anti-Pattern | Fix |
|--------------|-----|
| `setTimeout`/`setInterval` | Use `fakeAsync()` + `tick()` |
| Missing timer cleanup | Add `discardPeriodicTasks()` |
| `Promise.all` race | Control execution order |
| `Date.now()` | Mock with `jest.spyOn` |
| Shared component state | Reset in `beforeEach` |

**Correct Pattern**:
```typescript
it('handles debounced input', fakeAsync(() => {
  // Arrange
  const spy = jest.spyOn(service, 'search');

  // Act
  component.onInput('test');
  tick(300); // Advance past debounce

  // Assert
  expect(spy).toHaveBeenCalledWith('test');

  // Cleanup
  discardPeriodicTasks();
}));
```

**Reference**: `.golden-examples/async-patterns/`

### Java/JUnit

| Anti-Pattern | Fix |
|--------------|-----|
| `Thread.sleep()` | Use `CompletableFuture.get(timeout, unit)` |
| `get()` without timeout | Add timeout parameter |
| `LocalDateTime.now()` | Inject and mock `Clock` |
| Shared static state | Use `@BeforeEach` reset |
| Missing `@Timeout` | Add `@Timeout(5)` annotation |

**Correct Pattern**:
```java
@Test
@Timeout(5)
void shouldCompleteAsyncOperation() {
    CompletableFuture<Result> future = service.processAsync(request);

    assertThat(future)
        .succeedsWithin(Duration.ofSeconds(2))
        .satisfies(result -> assertThat(result.isSuccess()).isTrue());
}
```

**Reference**: `.golden-examples/async-patterns/`

## Diagnosis Steps

1. **Identify the failure pattern**:
   - Does it fail randomly? (timing issue)
   - Does it fail after other tests? (shared state)
   - Does it fail only in CI? (environment dependency)

2. **Run test in isolation**:
   ```bash
   # Angular
   npm test -- --testPathPattern="test-name" --runInBand

   # Java
   mvn test -Dtest=TestClass#testMethod
   ```

3. **Run multiple times to confirm flakiness**:
   ```bash
   # Angular
   for i in {1..10}; do npm test -- --testPathPattern="test-name"; done

   # Java
   for i in {1..10}; do mvn test -Dtest=TestClass#testMethod; done
   ```

## Output

1. Document the root cause
2. Show before/after code
3. Apply the fix
4. Verify with 5+ consecutive passes
