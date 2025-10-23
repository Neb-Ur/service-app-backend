#!/bin/bash

# Test script para probar la API de Servicio Ágil

BASE_URL="http://localhost:8081"
echo "🚀 Probando API de Servicio Ágil en $BASE_URL"
echo "================================================"

# Test 1: Página de inicio
echo "✅ Test 1: Página de inicio (GET /)"
curl -s "$BASE_URL" | jq . 2>/dev/null || curl -s "$BASE_URL"
echo -e "\n"

# Test 2: Información de la API
echo "✅ Test 2: Información de la API (GET /api)"
curl -s "$BASE_URL/api" | jq . 2>/dev/null || curl -s "$BASE_URL/api"
echo -e "\n"

# Test 3: Health check
echo "✅ Test 3: Health check (GET /api/health)"
curl -s "$BASE_URL/api/health" | jq . 2>/dev/null || curl -s "$BASE_URL/api/health"
echo -e "\n"

# Test 4: Obtener todos los usuarios
echo "✅ Test 4: Obtener todos los usuarios (GET /api/usuarios)"
curl -s "$BASE_URL/api/usuarios" | jq . 2>/dev/null || curl -s "$BASE_URL/api/usuarios"
echo -e "\n"

# Test 5: Crear un usuario
echo "✅ Test 5: Crear un usuario (POST /api/usuarios)"
curl -s -X POST "$BASE_URL/api/usuarios" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Roberto Silva",
    "email": "roberto.silva@email.com"
  }' | jq . 2>/dev/null || curl -s -X POST "$BASE_URL/api/usuarios" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Roberto Silva",
    "email": "roberto.silva@email.com"
  }'
echo -e "\n"

# Test 6: Obtener usuarios activos
echo "✅ Test 6: Obtener usuarios activos (GET /api/usuarios/activos)"
curl -s "$BASE_URL/api/usuarios/activos" | jq . 2>/dev/null || curl -s "$BASE_URL/api/usuarios/activos"
echo -e "\n"

# Test 7: Estadísticas
echo "✅ Test 7: Estadísticas de usuarios (GET /api/usuarios/estadisticas)"
curl -s "$BASE_URL/api/usuarios/estadisticas" | jq . 2>/dev/null || curl -s "$BASE_URL/api/usuarios/estadisticas"
echo -e "\n"

# Test 8: Swagger UI
echo "✅ Test 8: Documentación Swagger disponible en: $BASE_URL/swagger-ui.html"
echo "✅ Test 9: Consola H2 disponible en: $BASE_URL/h2-console"

echo "================================================"
echo "🎉 Pruebas completadas!"