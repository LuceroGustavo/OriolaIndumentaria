# Migración a LightNode - COMPLETADA ✅

**Fecha:** 11 de Octubre, 2025  
**Commit:** 60  
**Estado:** DESPLIEGUE EXITOSO

## 🎯 **RESUMEN EJECUTIVO**

Se completó exitosamente la migración del proyecto Oriola Indumentaria de Railway a LightNode, logrando un despliegue estable, persistente y más económico.

## 🚀 **SERVIDOR LIGHTNODE CONFIGURADO**

### **Especificaciones del Servidor:**
- **Proveedor:** LightNode
- **Ubicación:** Buenos Aires, Argentina
- **IP Pública:** 149.104.92.116
- **Sistema Operativo:** Ubuntu 22.04.5 LTS
- **Recursos:** 1 vCPU, 2 GB RAM, 50 GB SSD
- **Costo:** $7.71 USD/mes

### **Credenciales de Acceso:**
- **Usuario SSH:** root
- **Contraseña:** Qbasic.1977.server!
- **Puerto SSH:** 22

## 🗄️ **BASE DE DATOS MYSQL CONFIGURADA**

### **Configuración de Base de Datos:**
- **Base de datos:** orioladenim
- **Usuario:** oriola_user
- **Contraseña:** OriolaDB2025!
- **Host:** localhost:3306

### **Credenciales Root MySQL:**
- **Usuario:** root
- **Contraseña:** OriolaMySQL2025!

## 📁 **ESTRUCTURA DE ARCHIVOS PERSISTENTES**

```
/home/oriola/
├── OriolaIndumentaria/          # Código fuente del proyecto
│   ├── target/
│   │   └── oriola-denim-0.0.1-SNAPSHOT.jar
│   └── src/
├── uploads/                     # Imágenes subidas (PERSISTENTE)
└── backups/                     # Backups del sistema (PERSISTENTE)
```

## ⚙️ **CONFIGURACIÓN DE LA APLICACIÓN**

### **Perfiles Configurados:**
- **`application.properties`** - Configuración base + perfil dev por defecto
- **`application-dev.properties`** - Desarrollo local (create-drop)
- **`application-lightnode.properties`** - Producción LightNode (update)

### **Configuración LightNode:**
```properties
# Base de datos LightNode
spring.datasource.url=jdbc:mysql://localhost:3306/orioladenim
spring.datasource.username=oriola_user
spring.datasource.password=OriolaDB2025!

# Archivos persistentes
file.upload-dir=/home/oriola/uploads
backup.directory=/home/oriola/backups
```

## 🌐 **ACCESO A LA APLICACIÓN**

### **URLs de Acceso:**
- **Aplicación Principal:** http://149.104.92.116:8080
- **Panel de Administración:** http://149.104.92.116:8080/admin

### **Credenciales de Administración:**
- **Usuario:** admin
- **Contraseña:** OriolaAdmin2025!

## 🔧 **FLUJO DE TRABAJO ESTABLECIDO**

### **Para Actualizaciones Futuras:**

#### **Opción A: Actualización Rápida (Recomendada)**
```bash
# 1. Conectar al servidor
ssh root@149.104.92.116

# 2. Entrar al proyecto
cd /home/oriola/OriolaIndumentaria

# 3. Parar aplicación actual
pkill -f "oriola-denim"

# 4. Actualizar desde GitHub
git pull origin master

# 5. Recompilar
mvn clean package -DskipTests

# 6. Ejecutar en segundo plano persistente
nohup java -jar target/oriola-denim-0.0.1-SNAPSHOT.jar --spring.profiles.active=lightnode > /dev/null 2>&1 &

# 7. Verificar que esté ejecutándose
ps aux | grep java

# 8. Salir del servidor
exit
```

#### **Opción B: Clonado Completo (Para cambios grandes)**
```bash
# 1. Conectar al servidor
ssh root@149.104.92.116

# 2. Eliminar proyecto anterior
rm -rf /home/oriola/OriolaIndumentaria

# 3. Clonar desde GitHub
cd /home/oriola
git clone https://github.com/LuceroGustavo/OriolaIndumentaria.git

# 4. Entrar al proyecto
cd OriolaIndumentaria

# 5. Compilar
mvn clean package -DskipTests

# 6. Ejecutar en segundo plano persistente
nohup java -jar target/oriola-denim-0.0.1-SNAPSHOT.jar --spring.profiles.active=lightnode > /dev/null 2>&1 &
```

## 🛠️ **COMANDOS ÚTILES DE ADMINISTRACIÓN**

### **Verificar Estado de la Aplicación:**
```bash
ps aux | grep java
netstat -tlnp | grep 8080
```

### **Parar la Aplicación:**
```bash
pkill -f "oriola-denim"
```

### **Ver Logs del Sistema:**
```bash
journalctl -f
```

### **Reiniciar la Aplicación:**
```bash
pkill -f "oriola-denim"
nohup java -jar target/oriola-denim-0.0.1-SNAPSHOT.jar --spring.profiles.active=lightnode > /dev/null 2>&1 &
```

## ✅ **VENTAJAS OBTENIDAS**

### **Comparación con Railway:**
- ✅ **Costo reducido** (de $20+/mes a $7.71/mes)
- ✅ **Persistencia garantizada** de archivos
- ✅ **Control total** del servidor
- ✅ **Mejor rendimiento** y estabilidad
- ✅ **Acceso SSH completo** para administración
- ✅ **Base de datos MySQL dedicada**

### **Beneficios Técnicos:**
- ✅ **Archivos persistentes** entre reinicios
- ✅ **Base de datos estable** sin pérdida de datos
- ✅ **Despliegue automatizado** con Git
- ✅ **Logs accesibles** para debugging
- ✅ **Escalabilidad** futura disponible

## 🎉 **RESULTADO FINAL**

**El proyecto Oriola Indumentaria está ahora desplegado exitosamente en LightNode, funcionando de manera estable, persistente y económica.**

### **Estado Actual:**
- 🟢 **Aplicación:** FUNCIONANDO
- 🟢 **Base de Datos:** CONECTADA
- 🟢 **Archivos:** PERSISTENTES
- 🟢 **Acceso Web:** DISPONIBLE
- 🟢 **Administración:** OPERATIVA

---

**Migración completada el 11 de Octubre, 2025**  
**Commit 60 - Proyecto desplegado en servidor LightNode**
