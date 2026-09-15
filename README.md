# NOVA · Banca simulada

Aplicación Android en Kotlin y Jetpack Compose. Sin servidor ni operaciones reales.

## Acceso

- Correo: `demo@nova.com`
- Contraseña: `Nova123`

## Arquitectura

- `domain`: modelos, contrato del repositorio y validación del formulario; sin dependencias Android.
- `data`: repositorio en memoria, credenciales simuladas y latencia artificial.
- `presentation`: ViewModel con StateFlow y estados Idle, Loading, Success y Error; pantallas Compose y Navigation Compose.
- `ui/theme`: tema visual.

El repositorio se inyecta mediante una fábrica de ViewModel. La UI observa el estado respetando el ciclo de vida. Los importes usan BigDecimal. El ViewModel conserva la sesión al rotar; al morir el proceso se requiere iniciar sesión de nuevo. La contraseña no se persiste y se borra después de ingresar.

## Prueba manual

1. Enviar campos vacíos o correo inválido: deben aparecer errores junto al campo.
2. Ingresar una contraseña incorrecta de seis o más caracteres: aparece Loading y luego Error; corregir o reintentar.
3. Usar las credenciales de arriba: navega a la cuenta, carga saldo y lista LazyColumn.
4. Tocar un movimiento: carga el detalle con importe, fecha, origen/destino y referencia. Volver regresa a la lista.
5. Pulsar «Simular un error de carga» al final de la lista: aparece Loading y luego Error. «Reintentar» recupera los datos.
6. «Actualizar» vuelve a cargar la cuenta. Rotar conserva el estado. «Cerrar sesión» limpia la sesión y el historial de navegación.

La lista tiene claves estables. La carga de detalle también maneja errores y permite reintentar. Los errores de cuenta son deterministas y solo se provocan desde el control de demostración.

## Verificación sin ejecutar la aplicación

```sh
./gradlew :app:compileDebugKotlin :app:testDebugUnitTest
```

Incluye pruebas unitarias de validación del formulario. Abrir el proyecto en Android Studio para ejecutar manualmente.
