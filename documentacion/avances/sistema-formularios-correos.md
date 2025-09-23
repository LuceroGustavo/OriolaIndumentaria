# Sistema de Formularios y Envíos por Correo - ORIOLA Denim

## 📋 Resumen del Sistema

Se implementó un sistema completo de gestión de consultas de clientes con notificaciones por correo electrónico, incluyendo:

- Formulario de contacto público
- Panel de administración para gestionar consultas
- Sistema de notificaciones por email
- Geolocalización automática
- Integración con WhatsApp

## 🏗️ Arquitectura del Sistema

### Entidades
- **Contact**: Entidad principal para almacenar consultas de clientes
- **ContactStats**: DTO para estadísticas de consultas

### Servicios
- **ContactService**: Lógica de negocio para consultas
- **EmailService**: Gestión de envíos de correo
- **GeolocationService**: Obtención de ubicación geográfica

### Controladores
- **ContactController**: Manejo de formularios y administración
- **PublicController**: Páginas públicas

## 📧 Sistema de Correos

### Configuración
```properties
# Configuración de correo electrónico
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=luceroprograma@gmail.com
spring.mail.password=kmqh ktkl lhyj gwlf
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com

# Configuración personalizada de correo
app.email.from=luceroprograma@gmail.com
app.email.to=luceroprograma@gmail.com
```

### Tipos de Correos

#### 1. Confirmación al Cliente
- **Trigger**: Cuando el cliente envía una consulta
- **Asunto**: "Confirmación de consulta - ORIOLA Denim"
- **Contenido**: 
  - Resumen de la consulta
  - Información de contacto
  - Confirmación de recepción

#### 2. Notificación al Administrador
- **Trigger**: Cuando llega una nueva consulta
- **Asunto**: "Nueva consulta de cliente - ORIOLA Denim"
- **Contenido**:
  - Datos del cliente
  - Ubicación geográfica
  - Producto de interés
  - Mensaje completo
  - Enlaces de administración

#### 3. Respuesta al Cliente
- **Trigger**: Cuando el administrador responde una consulta
- **Asunto**: "Re: [Asunto original] - ORIOLA Denim"
- **Contenido**:
  - Consulta original del cliente
  - Respuesta del administrador
  - Fecha de respuesta
  - Información de contacto adicional

## 🌍 Geolocalización

### Servicio
- **API**: ip-api.com (gratuita)
- **Timeout**: 3 segundos
- **Fallback**: "Desarrollo Local" para IPs locales

### Implementación
```java
@Service
public class GeolocationService {
    public String getLocationFromIP(String ipAddress) {
        // Obtiene ciudad, región y país
        // Formato: "Ciudad, Región, País"
    }
}
```

## 📱 Integración WhatsApp

### Funcionalidad
- Botón "Consultar por WhatsApp" en productos
- Detección automática de dispositivo (móvil/desktop)
- Mensaje pre-llenado con datos del producto

### Implementación
```javascript
const WhatsAppIntegration = {
    openWhatsApp: function(productName, productPrice) {
        const phoneNumber = "+5491112345678";
        const message = `Hola, me interesa el producto "${productName}" con un precio de ${productPrice}. ¿Podrías darme más información?`;
        // Lógica de detección de dispositivo
    }
};
```

## 🎨 Formulario de Contacto

### Características
- **Validación**: Campos requeridos y formato de email
- **Pre-llenado**: Producto de interés desde detalle de producto
- **Responsive**: Diseño adaptativo con Bootstrap
- **Estilos**: Tema personalizado ORIOLA

### Campos
- Nombre completo (requerido)
- Email (requerido)
- Teléfono (opcional)
- Asunto (opcional)
- Producto de interés (pre-llenado)
- Mensaje (requerido)

## 🔧 Panel de Administración

### Gestión de Consultas
- **Lista paginada**: Todas las consultas con filtros
- **Estados**: Nueva, Leída, Respondida
- **Acciones**: Ver, Marcar como leída, Responder, Eliminar
- **Estadísticas**: Contadores en tiempo real

### Detalle de Consulta
- **Información completa**: Datos del cliente y consulta
- **Ubicación**: Ciudad, región, país
- **Respuesta**: Formulario para responder
- **Historial**: Fechas de creación y actualización

## 📊 Estadísticas

### Métricas Disponibles
- Total de consultas
- Consultas no leídas
- Consultas no respondidas
- Consultas del día
- Consultas de la semana
- Consultas del mes

### Implementación
```java
public ContactStats obtenerEstadisticas() {
    return new ContactStats(
        contarTotal(),
        contarNoLeidas(),
        contarNoRespondidas(),
        contarHoy(),
        contarEstaSemana(),
        contarEsteMes()
    );
}
```

## 🚀 Funcionalidades Implementadas

### ✅ Completadas
- [x] Formulario de contacto público
- [x] Validación de datos
- [x] Pre-llenado desde productos
- [x] Geolocalización automática
- [x] Notificaciones por email
- [x] Panel de administración
- [x] Gestión de estados
- [x] Sistema de respuestas
- [x] Estadísticas en tiempo real
- [x] Integración WhatsApp
- [x] Diseño responsive
- [x] Estilos personalizados

### 🔄 Flujo de Trabajo
1. **Cliente** envía consulta desde formulario
2. **Sistema** obtiene ubicación geográfica
3. **Sistema** envía confirmación al cliente
4. **Sistema** notifica al administrador
5. **Administrador** gestiona consulta en panel
6. **Administrador** responde consulta
7. **Sistema** envía respuesta al cliente

## 🛠️ Tecnologías Utilizadas

- **Backend**: Spring Boot 3.4.4
- **Frontend**: Thymeleaf + Bootstrap 5
- **Base de datos**: MySQL 8.0
- **Email**: Spring Mail + Gmail SMTP
- **Geolocalización**: WebClient + ip-api.com
- **Seguridad**: Spring Security
- **Build**: Maven

## 📁 Archivos Principales

### Backend
- `Contact.java` - Entidad de consultas
- `ContactService.java` - Lógica de negocio
- `EmailService.java` - Gestión de correos
- `GeolocationService.java` - Geolocalización
- `ContactController.java` - Controlador principal

### Frontend
- `contact.html` - Formulario público
- `admin/contacts.html` - Lista de consultas
- `admin/contact-detail-simple.html` - Detalle de consulta
- `whatsapp.js` - Integración WhatsApp

### Configuración
- `application.properties` - Configuración de correo
- `SecurityConfig.java` - Configuración de seguridad

## 🎯 Beneficios del Sistema

1. **Automatización**: Notificaciones automáticas
2. **Trazabilidad**: Historial completo de consultas
3. **Eficiencia**: Panel de administración centralizado
4. **Experiencia**: Respuesta rápida a clientes
5. **Análisis**: Estadísticas en tiempo real
6. **Integración**: WhatsApp y email integrados

## 📈 Próximas Mejoras Sugeridas

- [ ] Notificaciones push en tiempo real
- [ ] Plantillas de respuesta predefinidas
- [ ] Sistema de tickets con numeración
- [ ] Exportación de consultas a Excel
- [ ] Dashboard con gráficos avanzados
- [ ] Integración con CRM externo

---

**Fecha de implementación**: 18 de Septiembre de 2025  
**Desarrollador**: Asistente AI  
**Cliente**: ORIOLA Denim  
**Estado**: ✅ Completado y funcional
