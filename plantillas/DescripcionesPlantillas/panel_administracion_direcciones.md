### Descripción de la Vista de **Administración de Direcciones**

**Nombre de la Vista**: `panel_administracion_direcciones.html`

**Descripción**:

- Esta vista permite a los **usuarios** (clientes, vendedores, administradores) **gestionar sus direcciones**. Los usuarios podrán ver la **lista de direcciones** que tienen registradas, con opciones para **editar** o **eliminar** cada una.
- Además, habrá un botón para **añadir una nueva dirección**. Los usuarios solo podrán gestionar sus propias direcciones, y no las de otros usuarios.

**Atributos `data`**:
Cada fila (row) de dirección incluirá los siguientes **atributos `data`**:

1. `data-direccion-id`: ID de la dirección.
2. `data-estado`: Estado de la dirección.
3. `data-ciudad`: Ciudad de la dirección.
4. `data-colonia`: Colonia de la dirección.
5. `data-calle`: Calle de la dirección.
6. `data-numero`: Número de domicilio.
7. `data-codigo-postal`: Código postal.

Estos atributos permitirán gestionar las direcciones dinámicamente en el frontend y ayudarán a identificar cada dirección individual.

**Estructura de la vista**:

1. **Botón "Añadir dirección"**: Un botón en la parte superior de la vista que llevará al formulario para crear una nueva dirección.
2. **Lista de direcciones**:

   - Cada dirección se mostrará en una tabla con las siguientes columnas:
     - **Estado**.
     - **Ciudad**.
     - **Colonia**.
     - **Calle**.
     - **Número de domicilio**.
     - **Código postal**.
     - **Opciones**:
       - **Botón "Editar"**: Permite editar la dirección seleccionada.
       - **Botón "Eliminar"**: Permite eliminar la dirección seleccionada.

3. **Botón "Guardar cambios"**: No es necesario un botón "Guardar cambios" en esta vista, ya que cada acción de editar o eliminar se ejecutará inmediatamente.

### Funcionalidades:

- **Edición de direcciones**: Cada dirección tendrá un botón de edición que permitirá modificar los detalles de la dirección.
- **Eliminación de direcciones**: Cada dirección tendrá un botón de eliminación que permitirá eliminar la dirección si le pertenece al usuario.
- **Agregar nuevas direcciones**: El botón "Añadir dirección" abrirá un formulario donde el usuario podrá crear una nueva dirección.

### Resumen:

Esta vista permitirá a los usuarios gestionar sus direcciones de forma eficiente, con la capacidad de ver, añadir, editar y eliminar las direcciones que tienen registradas en el sistema.
