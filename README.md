# Documentación de API - CRUD Categorías

## Crear Categoría
- **Método:** POST
- **Path:** `/api/categorias`
- **Body:**
```json
{
    "nombre": "string"  // Obligatorio, máx 40 caracteres, solo letras, espacios, guiones y apóstrofes
}
```

### Excepciones
- **403 Forbidden:** Usuario no tiene rol de Administrador
- **404 Not Found:** Usuario no encontrado
- **400 Bad Request:** Nombre de categoría duplicado
- **400 Bad Request:** Validaciones de formato no cumplidas

### Respuesta
- **201 Created**

---

## Obtener Datos de Edición
- **Método:** GET
- **Path:** `/api/categorias/{categoriaId}/edicion`
- **Parámetros de Ruta:**
  - `categoriaId`: Integer

### Excepciones
- **404 Not Found:** Categoría no encontrada

### Respuesta
- **200 OK**
```json
{
    "categoriaId": 1,
    "nombre": "Electrónicos"
}
```

---

## Obtener Todas las Categorías
- **Método:** GET
- **Path:** `/api/categorias`

### Respuesta
- **200 OK**
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

---

## Actualizar Categoría
- **Método:** PUT
- **Path:** `/api/categorias`
- **Body:**
```json
{
    "categoriaId": 1,  // Obligatorio, mínimo 0
    "nombre": "string"  // Obligatorio, máx 40 caracteres, solo letras, espacios, guiones y apóstrofes
}
```

### Excepciones
- **403 Forbidden:** Usuario no tiene rol de Administrador
- **404 Not Found:** Usuario o categoría no encontrada
- **400 Bad Request:** Nombre de categoría duplicado
- **400 Bad Request:** Validaciones de formato no cumplidas

### Respuesta
- **200 OK**

---

## Eliminar Categoría
- **Método:** DELETE
- **Path:** `/api/categorias/{categoriaId}`
- **Parámetros de Ruta:**
  - `categoriaId`: Integer

### Excepciones
- **403 Forbidden:** Usuario no tiene rol de Administrador
- **404 Not Found:** Usuario o categoría no encontrada

### Respuesta
- **204 No Content**

---

# Documentación API - CRUD Descuentos

## Obtener Mis Descuentos
- **Método:** GET
- **Path:** `/api/descuentos/mis-descuentos`

### Excepciones
- **404 Not Found:** Usuario no encontrado

### Respuesta
- **200 OK**
```json
[
    {
        "descuentoId": 1,
        "porcentaje": 15,
        "nombre": "Descuento Verano"
    }
]
```

---

## Crear Descuento
- **Método:** POST
- **Path:** `/api/descuentos`
- **Body:**
```json
{
    "porcentaje": 15,  // Requerido, entre 1 y 100
    "nombre": "string" // Requerido, debe ser único por usuario
}
```

### Excepciones
- **404 Not Found:** Usuario no encontrado
- **400 Bad Request:** Porcentaje inválido o fuera de rango
- **400 Bad Request:** Nombre de descuento duplicado para el usuario

### Respuesta
- **201 Created**

---

## Actualizar Descuento
- **Método:** PUT
- **Path:** `/api/descuentos/{descuentoId}`
- **Parámetros de Ruta:**
  - `descuentoId`: Integer
- **Body:**
```json
{
    "porcentaje": 20,  // Requerido, entre 1 y 100
    "nombre": "string" // Requerido, debe ser único por usuario
}
```

### Excepciones
- **404 Not Found:** Descuento o usuario no encontrado
- **403 Forbidden:** Descuento no pertenece al usuario
- **400 Bad Request:** Porcentaje inválido o fuera de rango
- **400 Bad Request:** Nombre de descuento duplicado para el usuario

### Respuesta
- **200 OK**

---

## Eliminar Descuento
- **Método:** DELETE
- **Path:** `/api/descuentos/{descuentoId}`
- **Parámetros de Ruta:**
  - `descuentoId`: Integer

### Excepciones
- **404 Not Found:** Descuento o usuario no encontrado
- **403 Forbidden:** Descuento no pertenece al usuario

### Respuesta
- **204 No Content**
