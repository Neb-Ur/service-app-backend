-- Migración para agregar tipo_usuario a usuarios existentes
-- Este script actualiza los registros existentes antes de aplicar la restricción NOT NULL

-- Primero agregamos la columna sin la restricción NOT NULL
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS tipo_usuario VARCHAR(20);

-- Actualizamos los registros existentes con un valor por defecto
UPDATE usuarios 
SET tipo_usuario = 'CLIENTE' 
WHERE tipo_usuario IS NULL;

-- Ahora agregamos la restricción NOT NULL y el CHECK
ALTER TABLE usuarios 
ALTER COLUMN tipo_usuario SET NOT NULL;

ALTER TABLE usuarios 
ADD CONSTRAINT check_tipo_usuario 
CHECK (tipo_usuario IN ('CLIENTE', 'TECNICO', 'EMPRESA'));