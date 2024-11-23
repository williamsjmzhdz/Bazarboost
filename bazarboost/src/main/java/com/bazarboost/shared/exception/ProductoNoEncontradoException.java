package com.bazarboost.shared.exception;

public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(String message) {
        super(message);
    }

    public ProductoNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
