package com.bazarboost.exception;

import com.bazarboost.model.Resenia;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        System.out.println("UsuarioNoEncontradoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ProductoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
        System.out.println("ProductoNoEncontradoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CategoriaNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleCategoriaNoEncontrada(CategoriaNoEncontradaException ex) {
        System.out.println("CategoriaNoEncontradaException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ReseniaNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleReseniaNoEncontrada(ReseniaNoEncontradaException ex) {
        System.out.println("ReseniaNoEncontradaException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ReseniaExistenteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleReseniaExistente(ReseniaExistenteException ex) {
        System.out.println("ReseniaExistenteException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccesoDenegado(AccesoDenegadoException ex) {
        System.out.println("AccesoDenegadoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(OrdenNoValidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleOrdenNoValido(OrdenNoValidoException ex) {
        System.out.println("OrdenNoValidoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AccionNoValidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleAccionNoValida(AccionNoValidaException ex) {
        System.out.println("AccionNoValidaException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ProductoYaEnCarritoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleProductoYaEnCarrito(ProductoYaEnCarritoException ex) {
        System.out.println("ProductoYaEnCarritoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ProductoNoEnCarritoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleProductoNoEnCarrito(ProductoNoEnCarritoException ex) {
        System.out.println("ProductoNoEnCarritoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ProductoPropioException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleProductoPropio(ProductoPropioException ex) {
        System.out.println("ProductoPropioException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(PorcentajeDescuentoInvalidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlePorcentajeDescuentoInvalido(PorcentajeDescuentoInvalidoException ex) {
        System.out.println("PorcentajeDescuentoInvalidoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DescuentoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleDescuentoNoEncontrado(DescuentoNoEncontradoException ex) {
        System.out.println("DescuentoNoEncontradoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NombreDescuentoDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleNombreDescuentoDuplicado(NombreDescuentoDuplicadoException ex) {
        System.out.println("NombreDescuentoDuplicadoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(StockInsuficienteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleStockInsuficiente(StockInsuficienteException ex) {
        System.out.println("StockInsuficienteException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(FondosInsuficientesException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ResponseEntity<String> handleFondosInsuficientes(FondosInsuficientesException ex) {
        System.out.println("FondosInsuficientesException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(ex.getMessage());
    }

    @ExceptionHandler(MetodoPagoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleMetodoPagoNoEncontrado(MetodoPagoNoEncontradoException ex) {
        System.out.println("MetodoPagoNoEncontradoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DireccionNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleDireccionNoEncontrada(DireccionNoEncontradaException ex) {
        System.out.println("DireccionNoEncontradaException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CantidadNoCoincideException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleCantidadNoCoincide(CantidadNoCoincideException ex) {
        System.out.println("CantidadNoCoincideException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "Formato de JSON inválido o campos faltantes. Verifica que todos los campos requeridos estén presentes y correctamente formateados.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(CarritoVacioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleCarritoVacio(CarritoVacioException ex) {
        System.out.println("CarritoVacioException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(PaginaFueraDeRangoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlePaginaFueraDeRango(PaginaFueraDeRangoException ex) {
        System.out.println("PaginaFueraDeRangoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NumeroTarjetaDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleNumeroTarjetaDuplicado(NumeroTarjetaDuplicadoException ex) {
        System.out.println("NumeroTarjetaDuplicadoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(NombreCategoriaDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleNombreCategoriaDuplicado(NombreCategoriaDuplicadoException ex) {
        System.out.println("NombreCategoriaDuplicadoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CorreoElectronicoExistenteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleCorreoElectronicoExistente(CorreoElectronicoExistenteException ex) {
        System.out.println("CorreoElectronicoExistenteException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(RolNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleRolNoEncontradO(RolNoEncontradoException ex) {
        System.out.println("RolNoEncontradoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MetodoPagoExpiradoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMetodoPagoExpirado(MetodoPagoExpiradoException ex) {
        System.out.println("MetodoPagoExpiradoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DescuentoInvalidoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleDescuentoInvalidoException(DescuentoInvalidoException ex) {
        System.out.println("DescuentoInvalidoException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        // Recorre todos los errores de los campos y agrega los mensajes de error a la lista
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }

    // Puedes agregar más métodos aquí para manejar otras excepciones personalizadas
}
