# Nenek Trivia

Aplicación Android de trivia desarrollada con Kotlin y Jetpack Compose. Nenek Trivia combina partidas contrarreloj, autenticación social, preferencias locales, progreso del jugador y un leaderboard respaldado por Firebase.

El proyecto está organizado como una aplicación multimódulo con MVVM, casos de uso, repositorios, Kotlin Flow e inyección de dependencias con Hilt.

## Funcionalidades

- Inicio de sesión con Google y Facebook.
- Partidas de trivia con preguntas y respuestas aleatorias.
- Temporizador por pregunta, puntaje y consejos educativos.
- Sincronización del progreso (`Honor`) al finalizar una partida.
- Leaderboard remoto con actualización de resultados.
- Perfil de usuario y preferencias de tema, sonido y vibración.
- Persistencia local con Room y Preferences DataStore.
- Valoración de la app mediante Google Play In-App Review.
- Temas y componentes reutilizables construidos con Material 3.

## Stack tecnológico

- Kotlin 2.4 y Java 17.
- Jetpack Compose y Material 3.
- Navigation Compose y arquitectura MVVM.
- Coroutines, Flow y StateFlow.
- Hilt y KSP.
- Room y Preferences DataStore.
- Firebase Authentication, Realtime Database, Analytics y Crashlytics.
- Retrofit, OkHttp y Gson.
- JUnit, Mockito, Turbine y Robolectric.
- Gradle Kotlin DSL y catálogo de versiones.

La aplicación compila con Android SDK 37, apunta a API 37 y soporta dispositivos desde API 26.

## Arquitectura

```text
:app
  ├── :ui
  │     └── :domain
  │           ├── :database
  │           │     └── :model
  │           ├── :network
  │           │     └── :model
  │           └── :model
  ├── :domain
  ├── :database
  ├── :network
  └── :model
```

| Módulo | Responsabilidad |
| --- | --- |
| `:app` | Configuración de la aplicación, variantes de compilación, arranque y ensamblado de dependencias. |
| `:ui` | Pantallas Compose, navegación, ViewModels, estados de UI, componentes y sistema de diseño. |
| `:domain` | Casos de uso para autenticación, preguntas, sesión, preferencias, leaderboard y progreso. |
| `:database` | Persistencia local con Room, DAOs, entidades, repositorios locales, seeds y DataStore. |
| `:network` | Integraciones remotas, repositorios de Firebase y cliente HTTP. |
| `:model` | Modelos compartidos entre las capas. |

El flujo principal sigue esta dirección:

```text
Compose UI -> ViewModel -> Use case -> Repository -> Room/Firebase
                  ^                         |
                  └──── Flow / StateFlow ───┘
```

Para decisiones de diseño más detalladas, consulta la [documentación de arquitectura](docs/topics/design-doc.md).

## Configuración local

### Requisitos

- Android Studio compatible con Android Gradle Plugin 9.2.
- JDK 17.
- Android SDK 37.
- Un proyecto de Firebase configurado para la aplicación.

### 1. Clonar el repositorio

```bash
git clone https://github.com/terry1921/nenek_trivia.git
cd nenek_trivia
```

### 2. Configurar Firebase

Descarga el archivo `google-services.json` desde Firebase Console y colócalo en:

```text
app/google-services.json
```

El archivo está ignorado por Git y no debe incluirse en commits.

Firebase Realtime Database debe contener las preguntas bajo el nodo `questions1`. Cada pregunta utiliza los campos `id`, `question`, `category`, `answergood`, `answerbad01`, `answerbad02`, `answerbad03` y, opcionalmente, `tip`.

### 3. Configurar valores locales

Agrega los valores necesarios a `local.properties` en la raíz del proyecto:

```properties
sdk.dir=/ruta/al/android/sdk
GOOGLE_WEB_CLIENT_ID=tu_web_client_id
FACEBOOK_APP_ID=tu_facebook_app_id
FACEBOOK_CLIENT_TOKEN=tu_facebook_client_token
CONFIG_API=tu_api_key
DATABASE_NAME=nenek-trivia.db
```

También puedes proporcionar estos valores como propiedades de Gradle o variables de entorno. `DATABASE_NAME` tiene el valor predeterminado `default.db`; los demás valores son necesarios únicamente para las integraciones que quieras utilizar.

### 4. Compilar y ejecutar

Abre el proyecto en Android Studio, sincroniza Gradle y ejecuta una variante `debug`. Desde la terminal puedes compilar todas las combinaciones debug con:

```bash
./gradlew assembleDebug
```

El proyecto define dos dimensiones de producto:

- Branding: `original` y `fenixarts`.
- Backend/store: `playstore` y `firebase`.

Por ejemplo, para instalar una variante concreta:

```bash
./gradlew installOriginalFirebaseDebug
```

## Pruebas y validación

Ejecuta las pruebas unitarias de los módulos principales:

```bash
./gradlew \
  :database:testDebugUnitTest \
  :domain:testDebugUnitTest \
  :network:testDebugUnitTest \
  :ui:testDebugUnitTest
```

Para compilar y probar el proyecto como lo hace el hook de pre-push:

```bash
./gradlew assembleDebug testDebugUnitTest
```

La integración continua de GitHub Actions compila las variantes debug y ejecuta las pruebas unitarias de `database`, `domain`, `network` y `ui`. Para usar los hooks locales instala [pre-commit](https://pre-commit.com/) y ejecuta:

```bash
pre-commit install
pre-commit install --hook-type commit-msg --hook-type pre-push
pre-commit run --all-files
```

## Estructura del repositorio

```text
NenekTrivia/
├── app/          # Aplicación Android y configuración de variantes
├── ui/           # Compose, navegación, ViewModels y design system
├── domain/       # Casos de uso
├── database/     # Room y DataStore
├── network/      # Firebase y servicios remotos
├── model/        # Modelos compartidos
├── docs/         # Documentación técnica
└── .github/      # CI, plantillas y configuración de GitHub
```

## Contribuir

Consulta [CONTRIBUTING.md](CONTRIBUTING.md) para conocer el flujo de contribución y la convención de commits. Los cambios deben incluir pruebas cuando corresponda y pasar la validación local antes de abrir un pull request.

## Seguridad

No publiques credenciales, archivos `google-services.json`, tokens, API keys ni keystores. Para reportar vulnerabilidades, revisa [SECURITY.md](SECURITY.md).

## Licencia

Este proyecto se distribuye bajo los términos descritos en [LICENSE](LICENSE).
