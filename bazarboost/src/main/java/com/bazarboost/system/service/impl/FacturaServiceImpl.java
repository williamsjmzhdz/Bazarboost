package com.bazarboost.system.service.impl;

import com.bazarboost.shared.exception.*;
import com.bazarboost.system.repository.*;
import com.bazarboost.system.service.FacturaService;
import com.bazarboost.system.dto.*;
import com.bazarboost.system.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
public class FacturaServiceImpl implements FacturaService {
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoCarritoRepository productoCarritoRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private ProductoFacturaRepository productoFacturaRepository;
    @Autowired
    private DescuentoRepository descuentoRepository;

    @Override
    @Transactional
    public CarritoPagoRespuestaDTO procesarPago(CarritoPagoSolicitudDTO solicitudDTO, Integer usuarioId) {
        log.debug("Iniciando proceso de pago para usuario {}.", usuarioId);
        validarCarrito(solicitudDTO);

        Usuario usuario = obtenerUsuario(usuarioId);
        BigDecimal precioTotal = calcularPrecioTotalYVerificarCantidades(solicitudDTO, usuarioId);
        log.debug("Precio total calculado: {} para usuario {}.", precioTotal, usuarioId);

        MetodoPago metodoPago = obtenerMetodoPagoValido(solicitudDTO.getMetodoPagoId(), usuario);
        Direccion direccion = obtenerDireccion(solicitudDTO.getDireccionId(), usuario);

        procesarTransaccion(solicitudDTO, precioTotal, metodoPago);
        Factura factura = generarFactura(precioTotal, usuario, metodoPago, direccion, solicitudDTO.getProductos());
        vaciarCarrito(usuarioId);

        log.debug("Pago procesado exitosamente. Factura generada: {} para usuario {}.", factura.getFacturaId(), usuarioId);
        return new CarritoPagoRespuestaDTO(factura.getFacturaId());
    }

    @Override
    @Transactional(readOnly = true)
    public FacturasPaginadasDTO obtenerFacturasPaginadasYOrdenadas(String ordenarPor, String direccionOrden,
                                                                   Integer pagina, Integer tamanoPagina,
                                                                   Integer usuarioId) {
        log.debug("Obteniendo facturas paginadas para usuario {}. Página: {}, Tamaño: {}, Orden: {} {}",
                usuarioId, pagina, tamanoPagina, ordenarPor, direccionOrden);

        Usuario usuario = obtenerUsuario(usuarioId);
        validarRolCliente(usuario);
        validarParametrosOrdenamiento(ordenarPor, direccionOrden);

        long totalFacturas = facturaRepository.countByUsuario(usuario);
        log.debug("Total de facturas encontradas: {} para usuario {}", totalFacturas, usuarioId);

        if (totalFacturas == 0) {
            return crearRespuestaVacia(pagina);
        }

        validarPaginacion(pagina, tamanoPagina, totalFacturas);
        return obtenerFacturasPaginadas(usuario, ordenarPor, direccionOrden, pagina, tamanoPagina);
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleFacturaDTO obtenerDetalleFactura(Integer facturaId, Integer usuarioId) {
        log.debug("Obteniendo detalle de factura {} para usuario {}", facturaId, usuarioId);

        Usuario usuario = obtenerUsuario(usuarioId);
        validarRolCliente(usuario);

        Factura factura = obtenerFactura(facturaId);
        validarPropietarioFactura(factura, usuarioId);

        List<ProductoFactura> productosFactura = obtenerProductosFactura(factura);
        List<DetalleFacturaProductoDTO> productosDTO = productosFactura.stream()
                .map(this::mapearADetalleFacturaProductoDTO)
                .toList();

        log.debug("Detalle de factura {} obtenido exitosamente con {} productos", facturaId, productosDTO.size());
        return new DetalleFacturaDTO(
                factura.getFacturaId(),
                factura.getFecha(),
                BigDecimal.valueOf(factura.getTotal()),
                productosDTO
        );
    }

    @Override
    @Transactional(readOnly = true)
    public VentasPaginadasDTO obtenerVentasPaginadasYOrdenadas(String ordenarPor, String direccionOrden,
                                                               Integer pagina, Integer tamanoPagina,
                                                               Integer vendedorId) {
        log.debug("Obteniendo ventas paginadas para vendedor {}. Página: {}, Tamaño: {}, Orden: {} {}",
                vendedorId, pagina, tamanoPagina, ordenarPor, direccionOrden);

        Usuario vendedor = obtenerUsuario(vendedorId);
        validarRolVendedor(vendedor);
        validarParametrosOrdenamiento(ordenarPor, direccionOrden);

        long totalVentas = productoFacturaRepository.countByProductoUsuario(vendedor);
        log.debug("Total de ventas encontradas: {} para vendedor {}", totalVentas, vendedorId);

        if (totalVentas == 0) {
            return new VentasPaginadasDTO(List.of(), pagina, 0, 0L, true, true);
        }

        validarPaginacion(pagina, tamanoPagina, totalVentas);
        return obtenerVentasPaginadas(vendedor, ordenarPor, direccionOrden, pagina, tamanoPagina);
    }

    private FacturasPaginadasDTO crearRespuestaVacia(Integer pagina) {
        log.debug("Creando respuesta vacía para página {}", pagina);
        return new FacturasPaginadasDTO(List.of(), pagina, 0, 0, true, true);
    }

    private void validarRolVendedor(Usuario usuario) {
        log.debug("Validando rol de vendedor para usuario {}", usuario.getUsuarioId());
        if (!usuarioRepository.tieneRol(usuario.getUsuarioId(), "Vendedor")) {
            log.debug("Usuario {} no tiene rol de vendedor", usuario.getUsuarioId());
            throw new AccesoDenegadoException("No puedes acceder al panel de ventas porque no tienes el rol de vendedor.");
        }
    }

    private VentasPaginadasDTO obtenerVentasPaginadas(Usuario vendedor, String ordenarPor,
                                                      String direccionOrden, Integer pagina, Integer tamanoPagina) {
        log.debug("Preparando consulta paginada de ventas para vendedor {}", vendedor.getUsuarioId());
        String campoOrdenamiento = "fecha".equals(ordenarPor) ? "factura.fecha" : "total";

        Sort sort = "asc".equalsIgnoreCase(direccionOrden)
                ? Sort.by(campoOrdenamiento).ascending()
                : Sort.by(campoOrdenamiento).descending();

        Pageable pageable = PageRequest.of(pagina, tamanoPagina, sort);

        Page<ProductoFactura> facturasPage = productoFacturaRepository.findByProductoUsuario(vendedor, pageable);
        log.debug("Se encontraron {} ventas para el vendedor {}", facturasPage.getTotalElements(), vendedor.getUsuarioId());

        List<VentaDTO> ventasDTO = facturasPage.getContent().stream()
                .map(this::mapearAVentaDTO)
                .toList();

        return new VentasPaginadasDTO(
                ventasDTO,
                facturasPage.getNumber(),
                facturasPage.getTotalPages(),
                facturasPage.getTotalElements(),
                facturasPage.isFirst(),
                facturasPage.isLast()
        );
    }

    private VentaDTO mapearAVentaDTO(ProductoFactura venta) {
        log.debug("Mapeando ProductoFactura {} a VentaDTO", venta.getProductoFacturaId());
        VentaDTO ventaDTO = new VentaDTO();

        ventaDTO.setVentaId(venta.getProductoFacturaId());
        ventaDTO.setFecha(venta.getFactura().getFecha());
        ventaDTO.setNombreCliente(venta.getFactura().getUsuario().getNombre());
        ventaDTO.setProducto(mapearADetalleFacturaProductoDTO(venta));

        return ventaDTO;
    }

    private void validarRolCliente(Usuario usuario) {
        log.debug("Validando rol de cliente para usuario {}", usuario.getUsuarioId());
        if (!usuarioRepository.tieneRol(usuario.getUsuarioId(), "CLIENTE")) {
            log.debug("Usuario {} no tiene rol de cliente", usuario.getUsuarioId());
            throw new AccesoDenegadoException("No tienes el rol de cliente.");
        }
    }

    private Factura obtenerFactura(Integer facturaId) {
        log.debug("Buscando factura {}", facturaId);
        return facturaRepository.findById(facturaId)
                .orElseThrow(() -> {
                    log.debug("Factura {} no encontrada", facturaId);
                    return new FacturaNoEncontradaException("No se encontró la factura especificada.");
                });
    }

    protected List<ProductoFactura> obtenerProductosFactura(Factura factura) {
        log.debug("Obteniendo productos de factura {}", factura.getFacturaId());
        List<ProductoFactura> productos = productoFacturaRepository.findByFactura(factura);
        log.debug("Se encontraron {} productos para la factura {}", productos.size(), factura.getFacturaId());
        return productos;
    }

    private DetalleFacturaProductoDTO mapearADetalleFacturaProductoDTO(ProductoFactura productoFactura) {
        log.debug("Mapeando ProductoFactura {} a DetalleFacturaProductoDTO", productoFactura.getProductoFacturaId());
        DetalleFacturaProductoDTO dto = new DetalleFacturaProductoDTO();
        dto.setNombre(productoFactura.getProductoNombre());
        dto.setPrecioUnitario(productoFactura.getPrecioUnitario());
        dto.setDescuentoUnitarioPorcentaje(productoFactura.getDescuentoUnitarioPorcentaje());
        dto.setDescuentoUnitarioValor(productoFactura.getDescuentoUnitarioValor());
        dto.setCantidad(productoFactura.getCantidad());

        BigDecimal totalSinDescuento = productoFactura.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(productoFactura.getCantidad()));
        dto.setTotalSinDescuento(totalSinDescuento);

        if (productoFactura.getDescuentoUnitarioValor() != null) {
            BigDecimal descuentoTotal = productoFactura.getDescuentoUnitarioValor()
                    .multiply(BigDecimal.valueOf(productoFactura.getCantidad()));
            dto.setDescuentoTotal(descuentoTotal);
            dto.setTotalFinal(totalSinDescuento.subtract(descuentoTotal));
        } else {
            dto.setDescuentoTotal(BigDecimal.ZERO);
            dto.setTotalFinal(totalSinDescuento);
        }

        return dto;
    }

    private void validarPropietarioFactura(Factura factura, Integer usuarioId) {
        log.debug("Validando propiedad de factura {} para usuario {}", factura.getFacturaId(), usuarioId);
        if (!factura.getUsuario().getUsuarioId().equals(usuarioId)) {
            log.debug("Usuario {} intentó acceder a factura {} que no le pertenece", usuarioId, factura.getFacturaId());
            throw new AccesoDenegadoException("La factura que intentas ver no te pertenece.");
        }
    }

    private BigDecimal calcularPrecioTotalYVerificarCantidades(CarritoPagoSolicitudDTO solicitudDTO, Integer usuarioId) {
        log.debug("Calculando precio total y verificando cantidades para usuario {}", usuarioId);
        BigDecimal total = solicitudDTO.getProductos().stream()
                .map(productoPagoDTO -> {
                    Producto producto = obtenerProducto(productoPagoDTO.getProductoId());
                    validarCantidadEnCarrito(usuarioId, producto, productoPagoDTO.getCantidad());
                    return calcularPrecioConDescuento(producto, productoPagoDTO)
                            .multiply(BigDecimal.valueOf(productoPagoDTO.getCantidad()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.debug("Precio total calculado: {} para usuario {}", total, usuarioId);
        return total;
    }

    private void procesarTransaccion(CarritoPagoSolicitudDTO solicitudDTO, BigDecimal precioTotal, MetodoPago metodoPago) {
        log.debug("Iniciando procesamiento de transacción por monto {}", precioTotal);
        actualizarStockProductos(solicitudDTO.getProductos());
        descontarMontoMetodoPago(metodoPago, precioTotal);
        log.debug("Transacción procesada exitosamente");
    }

    private Factura generarFactura(BigDecimal precioTotal, Usuario usuario, MetodoPago metodoPago,
                                   Direccion direccion, List<ProductoPagoDTO> productos) {
        log.debug("Generando factura para usuario {} por monto {}", usuario.getUsuarioId(), precioTotal);
        Factura factura = crearFactura(precioTotal, usuario, metodoPago, direccion);
        guardarDetallesFactura(factura, productos);
        log.debug("Factura {} generada exitosamente", factura.getFacturaId());
        return factura;
    }

    private void validarCantidadEnCarrito(Integer usuarioId, Producto producto, Integer cantidadSolicitada) {
        log.debug("Validando cantidad en carrito para producto {} de usuario {}", producto.getProductoId(), usuarioId);
        Integer cantidadEnCarrito = productoCarritoRepository.obtenerCantidadProductoCarrito(usuarioId, producto.getProductoId())
                .orElseThrow(() -> {
                    log.debug("Producto {} no encontrado en carrito de usuario {}", producto.getProductoId(), usuarioId);
                    return new ProductoNoEnCarritoException(
                            String.format("El producto con ID %d no está en el carrito del usuario con ID %d",
                                    producto.getProductoId(), usuarioId));
                });

        if (!cantidadEnCarrito.equals(cantidadSolicitada)) {
            log.debug("La cantidad enviada {} para el producto {} no coincide con la cantidad en el carrito ({}).",
                    cantidadSolicitada, producto.getProductoId(), cantidadEnCarrito);
            throw new CantidadNoCoincideException(String.format(
                    "La cantidad enviada (%d) para el producto '%s' no coincide con la cantidad en el carrito (%d).",
                    cantidadSolicitada, producto.getNombre(), cantidadEnCarrito));
        }
    }

    private BigDecimal calcularPrecioConDescuento(Producto producto, ProductoPagoDTO productoPagoDTO) {
        log.debug("Calculando precio con descuento para producto {}", producto.getProductoId());
        if (producto.getDescuento() == null) {
            return producto.getPrecio();
        }

        validarDescuento(producto, productoPagoDTO);
        BigDecimal precioConDescuento = producto.getPrecio().subtract(calcularValorDescuento(producto));
        log.debug("Precio calculado para producto {}: {}", producto.getProductoId(), precioConDescuento);
        return precioConDescuento;
    }

    private void validarDescuento(Producto producto, ProductoPagoDTO productoPagoDTO) {
        if (productoPagoDTO == null) return;

        log.debug("Validando descuento para producto {}", producto.getProductoId());
        Descuento descuento = descuentoRepository.findById(productoPagoDTO.getDescuentoId())
                .orElseThrow(() -> {
                    log.debug("Descuento {} no encontrado para producto {}",
                            productoPagoDTO.getDescuentoId(), producto.getProductoId());
                    return new DescuentoNoEncontradoException(
                            "No se encontró información del descuento aplicado al producto '" + producto.getNombre() + "'");
                });

        if (!producto.getDescuento().getDescuentoId().equals(productoPagoDTO.getDescuentoId()) ||
                !producto.getDescuento().getPorcentaje().equals(productoPagoDTO.getDescuentoUnitarioPorcentaje())) {
            log.debug("Descuento inválido para producto {}", producto.getProductoId());
            throw new DescuentoInvalidoException("El descuento seleccionado para el producto '" +
                    producto.getNombre() + "' no es válido");
        }
    }

    private void descontarMontoMetodoPago(MetodoPago metodoPago, BigDecimal precioTotal) {
        log.debug("Validando y descontando monto {} del método de pago {}", precioTotal, metodoPago.getMetodoPagoId());
        if (metodoPago.getMonto() == null || BigDecimal.valueOf(metodoPago.getMonto()).compareTo(precioTotal) < 0) {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
            log.debug("Fondos insuficientes en método de pago {}. Disponible: {}, Requerido: {}",
                    metodoPago.getMetodoPagoId(), metodoPago.getMonto(), precioTotal);
            throw new FondosInsuficientesException(String.format("Fondos insuficientes. Disponible: %s, Requerido: %s",
                    currencyFormat.format(metodoPago.getMonto()),
                    currencyFormat.format(precioTotal)));
        }

        metodoPagoRepository.reducirMonto(metodoPago.getMetodoPagoId(), precioTotal.doubleValue());
        log.debug("Monto descontado exitosamente del método de pago {}", metodoPago.getMetodoPagoId());
    }

    private void actualizarStock(ProductoPagoDTO productoPagoDTO) {
        log.debug("Actualizando stock para producto {}", productoPagoDTO.getProductoId());
        Producto producto = obtenerProducto(productoPagoDTO.getProductoId());

        if (!producto.getNombre().equals(productoPagoDTO.getNombre())) {
            log.debug("Nombre de producto no coincide. Esperado: {}, Recibido: {}",
                    producto.getNombre(), productoPagoDTO.getNombre());
            throw new ProductoInvalidoException("El nombre del producto ha sido modificado. Por favor, actualice la página e intente nuevamente.");
        }

        if (producto.getPrecio().compareTo(productoPagoDTO.getPrecioUnitario()) != 0) {
            log.debug("Precio de producto no coincide. Esperado: {}, Recibido: {}",
                    producto.getPrecio(), productoPagoDTO.getPrecioUnitario());
            throw new ProductoInvalidoException("El precio del producto ha cambiado. Por favor, actualice la página e intente nuevamente.");
        }

        int nuevaCantidad = producto.getExistencia() - productoPagoDTO.getCantidad();

        if (nuevaCantidad < 0) {
            log.debug("Stock insuficiente para producto {}. Disponible: {}, Solicitado: {}",
                    producto.getProductoId(), producto.getExistencia(), productoPagoDTO.getCantidad());
            throw new StockInsuficienteException(String.format("Stock insuficiente para '%s'. Disponible: %d",
                    producto.getNombre(), producto.getExistencia()));
        }

        productoRepository.actualizarStock(productoPagoDTO.getProductoId(), nuevaCantidad);
        log.debug("Stock actualizado exitosamente para producto {}", producto.getProductoId());
    }

    private void guardarDetallesFactura(Factura factura, List<ProductoPagoDTO> productos) {
        log.debug("Guardando detalles de factura {} para {} productos", factura.getFacturaId(), productos.size());
        productos.forEach(productoPagoDTO -> {
            Producto producto = obtenerProducto(productoPagoDTO.getProductoId());
            ProductoFactura detalle = crearDetalleFactura(factura, producto, productoPagoDTO);
            productoFacturaRepository.save(detalle);
        });
        log.debug("Detalles de factura {} guardados exitosamente", factura.getFacturaId());
    }

    private ProductoFactura crearDetalleFactura(Factura factura, Producto producto, ProductoPagoDTO productoPagoDTO) {
        log.debug("Creando detalle de factura para producto {}", producto.getProductoId());
        ProductoFactura detalle = new ProductoFactura();
        detalle.setFactura(factura);
        detalle.setProducto(producto);
        detalle.setProductoNombre(productoPagoDTO.getNombre());
        detalle.setPrecioUnitario(productoPagoDTO.getPrecioUnitario());
        detalle.setCantidad(productoPagoDTO.getCantidad());
        detalle.setTotal(calcularPrecioConDescuento(producto, null)
                .multiply(BigDecimal.valueOf(productoPagoDTO.getCantidad()))
                .doubleValue());

        if (producto.getDescuento() != null) {
            detalle.setDescuentoNombre(producto.getDescuento().getNombre());
            detalle.setDescuentoUnitarioPorcentaje(producto.getDescuento().getPorcentaje());
            detalle.setDescuentoUnitarioValor(calcularValorDescuento(producto));
        }

        return detalle;
    }

    private void validarCarrito(CarritoPagoSolicitudDTO solicitudDTO) {
        log.debug("Validando carrito");
        if (solicitudDTO.getProductos() == null || solicitudDTO.getProductos().isEmpty()) {
            log.debug("Carrito vacío");
            throw new CarritoVacioException("El carrito de compras está vacío");
        }
        log.debug("Carrito validado exitosamente");
    }

    private void validarParametrosOrdenamiento(String ordenarPor, String direccionOrden) {
        log.debug("Validando parámetros de ordenamiento: {} {}", ordenarPor, direccionOrden);
        if (!ordenarPor.equalsIgnoreCase("fecha") && !ordenarPor.equalsIgnoreCase("total")) {
            log.debug("Parámetro de ordenación no válido: {}", ordenarPor);
            throw new OrdenNoValidoException("Parámetro de ordenación no válido. Use 'fecha' o 'total'");
        }
        if (!direccionOrden.equalsIgnoreCase("asc") && !direccionOrden.equalsIgnoreCase("desc")) {
            log.debug("Dirección de orden no válida: {}", direccionOrden);
            throw new OrdenNoValidoException("Dirección de orden no válida. Use 'asc' o 'desc'");
        }
    }

    private void validarPaginacion(Integer pagina, Integer tamanoPagina, long totalFacturas) {
        log.debug("Validando paginación: página {}, tamaño {}, total {}", pagina, tamanoPagina, totalFacturas);
        int maximoPaginas = (int) Math.ceil((double) totalFacturas / tamanoPagina);
        if (pagina < 0 || pagina >= maximoPaginas) {
            log.debug("Número de página fuera de rango: {}", pagina);
            throw new PaginaFueraDeRangoException("Número de página fuera de rango. Total: " + maximoPaginas);
        }
    }

    private FacturasPaginadasDTO obtenerFacturasPaginadas(Usuario usuario, String ordenarPor,
                                                          String direccionOrden, Integer pagina, Integer tamanoPagina) {
        log.debug("Obteniendo facturas paginadas para usuario {}. Ordenar por: {}, Dirección: {}",
                usuario.getUsuarioId(), ordenarPor, direccionOrden);
        Sort sort = direccionOrden.equals("asc") ? Sort.by(ordenarPor).ascending() : Sort.by(ordenarPor).descending();
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, sort);
        Page<Factura> facturasPage = facturaRepository.findByUsuario(usuario, pageable);

        List<FacturaDTO> facturasDTO = facturasPage.getContent().stream()
                .map(factura -> new FacturaDTO(factura.getFacturaId(), factura.getFecha(), factura.getTotal()))
                .toList();

        log.debug("Se encontraron {} facturas para usuario {}", facturasDTO.size(), usuario.getUsuarioId());
        return new FacturasPaginadasDTO(
                facturasDTO,
                facturasPage.getNumber(),
                facturasPage.getTotalPages(),
                facturasPage.getTotalElements(),
                facturasPage.isFirst(),
                facturasPage.isLast()
        );
    }

    private MetodoPago obtenerMetodoPagoValido(Integer metodoPagoId, Usuario usuario) {
        log.debug("Validando método de pago {} para usuario {}", metodoPagoId, usuario.getUsuarioId());
        MetodoPago metodoPago = metodoPagoRepository.findByMetodoPagoIdAndUsuario(metodoPagoId, usuario)
                .orElseThrow(() -> {
                    log.debug("Método de pago {} no encontrado para usuario {}", metodoPagoId, usuario.getUsuarioId());
                    return new MetodoPagoNoEncontradoException(String.format(
                            "No se encontró un método de pago con ID %d para el usuario con ID %d",
                            metodoPagoId, usuario.getUsuarioId()));
                });

        if (metodoPago.getFechaExpiracion().isBefore(LocalDate.now())) {
            log.debug("Método de pago {} expirado", metodoPagoId);
            throw new MetodoPagoExpiradoException("El método de pago seleccionado ha expirado");
        }

        return metodoPago;
    }

    private BigDecimal calcularValorDescuento(Producto producto) {
        log.debug("Calculando valor de descuento para producto {}", producto.getProductoId());
        BigDecimal valorDescuento = producto.getPrecio()
                .multiply(BigDecimal.valueOf(producto.getDescuento().getPorcentaje()))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        log.debug("Valor de descuento calculado: {} para producto {}", valorDescuento, producto.getProductoId());
        return valorDescuento;
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        log.debug("Buscando usuario {}", usuarioId);
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.debug("Usuario {} no encontrado", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado");
                });
    }

    private Producto obtenerProducto(Integer productoId) {
        log.debug("Buscando producto {}", productoId);
        return productoRepository.findById(productoId)
                .orElseThrow(() -> {
                    log.debug("Producto {} no encontrado", productoId);
                    return new ProductoNoEncontradoException("Producto con ID " + productoId + " no encontrado");
                });
    }

    private Direccion obtenerDireccion(Integer direccionId, Usuario usuario) {
        log.debug("Buscando dirección {} para usuario {}", direccionId, usuario.getUsuarioId());
        return direccionRepository.findByDireccionIdAndUsuario(direccionId, usuario)
                .orElseThrow(() -> {
                    log.debug("Dirección {} no encontrada para usuario {}", direccionId, usuario.getUsuarioId());
                    return new DireccionNoEncontradaException(String.format(
                            "No se encontró una dirección con ID %d para el usuario con ID %d",
                            direccionId, usuario.getUsuarioId()));
                });
    }

    private void actualizarStockProductos(List<ProductoPagoDTO> productos) {
        log.debug("Actualizando stock para {} productos", productos.size());
        productos.forEach(this::actualizarStock);
        log.debug("Stock actualizado exitosamente para todos los productos");
    }

    private Factura crearFactura(BigDecimal precioTotal, Usuario usuario, MetodoPago metodoPago, Direccion direccion) {
        log.debug("Creando factura para usuario {} por monto {}", usuario.getUsuarioId(), precioTotal);
        Factura factura = new Factura();
        factura.setDireccion(direccion);
        factura.setTotal(precioTotal.doubleValue());
        factura.setFecha(LocalDateTime.now());
        factura.setMetodoPago(metodoPago);
        factura.setUsuario(usuario);
        Factura facturaGuardada = facturaRepository.save(factura);
        log.debug("Factura {} creada exitosamente", facturaGuardada.getFacturaId());
        return facturaGuardada;
    }

    private void vaciarCarrito(Integer usuarioId) {
        log.debug("Vaciando carrito de usuario {}", usuarioId);
        productoCarritoRepository.deleteByUsuarioUsuarioId(usuarioId);
        log.debug("Carrito vaciado exitosamente para usuario {}", usuarioId);
    }
}