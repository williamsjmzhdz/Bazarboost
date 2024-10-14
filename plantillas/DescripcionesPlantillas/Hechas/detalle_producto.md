### Descripción de la Vista de **Detalle del Producto** (ajustada)

**Nombre de la Vista**: `detalle_producto.html`

**Descripción**:

- Esta vista permite a los usuarios ver **todos los detalles** de un producto específico. La vista se genera cuando el usuario hace clic en la tarjeta del producto desde la lista de productos.
- Se mostrará la información completa del producto, incluyendo la **imagen**, **nombre**, **descripción**, **precio**, **existencia**, y la **categoría**, provenientes de la tabla de productos.
- Además, se mostrará la **calificación promedio** del producto, calculada con base en las reseñas dejadas por otros usuarios.
- Habrá una sección donde se mostrarán las **reseñas** hechas por otros usuarios, con la **calificación** y el **comentario**. El usuario podrá ver las reseñas, pero **no podrá eliminarlas ni editarlas**, ya que solo puede gestionar su propia reseña.
- Si el usuario ya ha dejado una reseña para este producto, podrá **editar** o **eliminar** su reseña. Si el usuario no ha dejado ninguna reseña, podrá **agregar una nueva reseña**.
- Solo se permite una **única reseña por producto** por cada usuario, de acuerdo a la estructura de la base de datos.

**Atributos `data`** (en la vista del detalle del producto):

1. `data-producto-id`: ID del producto.
2. `data-nombre`: Nombre del producto.
3. `data-descripcion`: Descripción detallada del producto.
4. `data-precio`: Precio del producto.
5. `data-categoria`: Categoría del producto.
6. `data-existencia`: Cantidad disponible de productos.
7. `data-imagen-url`: URL de la imagen del producto.
8. `data-promedio-calificacion`: Calificación promedio del producto.

**Estructura de la vista**:

1. **Sección de información del producto**:

   - **Imagen del producto**: Se mostrará la imagen completa del producto.
   - **Nombre del producto**: Se mostrará el nombre completo del producto.
   - **Descripción**: Detalles completos del producto, destacando sus características principales.
   - **Precio**: Se mostrará el precio del producto.
   - **Existencia**: Se indicará cuántas unidades están disponibles.
   - **Categoría**: Se mostrará la categoría a la que pertenece el producto.
   - **Calificación promedio**: Se mostrará la calificación promedio del producto basada en las reseñas de otros usuarios.

2. **Sección de reseñas**:

   - **Calificación del producto**: Se mostrará la calificación promedio del producto (de 1 a 5 estrellas).
   - **Comentarios/reseñas de otros usuarios**: Se mostrarán las reseñas de otros usuarios que ya hayan calificado el producto. Cada reseña incluirá:
     - **Nombre del usuario**.
     - **Calificación** (en estrellas).
     - **Comentario**.
     - **Fecha** del comentario.
   - **Agregar o gestionar reseña del usuario actual**:
     - Si el usuario **no ha dejado una reseña**, aparecerá un formulario para que el usuario agregue una reseña nueva con una calificación de 1 a 5 estrellas y un comentario.
     - Si el usuario ya ha dejado una reseña, tendrá las opciones de:
       - **Editar** su reseña mediante un formulario.
       - **Eliminar** su reseña mediante un botón.

3. **Botón de agregar/quitar del carrito**:

   - Si el producto **no está en el carrito**, se mostrará el botón **"Agregar al carrito"**.
   - Si el producto **ya está en el carrito**, se mostrará el botón **"Quitar del carrito"**.

4. **Botón "Volver a la lista de productos"**:
   - Un botón para regresar a la vista principal de la lista de productos.

---

### Funcionalidades:

1. **Ver detalles completos del producto**: Los usuarios podrán ver toda la información del producto, como descripción, imagen, precio, categoría, y cantidad disponible.
2. **Agregar/Quitar del carrito**: Los usuarios podrán agregar el producto al carrito si no lo tienen agregado, o quitarlo si ya está en el carrito.
3. **Ver reseñas de otros usuarios**: Los usuarios podrán ver las reseñas dejadas por otros usuarios, junto con la calificación del producto.
4. **Agregar, editar o eliminar reseña propia**:
   - **Agregar reseña**: Si el usuario no ha dejado una reseña, se mostrará un formulario para agregar una nueva reseña.
   - **Editar reseña**: Si el usuario ya ha dejado una reseña, tendrá la opción de editarla.
   - **Eliminar reseña**: Si el usuario ya ha dejado una reseña, tendrá la opción de eliminarla.

### Consideración sobre la restricción de reseñas:

- Un usuario **solo puede dejar una reseña** por producto, y esta reseña puede ser **editada o eliminada**. No se permiten múltiples reseñas por usuario para el mismo producto, de acuerdo a la estructura de la base de datos.
