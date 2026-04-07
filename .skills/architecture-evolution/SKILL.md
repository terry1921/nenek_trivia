# Skill: Architecture Evolution (Nenek Trivia)

## Description
This skill guides architectural evolution in Nenek Trivia.
It is used for refactors, module boundary improvements, dependency cleanup, feature scaling, and structural decisions that improve maintainability without breaking project consistency.

---

## Responsibilities
- Propose safe architectural improvements
- Refactor large features into clearer structures
- Improve module boundaries
- Reduce technical debt
- Preserve existing behavior while improving design

---

## Architecture Philosophy

Nenek Trivia must evolve with:
- clear module boundaries
- maintainable abstractions
- scalable feature organization
- minimal accidental complexity

The goal is NOT to introduce architecture for its own sake.
The goal is to improve clarity, consistency, testability, and scalability.

---

## Project Rules to Preserve
Always preserve these core project rules:
- Repository interfaces MUST live in `:model`
- Repository implementations MUST live in `:database`
- Room converters MUST use Gson
- UI must not contain business logic
- Flow / StateFlow are the preferred reactive primitives
- MVVM is the presentation pattern
- Modular boundaries must remain explicit

---

## When to Use This Skill
Use this skill when:
- a feature has become too large
- responsibilities are mixed across layers
- modules are becoming tightly coupled
- naming and structure are inconsistent
- a screen needs to be split into smaller pieces
- a ViewModel has too many responsibilities
- a repository is doing too much
- feature growth requires a clearer internal structure

---

## Refactor Priorities
Always prioritize refactors that improve:
1. readability
2. separation of concerns
3. module responsibility clarity
4. testability
5. long-term maintainability

Do not prioritize theoretical purity over practical usefulness.

---

## Safe Refactor Rules
When proposing or generating refactors:
- preserve behavior unless explicitly asked to change it
- avoid massive rewrites when incremental improvement is possible
- refactor in small, understandable steps
- explain tradeoffs clearly
- avoid introducing unnecessary layers

---

## Module Evolution Rules

### :model
Should remain focused on:
- repository interfaces
- shared contracts
- domain-facing models

Do not move implementations here.

### :database
Should remain focused on:
- entities
- DAO
- repository implementations
- persistence mappers
- converters

Do not place UI-facing state here.

### :ui
Should remain focused on:
- composables
- screens
- ViewModels
- UI state and events
- presentation-specific mapping if needed

Do not place persistence logic here.

### :app
Should remain the assembly/composition root only.

---

## ViewModel Refactor Rules
A ViewModel should be refactored when it:
- manages too many unrelated responsibilities
- contains complex mapping that should be extracted
- handles navigation, persistence, and heavy business logic together
- has large event handlers that are hard to read
- has unclear or bloated UiState

Preferred improvements:
- extract smaller private functions
- extract mappers
- split large state structures
- separate feature concerns where appropriate

---

## UI Refactor Rules
A screen should be refactored when it:
- is too large to understand easily
- mixes route logic and UI rendering
- contains repeated patterns
- mixes state collection with deep rendering logic
- has poor previewability

Preferred improvements:
- split into Route and Content
- extract reusable composables
- extract state-specific UI sections
- improve naming and hierarchy

---

## Repository Refactor Rules
A repository should be refactored when it:
- does too many unrelated things
- mixes persistence, mapping, and business rules in confusing ways
- exposes persistence models outside its boundary
- is difficult to test

Preferred improvements:
- extract mapping helpers
- narrow responsibilities
- clarify contracts
- simplify method naming

---

## Dependency Rules
When evolving architecture:
- prefer constructor injection
- avoid hidden dependencies
- avoid service locator patterns
- keep dependency direction clean

---

## Naming Improvement Rules
Refactors should improve:
- class names
- function names
- file organization
- intent clarity

Avoid:
- vague abstractions
- generic names like `Helper`, `Manager`, `Processor`, `Utils`
- names that hide responsibility

---

## Anti-Patterns to Detect
Flag and improve these patterns:
- business logic inside composables
- direct DAO usage from UI
- multiple unrelated responsibilities in one ViewModel
- repository interfaces outside `:model`
- repository implementations outside `:database`
- hard-to-follow state updates
- giant screen files
- over-engineered abstraction layers
- accidental circular dependencies
- persistence models leaking into UI

---

## Refactor Output Format
When generating an architecture refactor, provide:
1. Current problem
2. Why it is a problem
3. Proposed improvement
4. Expected benefits
5. Refactored structure or code
6. Risks or migration notes if relevant

---

## What to Avoid
- unnecessary new layers
- rewrite-heavy solutions without reason
- changing behavior unintentionally
- abstracting too early
- violating master project rules
- moving repository interfaces or implementations to the wrong module

---

## Final Rule
Architectural evolution must make the project easier to understand, maintain, and scale.
If a refactor adds complexity without clear benefit, do not do it.
