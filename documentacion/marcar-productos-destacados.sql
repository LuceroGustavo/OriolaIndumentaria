-- Script para marcar productos como destacados
-- Ejecutar este script para probar el sistema de productos destacados

-- Marcar los primeros 3 productos como destacados
UPDATE product 
SET es_destacado = true 
WHERE p_id IN (1, 2, 3);

-- Verificar que se marcaron correctamente
SELECT p_id, name, es_destacado, activo 
FROM product 
WHERE es_destacado = true;

-- Para desmarcar todos los productos destacados (si es necesario)
-- UPDATE product SET es_destacado = false;
