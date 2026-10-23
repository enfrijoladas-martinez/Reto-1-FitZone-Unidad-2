# Reto 1 · FitZone: inscripción a clases grupales

**Unidad 2 · Diseño de servicios** — Arquitectura Orientada a Servicios
**Equipo:** Elias Martinez Garcia y Jose de Jesus Junior Barba Cano

API REST que automatiza la inscripción a clases grupales (yoga, spinning y funcional) en las tres sucursales del gimnasio FitZone. Resuelve los problemas del caso: sin sobrecupo (bloqueo de la clase al inscribir), sin socios con membresía vencida (se valida contra la fecha de la clase) y con confirmación y recordatorio automáticos 2 horas antes.

## Tecnologías

Java 25 · Spring Boot 4.1.1 (Web MVC, Data JPA, eventos, `@Async`, `@Scheduled`) · MySQL · Maven · Postman.

## Ejecutar

1. Copiar `.env.example` como `.env` y poner la contraseña de MySQL (la base `fitzone` se crea sola).
2. `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`). Arranca en `http://localhost:8083` con datos de prueba relativos a la fecha actual.
3. Importar `postman/FitZone.postman_collection.json` y ejecutar las carpetas en orden.

## Modelado

| # | Actividad | Operación | Servicio | Tipo |
|---|---|---|---|---|
| 1 | El socio elige una clase | `consultarClase` | Clase | Entidad |
| 2 | Verificar membresía vigente | `consultarMembresia` | Socio | Entidad |
| 3 | Revisar que haya cupo | `consultarCupo` | Clase | Entidad |
| 4 | Registrar la inscripción | `registrarInscripcion` | Inscripción | Entidad |
| 5 | Enviar confirmación | `enviarConfirmacion` | Notificación | Utilidad |
| 6 | Recordatorio 2 h antes | `programarRecordatorio` | Notificación | Utilidad |
| — | Proceso completo | `inscribirEnClase` | Inscripción a Clase | Tarea |

## Endpoints

| Método | Ruta | Capacidad |
|---|---|---|
| POST | `/api/v1/inscripciones` | `inscribirEnClase` · body `{"idSocio":1,"idClase":1}` |
| GET | `/api/v1/clases/{id}/cupo` | `consultarCupo` |
| GET | `/api/v1/socios/{id}/membresia` | `consultarMembresia` |
| GET | `/api/v1/clases` · `?sucursal=NORTE` · `/{id}` | `consultarClase` |
| GET | `/api/v1/inscripciones` · `/{id}` · `/socio/{idSocio}` | Consulta de inscripciones |
| GET | `/api/v1/socios` · `/{id}` | Consulta de socios |
| GET | `/api/v1/notificaciones` · `/socio/{idSocio}` | Confirmaciones y recordatorios enviados |

## Errores

| Código | HTTP |
|---|---|
| `DATOS_INCOMPLETOS` | 400 |
| `SOCIO_NO_ENCONTRADO` · `CLASE_NO_ENCONTRADA` · `INSCRIPCION_NO_ENCONTRADA` | 404 |
| `CLASE_YA_INICIADA` · `MEMBRESIA_VENCIDA` · `INSCRIPCION_DUPLICADA` · `CLASE_SIN_CUPO` | 409 |

## Comunicación

`inscribirEnClase`, `consultarCupo` y `consultarMembresia` son síncronas (petición-respuesta). Al registrarse una inscripción se publica el evento `InscripcionRegistrada`; el servicio de Notificación lo recibe de forma asíncrona y envía la confirmación (publicación-suscripción). El recordatorio lo envía un proceso programado cada minuto, sin respuesta al socio (unidireccional).
