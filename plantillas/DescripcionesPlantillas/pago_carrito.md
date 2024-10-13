### Descripción de la Vista de **Pago del Carrito**

**Nombre de la Vista**: `pago_carrito.html`

**Descripción**:

- En esta vista, el usuario podrá **finalizar la compra** de los productos agregados al carrito seleccionando una **dirección de envío** y un **método de pago**.
- Solo se mostrarán las **direcciones** y **métodos de pago** asociados al usuario.
- Habrá dos botones principales:
  - **"Pagar carrito"**: Procesa el pago y realiza verificaciones de fondos y validez del método de pago.
  - **"Volver al carrito"**: Redirige al usuario de nuevo a la vista del carrito sin realizar el pago.
- Al hacer clic en **"Pagar carrito"**, se verificarán:
  1. **Fondos suficientes** en el método de pago.
  2. **Validez de la tarjeta**, asegurando que no esté expirada.
- Si alguna verificación falla, se mostrará un **mensaje de error**:
  - Fondos insuficientes.
  - Tarjeta expirada.
- Si las verificaciones son exitosas:
  1. Se generará una **factura**.
  2. Se crearán los **registros de productos factura**, asociando los productos del carrito a la factura.
  3. El usuario será **redirigido al detalle de la factura** recién generada.

**Estructura de la Vista**:

1. **Selección de dirección**: Lista de direcciones del usuario para seleccionar una como dirección de envío.
2. **Selección de método de pago**: Lista de métodos de pago asociados al usuario, mostrando el nombre del titular, número de tarjeta (parcialmente oculto), fecha de expiración, y fondos disponibles.
3. **Botón "Pagar carrito"**: Ejecuta el pago si las verificaciones de fondos y fecha de expiración son exitosas.
4. **Botón "Volver al carrito"**: Permite volver al carrito sin vaciarlo ni realizar el pago.

**Verificaciones**:

- **Fondos suficientes**: Verifica que el método de pago tenga fondos suficientes para cubrir el total del carrito.
- **Fecha de expiración**: Verifica que el método de pago no esté expirado.

**Proceso de Pago**:

- Si todas las verificaciones pasan, se crea la factura y sus productos asociados, y el usuario es redirigido al **detalle de la factura generada**.

---

### Resumen:

En esta vista, el usuario seleccionará una dirección y un método de pago para **proceder al pago** de los productos del carrito. Se verificarán los **fondos disponibles** y la **validez de la tarjeta**. Si el pago es exitoso, se generará una factura y el usuario será redirigido al **detalle de la factura**. Si hay errores, se mostrarán mensajes indicativos de los problemas encontrados (fondos insuficientes o tarjeta expirada).
