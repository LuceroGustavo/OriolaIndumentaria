-- Script SQL CORREGIDO para cargar 10 productos de prueba
-- Compatible con las entidades Category y Product actuales
-- Sin acentos y optimizado para WebP

-- Limpiar productos existentes (opcional)
-- DELETE FROM product_image WHERE product_id IN (SELECT p_id FROM product);
-- DELETE FROM product;

-- Verificar que las categorías existen con los nombres correctos de columnas
INSERT IGNORE INTO categories (name, description, color_code, icon_name, display_order, is_active, product_count, created_at, updated_at) VALUES
('Remeras', 'Variedad de remeras para todas las temporadas', '#FF6B6B', 'fa-tshirt', 1, true, 0, NOW(), NOW()),
('Buzos', 'Buzos cómodos y con estilo', '#4ECDC4', 'fa-hoodie', 2, true, 0, NOW(), NOW()),
('Camisas', 'Camisas elegantes y casuales', '#45B7D1', 'fa-shirt', 3, true, 0, NOW(), NOW()),
('Pantalones', 'Pantalones de moda y confortables', '#96CEB4', 'fa-pants', 4, true, 0, NOW(), NOW()),
('Vestidos', 'Vestidos elegantes y casuales', '#FFEAA7', 'fa-dress', 5, true, 0, NOW(), NOW()),
('Shorts', 'Shorts cómodos para el verano', '#DDA0DD', 'fa-shorts', 6, true, 0, NOW(), NOW()),
('Chombas', 'Chombas polo clásicas', '#98D8C8', 'fa-polo', 7, true, 0, NOW(), NOW());

-- Insertar 10 productos de prueba con category_id Y categoria (enum)
INSERT INTO product (
    name, talle, medidas, color, price, qty, descripcion, 
    categoria, category_id,  -- Ambos campos necesarios
    material, cuidados, temporada, genero, edad_recomendada, 
    tallas_disponibles, colores_disponibles, es_destacado, es_nuevo, 
    descuento_porcentaje, precio_original, activo, fecha_creacion, fecha_actualizacion
) VALUES 
-- Producto 1: Remera basica (category_id = 1 para Remeras)
(
    'Remera Basica Algodon',
    'M',
    'Largo: 70cm, Ancho: 50cm, Manga: 20cm',
    'Blanco',
    25.99,
    50,
    'Remera de algodon 100% comoda y fresca para el verano. Corte clasico y tela suave.',
    'REMERAS',  -- enum Categoria
    1, -- category_id FK
    'Algodon 100%',
    'Lavar en frio, no usar secadora, planchar a temperatura media',
    'VERANO',
    'UNISEX',
    'Adultos',
    'S,M,L,XL',
    'Blanco,Azul,Negro,Rosa',
    true,
    true,
    0.0,
    null,
    true,
    NOW(),
    NOW()
),

-- Producto 2: Buzo con capucha (category_id = 2 para Buzos)
(
    'Buzo Capucha Invierno',
    'L',
    'Largo: 75cm, Ancho: 55cm, Manga: 60cm',
    'Gris',
    45.99,
    30,
    'Buzo con capucha ideal para el invierno. Tela gruesa y calida.',
    'BUZOS',  -- enum Categoria
    2, -- category_id FK
    'Algodon 80%, Poliester 20%',
    'Lavar en frio, secar al aire, no planchar',
    'INVIERNO',
    'UNISEX',
    'Adultos',
    'S,M,L,XL,XXL',
    'Gris,Negro,Azul Marino',
    true,
    false,
    15.0,
    54.99,
    true,
    NOW(),
    NOW()
),

-- Producto 3: Camisa formal (category_id = 3 para Camisas)
(
    'Camisa Formal Hombre',
    'L',
    'Largo: 80cm, Ancho: 52cm, Manga: 65cm',
    'Azul',
    35.99,
    25,
    'Camisa formal de algodon para ocasiones especiales. Corte clasico y elegante.',
    'CAMISAS',  -- enum Categoria
    3, -- category_id FK
    'Algodon 100%',
    'Lavar en frio, planchar a temperatura alta',
    'TODO_EL_ANO',
    'HOMBRE',
    'Adultos',
    'S,M,L,XL,XXL',
    'Azul,Blanco,Rosa Claro',
    false,
    false,
    0.0,
    null,
    true,
    NOW(),
    NOW()
),

-- Producto 4: Pantalon jean (category_id = 4 para Pantalones)
(
    'Jean Clasico Mujer',
    'M',
    'Cintura: 38cm, Largo: 100cm, Ancho: 45cm',
    'Azul',
    55.99,
    40,
    'Jean clasico de mezclilla para mujer. Corte recto y comodo.',
    'PANTALONES',  -- enum Categoria
    4, -- category_id FK
    'Mezclilla 98%, Elastano 2%',
    'Lavar en frio, no usar secadora, planchar al reves',
    'TODO_EL_ANO',
    'MUJER',
    'Adultos',
    '38,40,42,44,46',
    'Azul,Negro,Gris Claro',
    false,
    true,
    20.0,
    69.99,
    true,
    NOW(),
    NOW()
),

-- Producto 5: Vestido verano (category_id = 5 para Vestidos)
(
    'Vestido Floral Verano',
    'M',
    'Largo: 95cm, Ancho: 48cm, Manga: 15cm',
    'Rosa',
    42.99,
    20,
    'Vestido floral perfecto para el verano. Tela liviana y fresca.',
    'VESTIDOS',  -- enum Categoria
    5, -- category_id FK
    'Viscosa 100%',
    'Lavar a mano, no usar secadora, planchar a temperatura baja',
    'VERANO',
    'MUJER',
    'Adultos',
    'S,M,L,XL',
    'Rosa,Azul,Verde,Blanco',
    true,
    false,
    0.0,
    null,
    true,
    NOW(),
    NOW()
),

-- Producto 6: Short deportivo (category_id = 6 para Shorts)
(
    'Short Deportivo Unisex',
    'M',
    'Largo: 45cm, Cintura: 40cm, Ancho: 50cm',
    'Negro',
    28.99,
    35,
    'Short deportivo comodo para gimnasio o deportes. Tela elastica.',
    'SHORTS',  -- enum Categoria
    6, -- category_id FK
    'Poliester 85%, Elastano 15%',
    'Lavar en frio, secar al aire, no planchar',
    'VERANO',
    'UNISEX',
    'Adultos',
    'S,M,L,XL',
    'Negro,Azul,Gris,Blanco',
    false,
    true,
    0.0,
    null,
    true,
    NOW(),
    NOW()
),

-- Producto 7: Chomba polo (category_id = 7 para Chombas)
(
    'Chomba Polo Clasica',
    'L',
    'Largo: 75cm, Ancho: 50cm, Manga: 25cm',
    'Blanco',
    32.99,
    30,
    'Chomba polo clasica de algodon. Perfecta para el verano.',
    'CHOMBAS',  -- enum Categoria
    7, -- category_id FK
    'Algodon 100%',
    'Lavar en frio, planchar a temperatura media',
    'VERANO',
    'UNISEX',
    'Adultos',
    'S,M,L,XL,XXL',
    'Blanco,Azul,Rosa,Verde',
    false,
    false,
    10.0,
    36.99,
    true,
    NOW(),
    NOW()
),

-- Producto 8: Buzo nino (category_id = 2 para Buzos)
(
    'Buzo Nino Invierno',
    'M',
    'Largo: 60cm, Ancho: 40cm, Manga: 45cm',
    'Azul',
    38.99,
    25,
    'Buzo calido para nino. Tela suave y resistente.',
    'BUZOS',  -- enum Categoria
    2, -- category_id FK
    'Algodon 70%, Poliester 30%',
    'Lavar en frio, secar al aire, no planchar',
    'INVIERNO',
    'NINO',
    '6-12 años',
    'S,M,L',
    'Azul,Rojo,Verde,Negro',
    true,
    false,
    0.0,
    null,
    true,
    NOW(),
    NOW()
),

-- Producto 9: Vestido nina (category_id = 5 para Vestidos)
(
    'Vestido Nina Primavera',
    'S',
    'Largo: 70cm, Ancho: 35cm, Manga: 20cm',
    'Rosa',
    29.99,
    20,
    'Vestido lindo para nina. Tela suave y comoda.',
    'VESTIDOS',  -- enum Categoria
    5, -- category_id FK
    'Algodon 100%',
    'Lavar en frio, secar al aire, planchar a temperatura baja',
    'PRIMAVERA',
    'NINA',
    '4-10 años',
    'XS,S,M',
    'Rosa,Azul,Verde,Amarillo',
    false,
    true,
    0.0,
    null,
    true,
    NOW(),
    NOW()
),

-- Producto 10: Pantalon nino (category_id = 4 para Pantalones)
(
    'Pantalon Nino Deportivo',
    'M',
    'Cintura: 30cm, Largo: 80cm, Ancho: 35cm',
    'Gris',
    24.99,
    30,
    'Pantalon deportivo para nino. Comodo y resistente.',
    'PANTALONES',  -- enum Categoria
    4, -- category_id FK
    'Algodon 60%, Poliester 40%',
    'Lavar en frio, secar al aire, no planchar',
    'TODO_EL_ANO',
    'NINO',
    '6-12 años',
    'S,M,L',
    'Gris,Azul,Negro,Verde',
    false,
    false,
    0.0,
    null,
    true,
    NOW(),
    NOW()
);

-- Actualizar contadores de productos en categorías
UPDATE categories SET product_count = (
    SELECT COUNT(*) FROM product WHERE category_id = categories.id
);

-- Verificar que se insertaron correctamente
SELECT 
    p.p_id, 
    p.name, 
    p.categoria as categoria_enum,
    c.name as categoria_nombre,
    p.genero, 
    p.talle, 
    p.price, 
    p.es_destacado, 
    p.es_nuevo,
    p.descuento_porcentaje
FROM product p
LEFT JOIN categories c ON p.category_id = c.id
ORDER BY p.p_id;

-- Verificar contadores de categorías
SELECT 
    c.name as categoria,
    c.product_count,
    c.color_code,
    c.icon_name
FROM categories c
ORDER BY c.display_order;
