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

## 🚀 Próximos Pasos
- Continuar ampliando la cobertura de tests (Integración/UI).
- Sincronización avanzada de progreso de usuario en la nube (historial de partidas completas).
- Optimización de rendimiento y animaciones.
- Soporte para más categorías y modos de juego.

---

🔎 Para más detalles técnicos ver: [`/docs/design-doc.md`](./docs/design-doc.md)
