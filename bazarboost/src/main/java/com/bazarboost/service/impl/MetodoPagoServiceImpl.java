package com.bazarboost.service.impl;

import com.bazarboost.dto.MetodoPagoCreacionDTO;
import com.bazarboost.dto.MetodoPagoDTO;
import com.bazarboost.exception.NumeroTarjetaDuplicadoException;
import com.bazarboost.exception.UsuarioNoEncontradoException;
import com.bazarboost.model.MetodoPago;
import com.bazarboost.model.Usuario;
import com.bazarboost.model.auxiliar.TipoTarjeta;
import com.bazarboost.repository.MetodoPagoRepository;
import com.bazarboost.repository.UsuarioRepository;
import com.bazarboost.service.MetodoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MetodoPagoServiceImpl implements MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPagoDTO> obtenerTodos(Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        return metodoPagoRepository.findByUsuario(usuario).stream().map(this::convertirAMetodoPagoDTO).toList();
    }

    @Override
    @Transactional
    public Void crear(MetodoPagoCreacionDTO metodoPagoCreacionDTO, Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);

        // Verificar si el numeroTarjeta ya existe
        if (metodoPagoRepository.existsByNumeroTarjeta(metodoPagoCreacionDTO.getNumeroTarjeta())) {
            throw new NumeroTarjetaDuplicadoException("El número de tarjeta ya está registrado. Use un número diferente.");
        }

        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setNombreTitular(metodoPagoCreacionDTO.getNombreTitular());
        metodoPago.setNumeroTarjeta(metodoPagoCreacionDTO.getNumeroTarjeta());
        metodoPago.setFechaExpiracion(metodoPagoCreacionDTO.getFechaExpiracion());
        metodoPago.setTipoTarjeta(metodoPagoCreacionDTO.getTipoTarjeta().equals("Crédito") ? TipoTarjeta.Crédito : TipoTarjeta.Débito);
        metodoPago.setMonto(metodoPagoCreacionDTO.getMonto());
        metodoPago.setUsuario(usuario);

        metodoPagoRepository.save(metodoPago);

        return null;
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado"));
    }

    private MetodoPagoDTO convertirAMetodoPagoDTO(MetodoPago metodoPago) {
        MetodoPagoDTO metodoPagoDTO = new MetodoPagoDTO();
        metodoPagoDTO.setMetodoPagoId(metodoPago.getMetodoPagoId());
        metodoPagoDTO.setNombreTitular(metodoPago.getNombreTitular()); // Corregido: usaba DTO en lugar de entidad
        metodoPagoDTO.setTerminacion(metodoPago.getNumeroTarjeta().substring(metodoPago.getNumeroTarjeta().length() - 4));
        metodoPagoDTO.setFechaExpiracion(metodoPago.getFechaExpiracion().format(DateTimeFormatter.ofPattern("MM/yyyy")));
        metodoPagoDTO.setTipo(metodoPago.getTipoTarjeta().name());

        // Formateo del monto usando BigDecimal
        Double montoOriginal = metodoPago.getMonto();
        BigDecimal montoFormateado = new BigDecimal(montoOriginal)
                .setScale(2, RoundingMode.HALF_UP);
        metodoPagoDTO.setMonto(montoFormateado.doubleValue());

        return metodoPagoDTO;
    }
}
