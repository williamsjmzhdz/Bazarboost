# Cambios para incluir información de descuento en el detalle de una factura

### 1. Cambios en Base de Datos
**Tabla ProductosFacturas (schema.sql)**
- Agregar columnas:
    - `descuento_unitario_porcentaje` (INT, permite NULL)
    - `descuento_unitario_valor` (DECIMAL(10,2), permite NULL)
    - `descuento_nombre` (VARCHAR, permite NULL)

**Datos de Prueba (data.sql)**
- Agregar valores para las nuevas columnas:
    - Porcentajes de descuentos unitarios
    - Valores de descuentos unitarios
    - Nombres de descuentos

### 2. Cambios en Entidades y DTOs
**Entidad ProductoFactura**
- Agregar campos:
    - `descuentoUnitarioPorcentaje` (Integer)
    - `descuentoUnitarioValor` (BigDecimal)
    - `descuentoNombre` (String)

**ProductoPagoDTO**
- Agregar campos:
    - `descuentoId`
    - `descuentoUnitarioPorcentaje`

**CarritoProductoDTO**
- Agregar campo:
    - `descuentoId` (si aplica)

### 3. Cambios en JavaScript
**carrito-compras.js**
- En función `crearFilaProducto()`:
    - Agregar descuentoId al dataset (si aplica)
    - Agregar descuentoUnitarioPorcentaje al dataset (si aplica)

- En callback de `procederPagoButton` (línea 339):
    - Agregar descuentoId (si aplica)
    - Agregar descuentoUnitarioPorcentaje al dataset (si aplica)

### 4. Cambios en Servicios
**FacturaServiceImpl**
- En método `calcularPrecioConDescuento()` (línea 203):
    - Verificar coincidencia entre:
        1. Porcentaje y ID del descuento (DTO vs BD)
        2. Descuento vs producto
    - Lanzar excepciones si:
        - No coinciden porcentaje e ID
        - Descuento no coincide con producto
    - Calcular valor con descuento si todo es correcto

- En método `guardarDetallesFacturas`:
    - Guardar información de descuentos:
        - Porcentaje unitario (si aplica)
        - Valor unitario (si aplica)
        - Nombre del descuento (si aplica)