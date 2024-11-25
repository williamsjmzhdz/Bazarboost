package com.bazarboost.system.service.impl;

import com.bazarboost.shared.exception.*;
import com.bazarboost.system.repository.*;
import com.bazarboost.system.service.FacturaService;
import com.bazarboost.system.dto.*;
import com.bazarboost.system.model.*;
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
        validarCarrito(solicitudDTO);

        Usuario usuario = obtenerUsuario(usuarioId);
        BigDecimal precioTotal = calcularPrecioTotalYVerificarCantidades(solicitudDTO, usuarioId);
        MetodoPago metodoPago = obtenerMetodoPagoValido(solicitudDTO.getMetodoPagoId(), usuario);
        Direccion direccion = obtenerDireccion(solicitudDTO.getDireccionId(), usuario);

        procesarTransaccion(solicitudDTO, precioTotal, metodoPago);
        Factura factura = generarFactura(precioTotal, usuario, metodoPago, direccion, solicitudDTO.getProductos());
        vaciarCarrito(usuarioId);

        return new CarritoPagoRespuestaDTO(factura.getFacturaId());
    }

    @Override
    @Transactional(readOnly = true)
    public FacturasPaginadasDTO obtenerFacturasPaginadasYOrdenadas(String ordenarPor, String direccionOrden,
                                                                   Integer pagina, Integer tamanoPagina,
                                                                   Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        validarRolCliente(usuario);
        validarParametrosOrdenamiento(ordenarPor, direccionOrden);

        long totalFacturas = facturaRepository.countByUsuario(usuario);
        if (totalFacturas == 0) {
            return crearRespuestaVacia(pagina);
        }

        validarPaginacion(pagina, tamanoPagina, totalFacturas);
        return obtenerFacturasPaginadas(usuario, ordenarPor, direccionOrden, pagina, tamanoPagina);
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleFacturaDTO obtenerDetalleFactura(Integer facturaId, Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        validarRolCliente(usuario);

        Factura factura = obtenerFactura(facturaId);
        validarPropietarioFactura(factura, usuarioId);

        List<ProductoFactura> productosFactura = obtenerProductosFactura(factura);
        List<DetalleFacturaProductoDTO> productosDTO = productosFactura.stream()
                .map(this::mapearADetalleFacturaProductoDTO)
                .toList();

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
        Usuario vendedor = obtenerUsuario(vendedorId);
        validarRolVendedor(vendedor);
        validarParametrosOrdenamiento(ordenarPor, direccionOrden);

        long totalVentas = productoFacturaRepository.countByProductoUsuario(vendedor);
        if (totalVentas == 0) {
            return new VentasPaginadasDTO(List.of(), pagina, 0, 0L, true, true);
        }

        validarPaginacion(pagina, tamanoPagina, totalVentas);
        return obtenerVentasPaginadas(vendedor, ordenarPor, direccionOrden, pagina, tamanoPagina);
    }

    private void validarRolVendedor(Usuario usuario) {
        if (!usuarioRepository.tieneRol(usuario.getUsuarioId(), "VENDEDOR")) {
            throw new AccesoDenegadoException("No puedes acceder al panel de ventas porque no tienes el rol de vendedor.");
        }
    }

    private VentasPaginadasDTO obtenerVentasPaginadas(Usuario vendedor, String ordenarPor,
                                                      String direccionOrden, Integer pagina, Integer tamanoPagina) {
        // Si ordenarPor es "fecha", usa "factura.fecha"; si es "total", usa "total".
        String campoOrdenamiento = "fecha".equals(ordenarPor) ? "factura.fecha" : "total";

        // Crear el objeto Sort dinámico
        Sort sort = "asc".equalsIgnoreCase(direccionOrden)
                ? Sort.by(campoOrdenamiento).ascending()
                : Sort.by(campoOrdenamiento).descending();

        Pageable pageable = PageRequest.of(pagina, tamanoPagina, sort);

        Page<ProductoFactura> facturasPage = productoFacturaRepository.findByProductoUsuario(vendedor, pageable);

        facturasPage.getContent().forEach(System.out::println);

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

        VentaDTO ventaDTO = new VentaDTO();

        ventaDTO.setVentaId(venta.getProductoFacturaId());
        ventaDTO.setFecha(venta.getFactura().getFecha());
        ventaDTO.setNombreCliente(venta.getFactura().getUsuario().getNombre());
        ventaDTO.setProducto(mapearADetalleFacturaProductoDTO(venta));

        return ventaDTO;
    }

    private void validarRolCliente(Usuario usuario) {
        if (!usuarioRepository.tieneRol(usuario.getUsuarioId(), "CLIENTE")) {
            throw new AccesoDenegadoException("No tienes el rol de cliente.");
        }
    }

    private Factura obtenerFactura(Integer facturaId) {
        return facturaRepository.findById(facturaId)
                .orElseThrow(() -> new FacturaNoEncontradaException("No se encontró la factura especificada."));
    }

    protected List<ProductoFactura> obtenerProductosFactura(Factura factura) {
        return productoFacturaRepository.findByFactura(factura);
    }

    private DetalleFacturaProductoDTO mapearADetalleFacturaProductoDTO(ProductoFactura productoFactura) {
        DetalleFacturaProductoDTO dto = new DetalleFacturaProductoDTO();
        dto.setNombre(productoFactura.getProductoNombre());
        dto.setPrecioUnitario(productoFactura.getPrecioUnitario());
        dto.setDescuentoUnitarioPorcentaje(productoFactura.getDescuentoUnitarioPorcentaje());
        dto.setDescuentoUnitarioValor(productoFactura.getDescuentoUnitarioValor());
        dto.setCantidad(productoFactura.getCantidad());

        // Calcular totales
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
        if (!factura.getUsuario().getUsuarioId().equals(usuarioId)) {
            throw new AccesoDenegadoException("La factura que intentas ver no te pertenece.");
        }
    }

    private BigDecimal calcularPrecioTotalYVerificarCantidades(CarritoPagoSolicitudDTO solicitudDTO, Integer usuarioId) {
        return solicitudDTO.getProductos().stream()
                .map(productoPagoDTO -> {
                    Producto producto = obtenerProducto(productoPagoDTO.getProductoId());
                    validarCantidadEnCarrito(usuarioId, producto, productoPagoDTO.getCantidad());
                    return calcularPrecioConDescuento(producto, productoPagoDTO)
                            .multiply(BigDecimal.valueOf(productoPagoDTO.getCantidad()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void procesarTransaccion(CarritoPagoSolicitudDTO solicitudDTO, BigDecimal precioTotal, MetodoPago metodoPago) {
        actualizarStockProductos(solicitudDTO.getProductos());
        descontarMontoMetodoPago(metodoPago, precioTotal);
    }

    private Factura generarFactura(BigDecimal precioTotal, Usuario usuario, MetodoPago metodoPago,
                                   Direccion direccion, List<ProductoPagoDTO> productos) {
        Factura factura = crearFactura(precioTotal, usuario, metodoPago, direccion);
        guardarDetallesFactura(factura, productos);
        return factura;
    }

    private void validarCantidadEnCarrito(Integer usuarioId, Producto producto, Integer cantidadSolicitada) {
        Integer cantidadEnCarrito = productoCarritoRepository.obtenerCantidadProductoCarrito(usuarioId, producto.getProductoId())
                .orElseThrow(() -> new ProductoNoEnCarritoException(
                        String.format("El producto con ID %d no está en el carrito del usuario con ID %d",
                                producto.getProductoId(), usuarioId)));

        if (!cantidadEnCarrito.equals(cantidadSolicitada)) {
            throw new CantidadNoCoincideException(String.format(
                    "La cantidad enviada (%d) para el producto '%s' no coincide con la cantidad en el carrito (%d).",
                    cantidadSolicitada, producto.getNombre(), cantidadEnCarrito));
        }
    }

    private BigDecimal calcularPrecioConDescuento(Producto producto, ProductoPagoDTO productoPagoDTO) {
        if (producto.getDescuento() == null) {
            return producto.getPrecio();
        }

        validarDescuento(producto, productoPagoDTO);
        return producto.getPrecio().subtract(calcularValorDescuento(producto));
    }

    private void validarDescuento(Producto producto, ProductoPagoDTO productoPagoDTO) {
        if (productoPagoDTO == null) return;

        Descuento descuento = descuentoRepository.findById(productoPagoDTO.getDescuentoId())
                .orElseThrow(() -> new DescuentoNoEncontradoException(
                        "No se encontró información del descuento aplicado al producto '" + producto.getNombre() + "'"));

        if (!producto.getDescuento().getDescuentoId().equals(productoPagoDTO.getDescuentoId()) ||
                !producto.getDescuento().getPorcentaje().equals(productoPagoDTO.getDescuentoUnitarioPorcentaje())) {
            throw new DescuentoInvalidoException("El descuento seleccionado para el producto '" +
                    producto.getNombre() + "' no es válido");
        }
    }

    private void descontarMontoMetodoPago(MetodoPago metodoPago, BigDecimal precioTotal) {
        if (metodoPago.getMonto() == null || BigDecimal.valueOf(metodoPago.getMonto()).compareTo(precioTotal) < 0) {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
            throw new FondosInsuficientesException(String.format("Fondos insuficientes. Disponible: %s, Requerido: %s",
                    currencyFormat.format(metodoPago.getMonto()),
                    currencyFormat.format(precioTotal)));
        }

        metodoPagoRepository.reducirMonto(metodoPago.getMetodoPagoId(), precioTotal.doubleValue());
    }

    private void actualizarStock(ProductoPagoDTO productoPagoDTO) {
        Producto producto = obtenerProducto(productoPagoDTO.getProductoId());

        // Validar nombre del producto
        if (!producto.getNombre().equals(productoPagoDTO.getNombre())) {
            throw new ProductoInvalidoException("El nombre del producto ha sido modificado. Por favor, actualice la página e intente nuevamente.");
        }

        // Validar precio unitario
        if (producto.getPrecio().compareTo(productoPagoDTO.getPrecioUnitario()) != 0) {
            throw new ProductoInvalidoException("El precio del producto ha cambiado. Por favor, actualice la página e intente nuevamente.");
        }

        int nuevaCantidad = producto.getExistencia() - productoPagoDTO.getCantidad();

        if (nuevaCantidad < 0) {
            throw new StockInsuficienteException(String.format("Stock insuficiente para '%s'. Disponible: %d",
                    producto.getNombre(), producto.getExistencia()));
        }

        productoRepository.actualizarStock(productoPagoDTO.getProductoId(), nuevaCantidad);
    }

    private void guardarDetallesFactura(Factura factura, List<ProductoPagoDTO> productos) {
        productos.forEach(productoPagoDTO -> {
            Producto producto = obtenerProducto(productoPagoDTO.getProductoId());
            ProductoFactura detalle = crearDetalleFactura(factura, producto, productoPagoDTO);
            productoFacturaRepository.save(detalle);
        });
    }

    private ProductoFactura crearDetalleFactura(Factura factura, Producto producto, ProductoPagoDTO productoPagoDTO) {
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

    // Métodos auxiliares y validaciones
    private void validarCarrito(CarritoPagoSolicitudDTO solicitudDTO) {
        if (solicitudDTO.getProductos() == null || solicitudDTO.getProductos().isEmpty()) {
            throw new CarritoVacioException("El carrito de compras está vacío");
        }
    }

    private void validarParametrosOrdenamiento(String ordenarPor, String direccionOrden) {
        if (!ordenarPor.equalsIgnoreCase("fecha") && !ordenarPor.equalsIgnoreCase("total")) {
            throw new OrdenNoValidoException("Parámetro de ordenación no válido. Use 'fecha' o 'total'");
        }
        if (!direccionOrden.equalsIgnoreCase("asc") && !direccionOrden.equalsIgnoreCase("desc")) {
            throw new OrdenNoValidoException("Dirección de orden no válida. Use 'asc' o 'desc'");
        }
    }

    private void validarPaginacion(Integer pagina, Integer tamanoPagina, long totalFacturas) {
        int maximoPaginas = (int) Math.ceil((double) totalFacturas / tamanoPagina);
        if (pagina < 0 || pagina >= maximoPaginas) {
            throw new PaginaFueraDeRangoException("Número de página fuera de rango. Total: " + maximoPaginas);
        }
    }

    private FacturasPaginadasDTO crearRespuestaVacia(Integer pagina) {
        return new FacturasPaginadasDTO(List.of(), pagina, 0, 0, true, true);
    }

    private FacturasPaginadasDTO obtenerFacturasPaginadas(Usuario usuario, String ordenarPor,
                                                          String direccionOrden, Integer pagina, Integer tamanoPagina) {
        Sort sort = direccionOrden.equals("asc") ? Sort.by(ordenarPor).ascending() : Sort.by(ordenarPor).descending();
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, sort);
        Page<Factura> facturasPage = facturaRepository.findByUsuario(usuario, pageable);

        List<FacturaDTO> facturasDTO = facturasPage.getContent().stream()
                .map(factura -> new FacturaDTO(factura.getFacturaId(), factura.getFecha(), factura.getTotal()))
                .toList();

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
        MetodoPago metodoPago = metodoPagoRepository.findByMetodoPagoIdAndUsuario(metodoPagoId, usuario)
                .orElseThrow(() -> new MetodoPagoNoEncontradoException(String.format(
                        "No se encontró un método de pago con ID %d para el usuario con ID %d",
                        metodoPagoId, usuario.getUsuarioId())));

        if (metodoPago.getFechaExpiracion().isBefore(LocalDate.now())) {
            throw new MetodoPagoExpiradoException("El método de pago seleccionado ha expirado");
        }

        return metodoPago;
    }

    private BigDecimal calcularValorDescuento(Producto producto) {
        return producto.getPrecio()
                .multiply(BigDecimal.valueOf(producto.getDescuento().getPorcentaje()))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado"));
    }

    private Producto obtenerProducto(Integer productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto con ID " + productoId + " no encontrado"));
    }

    private Direccion obtenerDireccion(Integer direccionId, Usuario usuario) {
        return direccionRepository.findByDireccionIdAndUsuario(direccionId, usuario)
                .orElseThrow(() -> new DireccionNoEncontradaException(String.format(
                        "No se encontró una dirección con ID %d para el usuario con ID %d",
                        direccionId, usuario.getUsuarioId())));
    }

    private void actualizarStockProductos(List<ProductoPagoDTO> productos) {
        productos.forEach(this::actualizarStock);
    }

    private Factura crearFactura(BigDecimal precioTotal, Usuario usuario, MetodoPago metodoPago, Direccion direccion) {
        Factura factura = new Factura();
        factura.setDireccion(direccion);
        factura.setTotal(precioTotal.doubleValue());
        factura.setFecha(LocalDateTime.now());
        factura.setMetodoPago(metodoPago);
        factura.setUsuario(usuario);
        return facturaRepository.save(factura);
    }

    private void vaciarCarrito(Integer usuarioId) {
        productoCarritoRepository.deleteByUsuarioUsuarioId(usuarioId);
    }
}