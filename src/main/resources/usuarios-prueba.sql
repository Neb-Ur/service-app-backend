-- Script para ingresar usuarios de prueba en la base de datos

-- Usuarios técnicos (tipo_usuario = 'TECNICO')
INSERT INTO usuarios (id, nombre, apellido, email, telefono, tipo_usuario, activo, fecha_creacion) VALUES
(1, 'Técnico 1', 'Apellido 1', 'tecnico1@example.com', '+5550001', 'TECNICO', true, CURRENT_TIMESTAMP),
(2, 'Técnico 2', 'Apellido 2', 'tecnico2@example.com', '+5550002', 'TECNICO', true, CURRENT_TIMESTAMP),
(3, 'Técnico 3', 'Apellido 3', 'tecnico3@example.com', '+5550003', 'TECNICO', true, CURRENT_TIMESTAMP),
(4, 'Técnico 4', 'Apellido 4', 'tecnico4@example.com', '+5550004', 'TECNICO', true, CURRENT_TIMESTAMP),
(5, 'Técnico 5', 'Apellido 5', 'tecnico5@example.com', '+5550005', 'TECNICO', true, CURRENT_TIMESTAMP),
(6, 'Técnico 6', 'Apellido 6', 'tecnico6@example.com', '+5550006', 'TECNICO', true, CURRENT_TIMESTAMP),
(7, 'Técnico 7', 'Apellido 7', 'tecnico7@example.com', '+5550007', 'TECNICO', true, CURRENT_TIMESTAMP),
(8, 'Técnico 8', 'Apellido 8', 'tecnico8@example.com', '+5550008', 'TECNICO', true, CURRENT_TIMESTAMP),
(9, 'Técnico 9', 'Apellido 9', 'tecnico9@example.com', '+5550009', 'TECNICO', true, CURRENT_TIMESTAMP),
(10, 'Técnico 10', 'Apellido 10', 'tecnico10@example.com', '+5550010', 'TECNICO', true, CURRENT_TIMESTAMP),
(11, 'Técnico 11', 'Apellido 11', 'tecnico11@example.com', '+5550011', 'TECNICO', true, CURRENT_TIMESTAMP),
(12, 'Técnico 12', 'Apellido 12', 'tecnico12@example.com', '+5550012', 'TECNICO', true, CURRENT_TIMESTAMP),
(13, 'Técnico 13', 'Apellido 13', 'tecnico13@example.com', '+5550013', 'TECNICO', true, CURRENT_TIMESTAMP),
(14, 'Técnico 14', 'Apellido 14', 'tecnico14@example.com', '+5550014', 'TECNICO', true, CURRENT_TIMESTAMP),
(15, 'Técnico 15', 'Apellido 15', 'tecnico15@example.com', '+5550015', 'TECNICO', true, CURRENT_TIMESTAMP),
(16, 'Técnico 16', 'Apellido 16', 'tecnico16@example.com', '+5550016', 'TECNICO', true, CURRENT_TIMESTAMP),
(17, 'Técnico 17', 'Apellido 17', 'tecnico17@example.com', '+5550017', 'TECNICO', true, CURRENT_TIMESTAMP),
(18, 'Técnico 18', 'Apellido 18', 'tecnico18@example.com', '+5550018', 'TECNICO', true, CURRENT_TIMESTAMP),
(19, 'Técnico 19', 'Apellido 19', 'tecnico19@example.com', '+5550019', 'TECNICO', true, CURRENT_TIMESTAMP),
(20, 'Técnico 20', 'Apellido 20', 'tecnico20@example.com', '+5550020', 'TECNICO', true, CURRENT_TIMESTAMP);

-- Usuarios no técnicos (tipo_usuario = 'CLIENTE')
INSERT INTO usuarios (id, nombre, apellido, email, telefono, tipo_usuario, activo, fecha_creacion) VALUES
(21, 'Usuario 21', 'Apellido 21', 'usuario21@example.com', '+5550021', 'CLIENTE', true, CURRENT_TIMESTAMP),
(22, 'Usuario 22', 'Apellido 22', 'usuario22@example.com', '+5550022', 'CLIENTE', true, CURRENT_TIMESTAMP),
(23, 'Usuario 23', 'Apellido 23', 'usuario23@example.com', '+5550023', 'CLIENTE', true, CURRENT_TIMESTAMP),
(24, 'Usuario 24', 'Apellido 24', 'usuario24@example.com', '+5550024', 'CLIENTE', true, CURRENT_TIMESTAMP),
(25, 'Usuario 25', 'Apellido 25', 'usuario25@example.com', '+5550025', 'CLIENTE', true, CURRENT_TIMESTAMP),
(26, 'Usuario 26', 'Apellido 26', 'usuario26@example.com', '+5550026', 'CLIENTE', true, CURRENT_TIMESTAMP),
(27, 'Usuario 27', 'Apellido 27', 'usuario27@example.com', '+5550027', 'CLIENTE', true, CURRENT_TIMESTAMP),
(28, 'Usuario 28', 'Apellido 28', 'usuario28@example.com', '+5550028', 'CLIENTE', true, CURRENT_TIMESTAMP),
(29, 'Usuario 29', 'Apellido 29', 'usuario29@example.com', '+5550029', 'CLIENTE', true, CURRENT_TIMESTAMP),
(30, 'Usuario 30', 'Apellido 30', 'usuario30@example.com', '+5550030', 'CLIENTE', true, CURRENT_TIMESTAMP);

-- Técnicos asociados a usuarios técnicos y subcategoría 1 (Jardinería)
INSERT INTO tecnicos (usuario_id, descripcion_profesional, clasificacion, rating_promedio, total_resenas, total_trabajos_completados, precio_base_hora, disponible, verificado, certificados, anos_experiencia, radio_cobertura_km, activo, subcategoria_id, fecha_creacion) VALUES
(1, 'Experto en jardinería y mantención de áreas verdes', 5, 4.8, 10, 50, 15000, true, true, 'Certificado Jardinería', 8, 20, true, 1, CURRENT_TIMESTAMP),
(2, 'Especialista en diseño de jardines', 4, 4.5, 8, 40, 14000, true, false, 'Certificado Paisajismo', 6, 15, true, 1, CURRENT_TIMESTAMP),
(3, 'Técnico en mantención de césped', 4, 4.2, 6, 30, 13000, true, false, 'Certificado Césped', 5, 10, true, 1, CURRENT_TIMESTAMP),
(4, 'Jardinero profesional', 5, 4.9, 12, 60, 16000, true, true, 'Certificado Jardinería', 10, 25, true, 1, CURRENT_TIMESTAMP),
(5, 'Experto en plantas ornamentales', 4, 4.3, 7, 35, 13500, true, false, 'Certificado Plantas', 7, 18, true, 1, CURRENT_TIMESTAMP),
(6, 'Especialista en poda de árboles', 5, 4.7, 9, 45, 14500, true, true, 'Certificado Poda', 9, 22, true, 1, CURRENT_TIMESTAMP),
(7, 'Técnico en sistemas de riego', 4, 4.1, 5, 25, 12500, true, false, 'Certificado Riego', 4, 12, true, 1, CURRENT_TIMESTAMP),
(8, 'Jardinero integral', 5, 4.6, 11, 55, 15500, true, true, 'Certificado Jardinería', 8, 20, true, 1, CURRENT_TIMESTAMP),
(9, 'Especialista en fertilización', 4, 4.4, 8, 38, 13800, true, false, 'Certificado Fertilización', 6, 16, true, 1, CURRENT_TIMESTAMP),
(10, 'Técnico en paisajismo', 5, 4.8, 10, 50, 15000, true, true, 'Certificado Paisajismo', 8, 20, true, 1, CURRENT_TIMESTAMP),
(11, 'Jardinero de mantenimiento', 4, 4.2, 6, 30, 13000, true, false, 'Certificado Jardinería', 5, 10, true, 1, CURRENT_TIMESTAMP),
(12, 'Especialista en plantas medicinales', 4, 4.5, 8, 40, 14000, true, false, 'Certificado Plantas', 6, 15, true, 1, CURRENT_TIMESTAMP),
(13, 'Técnico en compostaje', 4, 4.3, 7, 35, 13500, true, false, 'Certificado Compostaje', 7, 18, true, 1, CURRENT_TIMESTAMP),
(14, 'Jardinero ecológico', 5, 4.7, 9, 45, 14500, true, true, 'Certificado Jardinería', 9, 22, true, 1, CURRENT_TIMESTAMP),
(15, 'Especialista en control de plagas', 4, 4.1, 5, 25, 12500, true, false, 'Certificado Plagas', 4, 12, true, 1, CURRENT_TIMESTAMP),
(16, 'Jardinero paisajista', 5, 4.6, 11, 55, 15500, true, true, 'Certificado Paisajismo', 8, 20, true, 1, CURRENT_TIMESTAMP),
(17, 'Técnico en viveros', 4, 4.4, 8, 38, 13800, true, false, 'Certificado Viveros', 6, 16, true, 1, CURRENT_TIMESTAMP),
(18, 'Especialista en diseño sustentable', 5, 4.8, 10, 50, 15000, true, true, 'Certificado Sustentabilidad', 8, 20, true, 1, CURRENT_TIMESTAMP),
(19, 'Jardinero urbano', 4, 4.2, 6, 30, 13000, true, false, 'Certificado Jardinería Urbana', 5, 10, true, 1, CURRENT_TIMESTAMP),
(20, 'Técnico en paisajismo vertical', 5, 4.9, 12, 60, 16000, true, true, 'Certificado Vertical', 10, 25, true, 1, CURRENT_TIMESTAMP);