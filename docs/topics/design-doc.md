# 📄 Design Doc — Nenek Trivia

**Autor:** Enrique Espinoza (terry1921)

**Fecha:** 04 de septiembre de 2025

**Estado:** Borrador

## 1. Contexto y Objetivo
**Nenek Trivia** es una aplicación móvil de preguntas y respuestas construida con Android Studio, Kotlin y Jetpack
Compose.
El objetivo principal es ofrecer una experiencia de trivia divertida y moderna, usando buenas prácticas de
arquitectura modular y tecnologías actuales para garantizar escalabilidad, mantenibilidad y un diseño limpio.

La motivación del rediseño/reconstrucción es migrar la app existente en la Play Store hacia una nueva base
tecnológica más robusta, siguiendo Clean Architecture y aprovechando Compose para UI, Firebase para autenticación y
base de datos en tiempo real, y Room para persistencia local.

---

## 2. Alcance
### Alcance Actual
- Pantalla Splash con XML y transición a MainActivity (Compose).
- Arquitectura modular configurada.
- Persistencia local con Room (migraciones y seeds).
- Entidades principales creadas.
- Repositorios iniciales definidos.
- Theming con Material 3 y design tokens de colores.

### Alcance Futuro
- Implementar casos de uso (`:domain`).
- UI completa con Compose (pantallas de juego, leaderboard, standings, auth).
- Autenticación con Firebase Auth.
- Sincronización con Firebase Realtime Database.
- Testing: unitarios, integración y UI.
- CI/CD con GitHub Actions y validaciones automáticas.

### Fuera de Alcance Inicial
- Backend propio (se usará Firebase en esta fase).
- Modo offline avanzado (básico ya contemplado con Room).
- Internacionalización completa (en fase inicial solo español).

---

## 3. Arquitectura del Proyecto
El proyecto sigue **Modular Clean Architecture**, separando responsabilidades en distintos módulos:

- **`:app`** → Orquestador principal, navegación, configuración global, SplashActivity.
- **`:model`** → Definición de entidades y contratos (interfaces de repositorios).
- **`:database`** → Implementaciones de persistencia local con Room, migraciones y seeds.
- **`:domain`** → Casos de uso (lógica de negocio).
- **`:network`** → Conexión a Firebase (Auth y Realtime Database).
- **`:ui`** → Componentes Jetpack Compose reutilizables, design system, design tokens.

### Diagrama de Dependencias

```
:app
├─ depends on → :ui
├─ depends on → :domain
└─ depends on → :model

:domain
├─ depends on → :database
├─ depends on → :network
└─ depends on → :model

:database
└─ depends on → :model

:network
└─ depends on → :model

:ui
└─ depends on → :model
```

---

## 4. Decisiones de Diseño
- **Persistencia:** Room con migraciones y seeds, inyectados vía Hilt.
- **Serialización:** Uso de **Gson** en lugar de kotlinx.serialization → mayor compatibilidad con Room converters.
- **Repositorios:**
    - Interfaces en `:model`.
    - Implementaciones en `:database` o `:network`.
- **Theming:** Material 3 + design tokens (colores y tipografía definidos).
- **CI/CD:** GitHub Actions con validaciones básicas; pre-commit hooks para lint y formato.

---

## 5. Estado Actual
- Entidades ya definidas:
    - `Question`
    - `Category`
    - `User`
    - `Score`
    - `GameSession`
- Migraciones iniciales implementadas en Room.
- Seeds configurados en `RoomDatabase.Callback`.
- Repositorios creados (interfaces en `:model`, implementaciones en `:database`).
- Splash Activity (XML con icono + degradado magenta → orange).
- `MainActivity` en Compose.
- Theming inicial con tokens y paleta derivada de la identidad visual.

---

## 6. Próximos Pasos (Roadmap Técnico)
- [ ] Generar interfaz de usuario en Compose.
- [ ] Implementar casos de uso en `:domain`.
- [ ] Conectar UI ↔ ViewModel ↔ Repository usando Flow.
- [ ] Crear pantallas iniciales: Standings, Preguntas, Leaderboard, Auth.
- [ ] Integrar Firebase Auth + Realtime DB.
- [ ] Implementar tests (JUnit5, MockK, Turbine, Robolectric, Compose UI Test).
- [ ] Expandir CI/CD con GitHub Actions para builds y pruebas automáticas.

---

## 7. Riesgos y Consideraciones
- **Firebase Realtime DB** podría limitar ciertas consultas; Firestore puede evaluarse a futuro.
- **Seeds de Room** pueden crecer en complejidad si el dataset inicial es grande.
- **Estados en Compose:** se estandarizó el uso de Flow + `collectAsStateWithLifecycle`.
- **Pruebas unitarias:** la modularidad facilita mockeo, pero requiere disciplina en dependencias.

---

## 8. Referencias
- [Android Developers — Official Documentation](https://developer.android.com)
- [Material Design 3 Guidelines](https://m3.material.io/)
- [Firebase Documentation](https://firebase.google.com/docs)
- Convenciones internas de código (naming, branching strategy).

---
