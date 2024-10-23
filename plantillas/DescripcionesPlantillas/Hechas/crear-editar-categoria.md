# Descripción de la plantilla `crear-editar-categoria.html`

## Propósito

La plantilla `crear-editar-categoria.html` está diseñada para permitir al usuario crear o editar una categoría en la plataforma BazarBoost. Esta vista será utilizada tanto para agregar nuevas categorías como para modificar las existentes.

## Estructura

### 1. **Formulario de creación/edición**

La plantilla incluye un formulario que contiene un campo para el nombre de la categoría, ya que es el único dato relevante para las categorías en la base de datos.

- **Campo de nombre de la categoría**: Este campo será de tipo texto y obligatorio. Se incluirán validaciones para asegurar que el campo no esté vacío y que no exceda los 40 caracteres.

### 2. **Botones**

La plantilla contendrá dos botones:

- **Guardar**: Este botón permite enviar el formulario y guardar los cambios.
  - Si es una categoría nueva, se creará un registro en la base de datos.
  - Si se está editando una categoría existente, se actualizarán los datos en la base de datos.
- **Volver a la lista de categorías**: Un botón secundario que permitirá al usuario volver a la lista de categorías sin guardar los cambios.

Ambos botones tendrán íconos correspondientes para facilitar la comprensión visual, y su diseño será consistente con el estilo de botones utilizado en las demás plantillas de la plataforma.

### 3. **Sección para mensajes de validación**

La plantilla incluirá una sección para mostrar mensajes de advertencia o éxito. Estos mensajes se mostrarán cuando ocurra alguna de las siguientes situaciones:

- **Advertencias**:

  - El campo de nombre de la categoría está vacío.
  - Se ha excedido el límite de caracteres permitido (40 caracteres).

- **Éxito**:
  - La categoría ha sido creada o actualizada exitosamente.

### 4. **Estilo**

La plantilla utilizará el archivo de estilos `base-creacion-edicion.css` para asegurar consistencia con las demás vistas de creación/edición en la plataforma. El diseño seguirá un estilo limpio y moderno, acorde al resto del sistema.

## Funcionalidades Clave

1. **Crear y Editar**: Los usuarios podrán crear nuevas categorías o editar categorías existentes.
2. **Validaciones en el frontend**: Se realizarán validaciones básicas como el campo obligatorio para el nombre de la categoría.
3. **Mensajes de retroalimentación**: El sistema proporcionará retroalimentación visual inmediata, tanto de errores de validación como de éxito al guardar los cambios.

## Interactividad

- El botón de "Guardar" activará el envío del formulario para guardar los cambios realizados.
- El botón de "Volver a la lista de categorías" permitirá regresar a la vista de lista sin guardar cambios.

## Consideraciones Técnicas

- La plantilla está diseñada para integrarse con el backend que maneja las operaciones de CRUD (Crear, Leer, Actualizar, Eliminar) para la entidad de `Categorias`.
- El nombre de la categoría debe ser único en la base de datos, y la plantilla manejará el error de duplicados con un mensaje adecuado.
