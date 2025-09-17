# Plan de Acción - Proyecto ORIOLA Indumentaria

## 🚀 Fase 1: Configuración Base del Proyecto

### 1.1 Configuración de Base de Datos
- [x] Cambiar de H2 a MySQL
- [x] Configurar conexión en `application.properties`
- [ ] Crear script de inicialización de base de datos
- [ ] Configurar perfiles de desarrollo y producción

### 1.2 Seguridad y Autenticación
- [x] Agregar Spring Security al proyecto
- [x] Configurar autenticación básica
- [x] Crear entidad User para administradores
- [x] Implementar login/logout
- [x] Configurar rutas protegidas
- [ ] Crear usuario admin por defecto (admin/admin)

### 1.3 Configuración de Archivos Estáticos
- [ ] Configurar carpeta para imágenes de productos
- [ ] Configurar subida de archivos
- [ ] Implementar validación de tipos de archivo
- [ ] Configurar tamaño máximo de archivos

## 🏗️ Fase 2: Modelado de Datos

### 2.1 Entidades Principales
- [ ] Modificar entidad `Product` para indumentaria
- [ ] Crear entidad `Category` para categorías de productos
- [ ] Crear entidad `Contact` para consultas de clientes
- [ ] Crear entidad `User` para administradores
- [ ] Crear entidad `ProductImage` para múltiples imágenes

### 2.2 Relaciones entre Entidades
- [ ] Product → Category (Many-to-One)
- [ ] Product → ProductImage (One-to-Many)
- [ ] Configurar cascadas y restricciones
- [ ] Crear índices para optimización

### 2.3 Repositorios
- [ ] Extender `ProductRepository` con consultas personalizadas
- [ ] Crear `CategoryRepository`
- [ ] Crear `ContactRepository`
- [ ] Crear `UserRepository`

## 🎨 Fase 3: Diseño y Frontend

### 3.1 Estructura de Templates
- [ ] Crear layout base con Bootstrap
- [ ] Implementar diseño colorido y juvenil
- [ ] Crear header con navegación
- [ ] Crear footer con redes sociales
- [ ] Implementar responsive design

### 3.2 Páginas Públicas
- [ ] **Página Principal (index.html)**
  - Hero section con presentación de ORIOLA
  - Productos destacados
  - Enlaces a redes sociales
  - Call-to-action para contacto

- [ ] **Catálogo de Productos (catalog.html)**
  - Grid de productos con filtros
  - Paginación
  - Búsqueda por nombre
  - Filtros por categoría, talla, color

- [ ] **Detalle de Producto (product-detail.html)**
  - Galería de imágenes
  - Información completa
  - Botón de contacto/consulta
  - Productos relacionados

- [ ] **Contacto (contact.html)**
  - Formulario de consultas
  - Información de contacto
  - Mapa de ubicación (opcional)
  - Enlaces a WhatsApp

- [ ] **Sobre ORIOLA (about.html)**
  - Historia de la marca
  - Misión y visión
  - Información del emprendedor

### 3.3 Panel de Administración
- [ ] **Login (admin/login.html)**
  - Formulario de autenticación
  - Manejo de errores
  - Recordar usuario

- [ ] **Dashboard (admin/dashboard.html)**
  - Estadísticas generales
  - Productos recientes
  - Consultas pendientes
  - Accesos rápidos

- [ ] **Gestión de Productos (admin/products.html)**
  - Lista de productos con acciones
  - Formulario de creación/edición
  - Subida de múltiples imágenes
  - Gestión de categorías

- [ ] **Gestión de Consultas (admin/contacts.html)**
  - Lista de consultas recibidas
  - Marcar como leída/respondida
  - Responder por email/WhatsApp

## ⚙️ Fase 4: Funcionalidades Backend

### 4.1 Controladores
- [ ] **PublicController** - Páginas públicas
- [ ] **ProductController** - Gestión de productos
- [ ] **ContactController** - Manejo de consultas
- [ ] **AdminController** - Panel de administración
- [ ] **FileController** - Manejo de archivos

### 4.2 Servicios
- [ ] **ProductService** - Lógica de negocio de productos
- [ ] **ContactService** - Manejo de consultas
- [ ] **FileService** - Gestión de archivos
- [ ] **EmailService** - Envío de notificaciones

### 4.3 Validaciones
- [ ] Validaciones de formularios
- [ ] Validaciones de archivos
- [ ] Sanitización de datos
- [ ] Manejo de errores personalizados

## 🔗 Fase 5: Integraciones

### 5.1 Redes Sociales
- [ ] Integración con Instagram API
- [ ] Botón de WhatsApp con mensaje predefinido
- [ ] Enlaces a Facebook
- [ ] Compartir productos en redes sociales

### 5.2 Sistema de Notificaciones
- [ ] Email de confirmación de consultas
- [ ] Notificaciones al administrador
- [ ] Integración con WhatsApp Business API (opcional)

## 🖼️ Fase 6: Procesador de Imágenes

### 6.1 Configuración de Procesamiento
- [ ] Agregar dependencias para procesamiento de imágenes
- [ ] Configurar conversión JPG/PNG a WebP
- [ ] Implementar redimensionado automático
- [ ] Configurar compresión de imágenes

### 6.2 Servicio de Imágenes
- [ ] Crear ImageProcessingService
- [ ] Implementar validación de archivos
- [ ] Generar thumbnails automáticos
- [ ] Configurar almacenamiento de imágenes

## 📊 Fase 7: Analytics y Estadísticas

### 7.1 Entidades de Analytics
- [ ] Crear entidad ProductView para visitas
- [ ] Crear entidad Contact para consultas
- [ ] Implementar contadores de visitas
- [ ] Crear repositorios para estadísticas

### 7.2 Dashboard de Analytics
- [ ] Crear vista de estadísticas generales
- [ ] Implementar métricas por producto
- [ ] Crear gráficos de tendencias
- [ ] Agregar exportación de reportes

## 🌐 Fase 8: Multilenguaje

### 8.1 Configuración de i18n
- [ ] Configurar Spring Boot i18n
- [ ] Crear archivos de propiedades para idiomas
- [ ] Implementar selector de idioma
- [ ] Configurar persistencia de idioma

### 8.2 Traducción de Contenido
- [ ] Traducir todas las páginas públicas
- [ ] Traducir panel de administración
- [ ] Implementar traducción dinámica
- [ ] Configurar URLs amigables

## 👤 Fase 9: Gestión de Usuario Admin

### 9.1 Funcionalidades de Usuario
- [ ] Crear formulario de cambio de contraseña
- [ ] Implementar validación de contraseña actual
- [ ] Crear perfil de usuario editable
- [ ] Implementar logs de actividad

### 9.2 Seguridad Avanzada
- [ ] Configurar requisitos de contraseña
- [ ] Implementar confirmación de cambios
- [ ] Crear sistema de logs de seguridad
- [ ] Configurar expiración de sesiones

## 📖 Fase 10: Manual de Usuario Integrado

### 10.1 Creación del Manual HTML
- [ ] Crear estructura HTML del manual integrado
- [ ] Diseñar navegación por secciones
- [ ] Escribir contenido de cada sección
- [ ] Crear índice interactivo con enlaces internos

### 10.2 Implementación en la App
- [ ] Crear página de ayuda en el panel admin (/admin/help)
- [ ] Implementar buscador interno del manual
- [ ] Agregar tooltips contextuales en formularios
- [ ] Integrar videos tutoriales en HTML5
- [ ] Crear formulario de soporte técnico

## 🧪 Fase 11: Testing y Optimización

### 11.1 Testing
- [ ] Tests unitarios para servicios
- [ ] Tests de integración
- [ ] Tests de controladores
- [ ] Tests de validaciones

### 11.2 Optimización
- [ ] Optimización de consultas a base de datos
- [ ] Compresión de imágenes
- [ ] Cache de consultas frecuentes
- [ ] Optimización de carga de páginas

## 🚀 Fase 12: Despliegue y Producción

### 12.1 Configuración de Railway
- [ ] Crear cuenta en Railway.app
- [ ] Conectar repositorio GitHub
- [ ] Configurar variables de entorno para producción
- [ ] Configurar base de datos MySQL en Railway
- [ ] Configurar archivos estáticos (uploads)
- [ ] Configurar perfil de producción (application-prod.properties)

### 12.2 Configuración de Dominio Personalizado
- [ ] Registrar dominio `orioladenim.com.ar`
- [ ] Configurar DNS para apuntar a Railway
- [ ] Configurar subdominios si es necesario
- [ ] Verificar configuración SSL automática
- [ ] Testing del dominio en producción

### 12.3 Configuración de Producción
- [ ] Variables de entorno específicas para Railway
- [ ] Configuración de base de datos de producción
- [ ] Configuración de archivos estáticos
- [ ] SSL/HTTPS automático con Railway

### 12.4 Monitoreo
- [ ] Logs de aplicación
- [ ] Monitoreo de errores
- [ ] Métricas de rendimiento
- [ ] Backup de base de datos

## 🌐 Configuración Específica para Railway y Dominio

### **Dominio Confirmado por Cliente:**
- **Dominio**: `orioladenim.com.ar`
- **Registrador**: Cualquier registrador argentino (.com.ar)
- **Costo estimado**: $15-25 USD/año

### **Configuración Railway:**
- **Plataforma**: Railway.app
- **Ventajas**: 
  - Despliegue automático desde GitHub
  - Base de datos MySQL incluida
  - SSL automático
  - Escalabilidad fácil
- **Costo estimado**: $5-20 USD/mes según uso

### **Variables de Entorno para Railway:**
```properties
# Base de datos
SPRING_DATASOURCE_URL=jdbc:mysql://railway-prod:3306/oriola_denim
SPRING_DATASOURCE_USERNAME=railway
SPRING_DATASOURCE_PASSWORD=[generated]

# Archivos estáticos
UPLOAD_PATH=/app/uploads
UPLOAD_THUMBNAIL_PATH=/app/uploads/thumbnails

# Configuración de producción
SPRING_PROFILES_ACTIVE=prod
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

### **Archivos de Configuración Requeridos:**
- `railway.json` - Configuración de Railway
- `Dockerfile` - Contenedor de la aplicación
- `application-prod.properties` - Configuración de producción

## 📋 Cronograma Estimado

| Fase | Duración Estimada | Prioridad |
|------|------------------|-----------|
| Fase 1: Configuración Base | 2-3 días | Alta |
| Fase 2: Modelado de Datos | 2-3 días | Alta |
| Fase 3: Diseño y Frontend | 5-7 días | Alta |
| Fase 4: Funcionalidades Backend | 4-5 días | Alta |
| Fase 5: Integraciones | 2-3 días | Media |
| Fase 6: Procesador de Imágenes | 3-4 días | Alta |
| Fase 7: Analytics y Estadísticas | 3-4 días | Media |
| Fase 8: Multilenguaje | 2-3 días | Media |
| Fase 9: Gestión de Usuario Admin | 2-3 días | Alta |
| Fase 10: Manual de Usuario | 2-3 días | Baja |
| Fase 11: Testing y Optimización | 2-3 días | Media |
| Fase 12: Despliegue Railway + Dominio | 2-3 días | Alta |

**Total estimado:** 32-44 días de desarrollo

## 🎯 Entregables por Fase

### Fase 1
- Proyecto configurado con MySQL y Spring Security
- Autenticación básica funcionando

### Fase 2
- Base de datos diseñada y creada
- Entidades y repositorios implementados

### Fase 3
- Diseño visual implementado
- Todas las páginas públicas funcionando

### Fase 4
- Panel de administración completo
- CRUD de productos funcionando

### Fase 5
- Integraciones con redes sociales
- Sistema de notificaciones

### Fase 6
- Aplicación testeada y optimizada
- Documentación técnica

### Fase 7
- Aplicación desplegada en producción
- Monitoreo configurado

---

**Fecha de creación:** 11 de enero de 2025  
**Versión:** 1.0  
**Estado:** Planificación
