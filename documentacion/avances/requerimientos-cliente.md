# Documentación del Proyecto - ORIOLA Indumentaria

## 📋 Datos del Cliente

**Cliente:** Alberto Seres  
**Marca/Empresa:** ORIOLA  
**Logo y colores:** Sí (disponibles)

## 🎯 Objetivos del Sitio Web

- ✅ Mostrar catálogo de productos
- ✅ Recibir consultas de clientes
- ✅ Vender online con pago (MercadoPago, transferencia, etc.)
- ✅ Posicionamiento de marca

## 👥 Público Objetivo

- **Tipo:** Público general
- **Edad:** General (sin restricción específica)
- **Ubicación:** Nacional (Argentina)

## 👕 Productos

- **Tipo:** Indumentaria (remeras, buzos, camisas, etc.)
- **Cantidad estimada:** 20 a 50 productos
- **Características:** Catálogo con fotos y descripciones

## ⚙️ Funcionalidades Requeridas

### Funcionalidades Principales
- ✅ Formulario de contacto
- ✅ Catálogo con fotos y descripciones
- ❌ Carrito de compras (no requerido)

### Administración
- **Administrador:** Una sola persona (Alberto Seres)
- **Rol:** Administrador con control total
- **Panel de gestión:** Sí - autogestionar productos (subir, modificar, eliminar)
- **Cambio de contraseña:** Sí - el admin debe poder cambiar su contraseña

### Funcionalidades Adicionales (Nuevas)
- ✅ **Procesador de imágenes:** Subida automática con conversión a WebP y redimensionado
- ✅ **Analytics básicos:** Vista de visitas y consultas por producto
- ✅ **Multilenguaje:** Soporte para Español e Inglés
- ✅ **Manual de usuario:** Documentación para el administrador

## 🎨 Diseño

- **Páginas de referencia:** No especificadas
- **Estilo preferido:** Colorido y llamativo / Juvenil

## 🔗 Extras

- ✅ Integración con redes sociales (Instagram, WhatsApp, etc.)
- ❌ Blog o sección de novedades
- ✅ Multilenguaje (Español / Inglés) - **ACTUALIZADO**
- ❌ Botón de pago directo (opcional a futuro)

## 🌐 Dominio y Hosting

- **Dominio personalizado**: `orioladenim.com.ar` - **CONFIRMADO POR CLIENTE**
- **Plataforma de hosting**: Railway.app
- **Configuración SSL**: Automática con Railway
- **Base de datos**: MySQL incluida en Railway
- **Costo estimado**: $75-265 USD/año (hosting + dominio)

## 🛠️ Stack Tecnológico

- **Backend:** Java 17, Spring Boot 3.4.4, Spring Security
- **Frontend:** Thymeleaf, Bootstrap 5.3.0, CSS, JavaScript
- **Base de datos:** MySQL
- **Build tool:** Maven
- **Otras:** Lombok, Bean Validation

## 📊 Estructura del Proyecto

### Páginas Públicas
1. **Página Principal** - Landing page con presentación de la marca
2. **Catálogo de Productos** - Galería de productos con filtros
3. **Detalle de Producto** - Vista individual con descripción completa
4. **Contacto** - Formulario de consultas
5. **Sobre ORIOLA** - Información de la marca

### Panel de Administración
1. **Login** - Autenticación del administrador
2. **Dashboard** - Resumen y estadísticas
3. **Gestión de Productos** - CRUD completo
4. **Gestión de Consultas** - Ver y responder consultas
5. **Configuración** - Datos de la empresa y redes sociales

## 🎯 Características Específicas de Productos

### Campos Requeridos
- Nombre del producto
- Descripción detallada
- Precio
- Categoría (remeras, buzos, camisas, etc.)
- Tallas disponibles
- Colores disponibles
- Imágenes (múltiples por producto)
- Estado (activo/inactivo)
- Fecha de creación

### Funcionalidades de Catálogo
- Filtros por categoría
- Filtros por talla
- Filtros por color
- Búsqueda por nombre
- Ordenamiento por precio, fecha, popularidad
- Vista en galería y lista

## 📱 Integración con Redes Sociales

- **Instagram:** Feed de productos y stories
- **WhatsApp:** Botón de contacto directo
- **Facebook:** Compartir productos
- **Enlaces:** Footer con todas las redes sociales

## 💳 Sistema de Pagos (Futuro)

- Integración con MercadoPago
- Opciones de pago: tarjeta, transferencia, efectivo
- Gestión de pedidos
- Notificaciones por email/WhatsApp

## 🖼️ Procesador de Imágenes

### Funcionalidades del Procesador
- **Conversión automática:** JPG/PNG → WebP
- **Redimensionado:** Tamaño predeterminado optimizado para web
- **Compresión:** Reducción de peso manteniendo calidad
- **Múltiples tamaños:** Generación de thumbnails automáticos
- **Validación:** Verificación de formato y tamaño de archivo

### Especificaciones Técnicas
- **Formato de salida:** WebP
- **Tamaño máximo:** 1920x1080px
- **Tamaño thumbnail:** 300x300px
- **Calidad:** 85% para balance peso/calidad
- **Peso máximo:** 2MB por imagen

## 📊 Analytics y Estadísticas

### Métricas por Producto
- **Visitas:** Contador de visualizaciones de cada producto
- **Consultas:** Número de consultas recibidas por producto
- **Popularidad:** Ranking de productos más consultados
- **Tendencias:** Productos con mayor crecimiento en consultas

### Dashboard de Analytics
- **Vista general:** Estadísticas globales del sitio
- **Productos destacados:** Los más visitados y consultados
- **Gráficos:** Visualización de tendencias temporales
- **Exportación:** Posibilidad de exportar reportes

## 🌐 Multilenguaje

### Idiomas Soportados
- **Español:** Idioma principal
- **Inglés:** Idioma secundario

### Implementación
- **Selector de idioma:** En el header del sitio
- **Persistencia:** Guardado en sesión del usuario
- **Contenido dinámico:** Traducción de textos y descripciones
- **URLs amigables:** Soporte para rutas en ambos idiomas

## 👤 Gestión de Usuario Administrador

### Funcionalidades de Usuario
- **Cambio de contraseña:** Formulario seguro para actualizar credenciales
- **Perfil de usuario:** Edición de datos personales
- **Sesiones activas:** Control de sesiones de administración
- **Logs de actividad:** Registro de acciones del administrador

### Seguridad
- **Validación de contraseña actual:** Verificación antes del cambio
- **Contraseña segura:** Requisitos de complejidad
- **Confirmación:** Doble verificación para cambios importantes

## 📖 Manual de Usuario

### Contenido del Manual
- **Instalación y configuración:** Guía de puesta en marcha
- **Gestión de productos:** Cómo agregar, editar y eliminar productos
- **Subida de imágenes:** Proceso de carga y optimización
- **Gestión de consultas:** Cómo responder a clientes
- **Configuración del sitio:** Ajustes generales y personalización
- **Solución de problemas:** FAQ y troubleshooting

### Formato Integrado
- **Manual HTML integrado:** Sección de ayuda dentro del panel de administración
- **Navegación por secciones:** Índice interactivo con enlaces internos
- **Ayuda contextual:** Tooltips y ayuda en línea en cada formulario
- **Búsqueda interna:** Buscador dentro del manual
- **Videos tutoriales:** Guías paso a paso integradas en HTML5
- **Soporte:** Formulario de contacto para soporte técnico

---

**Fecha de creación:** 11 de enero de 2025  
**Fecha de actualización:** 11 de enero de 2025  
**Versión:** 1.1  
**Estado:** En desarrollo
