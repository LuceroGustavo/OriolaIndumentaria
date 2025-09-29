# Configuración Railway - Oriola Denim

**Fecha:** 28 de Septiembre de 2025  
**Desarrollador:** Asistente AI  
**Estado:** ✅ LISTO PARA DESPLIEGUE

## 🚀 RESUMEN

Configuración completa del proyecto Oriola Denim para despliegue en Railway, manteniendo capacidad de desarrollo local.

## 📋 PREPARACIÓN COMPLETADA

### ✅ **Archivos Limpiados:**
- ❌ `Dockerfile` (eliminado - no necesario para Railway)
- ❌ `render.yaml` (eliminado - no necesario para Railway)
- ❌ `.dockerignore` (eliminado - no necesario para Railway)

### ✅ **Configuración Railway:**
- ✅ `application-railway.properties` configurado
- ✅ Variables de entorno preparadas
- ✅ Base de datos MySQL configurada
- ✅ Archivos estáticos configurados
- ✅ Seguridad configurada

## 🔧 CONFIGURACIÓN TÉCNICA

### **Perfil Activo:**
```properties
spring.profiles.active=railway
```

### **Variables de Entorno Requeridas:**
```bash
# Base de datos (Railway MySQL)
DATABASE_URL=jdbc:mysql://[host]:[port]/[database]
DB_USERNAME=[username]
DB_PASSWORD=[password]

# Administrador (opcional - tiene valores por defecto)
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123

# Email (opcional - usa configuración por defecto)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=luceroprograma@gmail.com
MAIL_PASSWORD=kmqh ktkl lhyj gwlf
```

### **Rutas de Archivos:**
- **Uploads:** `/app/uploads/`
- **Backups:** `/app/backups/`
- **Thumbnails:** `/app/uploads/thumbnails/`

## 📦 PROCESO DE DESPLIEGUE

### **1. Crear Proyecto en Railway:**
1. Ir a [railway.app](https://railway.app)
2. Iniciar sesión con GitHub
3. Crear nuevo proyecto
4. Conectar repositorio: `LuceroGustavo/OriolaIndumentaria`

### **2. Configurar Base de Datos:**
1. Agregar servicio MySQL
2. Railway generará automáticamente:
   - `DATABASE_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`

### **3. Configurar Variables de Entorno:**
```bash
# En Railway Dashboard > Variables
SPRING_PROFILES_ACTIVE=railway
ADMIN_USERNAME=admin
ADMIN_PASSWORD=[password_segura]
```

### **4. Desplegar:**
1. Railway detectará automáticamente que es un proyecto Java/Maven
2. Usará el comando: `mvn clean package -DskipTests`
3. Ejecutará: `java -jar target/oriola-denim-0.0.1-SNAPSHOT.jar`

## 🔄 DESARROLLO LOCAL

### **Mantener Desarrollo Local:**
- ✅ **Perfil local:** `application.properties` (sin cambios)
- ✅ **Base de datos local:** MySQL en puerto 3306
- ✅ **Archivos locales:** Carpeta `uploads/` local
- ✅ **Hot reload:** Spring Boot DevTools activo

### **Comandos de Desarrollo:**
```bash
# Desarrollo local (perfil por defecto)
mvn spring-boot:run

# Desarrollo con perfil específico
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Compilar para Railway
mvn clean package -DskipTests
```

## 🌐 ACCESO A LA APLICACIÓN

### **URLs de Acceso:**
- **Aplicación:** `https://[proyecto].railway.app`
- **Admin:** `https://[proyecto].railway.app/admin`
- **Health Check:** `https://[proyecto].railway.app/actuator/health`

### **Credenciales por Defecto:**
- **Usuario:** `admin`
- **Contraseña:** `admin123` (cambiar en producción)

## 📊 MONITOREO

### **Logs:**
- Railway proporciona logs en tiempo real
- Nivel de logging: INFO para la aplicación
- WARN para Spring Security y Hibernate

### **Métricas:**
- Health check endpoint disponible
- Información del sistema en `/actuator/info`

## 🔒 SEGURIDAD

### **Configuración Actual:**
- ✅ Usuario admin configurado
- ✅ Contraseña por defecto (cambiar en producción)
- ✅ Roles de seguridad implementados
- ✅ Endpoints protegidos

### **Recomendaciones para Producción:**
1. Cambiar contraseña de admin
2. Configurar HTTPS (Railway lo maneja automáticamente)
3. Revisar permisos de base de datos
4. Configurar backup automático

## 🚨 TROUBLESHOOTING

### **Problemas Comunes:**

#### **Error de Base de Datos:**
```bash
# Verificar variables de entorno
echo $DATABASE_URL
echo $DB_USERNAME
echo $DB_PASSWORD
```

#### **Error de Archivos:**
```bash
# Verificar permisos de directorio
ls -la /app/uploads/
ls -la /app/backups/
```

#### **Error de Puerto:**
```bash
# Railway usa variable PORT automáticamente
echo $PORT
```

## 📝 PRÓXIMOS PASOS

1. **Subir a GitHub** (ya completado)
2. **Crear proyecto en Railway**
3. **Configurar base de datos MySQL**
4. **Configurar variables de entorno**
5. **Desplegar y probar**

## ✅ CHECKLIST DE DESPLIEGUE

- [ ] Proyecto creado en Railway
- [ ] Repositorio conectado
- [ ] Base de datos MySQL agregada
- [ ] Variables de entorno configuradas
- [ ] Despliegue exitoso
- [ ] Aplicación accesible
- [ ] Admin login funcionando
- [ ] Subida de imágenes funcionando
- [ ] Backup/restore funcionando

---
*Documentación generada el 28 de Septiembre de 2025*
