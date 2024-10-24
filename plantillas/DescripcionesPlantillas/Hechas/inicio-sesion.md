### Descripción de la Vista de **Iniciar Sesión**

**Nombre de la Vista**: `inicio-sesion.html`

**Descripción**:

- Esta vista permite a los usuarios de **BazarBoost** acceder a su cuenta proporcionando su **correo electrónico** y **contraseña**.
- La vista tendrá un estilo similar al resto de las plantillas de la aplicación, manteniendo la coherencia visual en términos de diseño.
- Los campos estarán validados para asegurar que se ingresen datos correctos, y habrá mensajes de error si las credenciales no son válidas.

**Características de la Vista**:

1. **Formulario de Inicio de Sesión**:

   - El formulario contiene los siguientes campos:
     - **Correo Electrónico**:
       - Validación para asegurar que se introduzca una dirección de correo en un formato válido (por ejemplo, `usuario@dominio.com`).
     - **Contraseña**:
       - Campo para introducir la contraseña asociada a la cuenta.
       - El campo tendrá la opción de mostrar u ocultar la contraseña para facilitar el proceso de entrada.

2. **Botón de Iniciar Sesión**:

   - Al hacer clic en este botón, las credenciales ingresadas se enviarán al backend para validar el inicio de sesión.

3. **Mensajes de Éxito/Error**:

   - En caso de que las credenciales sean correctas, el usuario será redirigido a la página principal del sitio o al panel correspondiente.
   - Si hay un error en el inicio de sesión, se mostrará un mensaje de error:
     - **Error de credenciales**: Si el correo o la contraseña no coinciden con ningún usuario registrado.
     - El mensaje de error desaparecerá automáticamente después de 5 segundos.

4. **Botón "Olvidé mi Contraseña"**:
   - Debajo del formulario habrá un enlace para los usuarios que han olvidado su contraseña. Al hacer clic, serán redirigidos a una vista donde podrán recuperarla.
5. **Enlace para Crear Cuenta**:
   - Si el usuario no tiene una cuenta, habrá un texto pequeño al final del formulario con el mensaje: **"¿No tienes cuenta? Crea una aquí"**, que dirigirá al formulario de registro de cuenta.
   - El enlace debe estar subrayado para facilitar su visibilidad, pero no debe tener negrita.

**Estructura de la Vista**:

1. **Encabezado**:

   - El título será **"Iniciar Sesión"**, con una breve instrucción o mensaje de bienvenida opcional.

2. **Formulario con Campos**:

   - **Correo Electrónico**: Campo para ingresar el correo electrónico registrado.
   - **Contraseña**: Campo de contraseña con la opción de mostrar u ocultar el texto.
   - **Checkbox** para recordar la sesión opcional.

3. **Botones**:
   - **Iniciar Sesión**: Para enviar las credenciales y realizar el login.
   - **Olvidé mi Contraseña**: Enlace para la recuperación de contraseña.
   - **Enlace para Crear Cuenta**: Al final del formulario, un enlace para que el usuario pueda registrarse si no tiene cuenta.

**Validaciones**:

- **Correo Electrónico**: Validación de formato adecuado (por ejemplo, `usuario@dominio.com`).
- **Contraseña**: Validación de que se ha ingresado un valor.

**Mensajes de Éxito/Error**:

- **Éxito**: Si las credenciales son correctas, se redirigirá al usuario a su página de inicio.
- **Error**: Si hay un error en las credenciales, se mostrará un mensaje de error que desaparecerá tras 5 segundos.

**Funcionalidades**:

- **Validación en Tiempo Real**: Los campos serán validados mientras el usuario los llena.
- **Redirección Automática**: Tras un inicio de sesión exitoso, el usuario será redirigido a la página correspondiente.

Esta vista permite a los usuarios iniciar sesión de manera clara y fácil, respetando la integridad y seguridad de los datos.
