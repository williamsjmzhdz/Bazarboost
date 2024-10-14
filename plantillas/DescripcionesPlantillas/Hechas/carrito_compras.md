### Descripción de la Vista de **Carrito de Compras**

**Nombre de la Vista**: `carrito_compras.html`

**Descripción**:

- Esta vista muestra **todos los productos que el usuario ha agregado a su carrito**.
- Cada producto aparecerá en una **fila** que contendrá el **nombre del producto**, el **precio por unidad**, la **cantidad actual en el carrito** (que el usuario podrá modificar), y el **total por ese producto** (precio por unidad multiplicado por la cantidad).
- El usuario podrá **eliminar un producto por completo** del carrito haciendo clic en un botón específico de esa fila (por ejemplo, un icono de basurero).
- Para cada producto, habrá un **input numérico** (Number Input) que permitirá al usuario ajustar la **cantidad** del producto. Este input permitirá valores a partir de 1 (número mínimo) hasta cualquier cantidad que el usuario desee (aunque luego se verificará si hay suficiente existencia disponible).
- Cuando el usuario modifique la cantidad de un producto, se enviará una **llamada asíncrona** al backend para actualizar la cantidad del producto en el carrito en tiempo real, sin necesidad de recargar la página.
- Habrá un botón que permita al usuario **volver a la lista de productos** sin vaciar el carrito.
- También habrá un botón para **proceder al pago**, que llevará al usuario a una vista para completar la compra.

**Atributos `data`**:
Cada fila de producto incluirá los siguientes **atributos `data`**:

1. `data-producto-id`: ID del producto.
2. `data-nombre`: Nombre del producto.
3. `data-precio`: Precio por unidad del producto.
4. `data-cantidad`: Cantidad actual en el carrito del usuario.
5. `data-total`: Total calculado (precio por unidad \* cantidad).

**Estructura de la vista**:

1. **Lista de productos en el carrito**:

   - Los productos se mostrarán en una tabla o lista, y cada fila contendrá:
     - **Nombre del producto**: Texto con el nombre completo del producto.
     - **Precio por unidad**: Mostrará el precio unitario de cada producto.
     - **Cantidad**: Un input numérico que permitirá al usuario modificar la cantidad de ese producto en el carrito. El input aceptará valores a partir de 1. Cada vez que el usuario cambie este valor, se enviará una llamada al backend para actualizar la cantidad en el carrito.
     - **Total por producto**: Mostrará el precio total de ese producto en función de la cantidad actual.
     - **Botón de eliminar producto**: Un icono o botón que permitirá al usuario quitar completamente el producto del carrito.

2. **Botones de acción**:

   - **Botón "Volver a la lista de productos"**: Este botón permitirá al usuario volver a la vista de la lista de productos sin vaciar el carrito.
   - **Botón "Proceder al pago"**: Este botón llevará al usuario a la vista de pago, donde podrá confirmar su compra y elegir métodos de pago.

**Estructura de la fila de producto**:

Cada fila de producto tendrá la siguiente estructura:

- **Nombre del producto**.
- **Precio unitario**.
- **Input numérico** para modificar la cantidad.
- **Total por producto**.
- **Botón eliminar** para quitar el producto del carrito.

**Estructura general de la vista**:

1. **Tabla o lista de productos**: Donde cada fila muestra la información detallada del producto, incluyendo la cantidad y el total.
2. **Botón para regresar a la lista de productos**: No elimina los productos del carrito, simplemente redirige a la vista de productos.
3. **Botón para proceder al pago**: Redirige a la vista de pago.

---

### Funcionalidades:

1. **Modificar la cantidad de productos en el carrito**:

   - El usuario puede ajustar la cantidad de productos en el carrito usando un **input numérico**. El valor mínimo permitido es **1**.
   - Cuando el valor se modifica, se realiza una **llamada asíncrona al backend** para actualizar la cantidad de ese producto en el carrito.
   - Si el número ingresado es inválido o superior a la existencia disponible, la validación ocurrirá en la siguiente fase (cuando se haga la verificación de stock).

2. **Eliminar un producto del carrito**:

   - El usuario puede eliminar cualquier producto del carrito haciendo clic en un botón específico (como un icono de **basurero**). Esto eliminará el producto completamente del carrito.

3. **Volver a la lista de productos**:

   - Al hacer clic en el botón **"Volver a la lista de productos"**, el usuario regresará a la página de listado de productos sin vaciar su carrito.

4. **Proceder al pago**:
   - El botón **"Proceder al pago"** llevará al usuario a la siguiente vista para completar la compra.

---

### Resumen:

La vista del **carrito de compras** permitirá al usuario ver todos los productos que ha agregado al carrito, con opciones para **modificar la cantidad** de cada producto o **eliminar productos**. El usuario podrá hacer estos cambios de manera dinámica, ya que los cambios se enviarán al backend a través de llamadas asíncronas. Además, el usuario podrá optar por **volver a la lista de productos** o **proceder al pago**.
