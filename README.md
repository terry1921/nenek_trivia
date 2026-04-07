# 🧩 Nenek Trivia — Architecture Overview

## 🎯 Objetivo
**Nenek Trivia** es una app móvil de preguntas y respuestas construida con **Kotlin + Jetpack Compose** siguiendo **Clean Architecture modular**.
Su meta es ofrecer una experiencia divertida y moderna de trivia, con un código base escalable, mantenible y preparado para crecimiento futuro.

---

## 📦 Módulos del Proyecto
- **`:app`** → Orquestador principal, navegación, Splash Screen API.
- **`:model`** → Entidades de datos y contratos (interfaces de repositorios).
- **`:database`** → Implementación de Room (DAOs, migraciones, seeds) y DataStore.
- **`:domain`** → Casos de uso (Leaderboard, Sesión, Preferencias, Trivia, Sincronización de progreso) y lógica de negocio.
- **`:network`** → Cliente para servicios externos (Firebase Auth, Firebase Realtime Database para Leaderboard y Preguntas).
- **`:ui`** → Pantallas (Home, Auth, Profile, Leaderboard, Preferences, Trivia), ViewModels, componentes Compose reutilizables y Design System con Tokens.

### Diagrama de Dependencias

```
:app -> :ui, :domain, :model
:ui  -> :domain, :model
:domain -> :database, :network, :model
:database -> :model
:network -> :model
```

---

## ✅ Estado Actual
- **Arquitectura**: Clean Architecture + MVVM + Flow + Hilt implementados.
- **UI/UX**:
    - Pantallas completas: Autenticación, Home, Leaderboard, Perfil, Preferencias y Trivia (Juego).
    - Diseño moderno con Material 3 y Jetpack Compose. Componentes refactorizados para máxima reutilización.
    - Pantalla de juego con temporizador, sistema de puntajes y tips educativos.
- **Datos**:
    - **Room** para persistencia local y caché.
    - **DataStore** para preferencias de usuario.
    - **Firebase Realtime Database** integrado para Leaderboard y obtención de preguntas en tiempo real.
    - **Firebase Authentication** implementado con soporte para Google y Facebook.
- **Dominio**: Casos de uso para gestión de sesión, configuración, leaderboard, mecánica de juego y sincronización de progreso en la nube (Honor).
- **Funcionalidades Extra**:
    - Sincronización de progreso de usuario en la nube y actualización en tiempo real de Leaderboard tras cada partida.
    - In-App Review API integrada para valoraciones.
    - Navegación fluida con Jetpack Navigation Compose.
- **Testing**: Amplia cobertura de tests unitarios para Repositorios (locales y remotos), ViewModels y Casos de uso principales.
- **Otros**: CI/CD básico con GitHub Actions y pre-commit hooks.

---

## AI Agent Skills

## Estructura

```text
.skills/
  nenek-trivia/
    SKILL.md
  ui-compose/
    SKILL.md
  feature-flow-room/
    SKILL.md
  code-review-kotlin/
    SKILL.md
  design-system-compose/
    SKILL.md
  testing-turbine-mockk/
    SKILL.md
  architecture-evolution/
    SKILL.md
  trivia-game-mechanics/
    SKILL.md
```

## Qué hace cada una

### `.skills/nenek-trivia/SKILL.md`

**Master skill** del proyecto.
Define reglas globales: arquitectura modular, MVVM, Flow, Hilt, Room, Compose, interfaces en `:model`, implementaciones en `:database`, y uso de Gson para Room converters.

Úsala cuando quieras que el agente tenga el contexto completo del proyecto.

Prompt ejemplo:

```text
@nenek-trivia create a new stats feature for Nenek Trivia following project rules
```

---

### `.skills/ui-compose/SKILL.md`

Skill enfocada en UI con Jetpack Compose.

Cubre:

* separación Route/Content
* composables limpios
* Material 3
* previews
* manejo de loading/error/empty/content
* recomposition safety básica

Úsala cuando trabajes pantallas, componentes o refactors visuales.

Prompt ejemplo:

```text
@ui-compose build a settings screen with loading, error and content states
```

---

### `.skills/feature-flow-room/SKILL.md`

Skill para features conectadas a datos.

Cubre:

* Entity
* DAO
* Repository
* ViewModel
* UiState
* Flow / StateFlow
* mappers
* separación por módulos

Úsala cuando una pantalla necesite leer o persistir datos.

Prompt ejemplo:

```text
@feature-flow-room create a leaderboard feature using Room, Flow and ViewModel
```

---

### `.skills/code-review-kotlin/SKILL.md`

Skill de revisión técnica para Kotlin/Android/Compose.

Cubre:

* violaciones de arquitectura
* problemas de estado
* mal uso de Flow
* recomposition issues
* legibilidad
* testabilidad
* oportunidades de refactor

Úsala para PR reviews, snippets o refactors.

Prompt ejemplo:

```text
@code-review-kotlin review this ViewModel and detect architecture or state issues
```

---

### `.skills/design-system-compose/SKILL.md`

Skill para mantener consistencia visual en Nenek.

Cubre:

* uso de `NenekTheme`
* Material 3
* colores y tipografía desde tokens
* spacing consistente
* extracción de componentes reutilizables
* accesibilidad visual básica

Úsala cuando quieras construir UI alineada al sistema visual del proyecto.

Prompt ejemplo:

```text
@design-system-compose build this profile screen following Nenek theme and reusable component rules
```

---

### `.skills/testing-turbine-mockk/SKILL.md`

Skill para testing con tu stack.

Cubre:

* tests de ViewModel
* Flow con Turbine
* MockK
* estados loading/success/error
* tests claros y deterministas

Úsala para generar o revisar tests.

Prompt ejemplo:

```text
@testing-turbine-mockk generate unit tests for this ViewModel using Turbine and MockK
```

---

### `.skills/architecture-evolution/SKILL.md`

Skill para refactors grandes y evolución arquitectónica.

Cubre:

* dividir responsabilidades
* mejorar límites modulares
* reducir deuda técnica
* refactors incrementales
* preservar comportamiento existente
* evitar sobrearquitectura

Úsala cuando una feature ya creció demasiado o la estructura empieza a doler.

Prompt ejemplo:

```text
@architecture-evolution review this feature and refactor it into a cleaner modular structure
```

---

### `.skills/trivia-game-mechanics/SKILL.md`

Skill para la lógica del juego.

Cubre:

* flujo de preguntas
* score
* streaks
* progreso
* categorías
* resumen final
* ranking
* reglas de juego claras y testeables

Úsala para construir el corazón funcional de Nenek Trivia.

Prompt ejemplo:

```text
@trivia-game-mechanics create a score and streak system for a category-based quiz flow
```

## Cómo combinarlas

Aquí es donde se pone bueno.

### Feature completa

```text
@nenek-trivia @feature-flow-room @ui-compose create a leaderboard feature with loading, empty, error and content states
```

### UI consistente

```text
@ui-compose @design-system-compose build a profile screen using Nenek theme and reusable components
```

### Lógica de juego + tests

```text
@trivia-game-mechanics @testing-turbine-mockk generate score logic and unit tests for streak handling
```

### Refactor serio

```text
@architecture-evolution @code-review-kotlin review this large feature and propose a safe incremental refactor
```

## Orden recomendado para crearlas en el repo

Yo lo haría así:

1. `nenek-trivia`
2. `ui-compose`
3. `feature-flow-room`
4. `code-review-kotlin`
5. `design-system-compose`
6. `testing-turbine-mockk`
7. `architecture-evolution`
8. `trivia-game-mechanics`

Porque así empiezas por la base y luego agregas especialización.

## Siguiente paso más útil

Haz una primera prueba con una feature real, algo como:

```text
@nenek-trivia @feature-flow-room @ui-compose create a weekly leaderboard screen connected to Room with ViewModel and UiState
```

---

## 🚀 Próximos Pasos
- Continuar ampliando la cobertura de tests (Integración/UI).
- Sincronización avanzada de progreso de usuario en la nube (historial de partidas completas).
- Optimización de rendimiento y animaciones.
- Soporte para más categorías y modos de juego.

---

🔎 Para más detalles técnicos ver: [`/docs/design-doc.md`](./docs/design-doc.md)
