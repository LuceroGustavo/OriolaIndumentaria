-- Script para crear colores por defecto
-- Ejecutar después de crear la tabla colors

USE oriola_indumentaria;

-- Insertar colores básicos
INSERT INTO colors (name, hex_code, description, is_active, display_order, created_at, updated_at) VALUES
('Blanco', 'FFFFFF', 'Color blanco puro', true, 1, NOW(), NOW()),
('Negro', '000000', 'Color negro puro', true, 2, NOW(), NOW()),
('Gris', '808080', 'Color gris medio', true, 3, NOW(), NOW()),
('Azul', '0000FF', 'Color azul puro', true, 4, NOW(), NOW()),
('Rojo', 'FF0000', 'Color rojo puro', true, 5, NOW(), NOW()),
('Verde', '008000', 'Color verde puro', true, 6, NOW(), NOW()),
('Amarillo', 'FFFF00', 'Color amarillo puro', true, 7, NOW(), NOW()),
('Rosa', 'FFC0CB', 'Color rosa claro', true, 8, NOW(), NOW()),
('Marrón', 'A52A2A', 'Color marrón', true, 9, NOW(), NOW()),
('Beige', 'F5F5DC', 'Color beige claro', true, 10, NOW(), NOW()),
('Azul Marino', '000080', 'Color azul marino', true, 11, NOW(), NOW()),
('Verde Oliva', '808000', 'Color verde oliva', true, 12, NOW(), NOW()),
('Naranja', 'FFA500', 'Color naranja', true, 13, NOW(), NOW()),
('Púrpura', '800080', 'Color púrpura', true, 14, NOW(), NOW()),
('Turquesa', '40E0D0', 'Color turquesa', true, 15, NOW(), NOW());

-- Verificar que se insertaron correctamente
SELECT id, name, hex_code, description, is_active, display_order 
FROM colors 
ORDER BY display_order;
