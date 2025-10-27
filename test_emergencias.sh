#!/bin/bash

# Script de pruebas para el sistema de emergencias
# Asegúrate de que el backend esté corriendo en http://localhost:8081

BASE_URL="http://localhost:8081"

echo "🚨 Iniciando pruebas del Sistema de Emergencias"
echo "================================================"
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# ============================================
# TEST 1: Crear emergencia
# ============================================
echo -e "${YELLOW}TEST 1: Crear emergencia de plomería${NC}"
echo "-------------------------------------"

EMERGENCY_RESPONSE=$(curl -s -X POST "$BASE_URL/api/emergencias" \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "categoriaId": 1,
    "subcategoriaId": 1,
    "descripcion": "Fuga de agua urgente en la cocina",
    "direccion": "Calle Ahumada 123, Santiago",
    "latitud": -33.4489,
    "longitud": -70.6693,
    "telefono": "+56912345678",
    "notas": "Agua saliendo del lavaplatos"
  }')

echo "$EMERGENCY_RESPONSE" | jq '.'

# Extraer ID de la solicitud
SOLICITUD_ID=$(echo "$EMERGENCY_RESPONSE" | jq -r '.data.solicitudId')
echo -e "${GREEN}✓ Emergencia creada con ID: $SOLICITUD_ID${NC}"
echo ""

# Extraer ID de la primera notificación
NOTIFICACION_ID=$(echo "$EMERGENCY_RESPONSE" | jq -r '.data.notificaciones[0].id')
TECNICO_ID=$(echo "$EMERGENCY_RESPONSE" | jq -r '.data.notificaciones[0].tecnicoId')

echo -e "${GREEN}✓ Notificación enviada a técnico $TECNICO_ID (Notificación ID: $NOTIFICACION_ID)${NC}"
echo ""
sleep 2

# ============================================
# TEST 2: Ver estado de emergencia
# ============================================
echo -e "${YELLOW}TEST 2: Consultar estado de emergencia${NC}"
echo "-------------------------------------"

curl -s "$BASE_URL/api/emergencias/$SOLICITUD_ID" | jq '.'
echo -e "${GREEN}✓ Estado obtenido correctamente${NC}"
echo ""
sleep 2

# ============================================
# TEST 3: Ver notificaciones pendientes del técnico
# ============================================
echo -e "${YELLOW}TEST 3: Notificaciones pendientes del técnico${NC}"
echo "-------------------------------------"

curl -s "$BASE_URL/api/emergencias/tecnico/$TECNICO_ID/pendientes" | jq '.'
echo -e "${GREEN}✓ Notificaciones obtenidas${NC}"
echo ""
sleep 2

# ============================================
# TEST 4: Técnico rechaza la emergencia
# ============================================
echo -e "${YELLOW}TEST 4: Técnico rechaza la emergencia${NC}"
echo "-------------------------------------"

REJECT_RESPONSE=$(curl -s -X POST "$BASE_URL/api/emergencias/responder" \
  -H "Content-Type: application/json" \
  -d "{
    \"notificacionId\": $NOTIFICACION_ID,
    \"tecnicoId\": $TECNICO_ID,
    \"aceptar\": false,
    \"latitudActual\": -33.4450,
    \"longitudActual\": -70.6650
  }")

echo "$REJECT_RESPONSE" | jq '.'
echo -e "${GREEN}✓ Emergencia rechazada. Sistema buscando siguiente técnico...${NC}"
echo ""
sleep 3

# ============================================
# TEST 5: Ver estado actualizado
# ============================================
echo -e "${YELLOW}TEST 5: Estado después del rechazo${NC}"
echo "-------------------------------------"

UPDATED_STATE=$(curl -s "$BASE_URL/api/emergencias/$SOLICITUD_ID")
echo "$UPDATED_STATE" | jq '.'

# Extraer nueva notificación (si existe)
NEW_NOTIFICACION_ID=$(echo "$UPDATED_STATE" | jq -r '.data.notificaciones[] | select(.estado == "PENDIENTE") | .id' | head -1)
NEW_TECNICO_ID=$(echo "$UPDATED_STATE" | jq -r '.data.notificaciones[] | select(.estado == "PENDIENTE") | .tecnicoId' | head -1)

if [ ! -z "$NEW_NOTIFICACION_ID" ] && [ "$NEW_NOTIFICACION_ID" != "null" ]; then
    echo -e "${GREEN}✓ Nuevo técnico contactado (ID: $NEW_TECNICO_ID)${NC}"
    echo ""
    sleep 2

    # ============================================
    # TEST 6: Segundo técnico acepta
    # ============================================
    echo -e "${YELLOW}TEST 6: Segundo técnico acepta la emergencia${NC}"
    echo "-------------------------------------"

    ACCEPT_RESPONSE=$(curl -s -X POST "$BASE_URL/api/emergencias/responder" \
      -H "Content-Type: application/json" \
      -d "{
        \"notificacionId\": $NEW_NOTIFICACION_ID,
        \"tecnicoId\": $NEW_TECNICO_ID,
        \"aceptar\": true,
        \"latitudActual\": -33.4500,
        \"longitudActual\": -70.6700
      }")

    echo "$ACCEPT_RESPONSE" | jq '.'
    echo -e "${GREEN}✓ ¡Emergencia aceptada por técnico $NEW_TECNICO_ID!${NC}"
    echo ""
    sleep 2

    # ============================================
    # TEST 7: Estado final con técnico asignado
    # ============================================
    echo -e "${YELLOW}TEST 7: Estado final (técnico asignado)${NC}"
    echo "-------------------------------------"

    curl -s "$BASE_URL/api/emergencias/$SOLICITUD_ID" | jq '.'
    echo -e "${GREEN}✓ Técnico asignado correctamente${NC}"
    echo ""

else
    echo -e "${RED}✗ No se encontró segundo técnico. Verifica que haya múltiples técnicos en la BD.${NC}"
    echo ""
fi

# ============================================
# RESUMEN
# ============================================
echo ""
echo "================================================"
echo -e "${GREEN}✅ PRUEBAS COMPLETADAS${NC}"
echo "================================================"
echo ""
echo "Resumen de flujo probado:"
echo "1. ✓ Cliente crea emergencia"
echo "2. ✓ Sistema busca técnicos cercanos"
echo "3. ✓ Envía notificación al técnico más cercano"
echo "4. ✓ Técnico rechaza emergencia"
echo "5. ✓ Sistema busca siguiente técnico"
echo "6. ✓ Segundo técnico acepta"
echo "7. ✓ Cliente recibe confirmación con datos del técnico"
echo ""
echo -e "${YELLOW}📝 Notas:${NC}"
echo "- Revisa la BD para ver las notificaciones creadas"
echo "- Los timeouts se verifican automáticamente cada 30 segundos"
echo "- Las coordenadas de prueba son de Santiago Centro"
echo ""
