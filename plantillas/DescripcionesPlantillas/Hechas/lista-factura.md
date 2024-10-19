### Descripción de la Vista de **Lista de Facturas del Usuario**

**Nombre de la Vista**: `lista_facturas.html`

**Descripción**:

- Esta vista permite al usuario ver **todas sus facturas**, asociadas a las compras realizadas en el sistema.
- Se mostrarán únicamente las **facturas correspondientes al usuario autenticado** en la sesión.
- Las facturas se presentarán en una **lista**, mostrando:
  - **Fecha** en la que se generó la factura.
  - **Monto total** de la compra.
- El usuario podrá **ordenar las facturas por monto total**, de:
  - **Mayor a menor**.
  - **Menor a mayor**.
- Cada factura en la lista será **clicable**, lo que permitirá al usuario ser redirigido al **detalle de la factura** para ver la información completa de la compra.
- En esta vista no habrá opción para **crear**, **editar** ni **eliminar** facturas, ya que estas se generan automáticamente cuando se realiza un pago completo.

**Estructura de la Vista**:

1. **Lista de Facturas**:

   - Cada factura en la lista incluirá:
     - **Fecha** de creación de la factura.
     - **Monto total** de la compra.
   - El usuario podrá hacer clic en cada factura para acceder al **detalle de la compra**.

2. **Ordenar por Monto Total**:
   - El usuario podrá ordenar las facturas por el monto total de la compra, eligiendo entre:
     - **Mayor a menor**.
     - **Menor a mayor**.

**Funcionalidades**:

1. **Visualización de Facturas**: Muestra todas las facturas del usuario autenticado en una lista ordenada por fecha.
2. **Ordenación por Monto Total**: Permite al usuario ordenar las facturas por el monto total, en orden ascendente o descendente.
3. **Acceso al Detalle de la Factura**: Cada factura en la lista es clicable y redirige al usuario al **detalle de esa factura**.

---

### Resumen:

La **vista de facturas** permitirá al usuario ver una lista completa de sus facturas, con opción de ordenarlas por el **monto total**. Cada factura podrá ser seleccionada para ver su **detalle completo**. No hay opciones para crear, editar o eliminar facturas, ya que estas se generan automáticamente al completar un pago.
