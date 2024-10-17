### Descripción de la Vista de **Crear/Editar Descuento**

**Nombre de la Vista**: `crear_editar_descuento.html`

**Descripción**:

- Esta vista permite al **vendedor** crear un **nuevo descuento** o **editar un descuento existente** en su catálogo.
- La vista variará según si se está **creando** un nuevo descuento o **editando** uno existente. Si es un nuevo descuento, los campos estarán vacíos; si es una edición, los campos estarán precargados con los datos actuales del descuento.

**Atributos `data`**:

- Si es una edición, la vista incluirá los siguientes **atributos `data`**:
  1. `data-descuento-id`: ID del descuento a editar (solo presente si es una edición).
  2. `data-usuario-id`: ID del usuario (vendedor) que posee el descuento.

Estos atributos se utilizan para manejar correctamente la creación o edición del descuento en el backend.

**Estructura de la vista**:

1. **Formulario de descuento**:

   - **Nombre del descuento** (input de texto).
   - **Porcentaje de descuento** (input numérico, validado para estar entre 0 y 100).

2. **Botón "Guardar"**: Al hacer clic en este botón, se enviarán los datos del formulario al backend para crear o actualizar el descuento.

   - Si es un nuevo descuento, se enviará una solicitud de **creación**.
   - Si es una edición, se enviará una solicitud de **actualización** con el ID del descuento correspondiente.

3. **Botón "Cancelar"**: Este botón llevará al usuario de vuelta a la **lista de descuentos** sin realizar cambios.

### Funcionalidades:

- **Crear Descuento**: Si se trata de un nuevo descuento, el vendedor podrá llenar todos los campos y guardarlo. Se enviará una solicitud de creación al backend.
- **Editar Descuento**: Si se está editando un descuento, los campos estarán precargados con la información actual, y cualquier cambio será enviado al backend para actualizar el descuento.
- **Validación**: El porcentaje de descuento debe estar entre 0 y 100, y el nombre debe ser único para cada vendedor.

### Resumen:

Esta vista permitirá al vendedor **crear nuevos descuentos** o **editar descuentos existentes**, asegurándose de que la información sea válida y acorde a las restricciones de negocio.
