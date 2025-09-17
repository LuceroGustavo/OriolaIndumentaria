# Configuración Railway y Dominio - ORIOLA Denim

**Dominio confirmado por cliente:** `orioladenim.com.ar`  
**Plataforma de hosting:** Railway.app  
**Fecha de configuración:** 14 de enero de 2025

---

## 🌐 **CONFIGURACIÓN DEL DOMINIO**

### **Información del Dominio:**
- **Dominio**: `orioladenim.com.ar`
- **Tipo**: .com.ar (Argentina)
- **Registrador**: Cualquier registrador argentino
- **Costo estimado**: $15-25 USD/año
- **Propietario**: Alberto Seres (ORIOLA)

### **Registradores Recomendados:**
- **Nic.ar** - Registrador oficial argentino
- **GoDaddy** - Internacional con soporte .com.ar
- **Namecheap** - Económico y confiable
- **Donweb** - Registrador argentino especializado

### **Configuración DNS Requerida:**
```
Tipo: CNAME
Nombre: www
Valor: [URL_PROVIDED_BY_RAILWAY]
TTL: 3600

Tipo: A
Nombre: @
Valor: [IP_PROVIDED_BY_RAILWAY]
TTL: 3600
```

---

## 🚂 **CONFIGURACIÓN DE RAILWAY**

### **Paso 1: Crear Cuenta en Railway**
1. Ir a [railway.app](https://railway.app)
2. Registrarse con GitHub
3. Autorizar permisos de repositorio

### **Paso 2: Crear Proyecto**
1. **New Project** → **Deploy from GitHub repo**
2. Seleccionar repositorio `OriolaIndumentaria`
3. Configurar nombre del proyecto: `oriola-denim`

### **Paso 3: Configurar Base de Datos MySQL**
1. **Add Service** → **Database** → **MySQL**
2. Nombre del servicio: `oriola-mysql`
3. Railway generará automáticamente:
   - `MYSQL_URL`
   - `MYSQL_USERNAME`
   - `MYSQL_PASSWORD`
   - `MYSQL_DATABASE`

### **Paso 4: Configurar Variables de Entorno**
```bash
# Base de datos
SPRING_DATASOURCE_URL=${MYSQL_URL}
SPRING_DATASOURCE_USERNAME=${MYSQL_USERNAME}
SPRING_DATASOURCE_PASSWORD=${MYSQL_PASSWORD}

# Archivos estáticos
UPLOAD_PATH=/app/uploads
UPLOAD_THUMBNAIL_PATH=/app/uploads/thumbnails

# Configuración de producción
SPRING_PROFILES_ACTIVE=prod
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_SHOW_SQL=false

# Puerto (Railway lo maneja automáticamente)
PORT=${PORT:-8080}
```

---

## 📁 **ARCHIVOS DE CONFIGURACIÓN REQUERIDOS**

### **1. railway.json** (Raíz del proyecto)
```json
{
  "build": {
    "builder": "NIXPACKS"
  },
  "deploy": {
    "startCommand": "java -jar target/*.jar",
    "healthcheckPath": "/",
    "healthcheckTimeout": 100,
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```

### **2. Dockerfile** (Raíz del proyecto)
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar archivos de configuración
COPY pom.xml .
COPY src ./src

# Instalar Maven y compilar
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Exponer puerto
EXPOSE 8080

# Comando de inicio
CMD ["java", "-jar", "target/*.jar"]
```

### **3. application-prod.properties** (src/main/resources/)
```properties
# Configuración de producción para Railway
spring.profiles.active=prod

# Base de datos (Railway proporciona estas variables)
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Archivos estáticos
file.upload-dir=${UPLOAD_PATH}
file.upload-thumbnail-dir=${UPLOAD_THUMBNAIL_PATH}

# Logging
logging.level.com.otz=INFO
logging.level.org.springframework.web=WARN
logging.level.org.hibernate=WARN

# Puerto (Railway lo maneja automáticamente)
server.port=${PORT:8080}
```

---

## 🔧 **CONFIGURACIÓN DEL PROYECTO PARA RAILWAY**

### **Modificaciones en application.properties:**
```properties
# Configuración base (desarrollo)
spring.profiles.active=dev

# Configuración de archivos
file.upload-dir=uploads
file.upload-thumbnail-dir=uploads/thumbnails

# Configuración de base de datos (desarrollo)
spring.datasource.url=jdbc:mysql://localhost:3306/oriola_indumentaria
spring.datasource.username=root
spring.datasource.password=password
```

### **Modificaciones en pom.xml:**
```xml
<properties>
    <java.version>17</java.version>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludes>
                    <exclude>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

---

## 🚀 **PROCESO DE DESPLIEGUE**

### **Paso 1: Preparar el Proyecto**
```bash
# 1. Verificar que el proyecto compile
mvn clean compile

# 2. Ejecutar tests
mvn test

# 3. Crear JAR
mvn clean package -DskipTests
```

### **Paso 2: Subir a GitHub**
```bash
# 1. Agregar archivos de configuración
git add railway.json Dockerfile application-prod.properties

# 2. Commit
git commit -m "feat: Configurar proyecto para Railway y dominio orioladenim.com.ar"

# 3. Push
git push origin main
```

### **Paso 3: Desplegar en Railway**
1. **Railway detectará automáticamente** el repositorio
2. **Configurar variables de entorno** en el dashboard
3. **Conectar base de datos MySQL**
4. **Desplegar** - Railway construirá y desplegará automáticamente

### **Paso 4: Configurar Dominio**
1. **Obtener URL de Railway** (ej: `oriola-denim-production.up.railway.app`)
2. **Configurar DNS** en el registrador del dominio
3. **Verificar SSL** (automático con Railway)
4. **Testing** del dominio completo

---

## ✅ **CHECKLIST DE CONFIGURACIÓN**

### **Preparación del Proyecto:**
- [ ] Crear `railway.json`
- [ ] Crear `Dockerfile`
- [ ] Crear `application-prod.properties`
- [ ] Modificar `application.properties`
- [ ] Verificar `pom.xml`
- [ ] Compilar proyecto localmente
- [ ] Ejecutar tests

### **Configuración de Railway:**
- [ ] Crear cuenta en Railway
- [ ] Conectar repositorio GitHub
- [ ] Crear servicio MySQL
- [ ] Configurar variables de entorno
- [ ] Desplegar aplicación
- [ ] Verificar funcionamiento

### **Configuración del Dominio:**
- [ ] Registrar `orioladenim.com.ar`
- [ ] Configurar DNS
- [ ] Conectar dominio con Railway
- [ ] Verificar SSL
- [ ] Testing completo

### **Post-Despliegue:**
- [ ] Verificar carga de imágenes
- [ ] Probar panel de administración
- [ ] Verificar base de datos
- [ ] Testing de funcionalidades
- [ ] Configurar backup
- [ ] Documentar proceso

---

## 💰 **COSTOS ESTIMADOS**

| Servicio | Costo Mensual | Costo Anual | Notas |
|----------|---------------|-------------|-------|
| **Railway (Hobby Plan)** | $5 USD | $60 USD | Incluye base de datos MySQL |
| **Dominio .com.ar** | - | $15-25 USD | Registro anual |
| **SSL Certificate** | - | $0 USD | Incluido con Railway |
| **TOTAL** | **$5 USD** | **$75-85 USD** | **Costo muy accesible** |

---

## 🔍 **TROUBLESHOOTING COMÚN**

### **Problema: Error de conexión a base de datos**
**Solución:** Verificar variables de entorno `MYSQL_*` en Railway

### **Problema: Imágenes no se cargan**
**Solución:** Verificar configuración de `UPLOAD_PATH` y permisos

### **Problema: Dominio no funciona**
**Solución:** Verificar configuración DNS y propagación (puede tardar 24-48 horas)

### **Problema: SSL no funciona**
**Solución:** Railway maneja SSL automáticamente, verificar configuración de dominio

---

## 📞 **SOPORTE Y CONTACTO**

### **Railway Support:**
- **Documentación**: [docs.railway.app](https://docs.railway.app)
- **Discord**: Railway Discord Community
- **Email**: support@railway.app

### **Registrador de Dominio:**
- **Nic.ar**: [nic.ar](https://nic.ar) - Soporte en español
- **GoDaddy**: Soporte 24/7 en español
- **Donweb**: Soporte argentino especializado

---

**Desarrollado por:** Equipo de Desarrollo ORIOLA  
**Fecha:** 14 de enero de 2025  
**Versión:** 1.0  
**Estado:** Listo para implementación
