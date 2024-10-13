-- Alumno: Francisco Williams Jiménez Hernández
-- Proyecto: Bazarboost

-- Inserts de datos de prueba

-- Inserciones en la tabla Usuarios
INSERT INTO Usuarios (nombre, apellido_paterno, apellido_materno, correo_electronico, contrasenia, telefono) VALUES
('Juan', 'Pérez', 'López', 'juan.perez@example.com', 'password123', '5551234567'),
('María', 'Gómez', 'Martínez', 'maria.gomez@example.com', 'password123', '5557654321'),
('Pedro', 'Sánchez', 'Hernández', 'pedro.sanchez@example.com', 'password123', '5556781234'),
('Ana', 'Ramírez', 'García', 'ana.ramirez@example.com', 'password123', '5559876543'),
('Carlos', 'Fernández', 'Pérez', 'carlos.fernandez@example.com', 'password123', '5554567890');

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
('Smartphone', 'Último modelo con pantalla AMOLED', 699.99, 50, 'img/smartphone.jpg', 1, 1, 1),
('Camiseta', 'Camiseta 100% algodón', 19.99, 200, 'img/camiseta.jpg', 2, NULL, 2),
('Sofá', 'Sofá de tres plazas', 499.99, 10, 'img/sofa.jpg', 3, 3, 3),
('Libro de Java', 'Guía avanzada de Java', 29.99, 100, 'img/libro_java.jpg', 4, NULL, 5),
('Juguete Robot', 'Robot de juguete interactivo', 79.99, 30, 'img/robot.jpg', 5, 5, 4);

-- Inserciones en la tabla UsuariosRoles
INSERT INTO UsuariosRoles (usuario_id, rol_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 2),
(5, 3);

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
INSERT INTO Facturas (fecha, subtotal, total, porcentaje_impuestos, usuario_id, metodo_pago_id, direccion_id) VALUES

-- Facturas para usuario 1
('2024-10-11 10:00:00', 150.00, 165.00, 10, 1, 1, 1),
('2024-10-11 10:01:00', 160.00, 176.00, 10, 1, 1, 1),
('2024-10-11 10:02:00', 170.00, 187.00, 10, 1, 1, 1),
('2024-10-11 10:03:00', 180.00, 198.00, 10, 1, 1, 1),
('2024-10-11 10:04:00', 190.00, 209.00, 10, 1, 1, 1),
('2024-10-11 10:05:00', 200.00, 220.00, 10, 1, 1, 1),
('2024-10-11 10:06:00', 210.00, 231.00, 10, 1, 1, 1),
('2024-10-11 10:07:00', 220.00, 242.00, 10, 1, 1, 1),
('2024-10-11 10:08:00', 230.00, 253.00, 10, 1, 1, 1),
('2024-10-11 10:09:00', 240.00, 264.00, 10, 1, 1, 1),

-- Facturas para usuario 2
('2024-10-11 11:00:00', 300.00, 330.00, 10, 2, 2, 2),
('2024-10-11 11:01:00', 310.00, 341.00, 10, 2, 2, 2),
('2024-10-11 11:02:00', 320.00, 352.00, 10, 2, 2, 2),
('2024-10-11 11:03:00', 330.00, 363.00, 10, 2, 2, 2),
('2024-10-11 11:04:00', 340.00, 374.00, 10, 2, 2, 2),
('2024-10-11 11:05:00', 350.00, 385.00, 10, 2, 2, 2),
('2024-10-11 11:06:00', 360.00, 396.00, 10, 2, 2, 2),
('2024-10-11 11:07:00', 370.00, 407.00, 10, 2, 2, 2),
('2024-10-11 11:08:00', 380.00, 418.00, 10, 2, 2, 2),
('2024-10-11 11:09:00', 390.00, 429.00, 10, 2, 2, 2),

-- Facturas para usuario 3
('2024-10-11 12:00:00', 450.00, 495.00, 10, 3, 3, 3),
('2024-10-11 12:01:00', 460.00, 506.00, 10, 3, 3, 3),
('2024-10-11 12:02:00', 470.00, 517.00, 10, 3, 3, 3),
('2024-10-11 12:03:00', 480.00, 528.00, 10, 3, 3, 3),
('2024-10-11 12:04:00', 490.00, 539.00, 10, 3, 3, 3),
('2024-10-11 12:05:00', 500.00, 550.00, 10, 3, 3, 3),
('2024-10-11 12:06:00', 510.00, 561.00, 10, 3, 3, 3),
('2024-10-11 12:07:00', 520.00, 572.00, 10, 3, 3, 3),
('2024-10-11 12:08:00', 530.00, 583.00, 10, 3, 3, 3),
('2024-10-11 12:09:00', 540.00, 594.00, 10, 3, 3, 3),

-- Facturas para usuario 4
('2024-10-11 13:00:00', 200.00, 220.00, 10, 4, 4, 4),
('2024-10-11 13:01:00', 210.00, 231.00, 10, 4, 4, 4),
('2024-10-11 13:02:00', 220.00, 242.00, 10, 4, 4, 4),
('2024-10-11 13:03:00', 230.00, 253.00, 10, 4, 4, 4),
('2024-10-11 13:04:00', 240.00, 264.00, 10, 4, 4, 4),
('2024-10-11 13:05:00', 250.00, 275.00, 10, 4, 4, 4),
('2024-10-11 13:06:00', 260.00, 286.00, 10, 4, 4, 4),
('2024-10-11 13:07:00', 270.00, 297.00, 10, 4, 4, 4),
('2024-10-11 13:08:00', 280.00, 308.00, 10, 4, 4, 4),
('2024-10-11 13:09:00', 290.00, 319.00, 10, 4, 4, 4),

-- Facturas para usuario 5
('2024-10-11 14:00:00', 100.00, 110.00, 10, 5, 5, 5),
('2024-10-11 14:01:00', 110.00, 121.00, 10, 5, 5, 5),
('2024-10-11 14:02:00', 120.00, 132.00, 10, 5, 5, 5),
('2024-10-11 14:03:00', 130.00, 143.00, 10, 5, 5, 5),
('2024-10-11 14:04:00', 140.00, 154.00, 10, 5, 5, 5),
('2024-10-11 14:05:00', 150.00, 165.00, 10, 5, 5, 5),
('2024-10-11 14:06:00', 160.00, 176.00, 10, 5, 5, 5),
('2024-10-11 14:07:00', 170.00, 187.00, 10, 5, 5, 5),
('2024-10-11 14:08:00', 180.00, 198.00, 10, 5, 5, 5),
('2024-10-11 14:09:00', 190.00, 209.00, 10, 5, 5, 5);



-- Inserciones en la tabla Resenias
INSERT INTO Resenias (comentario, calificacion, usuario_id, producto_id) VALUES
('Excelente producto', 5, 1, 1),
('Buena calidad', 4, 2, 2),
('Cómodo y práctico', 4, 3, 3),
('Muy útil para aprender Java', 5, 4, 4),
('Divertido y educativo', 5, 5, 5);

-- Inserciones en la tabla ProductosCarrito
INSERT INTO ProductosCarrito (cantidad, total, usuario_id, producto_id) VALUES
(2, 39.98, 1, 2),
(1, 699.99, 2, 1),
(1, 499.99, 3, 3),
(3, 89.97, 4, 5),
(2, 59.98, 5, 4);

-- Inserciones en la tabla ProductosFacturas
INSERT INTO ProductosFacturas (cantidad, total, factura_id, producto_id) VALUES
(1, 699.99, 1, 1),
(2, 39.98, 2, 2),
(1, 499.99, 3, 3),
(1, 29.99, 4, 4),
(1, 79.99, 5, 5);
