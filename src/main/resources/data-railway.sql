-- Datos iniciales para Railway
-- Este script se ejecutará después de que Hibernate cree las tablas

-- Insertar categorías principales (solo si no existen)
INSERT INTO categorias (nombre, descripcion, activo, orden_visualizacion, color_hexadecimal, icono, fecha_creacion, fecha_modificacion, creado_por) 
SELECT 'Servicios de hogar', 'Servicios domésticos y de mantención del hogar', true, 1, '#4CAF50', 'home', NOW(), NOW(), 'sistema'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Servicios de hogar');

INSERT INTO categorias (nombre, descripcion, activo, orden_visualizacion, color_hexadecimal, icono, fecha_creacion, fecha_modificacion, creado_por)
SELECT 'Servicios técnicos', 'Servicios técnicos y urgencias especializadas', true, 2, '#FF9800', 'build', NOW(), NOW(), 'sistema'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Servicios técnicos');

INSERT INTO categorias (nombre, descripcion, activo, orden_visualizacion, color_hexadecimal, icono, fecha_creacion, fecha_modificacion, creado_por)
SELECT 'Servicios de belleza y bienestar', 'Servicios de cuidado personal y bienestar', true, 3, '#E91E63', 'spa', NOW(), NOW(), 'sistema'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Servicios de belleza y bienestar');

INSERT INTO categorias (nombre, descripcion, activo, orden_visualizacion, color_hexadecimal, icono, fecha_creacion, fecha_modificacion, creado_por)
SELECT 'Servicios de mascotas', 'Servicios de cuidado y atención para mascotas', true, 4, '#9C27B0', 'pets', NOW(), NOW(), 'sistema'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Servicios de mascotas');

INSERT INTO categorias (nombre, descripcion, activo, orden_visualizacion, color_hexadecimal, icono, fecha_creacion, fecha_modificacion, creado_por)
SELECT 'Servicios automotrices', 'Servicios de mantención y reparación automotriz', true, 5, '#3F51B5', 'directions_car', NOW(), NOW(), 'sistema'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Servicios automotrices');

INSERT INTO categorias (nombre, descripcion, activo, orden_visualizacion, color_hexadecimal, icono, fecha_creacion, fecha_modificacion, creado_por)
SELECT 'Eventos y fotografía', 'Servicios para eventos y producción audiovisual', true, 6, '#F44336', 'event', NOW(), NOW(), 'sistema'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Eventos y fotografía');

INSERT INTO categorias (nombre, descripcion, activo, orden_visualizacion, color_hexadecimal, icono, fecha_creacion, fecha_modificacion, creado_por)
SELECT 'Educación y clases', 'Servicios educativos y de capacitación', true, 7, '#2196F3', 'school', NOW(), NOW(), 'sistema'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Educación y clases');

INSERT INTO categorias (nombre, descripcion, activo, orden_visualizacion, color_hexadecimal, icono, fecha_creacion, fecha_modificacion, creado_por)
SELECT 'Profesionales y trámites', 'Servicios profesionales y gestión de trámites', true, 8, '#607D8B', 'business', NOW(), NOW(), 'sistema'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Profesionales y trámites');

-- Las subcategorías se insertarán después mediante el script categorias-data.sql
