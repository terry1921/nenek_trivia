---

# 🔍 3. `code-review-kotlin/SKILL.md`

```md
# Skill: Kotlin Code Review (Android + Compose)

## Description
This skill reviews Kotlin code in Nenek Trivia, ensuring architectural consistency, readability, performance, and best practices.

---

## Responsibilities
- Analyze PRs or code snippets
- Detect architectural violations
- Suggest improvements
- Identify risks and anti-patterns

---

## Review Areas

### 1. Architecture
- Are modules respected?
- Is the interface in `:model`?
- Is the implementation in `:database`?
- Is UI separated from data layer?

### 2. Compose
- Any business logic inside composables?
- Proper state handling?
- Recomposition issues?
- Overuse of remember or effects?

### 3. State Management
- Is state immutable?
- Is StateFlow used properly?
- Are loading and error states handled?

### 4. Flow Usage
- Correct Flow operators?
- Missing error handling?
- Cold vs hot flow confusion?

### 5. Readability
- Clear naming?
- Small functions?
- No unnecessary complexity?

### 6. Performance
- Heavy work inside composables?
- Unnecessary recompositions?
- Inefficient data usage?

### 7. Testing
- Is code testable?
- Missing obvious test cases?
- Over-reliance on mocks?

---

## Feedback Style
- Be direct and specific
- Suggest improvements with examples
- Prioritize impact over nitpicks

---

## Common Issues to Flag
- Business logic in UI
- Wrong module placement
- Missing error handling
- Poor naming
- Large classes or functions
- Tight coupling
- Improper Flow usage

---

## Output Format

When reviewing:

### Issues
List problems clearly

### Improvements
Provide actionable suggestions

### Optional Refactor
Show improved code when useful

---

## What to Avoid
- Generic feedback
- Vague comments
- Over-engineering suggestions
