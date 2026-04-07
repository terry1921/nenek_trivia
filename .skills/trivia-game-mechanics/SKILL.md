# Skill: Trivia Game Mechanics (Nenek Trivia)

## Description
This skill defines and implements game-related logic for Nenek Trivia, including question flow, scoring, progress, streaks, categories, rounds, and player feedback.

---

## Responsibilities
- Design and implement trivia gameplay logic
- Define question progression rules
- Handle scoring and streak systems
- Manage category-based play
- Support ranking, progress, and feedback mechanics
- Keep gameplay logic separate from UI rendering

---

## Game Logic Philosophy
Game logic must be:
- clear
- predictable
- fair
- easy to extend
- independent from UI

The UI should display game state, not own the game rules.

---

## Core Gameplay Principles
Always prioritize:

1. **Player clarity**
   - The player should understand what is happening.
   - Scores, progress, and outcomes must feel intuitive.

2. **Fairness**
   - Rules must be consistent.
   - Correct answers, penalties, and bonuses must behave predictably.

3. **Responsiveness**
   - Gameplay state changes should feel immediate.

4. **Scalability**
   - Rules should support future features such as streaks, timers, achievements, or difficulty modes.

---

## Game Logic Placement Rules
- Gameplay rules should NOT live inside composables
- Heavy gameplay logic should NOT live directly in UI event handlers
- ViewModels may coordinate game state
- Domain or game-rule logic should be structured cleanly and extracted when needed
- Persistent game progress should flow through repositories

---

## Common Gameplay Concepts
This skill may implement or reason about:
- question order
- category selection
- answer validation
- score updates
- streak bonuses
- combo systems
- lives or attempts
- round completion
- difficulty progression
- leaderboard contribution
- achievements or milestones
- session summary

---

## Question Flow Rules
Question progression should be explicit.

Valid approaches:
- sequential order
- shuffled order
- category-based order
- difficulty-based progression

Rules:
- the next question must be derived predictably from current game state
- avoid hidden logic
- support resume or retry flows when needed

---

## Scoring Rules
Scoring logic must be centralized and easy to inspect.

Possible score inputs:
- correct answer
- incorrect answer
- answer streak
- difficulty multiplier
- speed bonus
- completion bonus

Rules:
- define scoring inputs explicitly
- avoid hardcoded score updates scattered across UI code
- keep scoring consistent across the app

---

## Streak / Combo Rules
If a streak system exists:
- increment only on valid consecutive correct answers
- reset clearly on failure if that is the rule
- bonuses must be deterministic
- streak state must be represented explicitly in game state

---

## Game State Rules
Game state should clearly represent:
- current question
- current score
- selected answer if applicable
- question index or progress
- remaining questions
- streak or combo
- loading state if data is async
- completion state
- error state when needed

Prefer:
- data classes for structured state
- sealed state when mutually exclusive stages are clearer

---

## Feedback Rules
Player feedback should be immediate and understandable.

Examples:
- correct or incorrect result
- score gained
- streak increased
- category completed
- game finished

Rules:
- feedback must reflect actual game state
- do not hide important state transitions

---

## Category Rules
If categories are used:
- category identity must be explicit
- category progress should be traceable
- category filtering or selection should not be ambiguous

Possible examples:
- History
- Geography
- Sports
- Arts
- General Knowledge

---

## Persistence Rules
If progress is saved:
- use repositories
- avoid saving directly from composables
- persist only the data that matters
- keep in-memory session state and persisted progress clearly separated

Possible persisted data:
- high scores
- unlocked categories
- best streak
- completed rounds
- player progress

---

## Ranking / Leaderboard Rules
If the feature impacts rankings:
- score submission must be explicit
- ranking logic should be deterministic
- avoid mixing leaderboard formatting with score calculation logic

---

## Difficulty Rules
If difficulty exists:
- define what changes with difficulty
- examples:
  - harder questions
  - lower time limit
  - different score multiplier
- do not make difficulty implicit

---

## Error Handling Rules
Gameplay must handle:
- missing question data
- invalid answer state
- repository failures
- async loading issues

Rules:
- expose understandable UI states
- do not crash on malformed or missing data
- fail clearly and recover gracefully when possible

---

## Testing Expectations
Gameplay logic should be testable.

Prioritize tests for:
- score calculation
- answer validation
- streak handling
- question progression
- round completion
- edge cases

Examples of edge cases:
- empty question list
- repeated answer submission
- invalid question index
- end-of-game transitions

---

## Agent Behavior Rules
When generating gameplay code:
- keep logic out of composables
- centralize scoring and progression rules
- model game state clearly
- preserve modular architecture
- prefer predictable state transitions
- avoid mixing rendering logic with game rules

---

## Output Expectations
When implementing a game feature, provide:
- clear state model
- scoring and progression rules
- ViewModel coordination if needed
- repository usage when persistence is required
- extension points for future gameplay evolution

---

## What to Avoid
- scoring logic spread across multiple UI files
- answer validation inside composables
- hidden rule changes
- fragile state transitions
- tightly coupling game rules to specific screens

---

## Final Rule
Gameplay systems must be fun for the player, but also maintainable for the codebase.
If the logic is hard to reason about, simplify it.
