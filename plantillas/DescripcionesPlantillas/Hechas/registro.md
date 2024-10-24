### Descripción de la Vista de **Registro de Usuario**

**Nombre de la Vista**: `registro.html`

**Descripción**:

- La plantilla de **registro de usuario** permitirá a los nuevos usuarios crear su cuenta en **BazarBoost** ingresando la información solicitada en el formulario, que coincide con los campos de la tabla **Usuarios**. El formulario debe incluir validaciones en tiempo real para garantizar que los datos se ingresen correctamente antes de enviarse.
- Además, la vista tendrá mensajes de éxito y error que guiarán al usuario en caso de que ocurra algún problema durante el registro.

**Estructura de la Plantilla**:

1. **Barra de Navegación**:

   - La barra de navegación se presentará en la parte superior, con el logo o texto **BazarBoost** como siempre.
   - Sin embargo, a diferencia de otras vistas, no incluirá los enlaces regulares como "Carrito" o "Cliente". Solo mostrará un enlace que diga **"Iniciar sesión"**, que redirigirá a la vista `inicio-sesion.html` para los usuarios que ya tengan una cuenta.

2. **Formulario de Registro**:
   - Los campos del formulario coincidirán con los atributos de la tabla **Usuarios**:
     - **Nombre**: (input de texto).
     - **Apellido Paterno**: (input de texto).
     - **Apellido Materno**: (input de texto).
     - **Teléfono**: (input de texto, longitud exacta de 10 caracteres).
     - **Correo Electrónico**: (input de texto, validación de formato de email).
     - **Contraseña**: (input de contraseña, validación de longitud y formato).
     - **Confirmar Contraseña**: (input de contraseña, validación para asegurarse de que coincida con la contraseña).
3. **Validaciones**:

   - Todos los campos del formulario tendrán validación en tiempo real. Se mostrarán advertencias si el usuario intenta ingresar datos incorrectos o si falta información.
   - **Teléfono**: Se debe validar que tenga 10 caracteres y que sea único.
   - **Correo Electrónico**: Se validará el formato del correo y que no esté duplicado en la base de datos.
   - **Contraseña**: Se validará que tenga una longitud mínima de 8 caracteres y que coincida con la confirmación de contraseña.

4. **Mensajes de Éxito y Error**:

   - En la parte superior del formulario, se mostrarán mensajes de éxito o error con animaciones suaves para que no sean bruscos.
   - **Mensaje de Éxito**: Si el registro es exitoso, aparecerá un mensaje verde con el texto **"¡Registro exitoso! Bienvenido a BazarBoost."**, y redirigirá al usuario al inicio de sesión.
   - **Mensaje de Error**: Si ocurre algún error (como un correo electrónico o teléfono duplicado, o algún campo no cumple con las validaciones), se mostrará un mensaje rojo con el texto **"Hubo un error en el registro. Verifica tus datos e inténtalo nuevamente."**.

5. **Botones**:

   - **Registrarse**: Este botón enviará el formulario para crear la cuenta después de pasar todas las validaciones.
   - **Cancelar**: Opción para descartar los datos del formulario y regresar a la página de inicio de sesión.

6. **Sección Inferior (Iniciar Sesión)**:
   - Al final del formulario, aparecerá el texto **"¿Ya tienes cuenta? Inicia sesión"**, con un enlace que llevará a la página `inicio-sesion.html` para que los usuarios que ya tengan cuenta puedan iniciar sesión directamente.
   - Este enlace será discreto, en texto pequeño y alineado al centro como es común en las vistas de registro de muchas páginas.

**Atributos `data` para Validaciones**:

- Los inputs del formulario tendrán atributos `data` como `data-nombre`, `data-apellido-paterno`, `data-correo-electronico`, etc., para facilitar la gestión de validaciones y mensajes.

### Funcionalidades:

1. **Validación en Tiempo Real**:

   - A medida que el usuario complete el formulario, las validaciones se realizarán en tiempo real. Si algún campo no cumple con los requisitos, se mostrará una advertencia debajo del campo correspondiente.
   - Los mensajes de error estarán visibles hasta que el campo sea corregido.

2. **Mensajes de Éxito/Error con Animación**:

   - Los mensajes de éxito o error aparecerán y desaparecerán con animaciones suaves para mejorar la experiencia del usuario.

3. **Redirección**:
   - Después de un registro exitoso, el sistema redirigirá automáticamente al usuario a la vista de **inicio de sesión**.

### Estructura de la Vista:

1. **Encabezado**:

   - Un título claro: **"Crear Cuenta"**.
   - Instrucciones breves para el usuario.

2. **Formulario con Campos**:

   - **Nombre**.
   - **Apellido Paterno**.
   - **Apellido Materno**.
   - **Teléfono**.
   - **Correo Electrónico**.
   - **Contraseña** (con confirmación de contraseña).

3. **Botones**:

   - **Registrarse**: Botón principal para crear la cuenta.
   - **Cancelar**: Botón para descartar el registro y regresar a la página de inicio de sesión.

4. **Mensaje Inferior**:
   - **"¿Ya tienes cuenta? Inicia sesión"**: Enlace a la plantilla de inicio de sesión.

Esta vista permitirá a los nuevos usuarios registrarse de manera clara y eficiente, manteniendo un estilo homogéneo con las demás plantillas de **BazarBoost**.
