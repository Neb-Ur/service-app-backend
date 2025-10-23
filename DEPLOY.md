# 🚀 Despliegue en Railway - Servicio Ágil Backend

## 📋 Pasos para desplegar en Railway

### 1. **Login en Railway**
```bash
railway login
```

### 2. **Crear nuevo proyecto en Railway**
```bash
cd /Users/rabenavidess/Documents/Personal/appService/Backend/servicio-agil-v1
railway init
```

### 3. **Conectar con GitHub (Opcional)**
Si quieres conectar con un repositorio existente:
```bash
# Agregar remote del repositorio existente
git remote add origin https://github.com/TU_USUARIO/TU_REPOSITORIO.git

# Push forzado para sobrescribir el repositorio anterior
git push -u origin main --force
```

### 4. **Desplegar directamente desde el directorio**
```bash
railway up
```

### 5. **Configurar variables de entorno (si es necesario)**
```bash
# Si quieres usar PostgreSQL en Railway
railway variables set SPRING_PROFILES_ACTIVE=railway
```

### 6. **Ver los logs del despliegue**
```bash
railway logs
```

### 7. **Obtener la URL del servicio desplegado**
```bash
railway status
```

## 🔧 Configuración automática

El proyecto ya está configurado con:

### ✅ **Archivos de Railway:**
- `Procfile` - Comando de inicio para Railway
- `railway.json` - Configuración de build y deploy
- `application-railway.properties` - Configuración para producción

### ✅ **Variables de entorno soportadas:**
- `PORT` - Puerto del servidor (automático en Railway)
- `DATABASE_URL` - URL de base de datos PostgreSQL (si se agrega)
- `SPRING_PROFILES_ACTIVE` - Perfil activo (opcional)

### ✅ **Base de datos:**
- **Desarrollo:** H2 en memoria
- **Railway:** Puede usar H2 o PostgreSQL (si agregas servicio de DB)

## 🌐 Endpoints disponibles después del despliegue

Una vez desplegado, tu API estará disponible en:
```
https://tu-proyecto.railway.app/
```

### 📱 Endpoints principales:
- `GET /` - Página de inicio
- `GET /api` - Información de la API  
- `GET /api/health` - Health check
- `GET /api/usuarios` - Listar usuarios
- `POST /api/usuarios` - Crear usuario
- `GET /swagger-ui.html` - Documentación Swagger

## 🛠️ Comandos útiles de Railway

```bash
# Ver status del proyecto
railway status

# Ver logs en tiempo real
railway logs --follow

# Abrir el proyecto en el navegador
railway open

# Ver variables de entorno
railway variables

# Conectar con GitHub
railway connect

# Redeploy manual
railway up

# Ver información del servicio
railway service
```

## 🔄 Para actualizar el despliegue

```bash
# Hacer cambios en el código
git add .
git commit -m "Descripción de cambios"

# Si está conectado a GitHub
git push origin main

# O desplegar directamente
railway up
```

## 🗄️ Para agregar base de datos PostgreSQL

```bash
# Agregar servicio de PostgreSQL
railway add postgresql

# La variable DATABASE_URL se configurará automáticamente
# El proyecto ya está preparado para usar PostgreSQL
```

---

## 📞 Soporte

Si tienes problemas:
1. Verifica los logs: `railway logs`
2. Revisa el status: `railway status` 
3. Consulta la documentación: https://docs.railway.app

¡Tu API estará lista para usar en pocos minutos! 🎉