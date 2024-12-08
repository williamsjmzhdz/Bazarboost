# BazarBoost 🛍️

## Descripción General

BazarBoost es una plataforma e-commerce que implementa una arquitectura híbrida (MVC + REST), diseñada para facilitar operaciones de venta en línea. La plataforma permite a vendedores independientes establecer y gestionar sus tiendas en línea mientras proporciona a los clientes una experiencia de compra fluida. Construida pensando en la escalabilidad y seguridad, BazarBoost aprovecha el robusto ecosistema Spring.

## Características Principales

### Experiencia del Cliente

- Exploración dinámica de productos con búsqueda y filtrado en tiempo real
- Gestión fluida del carrito con actualizaciones en vivo
- Proceso de pago seguro
- Historial detallado de facturas y sus respectivos detalles
- Sistema de reseñas y calificaciones de productos
- Gestión de cuenta personal

### Portal del Vendedor

- Gestión integral de productos (operaciones CRUD)
- Sistema de descuentos personalizable por vendedor
- Visualización de ventas y gestión de inventario

### Controles Administrativos

- Supervisión de usuarios
- Administración de roles de vendedor a usuarios
- Gestión integral de categorías

## Stack Tecnológico

### Infraestructura Backend

- Java 17
- Spring Boot 3.2
- Spring Security con JWT (algoritmo HS512)
- Spring Data JPA
- MariaDB 10.6+
- Encriptación de contraseñas BCrypt

### Tecnologías Frontend

- JavaScript Vanilla
- HTML5/CSS3
- Bootstrap 5
- Operaciones asíncronas (consumo de endpoints REST)

## Arquitectura del Sistema

### Componentes Principales

- Arquitectura por capas (Controlador, Servicio, Repositorio)
- Sistema dual de controladores (REST + MVC)
- Patrón DTO integral
- Manejo global de excepciones
- Gestión transaccional

### Diseño de Base de Datos

- 12 entidades normalizadas
- Modelo relacional complejo
- Integridad transaccional
- Indexación

![Modelo Entidad-Relación](/modelo-relacional-bazarboost.png)

## Implementación de Seguridad

- Autenticación híbrida:

  - Formularios y sesiones con Spring Security para vistas Thymeleaf
  - JWT en cookies httpOnly para endpoints REST
  - Redirección a login para accesos no autorizados

- Control de acceso:

  - Roles predefinidos (Cliente, Vendedor, Administrador)
  - Autorización basada en roles para endpoints MVC y REST
  - Validación de propiedad de recursos

- Protección de datos:

  - Encriptación de contraseñas con BCrypt
  - Tokens JWT firmados con HS512
  - Protección CSRF para formularios
  - Cookies seguras para JWT

- Monitoreo:
  - Logging de eventos de autenticación con SLF4J
  - Registro de operaciones críticas
  - Trazabilidad de accesos y errores

## Implementación de Funcionalidades Clave

### Sistema de Carrito

- Actualizaciones en tiempo real
- Persistencia de sesión
- Validación de inventario
- Precios dinámicos con descuentos

### Procesamiento de Pagos

- Manejo seguro de transacciones
- Soporte para múltiples métodos de pago
- Generación automatizada de facturas
- Validación de métodos de pago

### Gestión de Productos

- Carga y almacenamiento de imágenes
- Seguimiento de inventario
- Gestión de descuentos
- Organización por categorías

## Guía de Instalación

### Requisitos Previos

```bash
- Java 17+
- MariaDB 10.6+
- Maven 3.8+
```

### Proceso de Configuración

1. Clonar el Repositorio

```bash
git clone https://github.com/yourusername/bazarboost.git
cd bazarboost
```

2. Configurar Almacenamiento de Imágenes

```properties
# Actualizar en application.properties
app.imagenes.directorio={su_ruta}
# Ejemplo:
app.imagenes.directorio=C:/Users/Alumno/Documents/ArchivosDiplomadoJava/Bazarboost/bazarboost/src/main/resources/estaticos/img
```

3. Configurar Conexión a Base de Datos

```properties
# Actualizar en application.properties
spring.datasource.url=jdbc:mariadb://localhost:{puerto_bd}/bazarboost
spring.datasource.username={su_usuario}
spring.datasource.password={su_contraseña}
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

4. Inicializar Base de Datos

```bash
# Ejecutar BazarBoostTests.java en el directorio /test
# Esto ejecutará schema.sql y data.sql desde /resources
```

5. Lanzar Aplicación

```bash
mvn clean install
mvn spring-boot:run
```

6. Acceder a la Aplicación

```
http://localhost:8080
```

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/bazarboost/
│   │   ├── auth/
│   │   ├── shared/
│   │   └── system/
│   └── resources/
│       ├── estaticos/
│       └── plantillas/
└── test/
    └── java/com/bazarboost/
```

## Mejoras Futuras

- Integración con pasarela de pagos real
- Sistema de notificaciones en tiempo real
- Análisis avanzado de ventas
- Optimización de rendimiento
- Suite completa de pruebas

## Contribuciones

¡Las contribuciones son bienvenidas! Para cambios importantes, por favor abra primero un issue para discutir las modificaciones propuestas.

## Licencia

[MIT](https://choosealicense.com/licenses/mit/)

## Autor

Francisco Williams Jiménez Hernández
