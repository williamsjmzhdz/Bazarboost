### Descripción de la Vista del **Panel de Administración de Descuentos del Vendedor**

**Nombre de la Vista**: `panel_administracion_descuentos.html`

**Descripción**:

- Esta vista permite al **vendedor** gestionar sus **descuentos**. El vendedor puede ver una **lista de los descuentos** que ha creado, con opciones para **crear**, **editar** o **eliminar** descuentos.
- Cada descuento aparece listado con su **nombre** y el **porcentaje** de descuento aplicado.
- El vendedor podrá agregar un **nuevo descuento** a través de un botón que llevará al formulario de creación de descuentos.

**Atributos `data`**:
Cada fila (row) de descuento incluirá los siguientes **atributos `data`**:

1. `data-descuento-id`: ID del descuento.
2. `data-nombre`: Nombre del descuento.
3. `data-porcentaje`: Porcentaje del descuento.

Estos atributos permitirán manejar los descuentos de forma dinámica en el frontend. Los descuentos se cargarán en la vista y se podrán editar o eliminar eficientemente.

**Estructura de la vista**:

1. **Botón "Añadir descuento"**: Un botón en la parte superior que llevará al formulario para crear un nuevo descuento.
2. **Lista de descuentos**: Tabla con las siguientes columnas:

   - **Nombre del descuento**.
   - **Porcentaje del descuento**.
   - **Opciones**: Botones de **Editar** y **Eliminar**.

3. **Botón "Guardar cambios"**: Después de realizar modificaciones, este botón enviará las actualizaciones necesarias al backend (como eliminar o editar descuentos).

### Funcionalidades:

- **Edición de descuentos**: Cada descuento tendrá un botón de edición que permitirá modificar los detalles del descuento, como el nombre y el porcentaje.
- **Eliminación de descuentos**: El vendedor podrá eliminar un descuento, siempre y cuando le pertenezca.
- **Agregar nuevos descuentos**: El botón "Añadir descuento" abrirá un formulario donde el vendedor podrá crear un nuevo descuento.

### Resumen:

Esta vista permitirá al **vendedor** gestionar sus descuentos de manera eficiente, con opciones claras para crear, editar y eliminar los descuentos en su catálogo personal.
