### Descripción de la Vista de **Perfil de Usuario**

**Nombre de la Vista**: `perfil_usuario.html`

**Descripción**:

- Esta vista permite al usuario ver y editar su información personal almacenada en la tabla **Usuarios**.
- Los campos mostrados en la vista y que pueden ser editados son:
  - **Nombre**.
  - **Apellido Paterno**.
  - **Apellido Materno**.
  - **Teléfono**.
  - **Correo Electrónico**.
  - **Contraseña**.

**Basado en la Estructura de la Tabla `Usuarios`**:

- Los datos se gestionarán en base a los siguientes atributos del modelo de datos:
  - `nombre` (VARCHAR 40): El nombre del usuario.
  - `apellido_paterno` (VARCHAR 40): El apellido paterno.
  - `apellido_materno` (VARCHAR 40): El apellido materno.
  - `telefono` (VARCHAR 10): El número de teléfono.
  - `correo_electronico` (VARCHAR 80): La dirección de correo electrónico.
  - `contrasenia` (VARCHAR 40): La contraseña del usuario.

**Características de la Vista**:

1. **Formulario Editable**:

   - Todos los campos mencionados en la tabla **Usuarios** serán editables y estarán pre-llenados con los datos del usuario actual.
   - Se implementarán validaciones para garantizar la integridad de los datos:
     - **Teléfono**: Debe tener 10 dígitos y ser único.
     - **Correo Electrónico**: Formato correcto de correo y ser único.
     - **Contraseña**: Requiere confirmación con un campo adicional para verificar que ambas contraseñas coincidan.

2. **Botón de Guardar Cambios**:

   - Al hacer clic en el botón **"Guardar Cambios"**, los datos modificados se enviarán al backend para actualizar la información en la base de datos. Se validarán las restricciones de unicidad para **teléfono** y **correo electrónico**.

3. **Mensajes de Éxito/Error**:

   - Si los cambios se guardan correctamente, aparecerá un **mensaje de éxito** que desaparecerá después de 5 segundos.
   - Si se producen errores (como la duplicidad de **teléfono** o **correo electrónico**), se mostrará un **mensaje de error**, con validaciones visibles en cada campo que no cumpla las condiciones.
   - Los mensajes de éxito/error deben tener animaciones suaves para aparecer y desaparecer.

4. **Botón de Cancelar**:

   - El botón **"Cancelar"** permitirá al usuario descartar los cambios y regresar a la vista anterior sin que se guarde ninguna modificación.

5. **Validaciones en Tiempo Real**:
   - Los campos **teléfono**, **correo electrónico** y **contraseña** tendrán validaciones en tiempo real:
     - **Teléfono**: Debe tener exactamente 10 caracteres numéricos.
     - **Correo Electrónico**: Debe estar en un formato válido (por ejemplo, `usuario@dominio.com`).
     - **Contraseña**: Se debe introducir una confirmación de contraseña.

**Estructura de la Vista**:

1. **Encabezado**:

   - Un título claro: **"Perfil de Usuario"**.
   - Mensajes breves de bienvenida o instrucciones opcionales.

2. **Formulario con Campos**:

   - **Nombre**.
   - **Apellido Paterno**.
   - **Apellido Materno**.
   - **Teléfono**.
   - **Correo Electrónico**.
   - **Contraseña** (con confirmación).

3. **Botones**:
   - **Guardar Cambios**: Para enviar los datos actualizados al backend.
   - **Cancelar**: Para volver a la vista anterior sin guardar los cambios.

**Atributos `data` para validación**:

- Los campos del formulario contendrán atributos `data` como `data-nombre`, `data-apellido-paterno`, etc., para gestionar la validación y la actualización de los datos.

### Funcionalidades:

- **Validación en Tiempo Real**: Los campos serán validados mientras el usuario los llena, mostrando advertencias en caso de formato incorrecto o duplicación de datos.
- **Guardar Cambios**: Al hacer clic en "Guardar Cambios", se enviarán los datos al backend.
- **Mensajes de Éxito/Error**: Se mostrarán mensajes de manera animada, desapareciendo después de 5 segundos.

Esta vista permitirá al usuario gestionar su información personal de forma clara y fácil de usar, manteniendo la integridad de los datos en la base de datos.
