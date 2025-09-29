# 📄 Design Doc — Pantalla de Preferencias (SettingsScreen)

**Autor:** Gemini (UX Expert)
**Fecha:** 29 de septiembre de 2025
**Estado:** Borrador

## 1. Contexto y Objetivo

La pantalla de **Preferencias** es un centro de control para que el usuario personalice su experiencia en
**Nenek Trivia.**
El objetivo es ofrecer opciones claras y accesibles para ajustar la jugabilidad, la apariencia y el audio, mejorando
la satisfacción y la retención del usuario al darle control sobre la aplicación.

Esta primera versión se centrará en construir la **UI con Jetpack Compose** y la persistencia local de las
configuraciones usando datos estáticos y **Jetpack DataStore.**

---

## 2. Alcance

### Alcance Actual

- Pantalla `SettingsScreen` en Jetpack Compose con un `LazyColumn`.
- Secciones agrupadas por categorías (Jugabilidad, Sonido y Apariencia, etc.).
- Componentes reutilizables para cada tipo de preferencia:
  - `SwitchRow` para opciones booleanas (on/off).
  - `NavigationRow` para opciones que abren un diálogo o navegan a otra pantalla.
- Persistencia local de las preferencias usando Jetpack DataStore.

### Fuera de Alcance Inicial

- Sincronización de preferencias con Firebase.
- Gestión de cuenta avanzada (cambio de contraseña, eliminación de cuenta).
- Internacionalización del contenido de la pantalla.

---

## 3. Arquitectura de Pantalla

### UI (Compose):

- `SettingsScreen` → Contenedor principal que observa el `ViewModel`.
- `SettingsSectionHeader` → Título para cada grupo de preferencias.
- `SwitchRow` → Fila con un título, descripción opcional y un `Switch`.
- `NavigationRow` → Fila con texto que indica una acción o navegación.

### ViewModel (SettingsViewModel):

Expone un estado `SettingsUiState` con los valores actuales de las preferencias.
Contiene funciones para actualizar cada preferencia (ej. `onThemeChanged`, `onMusicToggle`).

### Estados de UI:

```kotlin
data class SettingsUiState(
    val isLoading: Boolean = false,
    val isMusicEnabled: Boolean = true,         // Corresponde a "Música de fondo"
    val isHapticsEnabled: Boolean = true,       // Corresponde a "Vibración al responder"
    val selectedTheme: Theme = Theme.SYSTEM,    // Corresponde a "Predeterminado del sistema"
    val appVersion: String = "1.0.0",           // Corresponde a "Versión 1.0.0"
    val error: String? = null
)

enum class Theme(val displayName: String) {
    LIGHT("Claro"),
    DARK("Oscuro"),
    SYSTEM("Predeterminado del sistema")
}
```

### UI Mock / Wireframe

```
+--------------------------------------------------+
|                   Preferencias                   |
|                                                  |
|  SONIDO Y APARIENCIA                             |
|  -------------------                             |
|  Música de fondo                    [Switch ON]  |
|  Vibración al responder             [Switch ON]  |
|  Predeterminado del sistema                   >  |
|                                                  |
|  CUENTA                                          |
|  -------------------                             |
|  Cerrar sesión                                   |
|                                                  |
|  ACERCA DE NENEK TRIVIA                          |
|  -------------------                             |
|  Calificar en Playstore                          |
|  Politica de Privacidad                          |
|  Versión 1.0.0                                   |
|                                                  |
+--------------------------------------------------+
```

### Estructura General

La pantalla será una `LazyColumn` que contendrá items para cada opción, agrupados bajo encabezados para mejorar la
legibilidad. El diseño seguirá las guías de **Material 3** y los *design tokens* ya definidos en el módulo `:ui`.

### Desglose de Componentes por Sección

1.  **Sonido y Apariencia**
    -   `SettingsHeader(title = "Sonido y Apariencia")`
    -   `SwitchRow(title = "Música de fondo", isChecked = ...)`
    -   `SwitchRow(title = "Vibración al responder", isChecked = ...)`
    -   `NavigationRow(title = "Tema", currentChoice = "Oscuro")` -> Abre un `AlertDialog` con opciones
    (Claro, Oscuro, Sistema).

2.  **Cuenta y Notificaciones**
    -   `SettingsHeader(title = "Cuenta")`
    -   `ClickableRow(title = "Cerrar Sesión", onClick = ...)` -> Ejecuta la lógica de logout.

3.  **Ayuda y Acerca De**
    -   `SettingsHeader(title = "Acerca de Nenek Trivia")`
    -   `ClickableRow(title = "Calificar en Play Store", onClick = ...)`
    -   `ClickableRow(title = "Política de Privacidad", onClick = ...)`
    -   `InfoRow(title = "Versión", value = "1.0.0")`

### Ejemplo de Código

```Kotlin
@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onMusicToggle: (Boolean) -> Unit,
    onHapticsToggle: (Boolean) -> Unit,
    onChangeThemeClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    onRateAppClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            Box(contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // --- Sección Sonido y Apariencia ---
                item { SettingsSectionHeader("SONIDO Y APARIENCIA") }
                item {
                    SwitchRow(
                        title = "Música de fondo",
                        isChecked = state.isMusicEnabled,
                        onCheckedChange = onMusicToggle
                    )
                }
                item {
                    SwitchRow(
                        title = "Vibración al responder",
                        isChecked = state.isHapticsEnabled,
                        onCheckedChange = onHapticsToggle
                    )
                }
                item {
                    NavigationRow(
                        title = "Tema de la aplicación",
                        currentValue = state.selectedTheme.displayName,
                        onClick = onChangeThemeClicked
                    )
                }

                item { Spacer(Modifier.height(16.dp)) }

                // --- Sección Cuenta ---
                item { SettingsSectionHeader("CUENTA") }
                item {
                    ActionRow(
                        title = "Cerrar sesión",
                        onClick = onLogoutClicked
                    )
                }

                item { Spacer(Modifier.height(16.dp)) }

                // --- Sección Acerca de Nenek Trivia ---
                item { SettingsSectionHeader("ACERCA DE NENEK TRIVIA") }
                item {
                    ActionRow(
                        title = "Calificar en Play Store",
                        onClick = onRateAppClicked
                    )
                }
                item {
                    ActionRow(
                        title = "Política de Privacidad",
                        onClick = onPrivacyPolicyClicked
                    )
                }
                item {
                    InfoRow(
                        title = "Versión",
                        value = state.appVersion
                    )
                }
            }
        }
    }
}

// --- Componentes Reutilizables (Helpers) ---

@Composable
private fun SettingsSectionHeader(title: String) { /* ... sin cambios ... */ }

@Composable
private fun SwitchRow(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) { /* ... sin cambios, versión simplificada ... */ }

@Composable
private fun NavigationRow(
    title: String,
    currentValue: String,
    onClick: () -> Unit
) { /* ... sin cambios ... */ }

/**
 * Nueva fila para acciones simples que no tienen un estado visible (switch/valor).
 */
@Composable
private fun ActionRow(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
    }
}

/**
 * Nueva fila para mostrar información estática.
 */
@Composable
private fun InfoRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
```

### Flujo de Navegación

-   El usuario accederá a `SettingsScreen` desde un ícono de engranaje (⚙️) en la pantalla principal o el perfil de
usuario.
-   La pantalla es un destino único en el grafo de navegación.
-   Ciertas opciones (como "Categorías favoritas") navegarán a una pantalla secundaria y luego regresarán.

---

## 4. Arquitectura e Implementación Técnica

Esta funcionalidad impactará varios módulos, siguiendo los principios de **Modular Clean Architecture**.

### Módulos Afectados

-   **:ui**:
    -   `SettingsScreen.kt`: Contendrá el Composable principal.
    -   `SettingsViewModel.kt`: Gestionará el estado de la UI y la lógica de interacción. Expondrá un
    `StateFlow<SettingsUiState>`.
    -   Se crearán componentes reutilizables como `SwitchRow`, `NavigationRow`, etc.

-   **:domain**:
    -   `GetUserSettingsUseCase.kt`: Caso de uso para obtener las preferencias del usuario.
    -   `SaveThemeUseCase.kt`: Caso de uso para guardar la preferencia de tema.
    -   `SaveSoundPreferencesUseCase.kt`: Caso de uso para guardar las preferencias de sonido.
    -   Estos casos de uso interactuarán con los repositorios correspondientes.

-   **:database**:
    -   Se creará un **Repositorio para DataStore** (`PreferencesRepository`) para gestionar los ajustes simples
    (tema, sonido, vibración). La implementación vivirá aquí.
    -   El `UserRepository` existente podría expandirse para manejar la selección de categorías favoritas, que se
    almacenarían en una tabla de relación en Room.

-   **:model**:
    -   `interface PreferencesRepository`: Se definirá aquí.
    -   `data class UserSettings`: Podría modelar el conjunto de preferencias recuperadas.

### Gestión de Estado

Se seguirá el patrón establecido: el `SettingsViewModel` obtendrá los datos de los casos de uso (`:domain`), los
transformará en un `SettingsUiState` y lo expondrá a la UI mediante un `StateFlow`. La UI recolectará este flujo con `collectAsStateWithLifecycle`.

```kotlin
// en :ui/settings/SettingsViewModel.kt
data class SettingsUiState(
    val isHapticFeedbackEnabled: Boolean = true,
    val isMusicEnabled: Boolean = true,
    val selectedTheme: Theme = Theme.SYSTEM
)
```

### Persistencia de Datos

-   **DataStore Preferences:** Ideal para pares clave-valor no relacionales y simples. Se usará para:
    -   `theme_preference` (String: "light", "dark", "system")
    -   `music_enabled` (Boolean)
    -   `haptic_feedback_enabled` (Boolean)
-   **Room:** Para datos más estructurados y relacionados con el usuario. Se usará para:
    -   Persistir la lista de IDs de categorías favoritas del usuario.

---

## 5. Próximos Pasos (Roadmap Técnico)

-   [ ] **Model:** Definir `PreferencesRepository` y el modelo `UserSettings`.
-   [ ] **Database:** Implementar `PreferencesRepository` usando Jetpack DataStore para la persistencia local.
-   [ ] **Domain:** Crear los casos de uso necesarios (`Get`, `Save`).
-   [ ] **DI (Hilt):** Proveer las dependencias del nuevo repositorio y casos de uso.
-   [ ] **UI:** Construir el `SettingsViewModel` y el `SettingsUiState` para orquestar la lógica.
-   [ ] **UI:** Conectar el `ViewModel` con el repositorio a través de Casos de Uso (`:domain`).
-   [ ] **UI:** Desarrollar los Composables de la `SettingsScreen` y sus componentes `Composable` reutilizables.
-   [ ] **UI:** Implementar el diálogo de selección de tema (`AlertDialog`).
-   [ ] **Testing:** Añadir pruebas unitarias para el `ViewModel`, los casos de uso y de UI para la pantalla.

---

## 6. Riesgos y Consideraciones

-   **Valores por Defecto:** Es crucial definir valores por defecto sensatos para todas las preferencias, de modo que
la experiencia del usuario sea óptima en la primera apertura, antes de que visite esta pantalla.
-   **Complejidad del Estado:** La pantalla tendrá múltiples estados (toggles, selecciones). Se debe gestionar
cuidadosamente en el `SettingsUiState` para evitar inconsistencias.
-   **Migraciones:** Si en el futuro se añaden más preferencias a DataStore o Room, se deberá gestionar la migración
de datos para no afectar a los usuarios existentes.
-   **Consistencia del estado**: Asegurarse de que la UI siempre refleje el estado real guardado en DataStore para evitar
discrepancias.
-   **Valores por defecto**: Es clave definir valores por defecto apropiados para una buena experiencia de primer uso.
-   **Impacto en el rendimiento**: Las configuraciones (como desactivar animaciones en el futuro) deben aplicarse
correctamente en toda la app.
-   **Complejidad creciente**: A medida que se añadan más opciones, se debe mantener la pantalla organizada para no abrumar
al usuario.

---

## 7. Referencias

-   [Jetpack DataStore (Preferences)](https://developer.android.com/topic/libraries/architecture/datastore)
-   [Material 3 - Switch](https://m3.material.io/components/switch/overview)
-   [State and Jetpack Compose](https://developer.android.com/jetpack/compose/state)
-   [Documentación interna de NenekTheme](https://github.com/terry1921/nenek_trivia/blob/main/docs/topics/design-doc.md)
