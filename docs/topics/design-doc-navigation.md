# 📄 Design Doc — Navegación General (NavGraph) en Nenek Trivia

## 1. Contexto y Objetivo
La aplicación **Nenek Trivia** requiere un sistema de navegación centralizado, claro y extensible que conecte todas
las pantallas principales de la app.
El objetivo es definir un **NavGraph completo** usando **Navigation Compose**, alineado con la arquitectura modular y
respetando el tema visual **Nenek.Theme**.

Este documento describe:
- El flujo principal de la app (desde `AuthScreen` hasta `MainScreen` y sus secciones).
- La estructura de rutas y pantallas.
- Consideraciones de escalabilidad futura.

---

## 2. Alcance
### Alcance Actual
- Implementar un **NavHost principal** en `:app`.
- Definir rutas iniciales:
    - `auth`
    - `main` (con subpantallas: `home`, `profile`, `leaderboard`, `options`)

### Fuera de Alcance Inicial
- Flujos secundarios (ej. detalle de preguntas, configuración avanzada).
- Animaciones personalizadas entre pantallas.
- Deep links y notificaciones push.

---

## 3. Arquitectura de Navegación
La navegación se implementará con **Navigation Compose** en un `NavHost`.

- **StartDestination:** `AuthScreen`
- **Rutas Principales:**
    - `auth` → Pantalla inicial de autenticación (social buttons).
    - `main` → Contenedor principal con `BottomNavigation`.

- **Subrutas de MainScreen:**
    - `home` → Pantalla de inicio (background huasteco + muñeca + botón "Jugar Trivia").
    - `profile` → Información del usuario.
    - `leaderboard` → Tabla de posiciones.
    - `options` → Opciones (incluye cierre de sesión futuro).

---

## 4. Diagrama de Flujo

```
[AuthScreen] --> (Si autenticado automáticamente)
     |
     v
[MainScreen] <—————————————+ (Pantalla principal)
     |
     +-> [HomeScreen] → Botón “Jugar Trivia”
     |
     +-> [ProfileScreen]
     |
     +-> [LeaderboardScreen]
     |
     +-> [OptionsScreen]
                |
                +-> [Cerrar sesión] -> [AuthScreen]
                |
                +-> [opciones futuras]
```

---

## 5. Definición de Rutas
Se definirán en un objeto `Routes` para evitar strings hardcodeados.

```kotlin
object Routes {
    const val AUTH = "auth"
    const val MAIN = "main"

    object Main {
        const val HOME = "home"
        const val PROFILE = "profile"
        const val LEADERBOARD = "leaderboard"
        const val OPTIONS = "options"
    }
}
```

---

## 6. Ejemplo de NavHost

```kotlin
@Composable
fun NenekNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.AUTH
    ) {
        composable(Routes.AUTH) {
            AuthScreen(
                onLoginSuccess = { navController.navigate(Routes.MAIN) }
            )
        }
        navigation(
            startDestination = Routes.Main.HOME,
            route = Routes.MAIN
        ) {
            composable(Routes.Main.HOME) { HomeScreen(/* ... */) }
            composable(Routes.Main.PROFILE) { ProfileScreen(/* ... */) }
            composable(Routes.Main.LEADERBOARD) { LeaderboardScreen(/* ... */) }
            composable(Routes.Main.OPTIONS) { OptionsScreen(/* ... */) }
        }
    }
}
```

## 7. Roadmap Técnico

    1.	Definir objeto Routes con constantes de navegación.
    2.	Implementar NavHost principal en :app.
    3.	Crear SplashScreen con redirección a AuthScreen.
    4.	Implementar AuthScreen con callback a MainScreen.
    5.	Crear estructura de MainScreen con BottomNavigation + NavHost interno.
    6.	Conectar subpantallas (Home, Profile, Leaderboard, Options).
    7.	Preparar navegación de “Cerrar sesión” hacia AuthScreen.

⸻

## 8. Riesgos y Consideraciones
-
- Back stack: se debe limpiar al navegar de AuthScreen a MainScreen para evitar volver atrás al splash/login.
- Opciones futuras: se puede añadir un SettingsScreen, GameDetailScreen o AchievementsScreen.
- Accesibilidad: navegación debe cumplir con TalkBack/ScreenReaders.
- Deep links: a considerar en una fase posterior.

⸻

## 9. Referencias

- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- [Material 3 Navigation Patterns](https://m3.material.io/components/navigation/overview)
- [Compose Samples – Now in Android](https://github.com/android/nowinandroid)
