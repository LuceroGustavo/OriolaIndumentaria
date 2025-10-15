# Configuración de Perfiles - ORIOLA Indumentaria

## 📋 Perfiles Disponibles

### 1. **Local (Desarrollo)**
- **Archivo:** `application-local.properties`
- **Uso:** Desarrollo en tu computadora local
- **Base de datos:** MySQL local (localhost:3306)
- **Usuario:** root / root
- **Base de datos:** oriola_indumentaria

### 2. **LightNode (Servidor)**
- **Archivo:** `application-lightnode.properties`
- **Uso:** Servidor de producción en LightNode
- **Servidor:** 149.104.92.116
- **Base de datos:** MySQL en el servidor
- **Usuario:** oriola_user / OriolaDB2025!

## 🚀 Cómo Ejecutar

### Para Desarrollo Local:
```bash
mvn spring-boot:run
# O explícitamente:
mvn spring-boot:run -Dspring.profiles.active=local
```

### Para Servidor LightNode:
```bash
mvn spring-boot:run -Dspring.profiles.active=lightnode
```

## ⚙️ Configuración Actual

- **Perfil por defecto:** `local` (configurado en application.properties)
- **Puerto:** 8080 (en ambos perfiles)
- **Base de datos:** MySQL en ambos casos

## 🔧 Cambiar Perfil

Para cambiar el perfil, edita la línea en `application.properties`:
```properties
# Para desarrollo local
spring.profiles.active=local

# Para servidor LightNode
spring.profiles.active=lightnode
```

## 📁 Archivos de Configuración

- `application.properties` - Configuración base común
- `application-local.properties` - Configuración para desarrollo local
- `application-lightnode.properties` - Configuración para servidor LightNode

## ⚠️ Notas Importantes

1. **MySQL debe estar ejecutándose** en ambos casos
2. **Las credenciales** están configuradas para cada entorno
3. **Los archivos de upload** se guardan en rutas diferentes según el perfil
4. **El perfil local** crea la base de datos automáticamente si no existe

