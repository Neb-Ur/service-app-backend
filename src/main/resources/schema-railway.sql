-- Script de inicialización de la base de datos para Railway
-- Este script se ejecutará automáticamente cuando la aplicación se inicie por primera vez

-- Verificar y crear extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- El resto de las tablas se crearán automáticamente por Hibernate
-- Este archivo existe para asegurar que las extensiones y configuraciones necesarias estén disponibles
