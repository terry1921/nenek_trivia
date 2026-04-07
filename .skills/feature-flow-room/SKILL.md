# Skill: Feature Flow + Room Integration

## Description
This skill implements features that involve data flow using Room, repositories, Flow, and ViewModels, following Nenek Trivia architecture.

---

## Responsibilities
- Create DAO, Entity, Repository
- Connect data layer to ViewModel
- Expose UI state using Flow
- Ensure proper module separation

---

## Architecture Flow

Database -> Repository -> ViewModel -> UI

---

## Module Rules
- Repository interface -> :model
- Repository implementation -> :database
- UI + ViewModel -> :ui

---

## Room Layer

### Entity
- Persistence-only
- No UI logic

### DAO
- Clear queries
- Focused responsibility

### Converters
- MUST use Gson
- NEVER use kotlinx.serialization

---

## Repository

### Interface (:model)
- Define clear contract
- Use Flow where applicable

### Implementation (:database)
- Use DAO
- Map entities to domain models
- Keep logic clean and testable

---

## ViewModel

Responsibilities:
- Consume repository
- Expose `StateFlow`
- Handle loading, success, and error

Example pattern:
- map repository data into UI state
- handle exceptions explicitly
- expose immutable state

```kotlin
val uiState = repository.getData()
    .map { UiState.Success(it) }
    .catch { emit(UiState.Error) }
    .stateIn(...)
```

## UI State
Should include:
- loading
- data
- error

Possible shapes:
- single data class
- sealed class with Loading, Success, Error, Empty

or use sealed classes:

- Loading
- Success
- Error
- Empty

---

## Flow Rules
- Prefer Flow for reactive streams
- Use StateFlow for UI
- Avoid LiveData

---

## Mapping
- Never expose Room entities to UI
- Use mappers if needed

---

## Error Handling
- Always handle errors
- Do not swallow exceptions
- Provide meaningful UI states

---

## What to Avoid
- Skipping repository layer
- Putting DAO in UI
- Mixing modules
- Using wrong serialization
- Blocking threads

---

## Output Expectations

When generating features:
- Include Entity + DAO
- Include Repository interface + implementation
- Include ViewModel
- Include UiState
- Provide clean Flow-based pipeline
