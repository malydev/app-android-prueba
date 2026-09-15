# App de prueba

Aplicación Android en Kotlin y Jetpack Compose

## Arquitectura

- `domain`: modelos, contrato del repositorio y validación del formulario; sin dependencias Android.
- `data`: repositorio en memoria, credenciales simuladas y latencia artificial.
- `presentation/login`, `home`, `detail`: cada funcionalidad tiene su pantalla y su propio ViewModel.
- `presentation/navigation`: define destinos y conecta estado y acciones con cada pantalla.
- `presentation/session`: controla el cierre y la observación de la sesión.
- `presentation/components`: componentes visuales compartidos.
- `presentation/common`: estados Idle, Loading, Success y Error, conversión de errores y formato de importes.
- `BankApplication`: crea las dependencias compartidas durante la vida del proceso.
- `ui/theme`: tema visual.

Cada pantalla recibe datos y callbacks, sin acceder al repositorio ni al controlador de navegación. Los ViewModels de pantalla pertenecen a su destino de navegación y se liberan al retirarlo del historial. Los modelos, el validador y los datos simulados están en archivos separados.

El repositorio compartido se pasa directamente al constructor de cada ViewModel mediante el inicializador de `viewModel { ... }` de Compose, sin una clase Factory propia. La UI observa el estado respetando el ciclo de vida. Los importes usan BigDecimal. El repositorio compartido conserva la sesión al rotar; al morir el proceso se requiere iniciar sesión de nuevo. La contraseña no se persiste y se borra después de ingresar.

## Verificación sin ejecutar la aplicación

```sh
./gradlew :app:compileDebugKotlin :app:testDebugUnitTest
```

Incluye pruebas unitarias de validación del formulario. Abrir el proyecto en Android Studio para ejecutar manualmente.
