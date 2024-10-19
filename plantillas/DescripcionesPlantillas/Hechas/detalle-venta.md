# Detalle de Venta

## Propósito

Esta plantilla muestra los detalles específicos de una venta (o factura) seleccionada, proporcionando información detallada sobre los productos que fueron incluidos en esa venta. La vista permitirá a los usuarios revisar el resumen completo de los productos comprados, su precio unitario, los descuentos aplicados (si los hay), la cantidad de cada producto y el total a pagar por cada uno.

## Componentes clave

1. **Encabezado de la página**

   - Título: "Detalle de Venta".
   - Botón para regresar a la lista de ventas.

2. **Tabla de productos comprados**
   La tabla incluye los siguientes campos por cada producto en la venta:

   - **Nombre del Producto**: Muestra el nombre de cada producto comprado.
   - **Precio Unitario**: El precio sin descuentos.
   - **Descuento**:
     - Muestra el porcentaje de descuento aplicado si existe.
     - Descuento en dinero que se aplica a cada unidad.
   - **Cantidad Comprada**: Cantidad total del producto adquirido en la venta.
   - **Total (por Producto)**:  
     Total de cada producto, calculado tomando en cuenta la cantidad, el precio unitario y el descuento aplicado (si corresponde).

   Esta tabla debe mostrar una lista ordenada de los productos comprados, extraída de la tabla `ProductosFacturas` con la información complementaria de las tablas `Productos` y `Descuentos`.

3. **Totales generales de la venta**

   - Muestra el total de la venta, sumando el total de todos los productos, después de aplicar los descuentos, si corresponden.

4. **Estilos y estructura**

   - La tabla debe tener un estilo moderno, elegante y homogéneo con las demás plantillas de la aplicación.
   - Debe ser responsiva, para adaptarse bien a pantallas móviles y de escritorio.
   - Uso de colores sutiles para destacar los campos importantes como el total y los descuentos aplicados.

5. **Mensajes de advertencia o información**

   - Espacio para mensajes de advertencia o información en caso de errores en la carga de datos o situaciones excepcionales (por ejemplo, si no se pueden cargar los detalles de los productos).

6. **Botones de acción**
   - **Regresar a la lista de ventas**: Para volver a la vista de la lista de ventas.

## Estilo recomendado

Mantener consistencia visual con las demás plantillas de la aplicación. Estilo moderno y minimalista, con colores neutros y resaltados en botones o campos importantes como el total. Las tablas deben ser de fácil lectura, con las celdas alineadas de manera clara para una rápida comprensión.
