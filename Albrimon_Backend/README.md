# Albrimon API — Backend REST

Backend Java para conectar la aplicación Android Kotlin de Albrimon con una base de datos SQLite local.

## Decisiones de arquitectura

- Java 21 + Spring Boot 4.1.2.
- API REST con JSON.
- SQLite + Spring Data JPA / Hibernate.
- Spring Security + JWT y contraseñas BCrypt.
- Roles `DISENADOR` y `CLIENTE`.
- El registro público siempre crea cuentas `CLIENTE`. La cuenta `DISENADOR` se crea mediante variables de entorno de bootstrap.
- El algoritmo `CutPlanner` permanece en Kotlin (como indica el documento de Retazos). El backend valida y persiste el resultado del plan en una transacción.

## Requisitos

- JDK 21
- Maven 3.9+
- Android Studio para la app (no necesario para ejecutar la API)

## Ejecutar localmente

Desde la carpeta `Albrimon_Backend`:

**Windows PowerShell**
```powershell
$env:BOOTSTRAP_DESIGNER_NAME="Confeccionador Albrimon"
$env:BOOTSTRAP_DESIGNER_EMAIL="disenador@albrimon.local"
$env:BOOTSTRAP_DESIGNER_PASSWORD="CambiaEstaClave123"
$env:JWT_SECRET="pon-aqui-una-clave-aleatoria-de-32-caracteres-o-mas"
mvn spring-boot:run
```

**Linux / macOS**
```bash
export BOOTSTRAP_DESIGNER_NAME="Confeccionador Albrimon"
export BOOTSTRAP_DESIGNER_EMAIL="disenador@albrimon.local"
export BOOTSTRAP_DESIGNER_PASSWORD="CambiaEstaClave123"
export JWT_SECRET="pon-aqui-una-clave-aleatoria-de-32-caracteres-o-mas"
mvn spring-boot:run
```

La API queda en `http://localhost:8080`. SQLite se crea en `./data/albrimon.db`.
El directorio `data/` está incluido en el proyecto. Para Android Emulator, el host de tu PC es `http://10.0.2.2:8080`; en un teléfono físico usa la IP local de la PC y permite el puerto 8080 en el firewall.

> Las credenciales de ejemplo son solo para desarrollo. No las uses en producción.

## Seguridad y roles

- `POST /api/auth/register` no acepta el rol enviado por el cliente: siempre crea `CLIENTE`.
- `DISENADOR` solo se crea con `BOOTSTRAP_DESIGNER_*` si el correo aún no existe. Para producción, conviene crear/promover diseñadores desde un proceso administrativo protegido.
- El token JWT se envía en `Authorization: Bearer <token>`.
- Cada confeccionador solo accede a sus clientes, proyectos y retazos.
- El cliente solo consulta sus proyectos y su ficha vinculada.
- El código de vínculo de seis caracteres se genera al crear una ficha de cliente. Se entrega al cliente para que lo introduzca al registrarse; el código se invalida al vincularlo.

## Endpoints

### Salud y autenticación

| Método | Ruta | Acceso |
|---|---|---|
| GET | `/api/health` | Público |
| POST | `/api/auth/register` | Público |
| POST | `/api/auth/login` | Público |
| GET | `/api/auth/me` | JWT |

Registro:
```json
{
  "name": "Ana López",
  "email": "ana@example.com",
  "password": "clave123",
  "linkCode": "A2BC9D"
}
```
`linkCode` es opcional. No envíes `role`: toda cuenta pública nace como `CLIENTE`.

Inicio:
```json
{"email":"disenador@albrimon.local","password":"CambiaEstaClave123"}
```

### Clientes

| Método | Ruta | Función |
|---|---|---|
| GET | `/api/clients` | Lista del confeccionador |
| GET | `/api/clients/me` | Ficha del cliente autenticado y vinculado |\n| GET | `/api/clients/{id}` | Detalle |
| POST | `/api/clients` | Crear ficha y código de vínculo |
| PUT | `/api/clients/{id}` | Actualizar ficha |

Ejemplo:
```json
{
  "name": "Ana López",
  "phone": "9611234567",
  "age": 28,
  "size": "M",
  "bustCm": 88,
  "waistCm": 68,
  "hipCm": 94,
  "lengthCm": 60,
  "active": true
}
```

### Proyectos y piezas de patronaje

| Método | Ruta | Función |
|---|---|---|
| GET | `/api/projects` | Diseñador: todos; cliente: solo los vinculados |
| GET | `/api/projects/{id}` | Detalle autorizado |
| POST | `/api/projects` | Crear proyecto |
| PUT | `/api/projects/{id}` | Actualizar proyecto |
| GET | `/api/projects/{projectId}/pieces` | Consultar piezas |
| POST | `/api/projects/{projectId}/pieces` | Agregar pieza |
| PUT | `/api/projects/{projectId}/pieces/{pieceId}` | Editar pieza |
| DELETE | `/api/projects/{projectId}/pieces/{pieceId}` | Eliminar pieza |

Proyecto:
```json
{
  "name": "Vestido midi",
  "clientId": 1,
  "status": "PENDIENTE",
  "dueDate": "2026-12-12",
  "notes": "Tela principal azul"
}
```
Estados válidos: `PENDIENTE`, `EN_PROCESO`, `ENTREGADO`, `TERMINADO`, `CANCELADO`.

Pieza:
```json
{"name":"Delantero","widthMm":350,"heightMm":300,"canRotate":false}
```

### Retazos e historial

| Método | Ruta | Función |
|---|---|---|
| GET | `/api/scraps` | Inventario del confeccionador |
| GET | `/api/scraps/{id}` | Detalle |
| POST | `/api/scraps` | Registrar retazo |
| PUT | `/api/scraps/{id}` | Editar retazo disponible |
| DELETE | `/api/scraps/{id}` | Eliminar retazo disponible sin uso |
| POST | `/api/scraps/apply-plan` | Aplicar plan y registrar consumo/sobrantes |
| GET | `/api/scraps/usage/project/{projectId}` | Historial de uso por proyecto |

Retazo:
```json
{
  "name": "Mezclilla azul",
  "fabricType": "Mezclilla",
  "colorHex": "#426B91",
  "widthMm": 700,
  "heightMm": 400,
  "hasGrain": true,
  "photoPath": "photos/retazo_01.jpg"
}
```

Aplicar un plan ya calculado en Kotlin:
```json
{
  "projectId": 1,
  "usages": [
    {
      "scrapId": 1,
      "usedAreaMm2": 140000,
      "leftoverWmm": 700,
      "leftoverHmm": 150
    }
  ]
}
```
La API verifica que el proyecto y los retazos pertenezcan al confeccionador, que el retazo siga disponible y que el área usada no exceda el área original. En una transacción marca el original como no disponible, registra `scrap_usage` y crea un nuevo retazo si ambas dimensiones del sobrante son de al menos 100 mm. El sobrante que se envía debe corresponder al resultado del optimizador; el backend no vuelve a calcular posiciones.

## Tablas SQLite

- `users`
- `clients`
- `projects`
- `project_pieces`
- `scraps`
- `scrap_usage`

Hibernate crea/actualiza el esquema al iniciar (`ddl-auto: update`). La definición de referencia está en `database/schema.sql`.

## Conexión desde Kotlin

Base URL:
- Emulador Android: `http://10.0.2.2:8080/api/`
- Dispositivo físico: `http://IP_LOCAL_DE_TU_PC:8080/api/`

Para integrar:
1. Agrega permiso `INTERNET` al `AndroidManifest.xml`: `<uses-permission android:name="android.permission.INTERNET" />`. En desarrollo local permite HTTP cleartext mediante una configuración de seguridad de red de debug; no habilites HTTP sin cifrar en la versión de producción.
2. En desarrollo local, permite HTTP cleartext en la configuración de red de Android; para producción usa HTTPS.
3. Crea DTOs Kotlin que correspondan a los JSON anteriores.
4. Implementa Retrofit + OkHttp; el interceptor debe adjuntar el JWT después del login.
5. Cambia `ClientRepository` y `ProjectRepository` para consultar la API en lugar de las listas de ejemplo.
6. Añade `role` al estado de sesión y dirige al usuario a la navegación correspondiente.
7. La pantalla Retazos debe llamar a `/api/scraps`; el `CutPlanner` calcula localmente y luego envía `/api/scraps/apply-plan`.

El ZIP del frontend que recibí todavía no incluye Retrofit, Room ni pantallas funcionales de Retazos. Por eso este proyecto entrega el backend REST listo para conectarse, pero no modifica automáticamente las vistas Kotlin.

## Decisiones pendientes antes de AWS

- Cambiar SQLite por PostgreSQL o una base administrada si habrá múltiples instancias/backend escalado. SQLite sirve para desarrollo local y demostración de un solo proceso.
- Configurar HTTPS, secretos de JWT y CORS para el dominio real.
- Definir almacenamiento de fotos. Este backend guarda `photoPath`, no sube ni almacena los archivos de imagen; el documento propone conservarlos en el almacenamiento privado del teléfono.
- Añadir migraciones versionadas (Flyway/Liquibase) antes de producción.
- Configurar copias de seguridad y política de privacidad.
