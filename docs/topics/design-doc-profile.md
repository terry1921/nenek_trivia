# 📄 Design Doc — Pantalla de Perfil (Nenek Trivia)

**Autor:** Enrique Espinoza (terry1921)
**Fecha:** 18 de septiembre de 2025
**Estado:** Borrador

## 1) Contexto y objetivo
La pantalla de **Perfil** muestra de forma simple la información principal del usuario y su “barra de conocimientos” por categoría, como un vistazo rápido a su progreso en Nenek Trivia.

Objetivos:
- Presentar identidad del usuario (avatar + nombre o correo).
- Visualizar nivel de conocimiento por categoría (1–100) basado en partidas jugadas.
- Mantener diseño **minimalista**, respetando **Nenek.Theme** (tokens de color/typografía/formas).

---

## 2) Alcance
### Alcance actual
- Avatar centrado en la parte superior.
- Debajo: nombre **o** correo (si no hay nombre).
- Debajo: **barras de conocimiento** para categorías fijas:
    - Arte, Deportes, General, Geografía, Historia (1–100).
- Implementación UI en **Jetpack Compose (Material 3)**.
- Estructura base de `ViewModel` + `UiState` (datos simulados por ahora).

### Fuera de alcance inicial
- Edición de perfil (cambiar foto/nombre).
- Cálculo real de porcentajes desde base de datos (se integrará más adelante).
- Integración con Firebase/Network.
- Métricas avanzadas (racha, logros, medallas).

---

## 3) Arquitectura y estados
- **Módulos**:
    - `:ui` → Composables de Perfil.
    - `:model` → Entidades/contratos (interfaces repos).
    - `:database` → Implementaciones repos (a futuro).
- **ViewModel**: `ProfileViewModel` (Hilt).
- **Estado UI**:
  ```kotlin
  data class ProfileUiState(
      val isLoading: Boolean = true,
      val displayName: String? = null,
      val email: String? = null,
      val avatarUrl: String? = null,
      val knowledge: Map<Category, Int> = emptyMap(), // 1..100
      val error: String? = null
  )
  enum class Category { Arte, Deportes, General, Geografía, Historia }
  ```
- **Flujos**: `Flow<ProfileUiState>` recolectado con collectAsStateWithLifecycle().

> Nota de arquitectura (alineada a tus preferencias): interfaces de repos en :model; implementaciones en :database
> (Room + Gson). El cálculo de conocimiento vendrá de Score/GameSession agregados.

---

## 4) Datos y reglas (versión inicial)

- displayName si existe; si no, email.
- `knowledge[cat]` ∈ **1..100** (entero).
- Si no hay datos, mostrar placeholder `(0 → “Sin datos”)`, pero no bajar de 1 si decides siempre mostrar algo.
- Cálculo (futuro, referencial):
  ```kotlin
  knowledge = clamp( round( correctAnswersInCategory / totalAnswersInCategory * 100 ), 1, 100 )
  ```
- Categorías son **fijas** por ahora (podrán ampliarse)

---

## 5) UI/ Wireframe


```console
+--------------------------------------------------+
|                (Avatar redondo)                  |
|               [  96–120 dp  ]                    |
|                                                  |
|                 Nombre o Correo                  |
|                                                  |
|   Arte        [██████████------]   68%           |
|   Deportes    [██████----------]   37%           |
|   General     [█████████████---]   82%           |
|   Geografía   [████████--------]   54%           |
|   Historia    [███████---------]   41%           |
+--------------------------------------------------+
```

Estilo minimalista, foco en legibilidad, uso de tokens **NenekTheme**:

- `colorScheme.primary` para acentos (avatar borde / títulos)
- Barras con `primary` o `tertiary` (según contraste de tu paleta)
- Tipografía de `TypographyTokens` (title/headline/body/label)

---

## 6) Componentes (Compose) — esqueleto

> Nota: íconos e imágenes son placeholders; integra tu loader de imágenes preferido (Coil).

```kotlin
@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        when {
            state.isLoading -> Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            state.error != null -> ErrorContent(
                message = state.error,
                onRetry = onRetry,
                modifier = Modifier.padding(padding)
            )

            else -> ProfileContent(
                state = state,
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Avatar(
            url = state.avatarUrl,
            size = 104.dp,
            contentDescription = "Foto de perfil"
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = state.displayName ?: state.email.orEmpty(),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        KnowledgeSection(knowledge = state.knowledge)
    }
}

@Composable
private fun KnowledgeSection(knowledge: Map<Category, Int>) {
    Column(Modifier.fillMaxWidth()) {
        knowledge.forEach { (category, value) ->
            KnowledgeBar(
                label = category.name,
                percent = value.coerceIn(1, 100)
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun KnowledgeBar(label: String, percent: Int) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(text = "$percent%", style = MaterialTheme.typography.labelMedium)
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = percent / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(999.dp))
        )
    }
}

@Composable
private fun Avatar(url: String?, size: Dp, contentDescription: String?) {
    // Placeholder minimalista; reemplazar con Coil AsyncImage
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = contentDescription,
            modifier = Modifier.size(size * 0.5f),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}
```

---

## 7) Accesibilidad

- Avatar con `contentDescription`.
- Contraste suficiente en barras/labels (validar con tu paleta).
- Área táctil mínima **48×48 dp** si añades acciones futuras.
- Texto escalable (no fijar tamaños absolutos fuera de tokens).

⸻

## 8) Navegación

- Ruta: profile dentro del NavHost de `MainScreen`.
- Entrada desde **Bottom Bar** (Perfil).
- Back navega al estado anterior en Main.

⸻

## 9) Roadmap (futuro)

- Conectar con repositorios (User, Score, GameSession) para calcular porcentajes.
- Cambiar avatar/nombre; botón “Editar perfil”.
- Añadir logros/insignias y racha.
- Agregar **placeholder** para “sin datos” (por ejemplo, barras grises con 0%).
- Tests de UI (Compose) + unit tests para cálculo de conocimiento.

⸻

## 10) Riesgos y consideraciones

- Datos incompletos pueden sesgar porcentajes (ej. pocas muestras). → Definir umbral mínimo.
- Posible desbalance visual en modos **dark/light** → verificar tokens.
- Localización futura (nombres de categorías) → considerar string resources.

⸻

## 11) Referencias

- [Material 3 (Progress Indicators, Typography, Color).](https://m3.material.io/components/progress-indicators/overview)
- [Guías internas de **Nenek.Theme** y tokens.](https://github.com/terry1921/nenek_trivia/blob/main/docs/topics/design-doc.md)
- [Navigation Compose (para integración con profile).](https://developer.android.com/develop/ui/compose/navigation)
