# Skill: Design System Compose (Nenek)

## Description
This skill enforces the Nenek Trivia design system using Jetpack Compose and Material 3.
It ensures visual consistency, reuse of components, and alignment with design tokens.

---

## Responsibilities
- Build UI using design tokens
- Maintain visual consistency across screens
- Create reusable components
- Apply theming correctly

---

## Design Philosophy
- Clean, minimal, expressive UI
- Inspired by Huasteca aesthetics: vibrant and organic
- Avoid visual noise
- Focus on readability and hierarchy

---

## Theme Rules

### Always use:
- `NenekTheme`
- Material 3

### Colors
- Use predefined palette ONLY
- Do NOT hardcode colors
- Prefer `MaterialTheme.colorScheme`


Example:
```kotlin
MaterialTheme.colorScheme.primary
```

⸻

### Typography
Use design tokens:
- Display
- Headline
- Title
- Body
- Label

Do NOT define inline TextStyles unless necessary

### Spacing
- Use consistent spacing values
- Avoid random dp values
- Prefer spacing tokens if available

---

## Component Rules

### Reusability
- Extract reusable components
- Avoid duplicating UI patterns

### Typical components
- CardItem
- Avatar
- ProgressBar
- StatRow
- ListItem

---

## Screen Layout
- Use proper padding and spacing
- Avoid clutter
- Maintain hierarchy:
  - Header
  - Content
  - Actions

---

## State Design
UI must reflect:
- Loading with progress indicator
- Empty with friendly message
- Error with clear feedback
- Content as main UI

---

## Accessibility
- Proper text contrast
- Readable font sizes
- Avoid relying only on color

---

## What to Avoid
- Hardcoded colors
- Inline styles everywhere
- Inconsistent spacing
- Duplicated components

---

## Output Expectations

When generating UI:
- Use Material 3 + NenekTheme
- Follow spacing and typography rules
- Extract reusable components
- Maintain visual consistency
