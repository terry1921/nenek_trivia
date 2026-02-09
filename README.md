# 🧩 Nenek Trivia — Architecture Overview

## 🎯 Objetivo
**Nenek Trivia** es una app móvil de preguntas y respuestas construida con **Kotlin + Jetpack Compose** siguiendo **Clean Architecture modular**.
Su meta es ofrecer una experiencia divertida y moderna de trivia, con un código base escalable, mantenible y preparado para crecimiento futuro.

---

## 📦 Módulos del Proyecto
- **`:app`** → Orquestador principal, navegación, Splash Screen API.
- **`:model`** → Entidades de datos y contratos (interfaces de repositorios).
- **`:database`** → Implementación de Room (DAOs, migraciones, seeds) y DataStore.
- **`:domain`** → Casos de uso (Leaderboard, Sesión, Preferencias) y lógica de negocio.
- **`:network`** → Cliente para servicios externos (Firebase Realtime Database para Leaderboard).
- **`:ui`** → Pantallas (Home, Auth, Profile, Leaderboard, Preferences), ViewModels, componentes Compose y Design System con Tokens.

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
- **UI/UX**: Pantallas de Autenticación, Home, Leaderboard, Perfil y Preferencias construidas con Jetpack Compose y Material 3.
- **Datos**:
    - Room para persistencia local (User, Score, GameSession, Question, Category).
    - DataStore para preferencias de usuario.
    - Firebase Realtime Database integrado para Leaderboard.
- **Dominio**: Casos de uso implementados para gestión de sesión, configuración y leaderboard.
- **Testing**: Tests unitarios iniciales para ViewModels.
- **Navegación**: Jetpack Navigation Compose configurado.
- **Otros**: CI/CD básico con GitHub Actions y pre-commit hooks.

---

## 🚀 Próximos Pasos
- Integración real con Firebase Authentication (actualmente simulada).
- Implementación de la pantalla de Juego (Trivia) y lógica de la partida.
- Ampliar cobertura de tests (Unitarios e Integración/UI).
- Sincronización de progreso de usuario en la nube.

---

🔎 Para más detalles técnicos ver: [`/docs/design-doc.md`](./docs/design-doc.md)
