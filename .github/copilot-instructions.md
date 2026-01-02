# GitHub Copilot Instructions - Test Optimization Lab

> **Purpose**: This repository contains two test optimization labs. This file configures GitHub Copilot to follow the Critique-then-Create methodology and reference Golden Examples for both technology stacks.

---

## Repository Structure

```
/angular    → Angular 19 + Jest lab
/java       → Java 17 Spring Boot + JUnit 5 lab
```

**When working in `/angular/**`, follow Angular patterns below.
**When working in `/java/**`, follow Java patterns below.

---

## Core Methodology: Critique-then-Create

**ALWAYS follow this two-phase approach for BOTH labs:**

### Phase 1: CRITIQUE (Analysis)
Before generating ANY test code:
1. Identify the component/service under test
2. Analyze existing test coverage
3. Identify anti-patterns and gaps
4. Document coverage gaps

### Phase 2: CREATE (Implementation)
After completing analysis:
1. Reference Golden Examples from the appropriate `.golden-examples/` folder
2. Address identified gaps systematically
3. Follow established patterns for consistency

---

# Angular Lab Instructions

## Golden Examples Location
**Path**: `/angular/.golden-examples/`

| Pattern | Location | Use For |
|---------|----------|---------|
| Parameterized Tests | `parameterized-tests/` | Multiple similar test cases |
| HTTP Mocking | `http-mocking/` | Service HTTP calls, error handling |
| Signal Testing | `signal-testing/` | Angular 19 signals, computed, effects |
| Async Patterns | `async-patterns/` | fakeAsync, timers, polling |
| Error Handling | `error-handling/` | Error states, retries |
| Boundary Testing | `boundary-testing/` | Min/max values, edge cases |

## Angular Testing Standards

### TestBed Configuration
```typescript
beforeEach(() => {
  TestBed.configureTestingModule({
    imports: [HttpClientTestingModule],
    providers: [ServiceUnderTest]
  });
  service = TestBed.inject(ServiceUnderTest);
  httpMock = TestBed.inject(HttpTestingController);
});

afterEach(() => {
  httpMock.verify(); // ALWAYS verify HTTP mocks
});
```

### Signal Testing
```typescript
expect(service.items()).toEqual([]);
service.addItem(item);
expect(service.items()).toContainEqual(item);
TestBed.flushEffects();
```

### Async Testing with fakeAsync
```typescript
it('should handle timers', fakeAsync(() => {
  service.startPolling();
  tick(5000);
  expect(service.data()).toBeDefined();
  flush();
  discardPeriodicTasks();
}));
```

### DO Generate for Angular:
- Parameterized tests with `test.each`
- HTTP error tests for all status codes (400, 401, 403, 404, 500, etc.)
- Signal state tests (initial, mutations, computed)
- fakeAsync for ALL timer operations

### DO NOT Generate for Angular:
- Real setTimeout in tests
- Tests without assertions
- Missing httpMock.verify()

---

# Java Lab Instructions

## Golden Examples Location
**Path**: `/java/.golden-examples/`

| Pattern | Location | Use For |
|---------|----------|---------|
| Parameterized Tests | `parameterized-tests/` | @ParameterizedTest patterns |
| Mockito Mocking | `mockito-mocking/` | @Mock, @InjectMocks |
| Repository Testing | `repository-testing/` | @DataJpaTest patterns |
| Async Patterns | `async-patterns/` | CompletableFuture testing |
| Error Handling | `error-handling/` | Exception testing |
| Boundary Testing | `boundary-testing/` | Boundary value analysis |

## Java Testing Standards

### Service Test Setup
```java
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        // Additional setup if needed
    }
}
```

### Parameterized Tests
```java
@ParameterizedTest
@CsvSource({
    "valid@email.com, true",
    "invalid, false",
    "'', false"
})
void shouldValidateEmail(String email, boolean expected) {
    assertThat(service.isValidEmail(email)).isEqualTo(expected);
}
```

### Exception Testing
```java
@Test
void shouldThrowWhenAmountNegative() {
    assertThrows(InvalidPaymentException.class, () ->
        service.processPayment(createPaymentWithAmount(-100))
    );
}
```

### DO Generate for Java:
- @ParameterizedTest for validation rules
- @Mock for all dependencies
- verify() for mock interaction testing
- assertThrows for exception testing

### DO NOT Generate for Java:
- Thread.sleep() in tests
- Catching generic Exception
- Missing mock verification
- Real database in unit tests

---

## Prompt Format for Both Labs

```
@workspace [Action] [Target] [with specific requirements].

CRITIQUE CONTEXT:
[Analysis findings from Phase 1]

CREATE REQUIREMENTS:
Reference [specific Golden Example path]

Apply these patterns:
[Numbered list of specific patterns to use]

[Output format expectations]
```

---

## File Detection

When a prompt references a file:
- Files in `/angular/` → Use Angular patterns
- Files in `/java/` → Use Java patterns
- Files with `.ts` extension → Use Angular/Jest patterns
- Files with `.java` extension → Use Java/JUnit patterns

---

*Remember: Quality comes from patterns. Patterns come from examples. Examples come from Golden Examples.*
