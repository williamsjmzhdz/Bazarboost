### Descripción de la Vista de **Lista de Productos**

**Nombre de la Vista**: `lista_productos.html`

**Descripción**:

- Esta vista muestra **todos los productos disponibles** en el sistema, es decir, aquellos productos que **tienen existencia** mayor a cero.
- Los usuarios podrán **filtrar por categoría** y **ordenar los productos por precio** de manera **ascendente** o **descendente**.
- Los filtros son **combinables**, por lo que los usuarios podrán aplicar ambos filtros simultáneamente. Por ejemplo, podrán seleccionar una categoría específica (por ejemplo, **Juguetes**) y ordenar los productos de esa categoría de menor a mayor precio o viceversa.
- Cada producto se mostrará en una **tarjeta**, la cual contendrá:
  - **Información relevante del producto** (nombre, precio, descripción).
  - La **calificación promedio** del producto, basada en las reseñas que haya recibido de otros usuarios.
  - Un botón para **agregar o quitar del carrito**.
- Habrá un **botón para limpiar los filtros** y volver a mostrar la lista de productos completa y sin ningún filtro aplicado.

**Atributos `data`**:
Cada tarjeta de producto incluirá los siguientes **atributos `data`**:

1. `data-producto-id`: ID del producto.
2. `data-nombre`: Nombre del producto.
3. `data-precio`: Precio del producto.
4. `data-categoria`: Categoría a la que pertenece el producto.
5. `data-existencia`: Existencia disponible del producto.
6. `data-promedio-calificacion`: Calificación promedio basada en las reseñas de los usuarios.

**Estructura de la vista**:

1. **Barra de filtros**:

   - **Filtro por categoría**: Un menú desplegable que permite al usuario seleccionar la categoría de los productos que desea ver.
   - **Ordenar por precio**:
     - Opción para ordenar los productos de **menor a mayor precio**.
     - Opción para ordenar los productos de **mayor a menor precio**.
   - **Botón "Limpiar filtros"**: Este botón permite restablecer todos los filtros y volver a mostrar la lista completa de productos sin ningún filtro aplicado.

2. **Lista de productos**:

   - Los productos se mostrarán en **tarjetas** con la siguiente información:
     - **Imagen del producto**.
     - **Nombre del producto**.
     - **Descripción** del producto (puede ser breve).
     - **Precio** del producto.
     - **Calificación promedio** (mostrada como estrellas o texto, basada en las reseñas).
     - **Existencia disponible** (opcional, si quieres mostrarla).
     - **Botón "Agregar al carrito"**: Si el producto no está en el carrito, se muestra el botón para agregarlo.
     - **Botón "Quitar del carrito"**: Si el producto ya está en el carrito, se muestra el botón para quitarlo.

3. **Botón de agregar/quitar del carrito**:

   - Dependiendo de si el producto ya está en el carrito o no, el botón cambiará su funcionalidad:
     - Si **no está** en el carrito, el botón mostrará **"Agregar al carrito"**.
     - Si **ya está** en el carrito, el botón mostrará **"Quitar del carrito"**.

4. **Botón "Limpiar filtros"**:
   - Al hacer clic en este botón, todos los filtros aplicados (categoría, orden por precio) se restablecerán, mostrando nuevamente **todos los productos disponibles** en el sistema sin ningún filtro aplicado.

### Funcionalidades:

1. **Filtro por categoría**: Los usuarios podrán filtrar los productos por una categoría específica usando un menú desplegable. Solo se mostrarán los productos que pertenezcan a esa categoría y tengan existencia.
2. **Ordenar por precio**: Los usuarios podrán ordenar los productos por precio de manera ascendente (de menor a mayor) o descendente (de mayor a menor). Este filtro puede combinarse con el de categoría.
3. **Agregar o quitar del carrito**: Cada producto tendrá un botón para agregarlo al carrito. Si el producto ya está en el carrito, se mostrará un botón para quitarlo.
4. **Calificación promedio**: En cada tarjeta de producto se mostrará la **calificación promedio** del producto, calculada a partir de las reseñas de los usuarios. La calificación puede mostrarse como estrellas o como un valor numérico (por ejemplo: "4.5 de 5").
5. **Limpiar filtros**: Al hacer clic en el botón de "Limpiar filtros", se quitarán todos los filtros y se volverán a mostrar todos los productos disponibles en el sistema, en su orden original.

---

### Resumen:

Esta vista permite a los usuarios navegar por **todos los productos disponibles** en el sistema. Los filtros de **categoría** y **precio** pueden combinarse para refinar los resultados según las preferencias del usuario. Además, cada tarjeta de producto muestra la **calificación promedio** basada en las reseñas dejadas por otros usuarios. Los usuarios pueden agregar o quitar productos de su carrito, y pueden restablecer la lista completa de productos utilizando el **botón de limpiar filtros**.
