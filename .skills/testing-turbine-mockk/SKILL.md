# Skill: Testing (ViewModel + Flow + Turbine + MockK)

## Description
This skill generates and reviews tests for Nenek Trivia using Kotlin, Flow, Turbine, and MockK, following clean testing practices.

---

## Responsibilities
- Write unit tests for ViewModels
- Test Flow emissions
- Validate UI state transitions
- Suggest improvements for testability

---

## Testing Philosophy
- Test behavior, not implementation
- Keep tests simple and readable
- Cover critical paths

---

## Tools
- JUnit
- MockK
- Turbine (for Flow testing)

---

## What to Test

### ViewModel
- Initial state
- Loading state
- Success state
- Error state

### Flow
- Use Turbine to verify emissions
- Assert emitted items clearly
- Validate completion or terminal states when relevant

Use Turbine:

```kotlin
flow.test {
    assertEquals(expected, awaitItem())
}
```

---

### Repository
- Test only if logic exists
- Otherwise mock it in ViewModel tests

---

## Mocking
- Use MockK
- Prefer simple mocks
- Avoid over-mocking

Example:

```kotlin
every { repository.getData() } returns flowOf(data)
```

State Testing Example

```kotlin
@Test
fun `should emit success when data is loaded`() = runTest {
    // Given
    every { repository.getData() } returns flowOf(listOf())

    // When
    val viewModel = MyViewModel(repository)

    // Then
    viewModel.uiState.test {
        assertTrue(awaitItem() is UiState.Success)
    }
}
```

---

## Best Practices
- Use `runTest`
- Avoid delays unless necessary
- Keep tests deterministic
- Use fake data when possible

---

## What to Avoid
- Testing implementation details
- Overusing mocks
- Flaky tests
- Ignoring error cases

---

## Output Expectations

When generating tests:
- Cover main states: loading, success, error
- Use Turbine for Flow
- Keep tests readable
- Provide complete test examples
