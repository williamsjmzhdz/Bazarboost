# Lista de Métodos de Pago

## Descripción

Esta plantilla corresponde a la vista en la que los usuarios pueden gestionar sus métodos de pago registrados. Cada método de pago se mostrará en una tabla que incluye información relevante como el nombre del titular, número de tarjeta, fecha de expiración, tipo de tarjeta (crédito o débito) y el monto asociado a cada método de pago.

## Secciones

### Barra de Navegación

Debe incluir una barra de navegación fija en la parte superior con los siguientes elementos:

- Inicio
- Carrito
- Métodos de Pago (opción activa)
- Perfil del usuario (con opciones de perfil y cierre de sesión)

### Tabla de Métodos de Pago

La sección principal es una tabla donde se listarán todos los métodos de pago asociados al usuario. Cada fila debe contener la siguiente información:

- **Nombre del Titular:** El nombre del titular de la tarjeta (campo `nombre_titular`).
- **Número de Tarjeta:** El número de la tarjeta (campo `numero_tarjeta`). Solo deben mostrarse los últimos 4 dígitos de la tarjeta por seguridad.
- **Fecha de Expiración:** La fecha de expiración del método de pago (campo `fecha_expiracion`).
- **Tipo de Tarjeta:** El tipo de tarjeta, que puede ser de **Crédito** o **Débito** (campo `tipo_tarjeta`).
- **Monto Disponible:** El monto total disponible en el método de pago (campo `monto`).

Cada método de pago debe incluir dos botones:

- **Editar:** Permite al usuario editar los detalles del método de pago (nombre, número de tarjeta, fecha de expiración, etc.).
- **Eliminar:** Permite eliminar el método de pago. Al hacer clic en este botón, debe aparecer una ventana modal que solicite la confirmación del usuario antes de eliminar.

### Botón Agregar Método de Pago

En la parte superior de la tabla, se debe incluir un botón que permita agregar un nuevo método de pago. Al hacer clic en este botón, el usuario será redirigido a una plantilla para agregar un nuevo método de pago (la creación de esta plantilla se realizará en un paso posterior).

## Comportamiento de la Plantilla

- **Edición de Método de Pago:** Al hacer clic en el botón **Editar** de una fila específica, se abrirá un formulario en el que el usuario podrá modificar los detalles del método de pago.
- **Eliminación de Método de Pago:** Al hacer clic en el botón **Eliminar**, se mostrará una ventana modal centrada que solicitará la confirmación del usuario para proceder con la eliminación. Si el usuario confirma, el método de pago se eliminará de la base de datos.
- **Validaciones:** La plantilla debe validar que los campos no sean nulos y que el monto sea mayor o igual a 0 (según la restricción de la base de datos). Las validaciones deben reflejarse en la interfaz de usuario con mensajes claros y visibles.

## Diseño Visual

La plantilla debe tener un diseño limpio, moderno y alineado con las demás plantillas del sitio. El estilo visual de la tabla debe ser coherente con el estilo general del sitio, manteniendo una estructura clara y fácil de entender.

- **Tabla:** Debe ser elegante y utilizar bordes sutiles con líneas que separen claramente cada fila.
- **Botones de Acción:** Los botones de editar y eliminar deben ser fáciles de identificar, con colores que indiquen su función (por ejemplo, azul para editar y rojo para eliminar).
- **Barra de Navegación:** Debería ser coherente con el diseño del sitio y mantenerse fija en la parte superior de la página.
