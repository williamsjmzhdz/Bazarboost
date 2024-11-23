package com.bazarboost.shared.util;

import com.bazarboost.system.model.Producto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class ProductoUtility {

    @Value("${app.imagenes.directorio}")
    private String directorioImagenes;

    public boolean guardarImagenProducto(Producto producto, MultipartFile imagenArchivo) throws IOException {
        if (!imagenArchivo.isEmpty()) {
            String nombreArchivo = imagenArchivo.getOriginalFilename();
            imagenArchivo.transferTo(new File(directorioImagenes + "/" + nombreArchivo));
            producto.setImagenUrl(nombreArchivo);
            return true;
        }
        return true;
    }

}
