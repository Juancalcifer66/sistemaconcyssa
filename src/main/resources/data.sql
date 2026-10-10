
-- Insertar roles principales si no existen
INSERT IGNORE INTO roles (id, nombre) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO roles (id, nombre) VALUES (2, 'ROLE_CONTROLADOR');
INSERT IGNORE INTO roles (id, nombre) VALUES (3, 'ROLE_SUPERVISOR');

-- Insertar usuario administrador con DNI y contraseña iguales ('12345678')
INSERT IGNORE INTO usuarios (id, dni, email, estado, nombre_completo, password, username) 
VALUES (1, '12345678', 'admin@concyssa.com', true, 'Administrador SICO', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin');

-- Asociar el rol de ADMIN al usuario 1
INSERT IGNORE INTO usuario_roles (usuario_id, rol_id) VALUES (1, 1);