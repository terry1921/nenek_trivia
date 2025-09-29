# 📄 Design Doc — Pantalla de Tabla de Posiciones (Leaderboard) en Nenek Trivia

**Autor:** Enrique Espinoza (terry1921)
**Fecha:** 22 de septiembre de 2025
**Estado:** Borrador

## 1. Contexto y Objetivo
La pantalla de **Leaderboard** mostrará una lista con los puntajes de los jugadores ordenados por posición.
El objetivo es dar visibilidad al desempeño de los usuarios y fomentar la competitividad en el juego.

En esta primera versión, solo se trabajará en el **diseño de la pantalla** usando datos estáticos o proporcionados,
respetando **Nenek.Theme** y las guías de **Material 3**.
La integración con **Firebase Database** y la caché local de 12 horas se implementará en fases posteriores.

---

## 2. Alcance
### Alcance Actual
- Pantalla `LeaderboardScreen` en **Jetpack Compose**.
- Fondo: `R.drawable.sentada_cafetales` con `alpha = 0.7f`.
- Lista superpuesta con **transparencia** y estilo Material 3.
- Cada fila incluye:
    - Posición (#)
    - Foto pequeña (avatar o placeholder)
    - Nombre/Correo
    - Puntos totales

### Fuera de Alcance Inicial
- Lógica de carga desde Firebase Database.
- Estrategia de caché local (12 horas).
- Paginación, filtros o métricas avanzadas.

---

## 3. Arquitectura de Pantalla
- **UI (Compose):**
    - `LeaderboardScreen` → Contenedor principal.
    - `LeaderboardList` → Lista de jugadores.
    - `LeaderboardRow` → Item de la lista (posición, foto, nombre, puntos).

- **ViewModel (LeaderboardViewModel):**
    - Expone un estado `LeaderboardUiState` con la lista de jugadores.
    - Por ahora: datos estáticos (fake/mock).
    - Futuro: integración con Firebase + caché.

- **Estados de UI:**
  ```kotlin
  data class LeaderboardUiState(
      val isLoading: Boolean = true,
      val players: List<PlayerScore> = emptyList(),
      val error: String? = null
  )

  data class PlayerScore(
      val position: Int,
      val avatarUrl: String?,
      val displayName: String,
      val score: Int
  )
  ```

---

## 4. UI Mock / Wireframe

```
+--------------------------------------------------+
|     Fondo: R.drawable.sentada_cafetales (0.7f)   |
|                                                  |
|   #1   [avatar]   Juan Pérez      1500 pts       |
|   #2   [avatar]   María López     1420 pts       |
|   #3   [avatar]   anon@correo.com 1350 pts       |
|   ...                                            |
|                                                  |
+--------------------------------------------------+
```

Estilo:

* Fondo difuminado con alpha 0.7.
* Lista en primer plano con **card transparente** y bordes redondeados.
* Tipografía y colores definidos en **NenekTheme**

---

## 5. Ejemplo de Código Inicial (Compose)

```Kotlin
@Composable
fun LeaderboardScreen(
    state: LeaderboardUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo
        Image(
            painter = painterResource(R.drawable.sentada_cafetales),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
            alpha = 0.7f
        )

        // Contenido principal
        when {
            state.isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            state.error != null -> ErrorContent(message = state.error, onRetry = onRetry)

            else -> LeaderboardList(players = state.players)
        }
    }
}

@Composable
private fun LeaderboardList(players: List<PlayerScore>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(players) { player ->
            LeaderboardRow(player = player)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LeaderboardRow(player: PlayerScore) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${player.position}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.width(40.dp)
            )

            Avatar(url = player.avatarUrl)

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.displayName,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                text = "${player.score} pts",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun Avatar(url: String?, size: Dp = 40.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (url == null) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            // Futuro: Coil AsyncImage(url)
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        }
    }
}
```

---

## 6. Roadmap Técnico

1. Crear LeaderboardScreen con **Compose + Material3.**
2. Definir **LeaderboardViewModel** con UiState y datos mock.
3. Integrar **LeaderboardScreen** en `NavGraph` bajo la ruta `leaderboard.`
4. (Futuro) Conectar con **Firebase Database.**
5. (Futuro) Implementar caché de 12 horas (DataStore o Room).
6. Pruebas de UI (Compose) para renderizado correcto de la lista.

---

## 7. Riesgos y Consideraciones

- **Carga inicial lenta** -> hasta implementar Firebase, simular con datos estaticos.
- **Cache 12h** -> requiere estrategia clara (timpestamp + invalidación).
- **Listas largas** -> considerar paginación si crece demasiado.
- **Accesibilidad** -> contraste suficiente entre fondo e items (validad en modo oscuro/claro).

---

## 8. Referencias

- [Material 3 - List](https://m3.material.io/components/lists/overview)
- [Material 3 - Cards](https://m3.material.io/components/cards/overview)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- [Documentación interna de **NenekTheme** (tokens de color y tipografia)](https://github.com/terry1921/nenek_trivia/blob/main/docs/topics/design-doc.md)
