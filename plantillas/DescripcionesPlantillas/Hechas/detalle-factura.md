# Plantilla: Detalle de Factura

## Descripción:

La plantilla **Detalle de Factura** está diseñada para mostrar el desglose de los productos comprados en una factura específica. Esta vista es accedida desde la lista de facturas y detalla la información relevante de cada producto adquirido, incluyendo nombre del producto, precio unitario, descuento unitario, cantidad comprada, descuento total y total con descuento aplicado. Además, cuenta con un botón para regresar a la lista de facturas.

## Secciones principales:

1. **Encabezado de la factura:**

   - Se muestra el número de la factura, la fecha de emisión y el nombre del cliente que realizó la compra.

2. **Tabla de productos adquiridos:**

   - La tabla lista los productos comprados en la factura con las siguientes columnas:
     - **Producto:** Nombre del producto.
     - **Precio Unitario:** Precio original por unidad.
     - **Descuento Unitario:** Cantidad de dinero descontada por unidad y el porcentaje aplicado.
     - **Cantidad:** Número de unidades compradas del producto.
     - **Total sin descuento:** Total original del producto (Precio Unitario \* Cantidad).
     - **Descuento total:** Suma del descuento total aplicado (Descuento Unitario \* Cantidad).
     - **Total con descuento:** El total del producto después de aplicar el descuento.

3. **Botón de navegación:**

   - Un botón que permite regresar a la lista de facturas, ubicado al final de la tabla.

4. **Resumen de la factura:**
   - Sección que muestra el subtotal (sin descuentos), total de descuentos y el total final con descuentos aplicados.

## Componentes HTML:

- Tabla con estilos consistentes con otras vistas del sitio, particularmente la tabla de carrito de compras.
- Botón para regresar a la lista de facturas al final de la tabla.
- Uso de `data-attributes` para asociar información relevante a cada producto.

## Requisitos visuales:

- Diseño limpio y moderno que prioriza la claridad de la información.
- Colores suaves con un fondo blanco para las filas de la tabla.
- Las filas de la tabla deben ser claramente diferenciadas y se debe usar una tipografía fácil de leer.
- Espacios bien definidos entre las filas y columnas para facilitar la lectura.

## Requerimientos adicionales:

- La tabla debe ser **responsiva**: si la tabla no cabe en la pantalla, se debe mostrar una barra de desplazamiento horizontal para que los usuarios puedan ver todo el contenido sin que se distorsione el diseño.
- El botón "Regresar a lista de facturas" debe ser fácilmente visible, con colores contrastantes que inviten a la interacción.

## Posibles estados:

- **Sin productos en la factura:** Si la factura no tiene productos asociados (caso raro), se muestra un mensaje indicando "No hay productos en esta factura".
- **Errores o advertencias:** La plantilla no presenta mensajes de error específicos ya que solo se trata de una vista de detalle estática.

## Ejemplo de implementación:

```html
<!-- Estructura general de la tabla de productos -->
<table class="table table-striped table-hover">
  <thead class="table-dark">
    <tr>
      <th>Producto</th>
      <th>Precio Unitario</th>
      <th>Descuento Unitario</th>
      <th>Cantidad</th>
      <th>Total sin descuento</th>
      <th>Descuento Total</th>
      <th>Total con descuento</th>
    </tr>
  </thead>
  <tbody>
    <!-- Cada fila representa un producto en la factura -->
    <tr data-producto="Laptop XYZ">
      <td>Laptop XYZ</td>
      <td>$1000.00</td>
      <td>$100 (10%)</td>
      <td>2</td>
      <td>$2000.00</td>
      <td>$200.00</td>
      <td>$1800.00</td>
    </tr>
    <!-- Más productos aquí -->
  </tbody>
</table>

<!-- Botón para regresar a la lista de facturas -->
<a href="lista_factura.html" class="btn btn-secondary">
  <i class="bi bi-arrow-left"></i> Regresar a la lista de facturas
</a>

<!-- Resumen de la factura -->
<div class="resumen-factura">
  <p><strong>Subtotal:</strong> $2000.00</p>
  <p><strong>Total de descuentos:</strong> $200.00</p>
  <p><strong>Total a pagar:</strong> $1800.00</p>
</div>
```
