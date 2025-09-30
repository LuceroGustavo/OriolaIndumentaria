# Configuración de Despliegue en Railway

## 🎯 **RESUMEN**
Esta documentación describe cómo configurar y desplegar la aplicación OriolaIndumentaria en Railway, incluyendo la configuración de la base de datos MySQL y las variables de entorno necesarias.

## 📋 **ARCHIVOS DE CONFIGURACIÓN**

### 1. **application-railway.properties**
Ubicación: `src/main/resources/application-railway.properties`

```properties
# Railway Configuration - Simplified

# Server Configuration
server.port=${PORT:8080}

# Database Configuration (Railway MySQL) - Using MYSQL_URL directly
spring.datasource.url=${MYSQL_URL}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# File Upload Configuration
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB

# File storage configuration
file.upload-dir=/app/uploads
backup.directory=/app/backups

# Static files configuration
spring.web.resources.static-locations=classpath:/static/,file:/app/uploads/
upload.path=/app/uploads
upload.thumbnail.path=/app/uploads/thumbnails

# Security Configuration
spring.security.user.name=${ADMIN_USERNAME:admin}
spring.security.user.password=${ADMIN_PASSWORD:admin123}
spring.security.user.roles=ADMIN

# Email configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME:luceroprograma@gmail.com}
spring.mail.password=${MAIL_PASSWORD:kmqh ktkl lhyj gwlf}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com

# Custom email configuration
app.email.from=${MAIL_USERNAME:luceroprograma@gmail.com}
app.email.to=${MAIL_USERNAME:luceroprograma@gmail.com}
```

### 2. **railway.json**
Ubicación: `railway.json` (raíz del proyecto)

```json
{
  "build": {
    "builder": "NIXPACKS"
  },
  "deploy": {
    "startCommand": "java -jar target/oriola-denim-0.0.1-SNAPSHOT.jar --spring.profiles.active=railway",
    "healthcheckPath": "/admin/dashboard",
    "healthcheckTimeout": 300,
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```

### 3. **nixpacks.toml**
Ubicación: `nixpacks.toml` (raíz del proyecto)

```toml
[phases.setup]
nixPkgs = ["jdk17", "maven"]

[phases.install]
cmds = ["mvn clean package -DskipTests"]

[phases.build]
cmds = ["echo 'Build completed'"]

[start]
cmd = "java -jar target/oriola-denim-0.0.1-SNAPSHOT.jar --spring.profiles.active=railway"
```

## 🔧 **VARIABLES DE ENTORNO EN RAILWAY**

### **Variables para la Aplicación (OriolaIndumentaria)**

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `SPRING_PROFILES_ACTIVE` | `railway` | Activa el perfil de Railway |
| `ADMIN_USERNAME` | `admin` | Usuario administrador |
| `ADMIN_PASSWORD` | `OriolaAdmin2025!` | Contraseña del administrador |
| `MAIL_USERNAME` | `luceroprograma@gmail.com` | Email para notificaciones |
| `MAIL_PASSWORD` | `kmqh ktkl lhyj gwlf` | Contraseña del email |

### **Variables para MySQL (Generadas automáticamente por Railway)**

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `MYSQL_URL` | `jdbc:mysql://root:password@mysql.railway.internal:3306/railway` | URL completa de conexión |
| `MYSQL_DATABASE` | `railway` | Nombre de la base de datos |
| `MYSQL_ROOT_PASSWORD` | `password` | Contraseña del root de MySQL |
| `MYSQLHOST` | `mysql.railway.internal` | Host interno de MySQL |
| `MYSQLPORT` | `3306` | Puerto de MySQL |
| `MYSQLUSER` | `root` | Usuario de MySQL |

## 🚀 **PROCESO DE DESPLIEGUE**

### **1. Preparación del Proyecto**
```bash
# Compilar el proyecto
mvn clean package -DskipTests

# Verificar que se generó el JAR
ls -la target/oriola-denim-0.0.1-SNAPSHOT.jar
```

### **2. Configuración en Railway**

#### **A. Crear Proyecto**
1. Ir a [Railway.app](https://railway.app)
2. Crear nuevo proyecto
3. Conectar con GitHub

#### **B. Agregar Servicios**
1. **MySQL Database:**
   - Agregar servicio MySQL
   - Railway genera automáticamente las variables de conexión

2. **Aplicación Spring Boot:**
   - Conectar repositorio GitHub
   - Configurar variables de entorno

#### **C. Variables de Entorno**
Configurar en Railway las variables para la aplicación:

```bash
SPRING_PROFILES_ACTIVE=railway
ADMIN_USERNAME=admin
ADMIN_PASSWORD=OriolaAdmin2025!
MAIL_USERNAME=luceroprograma@gmail.com
MAIL_PASSWORD=kmqh ktkl lhyj gwlf
```

**NOTA:** `MYSQL_URL` se genera automáticamente por Railway.

### **3. Despliegue**
1. Railway detecta cambios automáticamente
2. Ejecuta el build usando Nixpacks
3. Despliega la aplicación
4. Conecta a la base de datos MySQL

## 🔍 **SOLUCIÓN DE PROBLEMAS**

### **Error: "Driver claims to not accept jdbcUrl"**
**Problema:** La URL de MySQL no tiene el prefijo `jdbc:`
**Solución:** Asegurar que `MYSQL_URL` incluya `jdbc:mysql://`

### **Error: "Access denied for user"**
**Problema:** Variables de entorno no configuradas
**Solución:** Verificar que todas las variables estén configuradas en Railway

### **Error: "Unknown database"**
**Problema:** Base de datos no existe
**Solución:** Railway crea automáticamente la base de datos

## 📊 **MONITOREO**

### **Logs de Railway**
- **Build Logs:** Proceso de compilación
- **Deploy Logs:** Inicio de la aplicación
- **HTTP Logs:** Requests HTTP

### **Health Check**
- **Endpoint:** `/admin/dashboard`
- **Timeout:** 300 segundos
- **Política de reinicio:** ON_FAILURE

## 🎯 **URLS IMPORTANTES**

- **Aplicación:** `https://oriolaindumentaria-production.up.railway.app`
- **Admin Dashboard:** `https://oriolaindumentaria-production.up.railway.app/admin/dashboard`
- **Railway Dashboard:** `https://railway.app/project/[PROJECT_ID]`

## ✅ **VERIFICACIÓN DE DESPLIEGUE EXITOSO**

1. **Build completado** sin errores
2. **Aplicación iniciada** correctamente
3. **Conexión a MySQL** establecida
4. **Dashboard admin** accesible
5. **Health check** pasando

## 📝 **NOTAS IMPORTANTES**

- **Volúmenes persistentes:** Las imágenes se guardan en `/app/uploads`
- **Backups:** Se almacenan en `/app/backups`
- **Base de datos:** Se crea automáticamente con `hibernate.ddl-auto=update`
- **Perfil activo:** `railway` (configurado en `SPRING_PROFILES_ACTIVE`)

---

**Fecha de creación:** 30 de septiembre de 2025
**Última actualización:** 30 de septiembre de 2025
**Estado:** ✅ Funcionando correctamente
**Límites de subida:** 10MB por archivo, 50MB total por request