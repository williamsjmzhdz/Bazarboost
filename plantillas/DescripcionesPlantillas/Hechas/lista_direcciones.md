# Lista de Direcciones

## Descripción General

La plantilla `lista_direcciones.html` está diseñada para mostrar una lista de todas las direcciones asociadas a un usuario en particular. El usuario podrá visualizar todas las direcciones que ha registrado y tendrá la opción de **editar** o **eliminar** cualquier dirección que desee. Además, habrá un botón para **agregar una nueva dirección**.

La vista será similar a `lista_metodos_pago.html`, pero con columnas adaptadas a la información específica de las direcciones registradas, siguiendo el modelo de la tabla SQL proporcionado.

## Elementos Visuales

1. **Navbar:**
   - Se usará el mismo navbar común a todas las plantillas.
2. **Título de la página:**

   - Un encabezado `h2` que dice **"Lista de Direcciones"**.

3. **Botón "Agregar Dirección":**

   - Un botón de color verde ubicado encima de la tabla que permitirá al usuario agregar una nueva dirección.

4. **Tabla de Direcciones:**

   - La tabla mostrará las siguientes columnas:
     - **Estado**
     - **Ciudad**
     - **Colonia**
     - **Calle**
     - **Número**
     - **Código Postal**
     - **Acciones** (para editar y eliminar la dirección)
   - Cada fila de la tabla representará una dirección y contendrá dos botones:
     - Un botón azul para **editar** la dirección.
     - Un botón rojo para **eliminar** la dirección.
   - La tabla debe ser **responsiva** y mostrar un scroll horizontal cuando no quepa en la pantalla.

5. **Botones de Acción:**
   - **Editar:** Permite al usuario modificar una dirección existente.
   - **Eliminar:** Muestra una ventana modal solicitando confirmación antes de eliminar la dirección.

## Interacciones

- **Agregar Dirección:** Redirige a una nueva vista para agregar una dirección.
- **Editar Dirección:** Abre un formulario para modificar los datos de una dirección seleccionada.
- **Eliminar Dirección:** Despliega una ventana modal para confirmar la eliminación de una dirección.

## Funcionalidades adicionales

1. **Scroll horizontal en tabla:** La tabla debe mostrar un scroll horizontal en pantallas pequeñas o cuando los datos no quepan en el ancho disponible.
2. **Ventana Modal de Confirmación:** Cuando el usuario intente eliminar una dirección, se mostrará una ventana modal pidiendo confirmación antes de proceder con la eliminación.

## Estilo y Diseño

- La plantilla seguirá el estilo **elegante y moderno** que hemos aplicado en las otras vistas, con colores consistentes y uso adecuado de espacios.
- El diseño será **responsivo**, optimizado para dispositivos móviles, tabletas y computadoras de escritorio.

## Reglas de Validación

- Si no hay direcciones disponibles, se mostrará un mensaje indicando que no hay direcciones registradas, junto con un botón para agregar una nueva.
- En caso de un error al intentar eliminar o editar una dirección, se mostrará un mensaje de error correspondiente en la parte superior de la tabla.
