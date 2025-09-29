# Variables de Entorno - Railway Oriola Denim

**Fecha:** 28 de Septiembre de 2025  
**Proyecto:** Oriola Denim  
**Plataforma:** Railway  
**Estado:** ✅ CONFIGURADO

## 🔧 VARIABLES DE ENTORNO CONFIGURADAS

### **📊 Variables Generadas Automáticamente por Railway:**

| Variable | Valor | Descripción | Fuente |
|----------|-------|-------------|---------|
| `DATABASE_URL` | `jdbc:mysql://viaduct.proxy.rlwy.net:3306/railway` | URL completa de conexión a MySQL | Railway MySQL Service |
| `DB_USERNAME` | `root` | Usuario de la base de datos | Railway MySQL Service |
| `DB_PASSWORD` | `[generada_automáticamente]` | Contraseña de la base de datos | Railway MySQL Service |

### **⚙️ Variables Configuradas Manualmente:**

| Variable | Valor | Descripción | Requerida |
|----------|-------|-------------|-----------|
| `SPRING_PROFILES_ACTIVE` | `railway` | Perfil de Spring Boot activo | ✅ SÍ |
| `ADMIN_USERNAME` | `admin` | Usuario administrador | ✅ SÍ |
| `ADMIN_PASSWORD` | `[personalizada]` | Contraseña del administrador | ✅ SÍ |
| `MAIL_USERNAME` | `luceroprograma@gmail.com` | Email para envío de correos | ⚠️ OPCIONAL |
| `MAIL_PASSWORD` | `kmqh ktkl lhyj gwlf` | Contraseña del email | ⚠️ OPCIONAL |

## 🚀 CONFIGURACIÓN EN RAILWAY

### **Paso 1: Acceder a Variables de Entorno**
1. Ir a Railway Dashboard
2. Seleccionar proyecto "OriolaIndumentaria"
3. Ir a la pestaña "Variables"

### **Paso 2: Agregar Variables Manuales**
```bash
# Variables requeridas
SPRING_PROFILES_ACTIVE=railway
ADMIN_USERNAME=admin
ADMIN_PASSWORD=tu_contraseña_segura_aqui

# Variables opcionales (para emails)
MAIL_USERNAME=luceroprograma@gmail.com
MAIL_PASSWORD=kmqh ktkl lhyj gwlf
```

### **Paso 3: Verificar Configuración**
- ✅ `DATABASE_URL` debe estar presente
- ✅ `DB_USERNAME` debe estar presente  
- ✅ `DB_PASSWORD` debe estar presente
- ✅ `SPRING_PROFILES_ACTIVE=railway` debe estar presente
- ✅ `ADMIN_USERNAME` debe estar presente
- ✅ `ADMIN_PASSWORD` debe estar presente

## 🔒 SEGURIDAD

### **Variables Sensibles:**
- 🔐 `DB_PASSWORD` - Generada automáticamente por Railway
- 🔐 `ADMIN_PASSWORD` - **DEBE SER CAMBIADA** de la por defecto
- 🔐 `MAIL_PASSWORD` - Contraseña de aplicación de Gmail

### **Recomendaciones de Seguridad:**
1. **Cambiar `ADMIN_PASSWORD`** inmediatamente después del primer login
2. **No compartir** las variables sensibles
3. **Usar contraseñas fuertes** para `ADMIN_PASSWORD`
4. **Revisar permisos** de la base de datos periódicamente

## 📋 CHECKLIST DE CONFIGURACIÓN

### **Variables de Base de Datos:**
- [x] `DATABASE_URL` - Configurada por Railway
- [x] `DB_USERNAME` - Configurada por Railway  
- [x] `DB_PASSWORD` - Configurada por Railway

### **Variables de Aplicación:**
- [ ] `SPRING_PROFILES_ACTIVE=railway` - **AGREGAR**
- [ ] `ADMIN_USERNAME=admin` - **AGREGAR**
- [ ] `ADMIN_PASSWORD=[segura]` - **AGREGAR**

### **Variables de Email (Opcionales):**
- [ ] `MAIL_USERNAME=luceroprograma@gmail.com` - **AGREGAR**
- [ ] `MAIL_PASSWORD=kmqh ktkl lhyj gwlf` - **AGREGAR**

## 🌐 ACCESO A LA APLICACIÓN

### **URLs de Acceso:**
- **Aplicación Principal:** `https://[proyecto].railway.app`
- **Panel Admin:** `https://[proyecto].railway.app/admin`
- **Health Check:** `https://[proyecto].railway.app/actuator/health`

### **Credenciales de Acceso:**
- **Usuario:** Valor de `ADMIN_USERNAME`
- **Contraseña:** Valor de `ADMIN_PASSWORD`

## 🔄 DESARROLLO LOCAL

### **Mantener Desarrollo Local:**
- ✅ **Sin cambios** en `application.properties`
- ✅ **Base de datos local** sigue funcionando
- ✅ **Hot reload** mantenido
- ✅ **Archivos locales** en `uploads/`

### **Comandos de Desarrollo:**
```bash
# Desarrollo local (usa application.properties)
mvn spring-boot:run

# Compilar para Railway
mvn clean package -DskipTests
```

## 🚨 TROUBLESHOOTING

### **Error: "Profile 'railway' not found"**
- **Causa:** `SPRING_PROFILES_ACTIVE` no configurada
- **Solución:** Agregar `SPRING_PROFILES_ACTIVE=railway`

### **Error: "Cannot connect to database"**
- **Causa:** Variables de base de datos incorrectas
- **Solución:** Verificar `DATABASE_URL`, `DB_USERNAME`, `DB_PASSWORD`

### **Error: "Access denied for user"**
- **Causa:** Credenciales de base de datos incorrectas
- **Solución:** Verificar que Railway haya generado las variables correctamente

### **Error: "Admin login failed"**
- **Causa:** `ADMIN_USERNAME` o `ADMIN_PASSWORD` incorrectas
- **Solución:** Verificar variables de administrador

## 📊 MONITOREO

### **Logs de Railway:**
- Ver logs en tiempo real en Railway Dashboard
- Buscar errores de conexión a base de datos
- Verificar que el perfil `railway` esté activo

### **Verificación de Variables:**
```bash
# En los logs de Railway, buscar:
"Active profiles: railway"
"Database URL: jdbc:mysql://..."
"Admin user: admin"
```

## ✅ ESTADO ACTUAL

- [x] Proyecto creado en Railway
- [x] Base de datos MySQL configurada
- [x] Variables de base de datos generadas
- [ ] Variables de aplicación configuradas
- [ ] Despliegue exitoso
- [ ] Aplicación accesible

## 🎯 PRÓXIMOS PASOS

1. **Agregar variables faltantes** en Railway
2. **Reiniciar el servicio** para aplicar cambios
3. **Verificar despliegue** exitoso
4. **Probar acceso** a la aplicación
5. **Cambiar contraseña** de administrador

---
*Documentación generada el 28 de Septiembre de 2025*
