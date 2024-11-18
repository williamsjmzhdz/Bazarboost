# APIs RESTful Documentation

## Table of Contents
- [Categorías API](#categorías-api)
  - [Crear Categoría](#crear-categoría)
  - [Obtener Datos de Edición](#obtener-datos-de-edición)
  - [Obtener Todas las Categorías](#obtener-todas-las-categorías)
  - [Actualizar Categoría](#actualizar-categoría)
  - [Eliminar Categoría](#eliminar-categoría)
- [Descuentos API](#descuentos-api)
  - [Obtener Mis Descuentos](#obtener-mis-descuentos)
  - [Crear Descuento](#crear-descuento)
  - [Actualizar Descuento](#actualizar-descuento)
  - [Eliminar Descuento](#eliminar-descuento)
- [Direcciones API](#direcciones-api)
  - [Crear Dirección](#crear-dirección)
  - [Obtener Todas las Direcciones](#obtener-todas-las-direcciones)
  - [Obtener Datos de Edición](#obtener-datos-de-edición-2)
  - [Actualizar Dirección](#actualizar-dirección)
  - [Eliminar Dirección](#eliminar-dirección)
- [Métodos de Pago API](#métodos-de-pago-api)
  - [Obtener Datos de Edición](#obtener-datos-de-edición-3)
  - [Obtener Todos los Métodos de Pago](#obtener-todos-los-métodos-de-pago)
  - [Crear Método de Pago](#crear-método-de-pago)
  - [Actualizar Método de Pago](#actualizar-método-de-pago)
  - [Eliminar Método de Pago](#eliminar-método-de-pago)

# Categorías API

## Crear Categoría

**Endpoint:** `POST /api/categorias`

**Request Body:**
```json
{
    "nombre": "string"  // Obligatorio, máx 40 caracteres, solo letras, espacios, guiones y apóstrofes
}
```

**Responses:**
- `201 Created` - Categoría creada exitosamente
- `400 Bad Request` - Nombre duplicado o validaciones no cumplidas
- `403 Forbidden` - Usuario sin rol Administrador
- `404 Not Found` - Usuario no encontrado

## Obtener Datos de Edición

**Endpoint:** `GET /api/categorias/{categoriaId}/edicion`

**Path Parameters:**
- `categoriaId` - Integer

**Responses:**
- `200 OK`
```json
{
    "categoriaId": 1,
    "nombre": "Electrónicos"
}
```
- `404 Not Found` - Categoría no encontrada

## Obtener Todas las Categorías

**Endpoint:** `GET /api/categorias`

**Responses:**
- `200 OK`
```json
[
    {
        "categoriaId": 1,
        "nombre": "Electrónicos"
    },
    {
        "categoriaId": 2,
        "nombre": "Ropa"
    }
]
```

## Actualizar Categoría

**Endpoint:** `PUT /api/categorias`

**Request Body:**
```json
{
    "categoriaId": 1,  // Obligatorio, mínimo 0
    "nombre": "string"  // Obligatorio, máx 40 caracteres, solo letras, espacios, guiones y apóstrofes
}
```

**Responses:**
- `200 OK` - Categoría actualizada exitosamente
- `400 Bad Request` - Nombre duplicado o validaciones no cumplidas
- `403 Forbidden` - Usuario sin rol Administrador
- `404 Not Found` - Usuario o categoría no encontrada

## Eliminar Categoría

**Endpoint:** `DELETE /api/categorias/{categoriaId}`

**Path Parameters:**
- `categoriaId` - Integer

**Responses:**
- `204 No Content` - Categoría eliminada exitosamente
- `403 Forbidden` - Usuario sin rol Administrador
- `404 Not Found` - Usuario o categoría no encontrada

# Descuentos API

## Obtener Mis Descuentos

**Endpoint:** `GET /api/descuentos/mis-descuentos`

**Responses:**
- `200 OK`
```json
[
    {
        "descuentoId": 1,
        "porcentaje": 15,
        "nombre": "Descuento Verano"
    }
]
```
- `404 Not Found` - Usuario no encontrado

## Crear Descuento

**Endpoint:** `POST /api/descuentos`

**Request Body:**
```json
{
    "porcentaje": 15,  // Requerido, entre 1 y 100
    "nombre": "string" // Requerido, debe ser único por usuario
}
```

**Responses:**
- `201 Created` - Descuento creado exitosamente
- `400 Bad Request` - Porcentaje inválido o nombre duplicado
- `404 Not Found` - Usuario no encontrado

## Actualizar Descuento

**Endpoint:** `PUT /api/descuentos/{descuentoId}`

**Path Parameters:**
- `descuentoId` - Integer

**Request Body:**
```json
{
    "porcentaje": 20,  // Requerido, entre 1 y 100
    "nombre": "string" // Requerido, debe ser único por usuario
}
```

**Responses:**
- `200 OK` - Descuento actualizado exitosamente
- `400 Bad Request` - Porcentaje inválido o nombre duplicado
- `403 Forbidden` - Descuento no pertenece al usuario
- `404 Not Found` - Descuento o usuario no encontrado

## Eliminar Descuento

**Endpoint:** `DELETE /api/descuentos/{descuentoId}`

**Path Parameters:**
- `descuentoId` - Integer

**Responses:**
- `204 No Content` - Descuento eliminado exitosamente
- `403 Forbidden` - Descuento no pertenece al usuario
- `404 Not Found` - Descuento o usuario no encontrado

# Direcciones API

## Crear Dirección

**Endpoint:** `POST /api/direcciones`

**Request Body:**
```json
{
    "estado": "string",      // Obligatorio, máx 40 caracteres, solo letras, espacios, guiones y apóstrofes
    "ciudad": "string",      // Obligatorio, máx 40 caracteres, solo letras, espacios, guiones y apóstrofes
    "colonia": "string",     // Obligatorio, máx 40 caracteres, solo letras, espacios, guiones y apóstrofes
    "calle": "string",       // Obligatorio, máx 60 caracteres, solo letras, espacios, guiones y apóstrofes
    "numeroDomicilio": 123,  // Obligatorio, mínimo 0
    "codigoPostal": "12345"  // Obligatorio, exactamente 5 dígitos
}
```

**Responses:**
- `201 Created` - Dirección creada exitosamente
- `400 Bad Request` - Validaciones no cumplidas
- `404 Not Found` - Usuario no encontrado

## Obtener Todas las Direcciones

**Endpoint:** `GET /api/direcciones`

**Responses:**
- `200 OK`
```json
[
    {
        "direccionId": 1,
        "estado": "Jalisco",
        "ciudad": "Guadalajara",
        "colonia": "Centro",
        "calle": "Juárez",
        "numeroDomicilio": 123,
        "codigoPostal": "44100"
    }
]
```
- `404 Not Found` - Usuario no encontrado

## Obtener Datos de Edición

**Endpoint:** `GET /api/direcciones/{direccionId}/edicion`

**Path Parameters:**
- `direccionId` - Integer

**Responses:**
- `200 OK`
```json
{
    "direccionId": 1,
    "estado": "Jalisco",
    "ciudad": "Guadalajara",
    "colonia": "Centro",
    "calle": "Juárez",
    "numeroDomicilio": 123,
    "codigoPostal": "44100"
}
```
- `404 Not Found` - Dirección no encontrada o no pertenece al usuario

## Actualizar Dirección

**Endpoint:** `PUT /api/direcciones`

**Request Body:**
```json
{
    "direccionId": 1,        // Obligatorio, mínimo 0
    "estado": "string",      // Obligatorio, máx 40 caracteres, solo letras, espacios, guiones y apóstrofes
    "ciudad": "string",      // Obligatorio, máx 40 caracteres, solo letras, espacios, guiones y apóstrofes
    "colonia": "string",     // Obligatorio, máx 40 caracteres, solo letras, espacios, guiones y apóstrofes
    "calle": "string",       // Obligatorio, máx 60 caracteres, solo letras, espacios, guiones y apóstrofes
    "numeroDomicilio": 123,  // Obligatorio, mínimo 0
    "codigoPostal": "12345"  // Obligatorio, exactamente 5 dígitos
}
```

**Responses:**
- `200 OK` - Dirección actualizada exitosamente
- `400 Bad Request` - Validaciones no cumplidas
- `404 Not Found` - Dirección no encontrada o no pertenece al usuario

## Eliminar Dirección

**Endpoint:** `DELETE /api/direcciones/{direccionId}`

**Path Parameters:**
- `direccionId` - Integer, obligatorio, mínimo 1

**Responses:**
- `204 No Content` - Dirección eliminada exitosamente
- `400 Bad Request` - ID de dirección inválido
- `404 Not Found` - Dirección no encontrada o no pertenece al usuario

# Métodos de Pago API

## Obtener Datos de Edición

**Endpoint:** `GET /api/metodos-pago/{metodoPagoId}/edicion`

**Path Parameters:**
- `metodoPagoId` - Integer

**Responses:**
- `200 OK`
```json
{
    "metodoPagoId": 1,
    "nombreTitular": "Juan Pérez",
    "numeroTarjeta": "4111111111111111",
    "fechaExpiracion": "2025-12-31",
    "tipoTarjeta": "CREDITO",
    "monto": 1000.00
}
```
- `404 Not Found` - Método de pago no encontrado o no pertenece al usuario

## Obtener Todos los Métodos de Pago

**Endpoint:** `GET /api/metodos-pago`

**Responses:**
- `200 OK`
```json
[
    {
        "metodoPagoId": 1,
        "nombreTitular": "Juan Pérez",
        "terminacion": "1111",
        "fechaExpiracion": "12/2025",
        "tipo": "CREDITO",
        "monto": 1000.00
    }
]
```
- `404 Not Found` - Usuario no encontrado

## Crear Método de Pago

**Endpoint:** `POST /api/metodos-pago`

**Request Body:**
```json
{
    "nombreTitular": "string",     // Obligatorio, máx 120 caracteres, solo letras, espacios, guiones y apóstrofes
    "numeroTarjeta": "string",     // Obligatorio, entre 13 y 19 dígitos
    "fechaExpiracion": "2025-12-31", // Obligatorio, debe ser fecha futura
    "tipoTarjeta": "CREDITO",      // Obligatorio, enum TipoTarjeta
    "monto": 1000.00              // Obligatorio, no negativo, máx 8 enteros y 2 decimales
}
```

**Responses:**
- `201 Created` - Método de pago creado exitosamente
- `400 Bad Request` 
  - Validaciones no cumplidas
  - Número de tarjeta duplicado
- `404 Not Found` - Usuario no encontrado

## Actualizar Método de Pago

**Endpoint:** `PUT /api/metodos-pago`

**Request Body:**
```json
{
    "metodoPagoId": 1,           // Obligatorio, mínimo 0
    "nombreTitular": "string",     // Obligatorio, máx 120 caracteres, solo letras, espacios, guiones y apóstrofes
    "numeroTarjeta": "string",     // Obligatorio, entre 13 y 19 dígitos
    "fechaExpiracion": "2025-12-31", // Obligatorio, debe ser fecha futura
    "tipoTarjeta": "CREDITO",      // Obligatorio, enum TipoTarjeta
    "monto": 1000.00              // Obligatorio, no negativo, máx 8 enteros y 2 decimales
}
```

**Responses:**
- `200 OK` - Método de pago actualizado exitosamente
- `400 Bad Request`
  - Validaciones no cumplidas
  - Número de tarjeta duplicado
- `404 Not Found` - Método de pago no encontrado o no pertenece al usuario

## Eliminar Método de Pago

**Endpoint:** `DELETE /api/metodos-pago/{metodoPagoId}`

**Path Parameters:**
- `metodoPagoId` - Integer, obligatorio, mínimo 1

**Responses:**
- `204 No Content` - Método de pago eliminado exitosamente
- `400 Bad Request` - ID del método de pago inválido
- `404 Not Found` - Método de pago no encontrado o no pertenece al usuario
