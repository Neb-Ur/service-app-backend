-- Insertar categorías principales
INSERT INTO categorias (nombre, descripcion, activo, orden_visualizacion, color_hexadecimal, icono, fecha_creacion, fecha_modificacion, creado_por) VALUES
('Servicios de hogar', 'Servicios domésticos y de mantención del hogar', true, 1, '#4CAF50', 'home', NOW(), NOW(), 'sistema'),
('Servicios técnicos', 'Servicios técnicos y urgencias especializadas', true, 2, '#FF9800', 'build', NOW(), NOW(), 'sistema'),
('Servicios de belleza y bienestar', 'Servicios de cuidado personal y bienestar', true, 3, '#E91E63', 'spa', NOW(), NOW(), 'sistema'),
('Servicios de mascotas', 'Servicios de cuidado y atención para mascotas', true, 4, '#9C27B0', 'pets', NOW(), NOW(), 'sistema'),
('Servicios automotrices', 'Servicios de mantención y reparación automotriz', true, 5, '#3F51B5', 'directions_car', NOW(), NOW(), 'sistema'),
('Eventos y fotografía', 'Servicios para eventos y producción audiovisual', true, 6, '#F44336', 'event', NOW(), NOW(), 'sistema'),
('Educación y clases', 'Servicios educativos y de capacitación', true, 7, '#2196F3', 'school', NOW(), NOW(), 'sistema'),
('Profesionales y trámites', 'Servicios profesionales y gestión de trámites', true, 8, '#607D8B', 'business', NOW(), NOW(), 'sistema');

-- Insertar subcategorías para "Servicios de hogar"
INSERT INTO subcategorias (nombre, descripcion, activo, orden_visualizacion, categoria_id, fecha_creacion, fecha_modificacion, creado_por) VALUES
('Jardinería', 'Servicios de mantención y diseño de jardines', true, 1, 1, NOW(), NOW(), 'sistema'),
('Limpieza hogar', 'Servicios de limpieza doméstica regular', true, 2, 1, NOW(), NOW(), 'sistema'),
('Limpieza post-obra', 'Limpieza especializada después de construcción o remodelación', true, 3, 1, NOW(), NOW(), 'sistema'),
('Pintura', 'Servicios de pintura interior y exterior', true, 4, 1, NOW(), NOW(), 'sistema'),
('Carpintería', 'Trabajos de carpintería y mueblería', true, 5, 1, NOW(), NOW(), 'sistema'),
('Albañilería', 'Servicios de construcción y reparación en albañilería', true, 6, 1, NOW(), NOW(), 'sistema'),
('Gasfitería', 'Servicios de plomería y gasfitería doméstica', true, 7, 1, NOW(), NOW(), 'sistema'),
('Electricidad residencial', 'Instalaciones y reparaciones eléctricas residenciales', true, 8, 1, NOW(), NOW(), 'sistema'),
('Climatización (A/C, calefacción)', 'Servicios de aire acondicionado y calefacción', true, 9, 1, NOW(), NOW(), 'sistema'),
('Mudanzas/Fletes pequeños', 'Servicios de mudanza y transporte de menor escala', true, 10, 1, NOW(), NOW(), 'sistema'),
('Sanitización', 'Servicios de desinfección y sanitización', true, 11, 1, NOW(), NOW(), 'sistema');

-- Insertar subcategorías para "Servicios técnicos"
INSERT INTO subcategorias (nombre, descripcion, activo, orden_visualizacion, categoria_id, fecha_creacion, fecha_modificacion, creado_por) VALUES
('Cerrajería', 'Servicios de cerrajería y seguridad', true, 1, 2, NOW(), NOW(), 'sistema'),
('Plomería/Gasfitería técnica', 'Servicios técnicos especializados de plomería', true, 2, 2, NOW(), NOW(), 'sistema'),
('Electricidad (tableros/certificaciones)', 'Trabajos eléctricos especializados y certificaciones', true, 3, 2, NOW(), NOW(), 'sistema'),
('Reparación de electrodomésticos', 'Reparación y mantención de electrodomésticos', true, 4, 2, NOW(), NOW(), 'sistema'),
('Redes/Internet', 'Instalación y configuración de redes e internet', true, 5, 2, NOW(), NOW(), 'sistema'),
('CCTV/Alarmas', 'Sistemas de seguridad y videovigilancia', true, 6, 2, NOW(), NOW(), 'sistema'),
('Smart Home (domótica)', 'Automatización y tecnología para el hogar', true, 7, 2, NOW(), NOW(), 'sistema'),
('Soporte PC/Notebook', 'Soporte técnico de computadores y equipos', true, 8, 2, NOW(), NOW(), 'sistema');

-- Insertar subcategorías para "Servicios de belleza y bienestar"
INSERT INTO subcategorias (nombre, descripcion, activo, orden_visualizacion, categoria_id, fecha_creacion, fecha_modificacion, creado_por) VALUES
('Peluquería', 'Servicios de corte y peinado', true, 1, 3, NOW(), NOW(), 'sistema'),
('Barbería', 'Servicios especializados de barbería masculina', true, 2, 3, NOW(), NOW(), 'sistema'),
('Manicure/Pedicure', 'Cuidado de uñas de manos y pies', true, 3, 3, NOW(), NOW(), 'sistema'),
('Maquillaje', 'Servicios de maquillaje profesional', true, 4, 3, NOW(), NOW(), 'sistema'),
('Depilación', 'Servicios de depilación y estética', true, 5, 3, NOW(), NOW(), 'sistema'),
('Masajes/Wellness (no médico)', 'Masajes relajantes y terapias de bienestar', true, 6, 3, NOW(), NOW(), 'sistema');

-- Insertar subcategorías para "Servicios de mascotas"
INSERT INTO subcategorias (nombre, descripcion, activo, orden_visualizacion, categoria_id, fecha_creacion, fecha_modificacion, creado_por) VALUES
('Paseo de mascotas', 'Servicios de paseo y ejercicio para mascotas', true, 1, 4, NOW(), NOW(), 'sistema'),
('Baño/Peluquería canina', 'Servicios de aseo y estética para mascotas', true, 2, 4, NOW(), NOW(), 'sistema'),
('Entrenamiento/Adiestramiento', 'Entrenamiento y educación de mascotas', true, 3, 4, NOW(), NOW(), 'sistema'),
('Cuidado a domicilio/Pensión', 'Cuidado de mascotas en casa o pensión', true, 4, 4, NOW(), NOW(), 'sistema'),
('Veterinaria a domicilio', 'Servicios veterinarios a domicilio', true, 5, 4, NOW(), NOW(), 'sistema');

-- Insertar subcategorías para "Servicios automotrices"
INSERT INTO subcategorias (nombre, descripcion, activo, orden_visualizacion, categoria_id, fecha_creacion, fecha_modificacion, creado_por) VALUES
('Mecánica ligera', 'Servicios básicos de mecánica automotriz', true, 1, 5, NOW(), NOW(), 'sistema'),
('Electricidad automotriz', 'Reparaciones del sistema eléctrico vehicular', true, 2, 5, NOW(), NOW(), 'sistema'),
('Scanner/Diagnóstico', 'Diagnóstico computarizado de vehículos', true, 3, 5, NOW(), NOW(), 'sistema'),
('Lavado/Detailing', 'Servicios de lavado y detallado de vehículos', true, 4, 5, NOW(), NOW(), 'sistema'),
('Desabolladura y pintura', 'Reparación de abolladuras y pintura automotriz', true, 5, 5, NOW(), NOW(), 'sistema'),
('Revisión pre-técnica/Trámite revisión técnica', 'Servicios relacionados con la revisión técnica', true, 6, 5, NOW(), NOW(), 'sistema');

-- Insertar subcategorías para "Eventos y fotografía"
INSERT INTO subcategorias (nombre, descripcion, activo, orden_visualizacion, categoria_id, fecha_creacion, fecha_modificacion, creado_por) VALUES
('Fotografía/Video (boda, producto)', 'Servicios profesionales de fotografía y video', true, 1, 6, NOW(), NOW(), 'sistema'),
('Banquetería/Coffee break', 'Servicios de catering y alimentación para eventos', true, 2, 6, NOW(), NOW(), 'sistema'),
('DJ/Iluminación', 'Servicios de sonido, música e iluminación', true, 3, 6, NOW(), NOW(), 'sistema'),
('Arriendo de mobiliario', 'Alquiler de muebles y decoración para eventos', true, 4, 6, NOW(), NOW(), 'sistema');

-- Insertar subcategorías para "Educación y clases"
INSERT INTO subcategorias (nombre, descripcion, activo, orden_visualizacion, categoria_id, fecha_creacion, fecha_modificacion, creado_por) VALUES
('Clases escuela/universidad', 'Clases de apoyo escolar y universitario', true, 1, 7, NOW(), NOW(), 'sistema'),
('Idiomas', 'Enseñanza de idiomas extranjeros', true, 2, 7, NOW(), NOW(), 'sistema'),
('Música', 'Clases de instrumentos musicales y canto', true, 3, 7, NOW(), NOW(), 'sistema');

-- Insertar subcategorías para "Profesionales y trámites"
INSERT INTO subcategorias (nombre, descripcion, activo, orden_visualizacion, categoria_id, fecha_creacion, fecha_modificacion, creado_por) VALUES
('Contabilidad/IVA', 'Servicios contables y tributarios', true, 1, 8, NOW(), NOW(), 'sistema'),
('Trámites SII/Patentes', 'Gestión de trámites en SII y patentes', true, 2, 8, NOW(), NOW(), 'sistema'),
('Asesoría legal básica', 'Consultoría legal básica y orientación jurídica', true, 3, 8, NOW(), NOW(), 'sistema');