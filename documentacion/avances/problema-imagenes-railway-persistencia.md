# Problema de Imágenes en Railway - Persistencia de Archivos

## 📅 **Fecha:** 30 de septiembre de 2025
## 🚨 **Problema:** Las imágenes se pierden en cada deployment de Railway

## 🔍 **PROBLEMA IDENTIFICADO**

### **Síntoma:**
- ✅ **Código se actualiza** correctamente en Railway
- ❌ **Imágenes no cargan** - Se muestran iconos de imagen rota
- ❌ **Archivos subidos se pierden** en cada deployment

### **Causa Raíz:**
Railway **NO persiste archivos** en el directorio `/app/uploads` por defecto. Cada vez que se hace un deployment:

1. **Nuevo contenedor** se crea
2. **Directorio `/app/uploads`** se elimina
3. **Imágenes subidas** se pierden
4. **Base de datos** mantiene referencias a archivos inexistentes

## 🔧 **SOLUCIÓN IMPLEMENTADA**

### **Configuración Anterior (Problemática):**
```properties
# Directorios temporales que se pierden
file.upload-dir=/app/uploads
backup.directory=/app/backups
upload.path=/app/uploads
upload.thumbnail.path=/app/uploads/thumbnails
```

### **Configuración Nueva (Persistente):**
```properties
# Directorios en volumen persistente
file.upload-dir=/app/data/uploads
backup.directory=/app/data/backups
upload.path=/app/data/uploads
upload.thumbnail.path=/app/data/uploads/thumbnails
```

## 🚀 **CONFIGURACIÓN REQUERIDA EN RAILWAY**

### **1. Crear Volumen Persistente:**
- **Nombre:** `data-volume`
- **Ruta de montaje:** `/app/data`
- **Tipo:** Persistent Volume

### **2. Configurar Variables de Entorno:**
```
RAILWAY_VOLUME_MOUNT_PATH=/app/data
```

### **3. Verificar Estructura de Directorios:**
```
/app/data/
├── uploads/
│   ├── thumbnails/
│   └── [imágenes subidas]
└── backups/
    └── [archivos de backup]
```

## 📊 **IMPACTO DE LA SOLUCIÓN**

### **Antes (Problemático):**
- ❌ **Imágenes se pierden** en cada deploy
- ❌ **Usuarios ven iconos rotos**
- ❌ **Backups se pierden**
- ❌ **Experiencia de usuario mala**

### **Después (Solucionado):**
- ✅ **Imágenes persisten** entre deployments
- ✅ **Backups se mantienen**
- ✅ **Experiencia de usuario consistente**
- ✅ **Datos seguros** en volumen persistente

## 🔄 **PROCESO DE MIGRACIÓN**

### **Paso 1: Configurar Volumen en Railway**
1. Ir a **Settings** → **Volumes**
2. Crear volumen `data-volume`
3. Montar en `/app/data`

### **Paso 2: Deployar Nueva Configuración**
1. Commit de la nueva configuración
2. Push a master
3. Railway aplicará los cambios

### **Paso 3: Re-subir Imágenes**
1. Acceder al admin
2. Re-subir imágenes de productos
3. Verificar que persisten

## ⚠️ **CONSIDERACIONES IMPORTANTES**

### **Límites de Railway:**
- **Volumen gratuito:** 1GB
- **Volumen de pago:** Hasta 100GB
- **Backup automático:** Incluido

### **Alternativas si no funciona:**
1. **Cloudinary** - Servicio de imágenes en la nube
2. **AWS S3** - Almacenamiento en la nube
3. **Base64** - Codificar imágenes en base de datos

## 📝 **PRÓXIMOS PASOS**

1. **Configurar volumen** en Railway
2. **Deployar** nueva configuración
3. **Re-subir** imágenes existentes
4. **Verificar** persistencia
5. **Documentar** proceso completo

## ✅ **ESTADO**

**Problema:** Identificado y solución implementada
**Configuración:** Actualizada para usar volumen persistente
**Próximo paso:** Configurar volumen en Railway

---

**Implementado por:** LuceroGustavo
**Fecha:** 30 de septiembre de 2025
**Prioridad:** ALTA - Afecta funcionalidad principal
