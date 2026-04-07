# Skill: UI Compose Builder

## Description
This skill generates and structures UI using Jetpack Compose for Nenek Trivia, following Material 3, clean UI patterns, and project-specific architecture rules.

---

## Responsibilities
- Build composables and screens
- Apply Material 3 design
- Structure UI into reusable components
- Ensure clean separation between UI and logic

---

## Compose Rules

### General
- Use Jetpack Compose + Material 3
- UI must be clean, readable, and modular
- Prefer stateless composables when possible

### Screen Structure
Preferred pattern:
- ScreenRoute connects ViewModel
- ScreenContent is pure UI

Example:

```kotlin
@Composable
fun ProfileScreenRoute(viewModel: ProfileViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    ProfileScreenContent(uiState = uiState)
}
```

---

### Composables
Rules:
- Must NOT contain business logic
- Must render state only
- Keep functions small and reusable
- Extract components when complexity grows

### State Handling
- UI receives state from ViewModel
- UI sends events back to ViewModel
- Avoid local state unless it is strictly UI-related

### Preview Rules
- Always create previews when possible
- Use fake or sample data
- Never depend on ViewModel or DI in previews

### Recomposition
- Avoid unnecessary recompositions
- Use `remember` only when needed
- Do not create heavy objects inside composables

### Side Effects
- Use `LaunchedEffect` carefully
- Avoid repeated execution
- Effects must be intentional

---

## UI States

Every screen should consider:
- Loading
- Empty
- Error
- Content

---

## Material 3
- Use Material 3 components
- Maintain consistency with theme
- Avoid unnecessary custom components

---

## What to Avoid
- Business logic inside composables
- Direct repository or database calls
- Large monolithic composables
- Overuse of remember or side effects

---

## Output Expectations

When generating UI:
- Provide complete composables
- Include state-driven rendering
- Follow naming conventions
- Suggest previews if applicable
