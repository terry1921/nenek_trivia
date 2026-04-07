# Skill: Nenek Trivia Master Rules

## Description
This skill defines the master development rules for Nenek Trivia, an Android app built with Kotlin, Jetpack Compose, and a modular architecture.
Its purpose is to guide the agent to generate, modify, and review code consistently with the project's architecture, conventions, and technical preferences.

---

## Project Overview

Nenek Trivia is an Android application focused on game experience, performance, maintainability, and scalability.
The project follows a modular architecture with clear separation between UI, domain contracts, and data access.

### Main stack
- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Flow / StateFlow
- Room
- Hilt
- Coroutines
- Gson for Room converters

---

## Core Principles

Always prioritize:

1. **Clarity over complexity**
   - Code must be easy to read, maintain, and extend.
   - Avoid clever solutions that reduce readability.

2. **Separation of concerns**
   - UI, business logic, and data access must be clearly separated.
   - Do not mix responsibilities in the same layer.

3. **Modular architecture**
   - Respect module boundaries.
   - Avoid unnecessary dependencies between modules.

4. **Predictable state**
   - UI state must be explicit, observable, and immutable from the UI layer.
   - State changes must be easy to trace.

5. **Scalability**
   - Design features with future growth in mind.
   - Avoid tight coupling.

6. **Consistency**
   - Keep naming, patterns, and structure consistent across the project.
   - New features must feel native to the system.

---

## Module Rules

### :model
Contains:
- domain models
- repository interfaces
- shared contracts

Rules:
- Repository interfaces MUST live in `:model`
- No implementations here
- Avoid framework dependencies
- Keep this module clean and portable

### :database
Contains:
- Room entities
- DAO
- repository implementations
- converters
- data mappers

Rules:
- Repository implementations MUST live in `:database`
- Use **Gson** for Room converters
- DO NOT use `kotlinx.serialization` for Room converters
- Do not expose Room entities directly to UI
- Use mappers when needed

### :ui
Contains:
- composables
- screens
- navigation
- ViewModels
- UI state and events
- reusable UI components

Rules:
- UI should render state, not handle business logic
- Composables must remain as pure as possible
- ViewModels belong to UI layer
- No direct Room access from UI
- No persistence logic inside composables

### :app
Contains:
- app entry point
- configuration
- root navigation
- module wiring

Rules:
- Keep it lightweight
- Do not centralize business logic here
- Acts as composition root only

---

## Architecture Rules

### MVVM

#### ViewModel responsibilities
- coordinate repositories or use cases
- expose UI state
- handle UI events
- transform data for presentation

Rules:
- Expose state via `StateFlow`
- Keep state immutable externally
- Do not expose `MutableStateFlow`
- Do not reference UI components

### UI State

Rules:
- Represent loading, success, empty, and error when applicable
- Prefer a single `UiState` per screen when possible
- Use data classes or sealed classes depending on complexity

### Data Flow

Pattern:
Repository -> ViewModel -> UiState -> UI

Rules:
- Prefer Flow for reactive data
- Use StateFlow for UI state
- Avoid LiveData unless strictly necessary
- Keep unidirectional data flow

---

## Compose Rules

### General
- Use Jetpack Compose + Material 3
- Build reusable and modular components
- Prioritize readability and stability

### Composables
Rules:
- Must render state, not contain business logic
- Keep functions small and focused
- Extract subcomponents when needed
- Hoist state when appropriate

### Screen Pattern
Preferred structure:
- ScreenRoute connects ViewModel
- ScreenContent is pure UI

### Previews
Rules:
- Always use fake or sample data
- Do not depend on Hilt or real data sources
- Separate UI from ViewModel when needed

### Recomposition & Stability
Rules:
- Avoid unnecessary object creation
- Use `remember` only when needed
- Avoid premature optimization
- Prevent obvious recomposition issues

### Side Effects
Rules:
- Use `LaunchedEffect` responsibly
- Avoid repeated side effects
- Every side effect must be intentional

---

## ViewModel Rules

### State
- Immutable public state
- Private mutable backing state

### Coroutines
- Use `viewModelScope`
- Never block threads
- Handle errors explicitly

### Events
- UI sends events
- ViewModel handles logic
- Avoid business logic in UI

---

## Repository Rules

### Contracts
- Defined in `:model`
- Clear and explicit

### Implementations
- Located in `:database`
- Must be testable and readable

### Return types
- Use Flow when observing data
- Use suspend functions for one-shot operations

### Mapping
- Do not expose database entities to UI
- Use explicit mappers when needed

---

## Room Rules

### Entities
- Persistence-focused only
- No UI concerns

### DAO
- Clear, focused queries
- Avoid large DAOs

### Converters
- Use Gson ONLY
- No kotlinx.serialization

### Access
- Always through repositories
- Never directly from UI

---

## Dependency Injection Rules

### Hilt
- Use constructor injection
- Keep modules organized
- Avoid service locators

---

## Naming Conventions

Use consistent naming:
- `ProfileScreen`
- `ProfileViewModel`
- `ProfileUiState`
- `ProfileRepository`
- `ProfileRepositoryImpl`
- `ProfileDao`
- `ProfileEntity`

Avoid:
- generic names like `Helper`, `Utils`
- unclear abbreviations

---

## Code Style Rules

- Functions should do one thing
- Keep classes cohesive
- Avoid long methods
- Prefer readability over cleverness

---

## UI/UX Rules

- Clean and minimal UI
- Consistent Material 3 usage
- Handle:
  - loading
  - empty state
  - error state
  - content state

---

## Testing Rules

Prioritize testing:
- ViewModels
- state logic
- repositories when needed

Preferences:
- clear tests
- cover loading, success, and error
- prefer fakes over complex mocks

---

## Navigation Rules

- Centralized navigation
- Clear route naming
- Avoid over-coupling between screens

---

## Performance Rules

- Avoid heavy work in composables
- Prevent unnecessary recompositions
- Avoid redundant data calls

---

## Feature Development Workflow

1. Define feature goal
2. Identify modules involved
3. Define contracts and models
4. Implement repository
5. Implement data layer if needed
6. Create ViewModel
7. Define UiState
8. Build UI
9. Connect everything
10. Add previews
11. Add tests
12. Review consistency

---

## Code Review Rules

Check:

### Architecture
- Correct module placement
- Interfaces in `:model`
- Implementations in `:database`

### UI
- Clean composables
- Proper state handling

### Data
- Proper use of Flow
- Clean repository design

### Quality
- Readable code
- Testable logic
- No unnecessary complexity

---

## Agent Behavior Rules

The agent MUST:
1. Respect architecture boundaries
2. Never place repository interfaces outside `:model`
3. Never place repository implementations outside `:database`
4. Always use Gson for Room converters
5. Prefer Flow over alternatives
6. Avoid business logic in composables
7. Generate maintainable and consistent code
8. Avoid unnecessary abstractions
9. Follow modular architecture strictly

---

## What to Avoid

- Breaking module boundaries
- Mixing UI with data layer
- Using kotlinx.serialization for Room
- Adding unnecessary layers
- Creating generic utility classes without purpose

---

## Final Rule

If there is a conflict between speed and architecture consistency:

Always prioritize consistency, clarity, and maintainability.
