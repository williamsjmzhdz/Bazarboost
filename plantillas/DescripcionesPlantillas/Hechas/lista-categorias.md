# Plantilla `lista-categorias.html`

## Descripción

La plantilla `lista-categorias.html` mostrará una lista de todas las categorías de productos existentes en el sistema, permitiendo al usuario visualizarlas, editarlas o eliminarlas. Esta plantilla se utilizará en el panel de administración para que el administrador pueda gestionar las categorías de productos.

### Estructura de la plantilla:

1. **Encabezado de la página**:
   - Debe contener un título claro que indique que el usuario está en la sección de "Lista de Categorías".
2. **Sección de mensajes**:

   - Un mensaje de éxito o advertencia, que se mostrará en caso de que alguna acción se haya realizado correctamente o si ocurre un error. El mensaje de éxito puede mostrarse al añadir, editar o eliminar una categoría. Si ocurre algún error, se mostrará una advertencia.

3. **Botón para añadir una nueva categoría**:

   - Un botón prominente con el texto "Agregar Categoría", que llevará al usuario a una nueva página o ventana para crear una nueva categoría.

4. **Tabla de categorías**:

   - La lista de categorías se mostrará en forma de tabla, donde cada fila representará una categoría.
   - La tabla debe tener las siguientes columnas:
     - **Nombre de la Categoría**: Muestra el nombre de la categoría.
     - **Acciones**: Contendrá botones para editar o eliminar la categoría.

5. **Acciones**:

   - **Editar**: El botón "Editar" permitirá al usuario modificar el nombre de una categoría.
   - **Eliminar**: El botón "Eliminar" abrirá una ventana modal para confirmar la eliminación de la categoría.

6. **Ventana Modal para la confirmación de eliminación**:

   - Cuando el usuario haga clic en "Eliminar", se mostrará una ventana modal centrada que pedirá la confirmación para eliminar la categoría. Esta ventana debe tener un mensaje claro y botones para confirmar o cancelar la eliminación.

7. **Responsividad**:
   - La plantilla debe ser completamente responsive, ajustándose a pantallas pequeñas como las de móviles y tabletas. Al visualizarse en dispositivos móviles, la tabla de categorías deberá ajustarse para facilitar la lectura y las acciones deben ser fácilmente accesibles.

## Reglas de negocio:

- Solo el administrador puede acceder a esta vista.
- El nombre de la categoría debe ser único y no puede estar vacío. En caso de error, se debe mostrar un mensaje de advertencia.
- No se debe permitir eliminar una categoría que esté asociada a productos activos.
