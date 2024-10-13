### Descripción de la Vista del **Panel de Administración de Usuarios**:

**Nombre de la Vista**: `panel_administracion_usuarios.html`

**Descripción**:

- Esta vista permite al administrador ver una **lista de usuarios** y asignar o quitar el rol de **vendedor**.
- Cada usuario aparece con su **nombre completo** y un **checkbox** que indica si tiene o no el rol de vendedor.
- La lista es **filtrable** por **correo electrónico** o **número de teléfono** utilizando un **campo de búsqueda**. No se realizarán consultas adicionales al backend para buscar, ya que los datos se manejarán desde el **HTML**.

**Atributos `data`**:
Cada fila (row) de usuario incluirá los siguientes **atributos `data`**:

1. `data-id`: ID del usuario.
2. `data-correo-electronico`: Correo electrónico del usuario.
3. `data-telefono`: Número de teléfono del usuario.

Estos atributos permitirán filtrar a los usuarios directamente en el frontend utilizando JavaScript, sin necesidad de hacer consultas adicionales al backend. El filtrado se basará en el correo electrónico o número de teléfono, ocultando los usuarios que no coincidan y mostrando solo aquellos que cumplan con los criterios de búsqueda. Habrá un botón para limpiar la búsqueda y restaurar la lista completa de usuarios. Los cambios realizados en los checkboxes se mantendrán, incluso si se aplica o quita el filtro de la lista.

**Estructura de la vista**:

1. **Barra de búsqueda**: Campo para buscar por **correo electrónico** o **número de teléfono**. El filtro se aplicará a los atributos `data` de cada fila.
2. **Lista de usuarios**: Tabla con dos columnas:
   - **Nombre del usuario**.
   - **Checkbox** para indicar si tiene el rol de vendedor.
3. **Botón "Guardar cambios"**: Al presionar este botón, se envían las actualizaciones necesarias (añadir o quitar el rol de vendedor) al backend.

### Funcionalidades:

- **Filtrado por atributos `data`**: Usar `data-correo-electronico` y `data-telefono` para buscar y filtrar usuarios en el frontend.
- **Modificación de roles**: El estado de los checkboxes indicará qué usuarios tienen el rol de vendedor.
- **Guardar cambios**: Enviar solo las actualizaciones necesarias al backend.

Esta vista te permitirá gestionar los roles de los usuarios de manera eficiente y optimizada.
