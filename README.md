# 🧩 Nenek Trivia — Architecture Overview

## 🎯 Objetivo
**Nenek Trivia** es una app móvil de preguntas y respuestas construida con **Kotlin + Jetpack Compose** siguiendo **Clean Architecture modular**.
Su meta es ofrecer una experiencia divertida y moderna de trivia, con un código base escalable, mantenible y preparado para crecimiento futuro.

---

## 📦 Módulos del Proyecto
- **`:app`** → Orquestador principal, navegación, Splash.
- **`:model`** → Entidades y contratos (interfaces de repositorios).
- **`:database`** → Room (DAOs, migraciones, seeds).
- **`:domain`** → Casos de uso (lógica de negocio).
- **`:network`** → Conexión a Firebase (Auth + Realtime DB).
- **`:ui`** → Componentes Compose, design system y tokens.

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
- Arquitectura modular configurada.
- Entidades principales definidas (`Question`, `Category`, `User`, `Score`, `GameSession`).
- Room con migraciones + seeds iniciales.
- Repositorios creados (interfaces en `:model`, implementación en `:database`).
- Theming con Material 3 + design tokens.
- Pantalla Splash (XML) + `MainActivity` (Compose).
- CI/CD básico con GitHub Actions y pre-commit hooks.

---

## 🚀 Próximos Pasos
- Conectar UI ↔ ViewModel ↔ Repository con Flow.
- Implementar casos de uso en `:domain`.
- Pantallas iniciales: Standings, Preguntas, Leaderboard, Auth.
- Integración Firebase Auth + Realtime DB.
- Tests unitarios e integración (JUnit5, MockK, Compose UI Test).

---

🔎 Para más detalles técnicos ver: [`/docs/design-doc.md`](./docs/design-doc.md)
