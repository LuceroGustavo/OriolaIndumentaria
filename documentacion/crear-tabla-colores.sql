-- Script SQL para crear la tabla de colores
-- Ejecutar después de crear la entidad Color

-- Crear tabla de colores
CREATE TABLE IF NOT EXISTS colors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    hex_code VARCHAR(7) UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT DEFAULT 0,
    product_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Agregar columna color_id a la tabla product (si no existe)
ALTER TABLE product ADD COLUMN IF NOT EXISTS color_id BIGINT;

-- Crear índice para la relación
CREATE INDEX IF NOT EXISTS idx_product_color_id ON product(color_id);

-- Agregar foreign key constraint (opcional, puede causar problemas si hay datos existentes)
-- ALTER TABLE product ADD CONSTRAINT fk_product_color FOREIGN KEY (color_id) REFERENCES colors(id);

-- Insertar colores por defecto
INSERT IGNORE INTO colors (name, description, hex_code, display_order, is_active, product_count) VALUES
('Blanco', 'Color blanco clásico', '#FFFFFF', 1, true, 0),
('Negro', 'Color negro elegante', '#000000', 2, true, 0),
('Azul', 'Color azul marino', '#000080', 3, true, 0),
('Rojo', 'Color rojo vibrante', '#FF0000', 4, true, 0),
('Verde', 'Color verde natural', '#008000', 5, true, 0),
('Gris', 'Color gris neutro', '#808080', 6, true, 0),
('Rosa', 'Color rosa suave', '#FFC0CB', 7, true, 0),
('Amarillo', 'Color amarillo brillante', '#FFFF00', 8, true, 0),
('Marrón', 'Color marrón tierra', '#A52A2A', 9, true, 0),
('Violeta', 'Color violeta elegante', '#800080', 10, true, 0);

-- Verificar que se crearon correctamente
SELECT * FROM colors ORDER BY display_order;
