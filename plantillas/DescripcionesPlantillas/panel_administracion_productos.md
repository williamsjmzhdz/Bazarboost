### Descripción de la Vista del **Panel de Administración de Productos del Vendedor**:

**Nombre de la Vista**: `panel_administracion_productos.html`

**Descripción**:

- Esta vista permite al usuario vendedor gestionar sus **productos**. El vendedor podrá ver una **lista de productos** que ha creado, con opciones para **crear**, **editar** o **eliminar** productos.
- Cada producto aparece listado con su **nombre** y sus respectivas opciones de **editar** y **eliminar**.
- Se incluye un **botón** para **añadir nuevos productos** que lleva al formulario de creación de productos.

**Atributos `data`**:
Cada fila (row) de producto incluirá los siguientes **atributos `data`**:

1. `data-producto-id`: ID del producto.
2. `data-nombre`: Nombre del producto.
3. `data-precio`: Precio del producto.
4. `data-existencia`: Cantidad en existencia del producto.

Estos atributos permitirán manejar los productos de forma dinámica en el frontend. Los productos se cargarán en la vista y se permitirá realizar acciones como **editar** o **eliminar** productos de forma eficiente, sin la necesidad de hacer consultas adicionales hasta que se guarden los cambios.

**Estructura de la vista**:

1. **Botón "Añadir producto"**: Un botón en la parte superior que lleva a un formulario para crear un nuevo producto.
2. **Lista de productos**: Tabla con varias columnas que muestran:
   - **Nombre del producto**.
   - **Precio**.
   - **Existencia** (cantidad disponible).
   - **Opciones**: Botones de **Editar** y **Eliminar**.
3. **Botón "Guardar cambios"**: Después de hacer modificaciones, el botón envía las actualizaciones necesarias (como eliminar o editar productos) al backend.

### Funcionalidades:

- **Edición de productos**: Cada producto tendrá un botón de edición que permitirá actualizar sus detalles, como nombre, precio y cantidad.
- **Eliminación de productos**: Botón para eliminar productos, asegurando que solo se eliminen los productos del usuario logueado.
- **Agregar nuevos productos**: El botón "Añadir producto" abrirá un formulario para crear un nuevo producto.

Esta vista permitirá al **vendedor** gestionar sus productos de manera eficiente, con opciones claras para crear, editar y eliminar productos en su catálogo personal.
