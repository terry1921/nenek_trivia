# 📄 Design Doc — Flujo de Autenticación (Nenek Trivia)

**Autor:** Enrique Espinoza (terry1921)
**Fecha:** 05 de septiembre de 2025
**Estado:** Borrador

## 1. Contexto y Objetivo
El proyecto **Nenek Trivia** necesita un flujo de autenticación que permita a los usuarios iniciar sesión de manera sencilla y moderna.
Este documento define la primera versión de la **pantalla de autenticación**, enfocada en la **UI con Jetpack Compose** y siguiendo **Material 3**.

En esta etapa:
- Solo se construirá la **pantalla visual** con botones de autenticación social.
- La **lógica real de Firebase Auth** se integrará en una fase posterior.

---

## 2. Alcance
### Alcance Actual
- Crear pantalla de **Login** en Compose.
- Incluir botones sociales (Google, Facebook, Email/Guest opcionales).
- Respetar guías de **Material 3** (colores, tipografía, espaciados).
- Preparar estructura de ViewModel + UI state para conectar después con Firebase.

### Fuera de Alcance Inicial
- Conexión a Firebase Auth.
- Persistencia de sesión (DataStore / Room).
- Flujos avanzados (registro, recuperación de contraseña).

---

## 3. Arquitectura de Pantalla
- **UI (Compose):**
    - `AuthScreen` → Contenedor principal.
    - `SocialLoginButton` → Componente reutilizable.
- **ViewModel (AuthViewModel):**
    - Manejar estado de la pantalla (loading, error, idle).
    - Exponer callbacks para botones sociales.
    - Acciones simuladas para Google, Facebook e Invitado (Implementado).
- **Navegación:**
    - `SplashActivity` redirige a `AuthScreen`.
    - En el futuro, tras login exitoso, navegará a `MainScreen`.

---

## 4. UI Mock / Wireframe

```
+-----------------------------------+
|           Nenek Trivia            |
|                                   |
|       [ Ilustración o Logo ]      |
|                                   |
|           Bienvenido a            |
|           Nenek Trivia            |
|     --------------------------    |
|     --------------------------    |
|                                   |
|       [ Iniciar con Google ]      |
|      [ Iniciar con Facebook ]     |
|                                   |
|            Política de Privacidad |
+-----------------------------------+
```

---

## 5. Ejemplo de Código Inicial (Compose)

```kotlin
@Composable
fun AuthScreen(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onGuestClick: () -> Unit,
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
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            SocialLoginButton(
                text = "Iniciar con Google",
                icon = Icons.Default.Person, // luego ícono Google
                onClick = onGoogleClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            SocialLoginButton(
                text = "Iniciar con Facebook",
                icon = Icons.Default.Person, // luego ícono FB
                onClick = onFacebookClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            SocialLoginButton(
                text = "Continuar como invitado",
                icon = Icons.Default.Person,
                onClick = onGuestClick
            )
        }
    }
}

@Composable
fun SocialLoginButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}
```

## 6. Roadmap Técnico

1. Crear AuthScreen con Compose + Material 3.
2. Definir AuthViewModel con estados (Idle, Loading, Error).
3. Integrar navegación desde SplashActivity hacia AuthScreen.
4. Preparar integración con Firebase Auth (siguiente fase).
5. Agregar pruebas unitarias simples (renderizado de botones).

---

## 7. Riesgos y Consideraciones

- Los íconos oficiales de Google y Facebook requieren recursos propios, no se deben usar íconos genéricos.
- La experiencia de "Invitado" puede necesitar luego persistencia local para guardar progreso.
- El flujo debe ser escalable para futuros proveedores (ej. Apple Sign-In).

## 8. Referencias

- [Android Developers — Official Documentation](https://developer.android.com)
- [Material 3 – Buttons](https://m3.material.io/components/buttons/overview)
- [Google Identity Design Guidelines](https://developers.google.com/identity/branding-guidelines?hl=es-419)
- [Facebook Login – Branding](https://developers.facebook.com/docs/facebook-login/userexperience/)
