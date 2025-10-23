# Servicio Ágil Backend v1.0.0

## Descripción
Backend API REST desarrollado en Java 17 con Spring Boot para la gestión de usuarios y servicios del sistema Servicio Ágil.

## Tecnologías Utilizadas
- **Java 17**: Lenguaje de programación
- **Spring Boot 3.1.5**: Framework principal
- **Spring Data JPA**: Persistencia de datos
- **H2 Database**: Base de datos en memoria (desarrollo)
- **Maven**: Gestión de dependencias
- **Swagger/OpenAPI 3**: Documentación de la API
- **Lombok**: Reducción de código boilerplate
- **Spring Boot Actuator**: Monitoreo y métricas

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/servicioagil/
│   │   ├── ServicioAgilApplication.java
│   │   ├── config/
│   │   │   └── OpenApiConfig.java
│   │   ├── controller/
│   │   │   ├── ApiController.java
│   │   │   ├── HealthController.java
│   │   │   ├── RootController.java
│   │   │   └── UsuarioController.java
│   │   ├── dto/
│   │   │   ├── CreateUsuarioDTO.java
│   │   │   ├── UpdateUsuarioDTO.java
│   │   │   └── UsuarioDTO.java
│   │   ├── entity/
│   │   │   └── Usuario.java
│   │   ├── exception/
│   │   │   ├── BusinessException.java
│   │   │   ├── ErrorResponse.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── ResourceNotFoundException.java
│   │   ├── repository/
│   │   │   └── UsuarioRepository.java
│   │   └── service/
│   │       └── UsuarioService.java
│   └── resources/
│       ├── application.properties
│       └── data.sql
└── test/ (por implementar)
```

## Instalación y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6 o superior

### Pasos para ejecutar

1. **Clonar el repositorio** (si aplica)
   ```bash
   git clone <url-del-repositorio>
   cd servicio-agil-v1
   ```

2. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

3. **Ejecutar tests** (cuando estén implementados)
   ```bash
   mvn test
   ```

4. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```
   
   O alternativamente:
   ```bash
   mvn clean package
   java -jar target/servicio-agil-v1-1.0.0.jar
   ```

5. **Acceder a la aplicación**
   - API: http://localhost:8080
   - Documentación Swagger: http://localhost:8080/swagger-ui.html
   - Consola H2 (desarrollo): http://localhost:8080/h2-console
   - Health Check: http://localhost:8080/api/health

## Endpoints de la API

### Endpoints Principales
- `GET /` - Página de inicio
- `GET /api` - Información de la API
- `GET /api/health` - Estado del sistema
- `GET /api/health/system` - Información del sistema

### Gestión de Usuarios
- `GET /api/usuarios` - Obtener todos los usuarios
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `GET /api/usuarios/activos` - Obtener usuarios activos
- `GET /api/usuarios/buscar?nombre={nombre}` - Buscar usuarios por nombre
- `GET /api/usuarios/estadisticas` - Estadísticas de usuarios
- `POST /api/usuarios` - Crear nuevo usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario

## Configuración de Base de Datos

### Desarrollo (H2 Database)
- **URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa`
- **Contraseña**: `password`
- **Consola H2**: http://localhost:8080/h2-console

### Producción
Para producción, modificar `application.properties` con los datos de tu base de datos:

```properties
# Ejemplo para PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/servicioagil
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## Ejemplos de Uso

### Crear un usuario
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Roberto Silva",
    "email": "roberto.silva@email.com"
  }'
```

### Obtener todos los usuarios
```bash
curl http://localhost:8080/api/usuarios
```

### Buscar usuarios por nombre
```bash
curl "http://localhost:8080/api/usuarios/buscar?nombre=Juan"
```

## Monitoreo y Salud

La aplicación incluye endpoints de Actuator para monitoreo:

- `/actuator/health` - Estado de salud
- `/actuator/info` - Información de la aplicación
- `/actuator/metrics` - Métricas del sistema

## Desarrollo

### Agregar nuevas dependencias
Editar `pom.xml` y agregar la dependencia deseada, luego ejecutar:
```bash
mvn clean install
```

### Hot Reload
La aplicación está configurada con Spring Boot DevTools para recarga automática durante el desarrollo.

## Próximas Mejoras

- [ ] Implementar autenticación y autorización (JWT)
- [ ] Agregar más entidades (Servicios, Categorías, etc.)
- [ ] Implementar tests unitarios y de integración
- [ ] Configurar perfiles de Spring (dev, test, prod)
- [ ] Agregar validaciones más robustas
- [ ] Implementar paginación en las consultas
- [ ] Agregar cache con Redis
- [ ] Implementar logging más detallado
- [ ] Dockerizar la aplicación

## Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo LICENSE para más detalles.

## Contacto

Para preguntas o sugerencias, contactar al equipo de desarrollo en desarrollo@servicioagil.com