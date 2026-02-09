# 📄 Design Doc — Pantalla Principal (MainScreen) de Nenek Trivia

**Autor:** Enrique Espinoza (terry1921)
**Fecha:** 09 de septiembre de 2025
**Estado:** Borrador

## 1. Contexto y Objetivo
Una vez autenticado, el usuario debe acceder a una **pantalla principal** clara y minimalista que sirva como
**hub de navegación** dentro de Nenek Trivia.
El diseño debe mantener coherencia con el tema **Nenek.Theme** (tokens de color, tipografía y formas) y tener
inspiración visual en la **Huasteca Potosina**: colores vivos, naturaleza, formas orgánicas, pero sin recargar
la UI.

El objetivo es:
- Ofrecer accesos rápidos a las funciones clave de la app.
- Mantener un diseño minimalista y accesible.
- Sentar las bases para futuras expansiones (notificaciones, eventos, etc.).

---

## 2. Alcance
### Alcance Actual
- Pantalla principal (`MainScreen`) implementada en **Jetpack Compose**.
- Cuatro accesos clave en forma de **barra de navegación**:
    - 🏠 **Inicio**
    - 🧑‍💻 **Mi Perfil**
    - 🏆 **Tabla de posiciones**
    - 🚪 **Opciones**
- Además de en pantalla **Inicio** con un background alegorico de la huasteca junto con la muñeca huasteca, se
encontrará el boton
  - 🎮 **Jugar Trivia**

- Diseño con **Material 3** + tokens personalizados de `NenekTheme`.

### Fuera de Alcance Inicial
- Lógica interna de cada sección (perfil, leaderboard, gameplay, opciones).
- Animaciones avanzadas o transiciones personalizadas.
- Integración real de cierre de sesión (Firebase Auth se integrará en otra fase).

---

## 3. Arquitectura de Pantalla
- **UI (Compose):**
    - `MainScreen` → Contenedor principal con NavHost.
    - `MainBottomBar` → Barra de navegación inferior reutilizable (ícono + texto).
    - `HomeContent` → Contenido de la pestaña “Inicio” (background huasteco + muñeca + botón “Jugar Trivia”).

- **ViewModel (MainViewModel):**
    - Manejar estado de usuario (ej. nombre, avatar).
    - Exponer eventos de navegación (`onProfileClick`, `onLeaderboardClick`, `onOptionsClick`, `onPlayClick`).

- **Navegación:**
    - `AuthScreen` → `MainScreen`.
    - `MainScreen` contiene `NavHost` con rutas: `home`, `profile`, `leaderboard`, `options`.

---

## 4. UI Mock / Wireframe

```
+—————————————–———————————–——————————————————–+
|                 Nenek Trivia                |
|                                             |
|       [ Background Huasteco + muñeca ]      |
|                                             |
|                                             |
|             [ 🎮 Jugar Trivia ]             |
|                                             |
+————————————————————————————————————————————+
| 🏠Inicio | 🧑‍💻Perfil | 🏆Tabla | ⚙️Opciones |
+———————————–———————————–————————————————————+
```

Estilo: minimalista, centrado, barra de navegación abajo, íconos representativos.

---

## 5. Ejemplo de Código Inicial (Compose)

```kotlin
@Composable
fun MainScreen(
    onProfileClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onPlayClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nenek Trivia",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            MenuButton("Mi Perfil", Icons.Default.Person, onProfileClick)
            Spacer(modifier = Modifier.height(16.dp))

            MenuButton("Tabla de posiciones", Icons.Default.Star, onLeaderboardClick)
            Spacer(modifier = Modifier.height(16.dp))

            MenuButton("Jugar Trivia", Icons.Default.PlayArrow, onPlayClick)
            Spacer(modifier = Modifier.height(16.dp))

            MenuButton("Cerrar sesión", Icons.Default.ExitToApp, onLogoutClick)
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}
```

---

## 6. Roadmap Técnico

    1.	Implementar MainScreen con Compose + Material 3.
    2.	Definir MenuButton como componente reutilizable.
    3.	Integrar MainViewModel para manejar datos básicos del usuario.
    4.	Conectar navegación desde AuthScreen → MainScreen.
    5.	Dejar onLogoutClick preparado para integración futura con Firebase.

---

## 7. Riesgos y Consideraciones

- El estilo minimalista debe balancearse con la identidad huasteca, evitando saturar la pantalla.
- La botonera debe cumplir con accesibilidad (tamaño mínimo táctil, contraste de colores).
- El flujo de “Cerrar sesión” no estará funcional hasta integrar Firebase.

___

## 8. Referencias

- [Material 3 – Buttons](https://m3.material.io/components/buttons/overview)
- [Material 3 – Navigation](https://m3.material.io/components/navigation/overview)
- [Lineamientos de accesibilidad Android](https://developer.android.com/guide/topics/ui/accessibility)

___
