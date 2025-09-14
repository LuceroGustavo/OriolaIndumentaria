-- Script SQL para cargar 10 productos de prueba
-- Sin acentos y optimizado para WebP

-- Limpiar productos existentes (opcional)
-- DELETE FROM product_image WHERE product_id IN (SELECT p_id FROM product);
-- DELETE FROM product;

-- Insertar 10 productos de prueba
INSERT INTO product (
    name, talle, medidas, color, price, qty, descripcion, categoria, 
    material, cuidados, temporada, genero, edad_recomendada, 
    tallas_disponibles, colores_disponibles, es_destacado, es_nuevo, 
    descuento_porcentaje, precio_original, activo, fecha_creacion, fecha_actualizacion
) VALUES 
-- Producto 1: Remera basica
(
    'Remera Basica Algodon',
    'M',
    'Largo: 70cm, Ancho: 50cm, Manga: 20cm',
    'Blanco',
    25.99,
    50,
    'Remera de algodon 100% comoda y fresca para el verano. Corte clasico y tela suave.',
    'REMERAS',
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

-- Producto 2: Buzo con capucha
(
    'Buzo Capucha Invierno',
    'L',
    'Largo: 75cm, Ancho: 55cm, Manga: 60cm',
    'Gris',
    45.99,
    30,
    'Buzo con capucha ideal para el invierno. Tela gruesa y calida.',
    'BUZOS',
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

-- Producto 3: Camisa formal
(
    'Camisa Formal Hombre',
    'L',
    'Largo: 80cm, Ancho: 52cm, Manga: 65cm',
    'Azul',
    35.99,
    25,
    'Camisa formal de algodon para ocasiones especiales. Corte clasico y elegante.',
    'CAMISAS',
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

-- Producto 4: Pantalon jean
(
    'Jean Clasico Mujer',
    'M',
    'Cintura: 38cm, Largo: 100cm, Ancho: 45cm',
    'Azul',
    55.99,
    40,
    'Jean clasico de mezclilla para mujer. Corte recto y comodo.',
    'PANTALONES',
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

-- Producto 5: Vestido verano
(
    'Vestido Floral Verano',
    'M',
    'Largo: 95cm, Ancho: 48cm, Manga: 15cm',
    'Rosa',
    42.99,
    20,
    'Vestido floral perfecto para el verano. Tela liviana y fresca.',
    'VESTIDOS',
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

-- Producto 6: Short deportivo
(
    'Short Deportivo Unisex',
    'M',
    'Largo: 45cm, Cintura: 40cm, Ancho: 50cm',
    'Negro',
    28.99,
    35,
    'Short deportivo comodo para gimnasio o deportes. Tela elastica.',
    'SHORTS',
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

-- Producto 7: Chomba polo
(
    'Chomba Polo Clasica',
    'L',
    'Largo: 75cm, Ancho: 50cm, Manga: 25cm',
    'Blanco',
    32.99,
    30,
    'Chomba polo clasica de algodon. Perfecta para el verano.',
    'CHOMBAS',
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

-- Producto 8: Buzo nino
(
    'Buzo Nino Invierno',
    'M',
    'Largo: 60cm, Ancho: 40cm, Manga: 45cm',
    'Azul',
    38.99,
    25,
    'Buzo calido para nino. Tela suave y resistente.',
    'BUZOS',
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

-- Producto 9: Vestido nina
(
    'Vestido Nina Primavera',
    'S',
    'Largo: 70cm, Ancho: 35cm, Manga: 20cm',
    'Rosa',
    29.99,
    20,
    'Vestido lindo para nina. Tela suave y comoda.',
    'VESTIDOS',
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

-- Producto 10: Pantalon nino
(
    'Pantalon Nino Deportivo',
    'M',
    'Cintura: 30cm, Largo: 80cm, Ancho: 35cm',
    'Gris',
    24.99,
    30,
    'Pantalon deportivo para nino. Comodo y resistente.',
    'PANTALONES',
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

-- Verificar que se insertaron correctamente
SELECT 
    p_id, 
    name, 
    categoria, 
    genero, 
    talle, 
    price, 
    es_destacado, 
    es_nuevo,
    descuento_porcentaje
FROM product 
ORDER BY p_id;
