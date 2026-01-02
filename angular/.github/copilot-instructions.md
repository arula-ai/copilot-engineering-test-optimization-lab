# GitHub Copilot Instructions for Angular Test Optimization Lab

> **Purpose**: This file configures GitHub Copilot to generate expert-level, production-ready tests following the patterns established in this lab. Copilot should always follow the Critique-then-Create methodology and reference Golden Examples.

---

## Core Methodology: Critique-then-Create

**ALWAYS follow this two-phase approach:**

### Phase 1: CRITIQUE (Analysis)
Before generating ANY code, analyze the existing implementation:

1. **Identify the component/service under test**
   - What is its primary responsibility?
   - What dependencies does it have?
   - What state does it manage (signals, observables)?

2. **Analyze existing test coverage**
   - What scenarios are already tested?
   - What edge cases are missing?
   - Are there redundant or overlapping tests?

3. **Identify anti-patterns in existing tests**
   - Are there timing-dependent (flaky) tests?
   - Is there duplicated setup code?
   - Are tests properly isolated?

4. **Document coverage gaps**
   - Error paths not tested
   - Boundary conditions missing
   - Integration points not verified

### Phase 2: CREATE (Implementation)
Only after completing analysis, generate code that:

1. **References Golden Examples** from `.golden-examples/`
2. **Follows established patterns** for this codebase
3. **Addresses identified gaps** systematically
4. **Maintains consistency** with existing test style

---

## Golden Examples Reference

**ALWAYS reference these patterns when generating tests:**

| Pattern | Location | Use For |
|---------|----------|---------|
| Parameterized Tests | `.golden-examples/parameterized-tests/` | Multiple similar test cases, validation rules |
| HTTP Mocking | `.golden-examples/http-mocking/` | Service HTTP calls, error handling |
| Signal Testing | `.golden-examples/signal-testing/` | Angular 19 signals, computed values, effects |
| Async Patterns | `.golden-examples/async-patterns/` | fakeAsync, timers, polling, debounce |
| Error Handling | `.golden-examples/error-handling/` | Error states, retries, recovery |
| Boundary Testing | `.golden-examples/boundary-testing/` | Min/max values, edge cases, null handling |

---

## Angular 19 Testing Standards

### TestBed Configuration

```typescript
// CORRECT: Minimal, focused configuration
beforeEach(() => {
  TestBed.configureTestingModule({
    imports: [HttpClientTestingModule],
    providers: [ServiceUnderTest]
  });
  service = TestBed.inject(ServiceUnderTest);
  httpMock = TestBed.inject(HttpTestingController);
});

// ALWAYS verify HTTP mocks
afterEach(() => {
  httpMock.verify();
});
```

### Signal Testing Requirements

```typescript
// Test initial state
expect(service.items()).toEqual([]);
expect(service.isLoading()).toBe(false);

// Test state transitions
service.addItem(item);
expect(service.items()).toContainEqual(item);

// Test computed signals
expect(service.itemCount()).toBe(1);

// Test effects with TestBed.flushEffects()
service.updateItems([item1, item2]);
TestBed.flushEffects();
expect(persistSpy).toHaveBeenCalled();
```

### Async Testing with fakeAsync

```typescript
// CORRECT: Use fakeAsync for ALL timer operations
it('should debounce search', fakeAsync(() => {
  service.search('query');
  service.search('query2');
  service.search('query3');

  expect(httpMock.expectNone('/api/search')); // No request yet

  tick(300); // Advance past debounce

  httpMock.expectOne('/api/search?q=query3').flush([]);

  flush(); // Clean up any remaining timers
}));

// NEVER use real setTimeout in tests
// NEVER use async/await with timers
```

### HTTP Mocking Patterns

```typescript
// Test request configuration
const req = httpMock.expectOne('/api/endpoint');
expect(req.request.method).toBe('POST');
expect(req.request.headers.get('Authorization')).toMatch(/^Bearer /);
expect(req.request.body).toEqual(expectedPayload);

// Test all error codes
test.each([
  [400, 'Invalid request'],
  [401, 'Unauthorized'],
  [403, 'Forbidden'],
  [404, 'Not found'],
  [500, 'Server error'],
])('should handle HTTP %d', (status, message) => {
  // ...
});
```

---

## Test Generation Rules

### DO Generate:

1. **Parameterized tests** for validation rules
   ```typescript
   test.each([
     ['valid@email.com', true],
     ['invalid', false],
     ['', false],
   ])('validateEmail("%s") should return %s', (email, expected) => {
     expect(service.validateEmail(email)).toBe(expected);
   });
   ```

2. **Boundary value tests** using documented constants
   ```typescript
   const BOUNDARIES = {
     MIN_AMOUNT: 0.01,
     MAX_AMOUNT: 999999.99
   };

   test.each([
     [BOUNDARIES.MIN_AMOUNT, true],
     [BOUNDARIES.MIN_AMOUNT - 0.01, false],
     [BOUNDARIES.MAX_AMOUNT, true],
     [BOUNDARIES.MAX_AMOUNT + 0.01, false],
   ])('validateAmount(%d) should return %s', (amount, valid) => {
     expect(service.validateAmount(amount).valid).toBe(valid);
   });
   ```

3. **Error state tests** with proper transitions
   ```typescript
   it('should transition through error states', () => {
     expect(service.error()).toBeNull();

     service.triggerError();
     expect(service.error()).not.toBeNull();

     service.clearError();
     expect(service.error()).toBeNull();
   });
   ```

4. **Comprehensive HTTP error tests**
   ```typescript
   it('should handle network failure', () => {
     service.getData().subscribe({ error: (e) => error = e });

     httpMock.expectOne('/api/data').error(
       new ProgressEvent('error'),
       { status: 0, statusText: 'Network Error' }
     );

     expect(error).toBeDefined();
   });
   ```

### DO NOT Generate:

1. **Tests without assertions**
   ```typescript
   // BAD
   it('should call method', () => {
     service.doSomething();
   });
   ```

2. **Generic "should work" tests**
   ```typescript
   // BAD
   it('should work correctly', () => { ... });
   ```

3. **Real setTimeout/setInterval**
   ```typescript
   // BAD - Never do this
   it('should wait', async () => {
     await new Promise(r => setTimeout(r, 1000));
   });
   ```

4. **Incomplete HTTP mocking**
   ```typescript
   // BAD - Missing afterEach verify
   it('should fetch', () => {
     service.getData().subscribe();
     // Missing httpMock.verify() in afterEach
   });
   ```

5. **Duplicate/redundant tests**
   ```typescript
   // BAD - Should be parameterized
   it('should validate email1', () => { ... });
   it('should validate email2', () => { ... });
   it('should validate email3', () => { ... });
   ```

---

## Coverage Gap Analysis Protocol

When asked to analyze coverage gaps, follow this checklist:

### 1. Happy Path Coverage
- [ ] Primary success scenario
- [ ] All valid input variations
- [ ] Expected state transitions

### 2. Error Path Coverage
- [ ] HTTP 4xx errors (400, 401, 403, 404, 422)
- [ ] HTTP 5xx errors (500, 502, 503)
- [ ] Network failures (status 0)
- [ ] Timeout scenarios
- [ ] Validation errors

### 3. Boundary Coverage
- [ ] Minimum valid values
- [ ] Maximum valid values
- [ ] Just below minimum
- [ ] Just above maximum
- [ ] Empty/null/undefined
- [ ] Type coercion cases

### 4. State Coverage
- [ ] Initial state
- [ ] Loading state
- [ ] Success state
- [ ] Error state
- [ ] State transitions

### 5. Integration Coverage
- [ ] Component-service interaction
- [ ] Service-HTTP interaction
- [ ] Signal propagation
- [ ] Effect triggers

---

## Refactoring Commands

When asked to refactor tests, apply these patterns:

### Convert to Parameterized Tests
```typescript
// BEFORE (redundant)
it('should validate email 1', () => {
  expect(validate('a@b.com')).toBe(true);
});
it('should validate email 2', () => {
  expect(validate('invalid')).toBe(false);
});

// AFTER (parameterized)
test.each([
  ['a@b.com', true, 'valid format'],
  ['invalid', false, 'missing @'],
])('validate("%s") = %s (%s)', (email, expected, _reason) => {
  expect(validate(email)).toBe(expected);
});
```

### Extract Common Setup
```typescript
// BEFORE (duplicated)
it('test 1', () => {
  const service = new Service();
  service.init();
  // test
});
it('test 2', () => {
  const service = new Service();
  service.init();
  // test
});

// AFTER (extracted)
let service: Service;
beforeEach(() => {
  service = new Service();
  service.init();
});
```

### Fix Flaky Async Tests
```typescript
// BEFORE (flaky - real timers)
it('should update', async () => {
  service.startPolling();
  await new Promise(r => setTimeout(r, 100));
  expect(service.data()).toBeDefined();
});

// AFTER (deterministic - fakeAsync)
it('should update', fakeAsync(() => {
  service.startPolling();
  tick(100);
  expect(service.data()).toBeDefined();
  discardPeriodicTasks();
}));
```

---

## Response Format Guidelines

When responding to test-related prompts:

1. **Start with analysis** (Critique phase)
2. **Reference applicable Golden Example**
3. **Show before/after** when refactoring
4. **Explain the pattern** being applied
5. **Include all necessary imports**
6. **Add meaningful test descriptions**

### Example Response Structure:

```markdown
## Analysis (Critique Phase)

Looking at `payment.service.spec.ts`, I identified:
- ✅ Happy path for credit card payments
- ❌ Missing: Other payment methods (debit, bank transfer, wallet)
- ❌ Missing: Error handling for declined cards
- ❌ Missing: Boundary tests for amounts

## Recommended Changes (Create Phase)

Based on `.golden-examples/parameterized-tests/`, here are the improvements:

[Generated test code with explanations]
```

---

## Project-Specific Context

### Services Under Test
- `PaymentService` - Payment processing with multiple methods
- `OrderService` - Order lifecycle management
- `UserService` - User validation and management
- `InventoryService` - Stock management with signals
- `NotificationService` - (Currently untested - needs full coverage)

### Known Issues to Fix
1. `order.service.spec.ts` - Flaky concurrent test using real timers
2. `user.service.spec.ts` - Redundant email validation tests
3. `validators.spec.ts` - Missing boundary value tests
4. `notification.service.spec.ts` - No tests exist

### Coverage Targets
- Line Coverage: 80%+
- Branch Coverage: 75%+
- Critical paths: 100%

---

## Quick Reference: Jest + Angular

```typescript
// Imports
import { TestBed, fakeAsync, tick, flush, discardPeriodicTasks } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

// Parameterized tests
test.each([...])('description %s', (param) => { });

// Async testing
fakeAsync(() => { tick(100); flush(); discardPeriodicTasks(); });

// HTTP mocking
httpMock.expectOne('/url').flush(data);
httpMock.expectOne('/url').flush(null, { status: 500, statusText: 'Error' });
httpMock.expectOne('/url').error(new ProgressEvent('error'));

// Signals
expect(service.signal()).toBe(value);
TestBed.flushEffects();

// Spies
jest.spyOn(service, 'method').mockReturnValue(value);
expect(spy).toHaveBeenCalledWith(args);
```

---

**Remember**: Quality over quantity. Every test should have a clear purpose and follow the established patterns in Golden Examples.
