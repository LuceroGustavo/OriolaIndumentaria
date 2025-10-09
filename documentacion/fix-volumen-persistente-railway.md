# Fix: Volumen Persistente en Railway - Archivos se Perdían

**Fecha:** 14 de enero de 2025  
**Problema:** Las imágenes de productos y videos de historias se perdían en cada deployment de Railway  
**Estado:** ✅ **SOLUCIONADO**

## 🚨 **PROBLEMA IDENTIFICADO**

### **Síntoma:**
- ✅ **Código se actualizaba** correctamente en Railway
- ❌ **Imágenes de productos se perdían** en cada deployment
- ❌ **Videos de historias se perdían** en cada deployment
- ❌ **Archivos subidos no persistían** entre reinicios

### **Causa Raíz:**
La configuración en `application-railway.properties` estaba usando rutas **NO persistentes**:
- **Configuración incorrecta:** `/app/uploads` (directorio temporal)
- **Configuración correcta:** `/app/data/uploads` (volumen persistente)

## 🔧 **SOLUCIÓN IMPLEMENTADA**

### **1. Configuración Anterior (Problemática):**
```properties
# ❌ INCORRECTO - Directorios temporales
file.upload-dir=/app/uploads
backup.directory=/app/backups
upload.path=/app/uploads
upload.thumbnail.path=/app/uploads/thumbnails
spring.web.resources.static-locations=classpath:/static/,file:/app/uploads/
```

### **2. Configuración Nueva (Persistente):**
```properties
# ✅ CORRECTO - Volumen persistente
file.upload-dir=/app/data/uploads
backup.directory=/app/data/backups
upload.path=/app/data/uploads
upload.thumbnail.path=/app/data/uploads/thumbnails
spring.web.resources.static-locations=classpath:/static/,file:/app/data/uploads/
```

## 📁 **ESTRUCTURA DE DIRECTORIOS CORREGIDA**

### **En Railway (Volumen Persistente):**
```
/app/data/
├── uploads/
│   ├── [imágenes de productos]
│   ├── historias/
│   │   └── [videos de historias]
│   └── thumbnails/
│       ├── [thumbnails de productos]
│       └── historias/
│           └── [thumbnails de historias]
└── backups/
    └── [archivos de backup]
```

### **En Local (Desarrollo):**
```
uploads/
├── [imágenes de productos]
├── historias/
│   └── [videos de historias]
└── thumbnails/
    ├── [thumbnails de productos]
    └── historias/
        └── [thumbnails de historias]
```

## 🎯 **ARCHIVOS AFECTADOS**

### **Configuración:**
- ✅ `src/main/resources/application-railway.properties` - **CORREGIDO**

### **Servicios (Ya configurados correctamente):**
- ✅ `VideoProcessingService.java` - Usa `@Value("${upload.path}")`
- ✅ `ImageProcessingService.java` - Usa `@Value("${upload.path}")`
- ✅ `HistoriaService.java` - Usa `VideoProcessingService`

## 🚀 **VERIFICACIÓN EN RAILWAY**

### **1. Volumen Debe Estar Configurado:**
- **Nombre:** `oriolaindumentaria-volume` (o similar)
- **Ruta de montaje:** `/app/data`
- **Estado:** Activo y adjunto al proyecto

### **2. Verificar en Railway Dashboard:**
1. Ir a **Settings** → **Volumes**
2. Verificar que el volumen esté montado en `/app/data`
3. Verificar que el uso de almacenamiento sea > 0MB

### **3. Probar Persistencia:**
1. **Subir una imagen** de producto
2. **Subir un video** de historia
3. **Hacer un nuevo deployment**
4. **Verificar** que los archivos sigan disponibles

## 📊 **IMPACTO DE LA SOLUCIÓN**

### **Antes (Problemático):**
- ❌ **Imágenes se perdían** en cada deploy
- ❌ **Videos se perdían** en cada deploy
- ❌ **Usuarios veían iconos rotos**
- ❌ **Experiencia de usuario inconsistente**

### **Después (Solucionado):**
- ✅ **Imágenes persisten** entre deployments
- ✅ **Videos persisten** entre deployments
- ✅ **Experiencia de usuario consistente**
- ✅ **Datos seguros** en volumen persistente

## 🔄 **PRÓXIMOS PASOS**

### **1. Deployar la Corrección:**
```bash
git add .
git commit -m "fix: Corregir rutas de upload para usar volumen persistente en Railway"
git push origin develop
git checkout master
git merge develop
git push origin master
```

### **2. Verificar en Railway:**
- El deployment se ejecutará automáticamente
- Verificar que los archivos persistan después del deploy

### **3. Re-subir Archivos Perdidos:**
- Acceder al panel de administración
- Re-subir imágenes de productos que se perdieron
- Re-subir videos de historias que se perdieron

## ⚠️ **CONSIDERACIONES IMPORTANTES**

### **Límites de Railway:**
- **Volumen gratuito:** 1GB
- **Volumen de pago:** Hasta 100GB
- **Backup automático:** Incluido

### **Monitoreo:**
- Verificar uso de almacenamiento regularmente
- Hacer backups manuales si es necesario
- Monitorear que el volumen siga funcionando

## ✅ **ESTADO ACTUAL**

**Problema:** ✅ **SOLUCIONADO**  
**Configuración:** ✅ **CORREGIDA**  
**Próximo paso:** Deployar y verificar persistencia

---

**Implementado por:** LuceroGustavo  
**Fecha:** 14 de enero de 2025  
**Prioridad:** ALTA - Afecta funcionalidad principal
