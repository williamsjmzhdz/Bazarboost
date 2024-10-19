# Descripción de la Plantilla: `crear_editar_metodo_pago.html`

## Propósito

La plantilla `crear_editar_metodo_pago.html` permitirá al usuario crear o editar un método de pago en su cuenta. El formulario de esta plantilla seguirá el modelo de la tabla `MetodosPago`, brindando campos que permitan al usuario introducir o modificar los datos requeridos de un método de pago, como el nombre del titular, número de tarjeta, fecha de expiración, tipo de tarjeta (crédito o débito) y el monto disponible.

## Estructura del Formulario

El formulario estará compuesto por los siguientes campos:

1. **Nombre del Titular** (`nombre_titular`): Un campo de texto donde el usuario deberá ingresar el nombre que aparece en la tarjeta. Este campo será obligatorio.
2. **Número de Tarjeta** (`numero_tarjeta`): Un campo de texto para ingresar el número de la tarjeta. El número de tarjeta debe ser único y validarse como un número de entre 13 a 19 dígitos.
3. **Fecha de Expiración** (`fecha_expiracion`): Un campo de fecha que el usuario podrá seleccionar desde un calendario emergente.
4. **Tipo de Tarjeta** (`tipo_tarjeta`): Un campo tipo `select` que permitirá al usuario seleccionar entre los valores `Crédito` o `Débito`.
5. **Monto Disponible** (`monto`): Un campo de tipo numérico en el que el usuario podrá ingresar la cantidad disponible en la tarjeta. El monto no puede ser negativo.

## Mensajes de Advertencia

En caso de que ocurra algún error en el ingreso de los datos, como un formato incorrecto o un campo obligatorio vacío, se mostrará un mensaje de advertencia similar al de otras plantillas. La plantilla contará con una sección que permanecerá oculta la mayor parte del tiempo, pero se mostrará cuando haya un error de validación o cualquier otra advertencia.

### Ejemplos de Validaciones

- "El número de tarjeta es inválido. Debe contener entre 13 y 19 dígitos."
- "La fecha de expiración ya ha pasado."
- "El nombre del titular es obligatorio."

## Botones de Acción

1. **Guardar Cambios**: Un botón principal para enviar los datos del formulario y crear o editar el método de pago.
2. **Volver a la Lista de Métodos de Pago**: Un botón adicional para regresar a la vista `lista-metodos-pago.html`, permitiendo al usuario volver sin guardar los cambios.

## Estilo

La plantilla seguirá el estilo homogéneo y elegante que hemos utilizado en las plantillas anteriores. Tendrá un diseño moderno, limpio y bien organizado. El formulario tendrá un formato centrado y utilizará los estilos de Bootstrap para garantizar su buen funcionamiento en diferentes tamaños de pantalla.

El diseño será responsivo, asegurando que en dispositivos móviles el formulario y los botones se adapten correctamente, manteniendo una disposición clara y fácil de utilizar.
