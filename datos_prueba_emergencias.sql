-- Script de datos de prueba para Sistema de Emergencias
-- Ejecutar después de que las tablas se hayan creado automáticamente

-- ============================================
-- 1. CREAR USUARIOS CON UBICACIÓN
-- ============================================

-- Cliente en Santiago Centro
INSERT INTO usuarios (nombre, apellido, email, telefono, direccion, latitude, longitude, tipo_usuario, activo)
VALUES 
('María', 'González', 'maria.gonzalez@test.com', '+56912345678', 
 'Calle Ahumada 123, Santiago', -33.4489, -70.6693, 'CLIENTE', true);

-- Técnicos en diferentes ubicaciones
INSERT INTO usuarios (nombre, apellido, email, telefono, direccion, latitude, longitude, tipo_usuario, activo)
VALUES 
('Juan', 'Pérez', 'juan.perez@test.com', '+56987654321',
 'Calle Huérfanos 456, Santiago', -33.4450, -70.6650, 'TECNICO', true),
 
('Pedro', 'López', 'pedro.lopez@test.com', '+56976543210',
 'Calle Bandera 789, Santiago', -33.4500, -70.6700, 'TECNICO', true),
 
('Carlos', 'Muñoz', 'carlos.munoz@test.com', '+56965432109',
 'Calle Moneda 321, Santiago', -33.4480, -70.6680, 'TECNICO', true);

-- ============================================
-- 2. CREAR CATEGORÍAS Y SUBCATEGORÍAS
-- ============================================

INSERT INTO categorias (nombre, descripcion, icono, activo)
VALUES 
('Plomería', 'Servicios de plomería', 'plumbing', true),
('Electricidad', 'Servicios eléctricos', 'electric', true),
('Gas', 'Servicios de gas', 'gas', true),
('Cerrajería', 'Servicios de cerrajería', 'lock', true);

-- Subcategorías de Plomería
INSERT INTO subcategorias (nombre, descripcion, categoria_id, activo)
VALUES 
('Fuga de Agua', 'Reparación de fugas de agua', 1, true),
('Destape de Cañería', 'Destape de cañerías', 1, true),
('Instalación', 'Instalación de artefactos', 1, true);

-- Subcategorías de Electricidad  
INSERT INTO subcategorias (nombre, descripcion, categoria_id, activo)
VALUES 
('Corte de Luz', 'Reparación de cortes eléctricos', 2, true),
('Cortocircuito', 'Reparación de cortocircuitos', 2, true),
('Instalación Eléctrica', 'Instalación de sistemas eléctricos', 2, true);

-- Subcategorías de Gas
INSERT INTO subcategorias (nombre, descripcion, categoria_id, activo)
VALUES 
('Fuga de Gas', 'Reparación de fugas de gas', 3, true),
('Calefón', 'Reparación de calefones', 3, true);

-- Subcategorías de Cerrajería
INSERT INTO subcategorias (nombre, descripcion, categoria_id, activo)
VALUES 
('Puerta Trabada', 'Apertura de puertas trabadas', 4, true),
('Cambio de Cerradura', 'Cambio e instalación de cerraduras', 4, true);

-- ============================================
-- 3. CREAR TÉCNICOS CON ESPECIALIDADES
-- ============================================

-- Juan Pérez - Plomero
INSERT INTO tecnicos (
  descripcion_profesional, clasificacion, rating_promedio, total_resenas, 
  total_trabajos_completados, precio_base_hora, disponible, verificado, 
  anos_experiencia, radio_cobertura, activo, usuario_id, subcategoria_id
)
VALUES (
  'Plomero con 10 años de experiencia. Especialista en emergencias.',
  5, 4.8, 150, 500, 25000, true, true, 10, 15, true, 2, 1
);

-- Pedro López - Electricista
INSERT INTO tecnicos (
  descripcion_profesional, clasificacion, rating_promedio, total_resenas,
  total_trabajos_completados, precio_base_hora, disponible, verificado,
  anos_experiencia, radio_cobertura, activo, usuario_id, subcategoria_id
)
VALUES (
  'Electricista certificado SEC. Atención de emergencias 24/7.',
  5, 4.9, 200, 600, 30000, true, true, 12, 20, true, 3, 4
);

-- Carlos Muñoz - Gasista
INSERT INTO tecnicos (
  descripcion_profesional, clasificacion, rating_promedio, total_resenas,
  total_trabajos_completados, precio_base_hora, disponible, verificado,
  anos_experiencia, radio_cobertura, activo, usuario_id, subcategoria_id
)
VALUES (
  'Gasista certificado SEC. Especialista en fugas y emergencias.',
  5, 4.7, 120, 400, 35000, true, true, 8, 10, true, 4, 7
);

-- ============================================
-- 4. DATOS DE EJEMPLO ADICIONALES
-- ============================================

-- Más técnicos de plomería (para pruebas de múltiples notificaciones)
INSERT INTO usuarios (nombre, apellido, email, telefono, direccion, latitude, longitude, tipo_usuario, activo)
VALUES 
('Luis', 'Silva', 'luis.silva@test.com', '+56954321098',
 'Calle Agustinas 654, Santiago', -33.4470, -70.6660, 'TECNICO', true),
 
('Roberto', 'Rojas', 'roberto.rojas@test.com', '+56943210987',
 'Calle Compañía 987, Santiago', -33.4460, -70.6670, 'TECNICO', true);

INSERT INTO tecnicos (
  descripcion_profesional, clasificacion, rating_promedio, total_resenas,
  total_trabajos_completados, precio_base_hora, disponible, verificado,
  anos_experiencia, radio_cobertura, activo, usuario_id, subcategoria_id
)
VALUES 
('Plomero especializado en emergencias residenciales.',
 4, 4.6, 80, 300, 22000, true, true, 7, 12, true, 5, 1),
 
('Plomero con experiencia en edificios y departamentos.',
 4, 4.5, 60, 250, 23000, true, true, 6, 10, true, 6, 1);

-- ============================================
-- CONSULTAS ÚTILES PARA VERIFICAR
-- ============================================

-- Ver todos los usuarios con ubicación
-- SELECT id, nombre, apellido, email, latitude, longitude, tipo_usuario FROM usuarios;

-- Ver todos los técnicos con su especialidad
-- SELECT 
--   t.id, u.nombre, u.apellido, s.nombre as especialidad, 
--   t.disponible, t.verificado, u.latitude, u.longitude
-- FROM tecnicos t
-- JOIN usuarios u ON t.usuario_id = u.id
-- JOIN subcategorias s ON t.subcategoria_id = s.id;

-- Ver categorías y subcategorías
-- SELECT c.nombre as categoria, s.nombre as subcategoria, s.id as subcategoria_id
-- FROM subcategorias s
-- JOIN categorias c ON s.categoria_id = c.id;

-- Buscar técnicos cercanos a un punto (ejemplo: -33.4489, -70.6693)
-- SELECT 
--   t.id as tecnico_id,
--   u.nombre,
--   u.apellido,
--   s.nombre as especialidad,
--   (6371 * acos(
--     cos(radians(-33.4489)) * cos(radians(u.latitude)) * 
--     cos(radians(u.longitude) - radians(-70.6693)) + 
--     sin(radians(-33.4489)) * sin(radians(u.latitude))
--   )) AS distancia_km
-- FROM tecnicos t
-- JOIN usuarios u ON t.usuario_id = u.id
-- JOIN subcategorias s ON t.subcategoria_id = s.id
-- WHERE t.disponible = true 
--   AND t.activo = true
--   AND u.latitude IS NOT NULL
-- HAVING distancia_km <= 10
-- ORDER BY distancia_km;
