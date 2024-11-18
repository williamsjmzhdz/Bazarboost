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
