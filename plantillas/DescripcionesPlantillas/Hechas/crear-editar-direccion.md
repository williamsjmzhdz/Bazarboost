# Descripción de la plantilla **crear_editar_direccion.html**

La plantilla **crear_editar_direccion.html** tiene como propósito permitir a los usuarios crear o editar una dirección asociada a su cuenta. Esta vista debe incluir un formulario que contenga los campos necesarios para ingresar la información de la dirección. Debe seguir las reglas de negocio establecidas por la base de datos **Direcciones**.

## Estructura General

1. **Barra de navegación**:

   - La plantilla debe incluir la barra de navegación global del sitio, similar a las otras plantillas, asegurando una experiencia homogénea para los usuarios.

2. **Formulario de Dirección**:

   - El formulario debe contener los siguientes campos:

     - **Estado** (input tipo texto)
     - **Ciudad** (input tipo texto)
     - **Colonia** (input tipo texto)
     - **Calle** (input tipo texto)
     - **Número de domicilio** (input tipo número)
     - **Código postal** (input tipo texto)

   - Cada campo debe ser obligatorio y validado correctamente, con mensajes de error que se muestren cuando se ingresen datos incorrectos o incompletos.
   - Se debe manejar la validación en el lado del cliente, y en caso de error, mostrar mensajes de advertencia adecuados.

3. **Mensajes de Advertencia**:

   - La plantilla debe incluir una sección para mostrar mensajes de advertencia en caso de errores de validación de los campos. Esto es similar a lo que hemos implementado en las plantillas anteriores, como **crear_editar_metodo_pago.html**.
   - Los mensajes de advertencia pueden ser:
     - Campos faltantes o inválidos.
     - Errores en el formato del código postal o en el número de domicilio.

4. **Botones de acción**:

   - **Guardar dirección**: Al hacer clic, el usuario enviará los datos ingresados para crear o editar la dirección.
   - **Cancelar**: Un botón para regresar a la vista **lista_direcciones.html** sin guardar cambios.

5. **Sección de mensajes de éxito**:
   - Debe haber una sección que muestre un mensaje de éxito al guardar la dirección correctamente.

## Estilo

- La plantilla debe mantener el estilo limpio, moderno y elegante que hemos aplicado en todas las demás vistas del sitio.
- Los elementos del formulario deben estar bien alineados y ser accesibles en dispositivos móviles.
- Se debe utilizar el estilo uniforme de botones que hemos empleado anteriormente.
