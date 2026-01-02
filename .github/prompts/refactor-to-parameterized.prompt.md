---
description: "Refactor redundant tests to use parameterized patterns (test.each for Jest, @ParameterizedTest for JUnit)"
---

# Refactor Redundant Tests to Parameterized

Convert repetitive test patterns into concise, maintainable parameterized tests.

## Prerequisites

Reference the analysis in `#file:docs/TEST_ANALYSIS.md` to identify redundant tests.

## Instructions

### For Angular/Jest

1. **Identify redundant patterns**:
   - Multiple `it()` blocks with identical structure
   - Only test data differs between tests

2. **Convert to test.each**:
   ```typescript
   // Before: 6 separate tests
   it('validates valid@email.com', () => { ... });
   it('validates user@domain.org', () => { ... });

   // After: 1 parameterized test
   test.each([
     ['valid@email.com', true, 'standard format'],
     ['user@domain.org', true, 'org domain'],
     ['invalid', false, 'missing @'],
   ])('validates "%s" as %s (%s)', (email, expected, reason) => {
     expect(validator.isValid(email)).toBe(expected);
   });
   ```

3. **Reference pattern**: `.golden-examples/parameterized-tests/`

### For Java/JUnit

1. **Identify redundant patterns**:
   - Multiple `@Test` methods with identical structure
   - Only test data differs between methods

2. **Convert to @ParameterizedTest**:
   ```java
   // Before: 6 separate @Test methods

   // After: 1 parameterized test
   @ParameterizedTest(name = "validates {0} as {1}")
   @CsvSource({
       "valid@email.com, true",
       "user@domain.org, true",
       "invalid, false"
   })
   void shouldValidateEmail(String email, boolean expected) {
       assertThat(validator.isValid(email)).isEqualTo(expected);
   }
   ```

3. **For complex objects, use @MethodSource**:
   ```java
   @ParameterizedTest
   @MethodSource("providePaymentScenarios")
   void shouldProcessPayment(PaymentRequest request, boolean expected) { ... }

   static Stream<Arguments> providePaymentScenarios() {
       return Stream.of(
           Arguments.of(validRequest(), true),
           Arguments.of(invalidRequest(), false)
       );
   }
   ```

4. **Reference pattern**: `.golden-examples/parameterized-tests/`

## Target

Reduce line count by **50% or more** while maintaining full test coverage.

## Output

Apply refactoring to the identified test file and verify tests still pass.
