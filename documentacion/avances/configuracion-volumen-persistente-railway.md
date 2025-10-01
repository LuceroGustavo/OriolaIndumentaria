# Configuración de Volumen Persistente en Railway

**Fecha:** 30 de septiembre de 2025  
**Objetivo:** Configurar volumen persistente para que las imágenes no se pierdan al reiniciar la aplicación.

## 🎯 **Problema**

Sin volumen persistente, las imágenes subidas a Railway se pierden cada vez que:
- Se reinicia la aplicación
- Se hace un nuevo deploy
- Railway reinicia el contenedor

## 🔧 **Solución: Volumen Persistente**

### **1. Configuración en application-railway.properties**

```properties
# File storage configuration (Railway Persistent Volume)
file.upload-dir=/app/data/uploads
backup.directory=/app/data/backups

# Static files configuration
spring.web.resources.static-locations=classpath:/static/,file:/app/data/uploads/
upload.path=/app/data/uploads
upload.thumbnail.path=/app/data/uploads/thumbnails
```

### **2. Configuración en Railway**

#### **Paso 1: Crear Volumen**
1. Ir a tu proyecto en Railway
2. **Settings** → **Volumes**
3. **Create Volume**
4. **Nombre:** `data-volume`
5. **Mount Path:** `/app/data`
6. **Crear el volumen**

#### **Paso 2: Verificar Montaje**
El volumen debe estar montado en `/app/data` para que las rutas funcionen:
- ✅ `/app/data/uploads` → Imágenes de productos
- ✅ `/app/data/backups` → Archivos de backup

### **3. Estructura de Directorios**

```
/app/data/
├── uploads/
│   ├── [imágenes de productos]
│   └── thumbnails/
│       └── [miniaturas]
└── backups/
    └── [archivos de backup]
```

## 🧪 **Verificación**

### **1. Subir una imagen:**
- Ir a admin → Productos → Crear/Editar
- Subir una imagen
- Verificar que se guarde en `/app/data/uploads/`

### **2. Reiniciar aplicación:**
- Hacer un nuevo deploy
- Verificar que la imagen siga disponible

### **3. Verificar en Railway:**
- **Settings** → **Volumes**
- Verificar que `data-volume` esté montado en `/app/data`

## ⚠️ **Importante**

- **Sin volumen persistente:** Las imágenes se pierden al reiniciar
- **Con volumen persistente:** Las imágenes persisten entre reinicios
- **Backup recomendado:** Hacer backups regulares de `/app/data/`

## 🚀 **Estado Actual**

- ✅ **Configuración de rutas:** Completada
- ✅ **Volumen en Railway:** Creado (`oriolaindumentaria-volume` montado en `/app/data`)
- ✅ **Verificación:** Listo para probar

---

**Nota:** Una vez creado el volumen, las imágenes subidas persistirán entre reinicios de la aplicación.
