-- Alumno: Francisco Williams Jiménez Hernández
-- Proyecto: Bazarboost

-- Inserts de datos de prueba

-- Inserciones en la tabla Usuarios con contraseña sin encriptar (password123)
INSERT INTO Usuarios (nombre, apellido_paterno, apellido_materno, correo_electronico, contrasenia, telefono) VALUES
('Juan', 'Pérez', 'López', 'juan.perez@example.com', 'password123', '5551234567'),
('María', 'Gómez', 'Martínez', 'maria.gomez@example.com', 'password123', '5557654321'),
('Pedro', 'Sánchez', 'Hernández', 'pedro.sanchez@example.com', 'password123', '5556781234'),
('Ana', 'Ramírez', 'García', 'ana.ramirez@example.com', 'password123', '5559876543'),
('Carlos', 'Fernández', 'Pérez', 'carlos.fernandez@example.com', 'password123', '5554567890'),
('Williams', 'Jiménez', 'Hernández', 'williams.jimenez@example.com', 'password123', '5571714144');

-- Inserciones en la tabla Categorias
INSERT INTO Categorias (nombre) VALUES
('Electrónica'),
('Ropa'),
('Hogar'),
('Juguetes'),
('Libros');

-- Inserciones en la tabla Roles
INSERT INTO Roles (nombre, descripcion) VALUES
('Administrador', 'Acceso total al sistema'),
('Cliente', 'Puede realizar compras y dejar reseñas'),
('Vendedor', 'Puede gestionar productos y ventas');

-- Inserciones en la tabla Descuentos
INSERT INTO Descuentos (porcentaje, nombre, usuario_id) VALUES
(10, 'Descuento de Bienvenida', 1),
(15, 'Descuento de Verano', 2),
(5, 'Descuento de Fin de Temporada', 3),
(20, 'Descuento de Fiestas', 4),
(25, 'Descuento Black Friday', 5);

-- Inserciones en la tabla Productos
INSERT INTO Productos (nombre, descripcion, precio, existencia, imagen_url, usuario_id, descuento_id, categoria_id) VALUES
('Smartphone', 'Último modelo con pantalla AMOLED', 699.99, 50, 'smartphone.jpg', 1, 1, 1),
('Camiseta', 'Camiseta 100% algodón', 19.99, 200, 'camiseta.jpg', 2, NULL, 2),
('Sofá', 'Sofá de tres plazas', 499.99, 10, 'sofa.jpg', 3, 3, 3),
('Libro de Java', 'Guía avanzada de Java', 29.99, 100, 'libro_java.jpg', 4, NULL, 5),
('Juguete Robot', 'Robot de juguete interactivo', 79.99, 30, 'robot.jpg', 5, 5, 4);

-- Inserciones en la tabla UsuariosRoles
INSERT INTO UsuariosRoles (usuario_id, rol_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(2, 3),
(3, 2),
(4, 2),
(5, 2),
(6, 2),
(6, 3);

-- Inserciones en la tabla Direcciones
INSERT INTO Direcciones (estado, ciudad, colonia, calle, numero_domicilio, codigo_postal, usuario_id) VALUES
('Ciudad de México', 'Benito Juárez', 'Del Valle', 'Avenida Insurgentes', 123, '03100', 1),
('Jalisco', 'Guadalajara', 'Centro', 'Avenida Juárez', 456, '44100', 2),
('Nuevo León', 'Monterrey', 'San Pedro', 'Avenida Real', 789, '66260', 3),
('Puebla', 'Puebla', 'La Paz', 'Calle 5', 101, '72000', 4),
('Veracruz', 'Boca del Río', 'Costa Verde', 'Calle 20', 202, '94293', 5);

-- Inserciones en la tabla MetodosPago
INSERT INTO MetodosPago (nombre_titular, numero_tarjeta, fecha_expiracion, tipo_tarjeta, monto, usuario_id) VALUES
('Juan Pérez', '4111111111111111', '2025-12-01', 'Crédito', 1000.00, 1),
('María Gómez', '4222222222222222', '2026-11-01', 'Débito', 500.00, 2),
('Pedro Sánchez', '4333333333333333', '2024-10-01', 'Crédito', 750.00, 3),
('Ana Ramírez', '4444444444444444', '2025-09-01', 'Débito', 1200.00, 4),
('Carlos Fernández', '4555555555555555', '2027-08-01', 'Crédito', 600.00, 5);

-- Inserciones en la tabla Facturas
INSERT INTO Facturas (fecha, total, usuario_id, metodo_pago_id, direccion_id) VALUES

-- Facturas para usuario 1
('2024-10-11 10:00:00', 514.97, 1, 1, 1),  -- 2 Camisetas (39.98) + Sofá (474.99)
('2024-10-11 10:01:00', 39.98, 1, 1, 1),   -- 2 Camisetas
('2024-10-11 10:02:00', 474.99, 1, 1, 1),  -- 1 Sofá
('2024-10-11 10:03:00', 89.97, 1, 1, 1),   -- 3 Libros Java
('2024-10-11 10:04:00', 119.98, 1, 1, 1),  -- 2 Robots
('2024-10-11 10:05:00', 474.99, 1, 1, 1),  -- 1 Sofá
('2024-10-11 10:06:00', 59.98, 1, 1, 1),   -- 2 Libros Java
('2024-10-11 10:07:00', 59.99, 1, 1, 1),   -- 1 Robot
('2024-10-11 10:08:00', 39.98, 1, 1, 1),   -- 2 Camisetas
('2024-10-11 10:09:00', 474.99, 1, 1, 1),  -- 1 Sofá

-- Facturas para usuario 2
('2024-10-11 11:00:00', 1104.98, 2, 2, 2), -- Smartphone (629.99) + Sofá (474.99)
('2024-10-11 11:01:00', 59.98, 2, 2, 2),   -- 2 Libros Java
('2024-10-11 11:02:00', 59.99, 2, 2, 2),   -- 1 Robot
('2024-10-11 11:03:00', 629.99, 2, 2, 2),  -- 1 Smartphone
('2024-10-11 11:04:00', 474.99, 2, 2, 2),  -- 1 Sofá
('2024-10-11 11:05:00', 59.98, 2, 2, 2),   -- 2 Libros Java
('2024-10-11 11:06:00', 59.99, 2, 2, 2),   -- 1 Robot
('2024-10-11 11:07:00', 629.99, 2, 2, 2),  -- 1 Smartphone
('2024-10-11 11:08:00', 474.99, 2, 2, 2),  -- 1 Sofá
('2024-10-11 11:09:00', 59.98, 2, 2, 2),   -- 2 Libros Java

-- Facturas para usuario 3
('2024-10-11 12:00:00', 669.97, 3, 3, 3),  -- Smartphone (629.99) + 2 Camisetas (39.98)
('2024-10-11 12:01:00', 59.98, 3, 3, 3),   -- 2 Libros Java
('2024-10-11 12:02:00', 59.99, 3, 3, 3),   -- 1 Robot
('2024-10-11 12:03:00', 629.99, 3, 3, 3),  -- 1 Smartphone
('2024-10-11 12:04:00', 39.98, 3, 3, 3),   -- 2 Camisetas
('2024-10-11 12:05:00', 59.98, 3, 3, 3),   -- 2 Libros Java
('2024-10-11 12:06:00', 59.99, 3, 3, 3),   -- 1 Robot
('2024-10-11 12:07:00', 629.99, 3, 3, 3),  -- 1 Smartphone
('2024-10-11 12:08:00', 39.98, 3, 3, 3),   -- 2 Camisetas
('2024-10-11 12:09:00', 59.98, 3, 3, 3),   -- 2 Libros Java

-- Facturas para usuario 4
('2024-10-11 13:00:00', 669.97, 4, 4, 4),  -- Smartphone (629.99) + 2 Camisetas (39.98)
('2024-10-11 13:01:00', 474.99, 4, 4, 4),  -- 1 Sofá
('2024-10-11 13:02:00', 59.99, 4, 4, 4),   -- 1 Robot
('2024-10-11 13:03:00', 629.99, 4, 4, 4),  -- 1 Smartphone
('2024-10-11 13:04:00', 39.98, 4, 4, 4),   -- 2 Camisetas
('2024-10-11 13:05:00', 474.99, 4, 4, 4),  -- 1 Sofá
('2024-10-11 13:06:00', 59.99, 4, 4, 4),   -- 1 Robot
('2024-10-11 13:07:00', 629.99, 4, 4, 4),  -- 1 Smartphone
('2024-10-11 13:08:00', 39.98, 4, 4, 4),   -- 2 Camisetas
('2024-10-11 13:09:00', 474.99, 4, 4, 4),  -- 1 Sofá

-- Facturas para usuario 5
('2024-10-11 14:00:00', 669.97, 5, 5, 5),  -- Smartphone (629.99) + 2 Camisetas (39.98)
('2024-10-11 14:01:00', 474.99, 5, 5, 5),  -- 1 Sofá
('2024-10-11 14:02:00', 59.98, 5, 5, 5),   -- 2 Libros Java
('2024-10-11 14:03:00', 629.99, 5, 5, 5),  -- 1 Smartphone
('2024-10-11 14:04:00', 39.98, 5, 5, 5),   -- 2 Camisetas
('2024-10-11 14:05:00', 474.99, 5, 5, 5),  -- 1 Sofá
('2024-10-11 14:06:00', 59.98, 5, 5, 5),   -- 2 Libros Java
('2024-10-11 14:07:00', 629.99, 5, 5, 5),  -- 1 Smartphone
('2024-10-11 14:08:00', 39.98, 5, 5, 5),   -- 2 Camisetas
('2024-10-11 14:09:00', 474.99, 5, 5, 5);  -- 1 Sofá

-- Inserciones en la tabla Resenias
INSERT INTO Resenias (comentario, calificacion, usuario_id, producto_id) VALUES
-- Producto 1 (nuevas reseñas, excluyendo la de usuario_id = 1)
('Muy satisfecho con la compra', 4, 2, 1),
('Buena calidad de construcción', 5, 3, 1),
('Cumple con las expectativas', 4, 4, 1),
('Buen producto pero algo costoso', 3, 5, 1),

-- Producto 2 (nuevas reseñas, excluyendo la de usuario_id = 2)
('Excelente durabilidad', 5, 1, 2),
('Funciona muy bien', 4, 3, 2),
('Gran compra, lo recomiendo', 5, 4, 2),
('Satisface mis necesidades', 4, 5, 2),

-- Producto 3 (nuevas reseñas, excluyendo la de usuario_id = 3)
('Muy útil para el día a día', 5, 1, 3),
('Calidad superior', 4, 2, 3),
('Excelente funcionamiento', 5, 4, 3),
('Buena inversión', 4, 5, 3),

-- Producto 4 (nuevas reseñas, excluyendo la de usuario_id = 4)
('Material muy didáctico', 5, 1, 4),
('Perfecto para aprendizaje', 5, 2, 4),
('Contenido bien estructurado', 4, 3, 4),
('Altamente recomendable', 5, 5, 4),

-- Producto 5 (nuevas reseñas, excluyendo la de usuario_id = 5)
('Fantástico producto educativo', 5, 1, 5),
('Excelente para el aprendizaje', 4, 2, 5),
('Muy interactivo y didáctico', 5, 3, 5),
('Lo uso constantemente', 4, 4, 5);


-- Inserciones en la tabla ProductosCarrito
INSERT INTO ProductosCarrito (cantidad, total, usuario_id, producto_id) VALUES
(2, 39.98, 1, 2),
(1, 699.99, 2, 1),
(1, 499.99, 3, 3),
(3, 89.97, 4, 5),
(2, 59.98, 5, 4);

-- Para las facturas del usuario 1 (no puede comprar productos del usuario 1)
INSERT INTO ProductosFacturas (cantidad, total, producto_nombre, precio_unitario, descuento_unitario_porcentaje, descuento_unitario_valor, descuento_nombre, factura_id, producto_id) VALUES
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 1, 2),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 1, 3),
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 2, 2),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 3, 3),
(3, 89.97, 'Libro de Java', 29.99, NULL, NULL, NULL, 4, 4),
(2, 119.98, 'Juguete Robot', 79.99, 25, 20.00, 'Descuento Black Friday', 5, 5),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 6, 3),
(2, 59.98, 'Libro de Java', 29.99, NULL, NULL, NULL, 7, 4),
(1, 59.99, 'Juguete Robot', 79.99, 25, 20.00, 'Descuento Black Friday', 8, 5),
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 9, 2),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 10, 3);

-- Para las facturas del usuario 2 (no puede comprar productos del usuario 2)
INSERT INTO ProductosFacturas (cantidad, total, producto_nombre, precio_unitario, descuento_unitario_porcentaje, descuento_unitario_valor, descuento_nombre, factura_id, producto_id) VALUES
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 11, 1),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 11, 3),
(2, 59.98, 'Libro de Java', 29.99, NULL, NULL, NULL, 12, 4),
(1, 59.99, 'Juguete Robot', 79.99, 25, 20.00, 'Descuento Black Friday', 13, 5),
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 14, 1),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 15, 3),
(2, 59.98, 'Libro de Java', 29.99, NULL, NULL, NULL, 16, 4),
(1, 59.99, 'Juguete Robot', 79.99, 25, 20.00, 'Descuento Black Friday', 17, 5),
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 18, 1),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 19, 3),
(2, 59.98, 'Libro de Java', 29.99, NULL, NULL, NULL, 20, 4);

-- Para las facturas del usuario 3 (no puede comprar productos del usuario 3)
INSERT INTO ProductosFacturas (cantidad, total, producto_nombre, precio_unitario, descuento_unitario_porcentaje, descuento_unitario_valor, descuento_nombre, factura_id, producto_id) VALUES
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 21, 1),
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 21, 2),
(2, 59.98, 'Libro de Java', 29.99, NULL, NULL, NULL, 22, 4),
(1, 59.99, 'Juguete Robot', 79.99, 25, 20.00, 'Descuento Black Friday', 23, 5),
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 24, 1),
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 25, 2),
(2, 59.98, 'Libro de Java', 29.99, NULL, NULL, NULL, 26, 4),
(1, 59.99, 'Juguete Robot', 79.99, 25, 20.00, 'Descuento Black Friday', 27, 5),
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 28, 1),
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 29, 2),
(2, 59.98, 'Libro de Java', 29.99, NULL, NULL, NULL, 30, 4);

-- Para las facturas del usuario 4 (no puede comprar productos del usuario 4)
INSERT INTO ProductosFacturas (cantidad, total, producto_nombre, precio_unitario, descuento_unitario_porcentaje, descuento_unitario_valor, descuento_nombre, factura_id, producto_id) VALUES
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 31, 1),
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 31, 2),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 32, 3),
(1, 59.99, 'Juguete Robot', 79.99, 25, 20.00, 'Descuento Black Friday', 33, 5),
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 34, 1),
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 35, 2),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 36, 3),
(1, 59.99, 'Juguete Robot', 79.99, 25, 20.00, 'Descuento Black Friday', 37, 5),
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 38, 1),
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 39, 2),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 40, 3);

-- Para las facturas del usuario 5 (no puede comprar productos del usuario 5)
INSERT INTO ProductosFacturas (cantidad, total, producto_nombre, precio_unitario, descuento_unitario_porcentaje, descuento_unitario_valor, descuento_nombre, factura_id, producto_id) VALUES
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 41, 1),
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 41, 2),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 42, 3),
(2, 59.98, 'Libro de Java', 29.99, NULL, NULL, NULL, 43, 4),
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 44, 1),
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 45, 2),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 46, 3),
(2, 59.98, 'Libro de Java', 29.99, NULL, NULL, NULL, 47, 4),
(1, 629.99, 'Smartphone', 699.99, 10, 70.00, 'Descuento de Bienvenida', 48, 1),
(2, 39.98, 'Camiseta', 19.99, NULL, NULL, NULL, 49, 2),
(1, 474.99, 'Sofá', 499.99, 5, 25.00, 'Descuento de Fin de Temporada', 50, 3);


