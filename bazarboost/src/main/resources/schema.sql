-- Alumno: Francisco Williams Jiménez Hernández
-- Proyecto: Bazarboost

USE bazarboost;

-- Eliminación de tablas en el orden adecuado (hijas primero)
DROP TABLE IF EXISTS ProductosFacturas;
DROP TABLE IF EXISTS ProductosCarrito;
DROP TABLE IF EXISTS Resenias;
DROP TABLE IF EXISTS Facturas;
DROP TABLE IF EXISTS MetodosPago;
DROP TABLE IF EXISTS Direcciones;
DROP TABLE IF EXISTS UsuariosRoles;
DROP TABLE IF EXISTS Productos;
DROP TABLE IF EXISTS Descuentos;
DROP TABLE IF EXISTS Roles;
DROP TABLE IF EXISTS Categorias;
DROP TABLE IF EXISTS Usuarios;

-- Creación de tablas y claves foráneas (padres primero)
CREATE TABLE Usuarios (
    usuario_id INT AUTO_INCREMENT,
    nombre VARCHAR(40) NOT NULL,
    apellido_paterno VARCHAR(40) NOT NULL,
    apellido_materno VARCHAR(40) NOT NULL,
    telefono VARCHAR(10) NOT NULL,
    correo_electronico VARCHAR(80) NOT NULL,
    contrasenia VARCHAR(40) NOT NULL,
    CONSTRAINT PK_Usuarios PRIMARY KEY (usuario_id),
    CONSTRAINT UQ_Usuarios_telefono UNIQUE (telefono),
    CONSTRAINT UQ_Usuarios_correo_electronico UNIQUE (correo_electronico)
);

CREATE TABLE Categorias (
    categoria_id INT AUTO_INCREMENT,
    nombre VARCHAR(40) NOT NULL,
    CONSTRAINT PK_Categorias PRIMARY KEY (categoria_id),
    CONSTRAINT UQ_Categorias_nombre UNIQUE (nombre)
);

CREATE TABLE Roles (
    rol_id INT AUTO_INCREMENT,
    nombre VARCHAR(20) NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    CONSTRAINT PK_Roles PRIMARY KEY (rol_id),
    CONSTRAINT UQ_Roles_nombre UNIQUE (nombre)
);

CREATE TABLE Descuentos(
    descuento_id INT AUTO_INCREMENT,
    porcentaje INT NOT NULL,
    nombre VARCHAR(40) NOT NULL,
    usuario_id INT NOT NULL,
    CONSTRAINT PK_Descuentos PRIMARY KEY (descuento_id),
    CONSTRAINT UQ_Descuentos_nombre UNIQUE (nombre),
    CONSTRAINT FK_Descuentos_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE CASCADE,
    CONSTRAINT CK_Descuentos_porcentaje CHECK (porcentaje >= 0 AND porcentaje <= 100)
);

CREATE TABLE Productos (
    producto_id INT AUTO_INCREMENT,
    nombre VARCHAR(40) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    existencia INT NOT NULL,
    imagen_url VARCHAR(255) NOT NULL,
    usuario_id INT NOT NULL,
    descuento_id INT,
    categoria_id INT,
    CONSTRAINT PK_Productos PRIMARY KEY (producto_id),
    CONSTRAINT UQ_Productos_nombre UNIQUE (nombre),
    CONSTRAINT FK_Productos_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE CASCADE,
    CONSTRAINT FK_Productos_descuento_id FOREIGN KEY (descuento_id) REFERENCES Descuentos(descuento_id) ON DELETE SET NULL,
    CONSTRAINT FK_Productos_categoria_id FOREIGN KEY (categoria_id) REFERENCES Categorias(categoria_id) ON DELETE SET NULL,
    CONSTRAINT CK_Productos_precio CHECK (precio >= 0),
    CONSTRAINT CK_Productos_existencia CHECK (existencia >= 0)
);

CREATE TABLE UsuariosRoles (
    usuario_rol_id INT AUTO_INCREMENT,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT NOT NULL,
    rol_id INT NOT NULL,
    CONSTRAINT PK_UsuariosRoles PRIMARY KEY (usuario_rol_id),
    CONSTRAINT UQ_UsuariosRoles_usuario_id_rol_id UNIQUE (usuario_id, rol_id),
    CONSTRAINT FK_UsuariosRoles_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE CASCADE,
    CONSTRAINT FK_UsuariosRoles_rol_id FOREIGN KEY (rol_id) REFERENCES Roles(rol_id) ON DELETE CASCADE
);

CREATE TABLE Direcciones (
    direccion_id INT AUTO_INCREMENT,
    estado VARCHAR(40) NOT NULL,
    ciudad VARCHAR(40) NOT NULL,
    colonia VARCHAR(40) NOT NULL,
    calle VARCHAR(60) NOT NULL,
    numero_domicilio INT NOT NULL,
    codigo_postal VARCHAR(10) NOT NULL,
    usuario_id INT NOT NULL,
    CONSTRAINT PK_Direcciones PRIMARY KEY (direccion_id),
    CONSTRAINT UQ_Direcciones UNIQUE (estado, ciudad, colonia, calle, numero_domicilio, codigo_postal),
    CONSTRAINT FK_Direcciones_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE CASCADE
);

CREATE TABLE MetodosPago (
    metodo_pago_id INT AUTO_INCREMENT,
    nombre_titular VARCHAR(120) NOT NULL,
    numero_tarjeta VARCHAR(20) NOT NULL,
    fecha_expiracion CHAR(7) NOT NULL,
    tipo_tarjeta ENUM('Débito', 'Crédito') NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    usuario_id INT NOT NULL,
    CONSTRAINT PK_MetodosPago PRIMARY KEY (metodo_pago_id),
    CONSTRAINT UQ_MetodosPago_numero_tarjeta UNIQUE (numero_tarjeta),
    CONSTRAINT FK_MetodosPago_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE CASCADE,
    CONSTRAINT CK_MetodosPago_monto CHECK (monto >= 0)
);


CREATE TABLE Facturas (
    factura_id INT AUTO_INCREMENT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subtotal DECIMAL(10, 2) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    porcentaje_impuestos INT NOT NULL,
    usuario_id INT,
    metodo_pago_id INT,
    direccion_id INT,
    CONSTRAINT PK_Facturas PRIMARY KEY (factura_id),
    CONSTRAINT UQ_Facturas_fecha_usuario_id UNIQUE (fecha, usuario_id),
    CONSTRAINT FK_Facturas_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE SET NULL,
    CONSTRAINT FK_Facturas_metodo_pago_id FOREIGN KEY (metodo_pago_id) REFERENCES MetodosPago(metodo_pago_id) ON DELETE SET NULL,
    CONSTRAINT FK_Facturas_direccion_id FOREIGN KEY (direccion_id) REFERENCES Direcciones(direccion_id) ON DELETE SET NULL,
    CONSTRAINT CK_Facturas_subtotal CHECK (subtotal >= 0),
    CONSTRAINT CK_Facturas_total CHECK (total >= 0),
    CONSTRAINT CK_Facturas_porcentaje_impuestos CHECK (porcentaje_impuestos >= 0 AND porcentaje_impuestos <= 100)
);

CREATE TABLE Resenias (
    resenia_id INT AUTO_INCREMENT,
    comentario VARCHAR(255) NOT NULL,
    calificacion INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT,
    producto_id INT NOT NULL,
    CONSTRAINT PK_Resenias PRIMARY KEY (resenia_id),
    CONSTRAINT UQ_Resenias_usuario_id_producto_id UNIQUE (usuario_id, producto_id),
    CONSTRAINT FK_Resenias_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE SET NULL,
    CONSTRAINT FK_Resenias_producto_id FOREIGN KEY (producto_id) REFERENCES Productos(producto_id) ON DELETE CASCADE,
    CONSTRAINT CK_Resenias_calificacion CHECK (calificacion >= 1 AND calificacion <= 5)
);

CREATE TABLE ProductosCarrito(
    producto_carrito_id INT AUTO_INCREMENT,
    cantidad INT NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    usuario_id INT NOT NULL,
    producto_id INT NOT NULL,
    CONSTRAINT PK_ProductosCarrito PRIMARY KEY (producto_carrito_id),
    CONSTRAINT UQ_ProductosCarrito_producto_id_usuario_id UNIQUE (producto_id, usuario_id),
    CONSTRAINT FK_ProductosCarrito_producto_id FOREIGN KEY (producto_id) REFERENCES Productos(producto_id) ON DELETE CASCADE,
    CONSTRAINT FK_ProductosCarrito_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id) ON DELETE CASCADE,
    CONSTRAINT CK_ProductosCarrito_cantidad CHECK (cantidad > 0),
    CONSTRAINT CK_ProductosCarrito_total CHECK (total >= 0)
);

CREATE TABLE ProductosFacturas(
    producto_factura_id INT AUTO_INCREMENT,
    cantidad INT NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    factura_id INT NOT NULL,
    producto_id INT,
    CONSTRAINT PK_ProductosFacturas PRIMARY KEY (producto_factura_id),
    CONSTRAINT UQ_ProductosFacturas_producto_id_factura_id UNIQUE (producto_id, factura_id),
    CONSTRAINT FK_ProductosFacturas_producto_id FOREIGN KEY (producto_id) REFERENCES Productos(producto_id) ON DELETE SET NULL,
    CONSTRAINT FK_ProductosFacturas_factura_id FOREIGN KEY (factura_id) REFERENCES Facturas(factura_id) ON DELETE CASCADE,
    CONSTRAINT CK_ProductosFacturas_cantidad CHECK (cantidad > 0),
    CONSTRAINT CK_ProductosFacturas_total CHECK (total >= 0)
);

-- Creación de índices
CREATE INDEX idx_facturas_usuario_id ON Facturas(usuario_id);
CREATE INDEX idx_productosfacturas_factura_id ON ProductosFacturas(factura_id);
CREATE INDEX idx_productos_producto_id ON Productos(producto_id);
CREATE INDEX idx_metodospago_metodo_pago_id ON MetodosPago(metodo_pago_id);
CREATE INDEX idx_direcciones_direccion_id ON Direcciones(direccion_id);
CREATE INDEX idx_usuarios_correo_electronico ON Usuarios(correo_electronico);
CREATE INDEX idx_productoscarrito_usuario_id ON ProductosCarrito(usuario_id);
CREATE INDEX idx_metodospago_usuario_id ON MetodosPago(usuario_id);
CREATE INDEX idx_direcciones_usuario_id ON Direcciones(usuario_id);
CREATE INDEX idx_descuentos_usuario_id ON Descuentos(usuario_id);
CREATE INDEX idx_resenias_producto_id ON Resenias(producto_id);
