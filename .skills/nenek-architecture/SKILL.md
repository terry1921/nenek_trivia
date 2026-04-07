# Skill: nenek-architecture

## Description
Guía para implementar features en Nenek Trivia siguiendo arquitectura modular y convenciones del proyecto.

## Project rules
- Usar Kotlin + Jetpack Compose.
- Mantener arquitectura modular.
- Las interfaces de repositorio viven en :model.
- Las implementaciones de repositorio viven en :database.
- Para Room converters usar Gson, no kotlinx.serialization.
- Priorizar Flow para streams de datos.
- ViewModels exponen uiState inmutable.
- Evitar lógica de negocio en composables.
- Usar Material 3 y componentes reutilizables.
- Mantener nombres claros y consistentes por feature.

## Feature workflow
1. Definir modelo y contratos.
2. Crear DAO / datasource si aplica.
3. Implementar repositorio.
4. Crear caso de uso si aplica.
5. Conectar ViewModel con Flow.
6. Exponer estado para Compose.
7. Agregar previews o fakes si es posible.
8. Agregar pruebas unitarias básicas.
