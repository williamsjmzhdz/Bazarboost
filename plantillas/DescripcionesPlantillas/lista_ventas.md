### Descripción de la Vista de **Ventas del Vendedor**

**Nombre de la Vista**: `lista_ventas.html`

**Descripción**:

- Esta vista permite al **vendedor** visualizar todas las ventas realizadas de sus productos. El vendedor puede ver la **lista de productos vendidos**, junto con la **cantidad vendida**, el **total generado** por cada venta, la **fecha** de la venta, y el **nombre del cliente** que compró los productos.
- No se permite **editar** ni **eliminar** las ventas, ya que solo son datos informativos.

**Atributos `data`**:
Cada fila (row) de venta incluirá los siguientes **atributos `data`**:

1. `data-producto-id`: ID del producto vendido.
2. `data-cantidad`: Cantidad de productos vendidos.
3. `data-total`: Total generado por esa venta.
4. `data-fecha`: Fecha en que se realizó la venta.
5. `data-cliente`: Nombre y apellido paterno del cliente que realizó la compra.

**Estructura de la vista**:

1. **Tabla de ventas**:

   - **Nombre del producto**: El nombre del producto vendido.
   - **Cantidad vendida**: La cantidad de productos vendidos.
   - **Total generado**: El monto total de la venta (multiplicación del precio por la cantidad).
   - **Fecha de venta**: La fecha en que se realizó la venta.
   - **Nombre del cliente**: El nombre y apellido del cliente que realizó la compra.

### Funcionalidades:

- **Visualización de ventas**: Muestra de manera clara todas las ventas realizadas por el vendedor, incluyendo los productos vendidos, las cantidades, los totales generados, la fecha de la venta, y los datos del cliente.
- **Organización de ventas**: El vendedor podrá ordenar las ventas por fecha o nombre de producto, si lo deseas implementar.
- **Filtrado opcional**: El vendedor podría filtrar las ventas por un rango de fechas o por un cliente específico (esto es opcional según lo que quieras implementar).

### Resumen:

Esta vista permitirá al **vendedor** tener una visión detallada de sus ventas y de los productos que ha vendido, junto con la cantidad, el total generado, la fecha de la venta, y quién fue el comprador. Es una vista solo informativa, sin opciones de edición ni eliminación de las ventas.
