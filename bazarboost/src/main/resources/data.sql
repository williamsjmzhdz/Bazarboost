-- Alumno: Francisco Williams Jiménez Hernández
-- Proyecto: Bazarboost

-- Inserts de datos de prueba

-- Inserciones en la tabla Usuarios
INSERT INTO Usuarios (nombre, apellido_paterno, apellido_materno, correo_electronico, contrasenia, telefono) VALUES
('Juan', 'Pérez', 'López', 'juan.perez@example.com', 'password123', '5551234567'),
('María', 'Gómez', 'Martínez', 'maria.gomez@example.com', 'password123', '5557654321'),
('Pedro', 'Sánchez', 'Hernández', 'pedro.sanchez@example.com', 'password123', '5556781234');

-- Inserciones en la tabla Categorias
INSERT INTO Categorias (nombre) VALUES
('Electrónica'),
('Ropa'),
('Hogar');

-- Inserciones en la tabla Roles
INSERT INTO Roles (nombre, descripcion) VALUES
('Administrador', 'Acceso total al sistema'),
('Cliente', 'Puede realizar compras y dejar reseñas'),
('Vendedor', 'Puede gestionar productos y ventas');

-- Inserciones en la tabla Descuentos
INSERT INTO Descuentos (porcentaje, nombre, usuario_id) VALUES
(10, 'Descuento de Bienvenida', 1),
(15, 'Descuento de Verano', 2),
(5, 'Descuento de Fin de Temporada', 3);

-- Inserciones en la tabla Productos
INSERT INTO Productos (nombre, descripcion, precio, existencia, imagen_url, usuario_id, descuento_id, categoria_id) VALUES
('Smartphone', 'Último modelo con pantalla AMOLED', 699.99, 50, 'img/smartphone.jpg', 1, 1, 1),
('Camiseta', 'Camiseta 100% algodón', 19.99, 200, 'img/camiseta.jpg', 2, NULL, 2),
('Sofá', 'Sofá de tres plazas', 499.99, 10, 'img/sofa.jpg', 3, 3, 3);

-- Inserciones en la tabla UsuariosRoles
INSERT INTO UsuariosRoles (usuario_id, rol_id) VALUES
(1, 1),
(2, 2),
(3, 3);

-- Inserciones en la tabla Direcciones
INSERT INTO Direcciones (estado, ciudad, colonia, calle, numero_domicilio, codigo_postal, usuario_id) VALUES
('Ciudad de México', 'Benito Juárez', 'Del Valle', 'Avenida Insurgentes', 123, '03100', 1),
('Jalisco', 'Guadalajara', 'Centro', 'Avenida Juárez', 456, '44100', 2),
('Nuevo León', 'Monterrey', 'San Pedro', 'Avenida Real', 789, '66260', 3);

-- Inserciones en la tabla MetodosPago
INSERT INTO MetodosPago (nombre_titular, numero_tarjeta, fecha_expiracion, tipo_tarjeta, monto, usuario_id) VALUES
('Juan Pérez', '4111111111111111', '12/2025', 'Crédito', 1000.00, 1),
('María Gómez', '4222222222222222', '11/2026', 'Débito', 500.00, 2),
('Pedro Sánchez', '4333333333333333', '10/2024', 'Crédito', 750.00, 3);


-- Inserciones en la tabla Facturas
INSERT INTO Facturas (subtotal, total, porcentaje_impuestos, usuario_id, metodo_pago_id, direccion_id) VALUES
(150.00, 165.00, 10, 1, 1, 1),
(300.00, 330.00, 10, 2, 2, 2),
(450.00, 495.00, 10, 3, 3, 3);

-- Inserciones en la tabla Resenias
INSERT INTO Resenias (comentario, calificacion, usuario_id, producto_id) VALUES
('Excelente producto', 5, 1, 1),
('Buena calidad', 4, 2, 2),
('Cómodo y práctico', 4, 3, 3);

-- Inserciones en la tabla ProductosCarrito
INSERT INTO ProductosCarrito (cantidad, total, usuario_id, producto_id) VALUES
(2, 39.98, 1, 2),
(1, 699.99, 2, 1),
(1, 499.99, 3, 3);

-- Inserciones en la tabla ProductosFacturas
INSERT INTO ProductosFacturas (cantidad, total, factura_id, producto_id) VALUES
(1, 699.99, 1, 1),
(2, 39.98, 2, 2),
(1, 499.99, 3, 3);
