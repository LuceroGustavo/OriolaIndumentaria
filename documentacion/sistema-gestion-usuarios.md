# Sistema de Gestión de Usuarios - ORIOLA

## 📋 **DESCRIPCIÓN GENERAL**

Sistema completo de gestión de usuarios con roles diferenciados, autenticación avanzada y funcionalidades de administración para el proyecto ORIOLA Indumentaria.

## 🏗️ **ARQUITECTURA**

### **Entidades Principales**

#### **User Entity**
```java
@Entity
@Table(name = "users")
public class User {
    // Identificación
    private Long id;
    private String username;
    private String email;
    private String password;
    
    // Perfil personal
    private String fullName;
    private String phone;
    private String address;
    private String city;
    private String country;
    
    // Seguridad
    private Boolean isActive;
    private Role role;
    private LocalDateTime lastLogin;
    private Integer loginAttempts;
    private Boolean accountLocked;
    private LocalDateTime passwordChangedAt;
    private Boolean mustChangePassword;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

#### **Category Entity**
```java
@Entity
@Table(name = "categories")
public class Category {
    private Long id;
    private String name;
    private String description;
    private String color;        // #FF5733
    private String icon;         // fa-tshirt
    private Integer displayOrder;
    private Boolean isActive;
    private Integer productCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Product> products;
}
```

## 🔐 **SISTEMA DE ROLES**

### **Roles Disponibles**

#### **ADMIN**
- **Acceso**: Panel de administración básico
- **Funcionalidades**:
  - Dashboard con estadísticas
  - Gestión de productos
  - Gestión de consultas
  - Cambio de contraseña personal
  - Visualización de usuarios (solo lectura)

#### **SUPER_ADMIN**
- **Acceso**: Control total del sistema
- **Funcionalidades**:
  - Todo lo de ADMIN +
  - Lista completa de usuarios
  - Reset de contraseñas de cualquier usuario
  - Activar/desactivar usuarios
  - Gestión completa de categorías
  - Acceso de emergencia

## 👥 **USUARIOS POR DEFECTO**

### **Administrador**
- **Usuario**: `admin`
- **Contraseña**: `admin`
- **Rol**: `ADMIN`
- **Propósito**: Gestión diaria del negocio

### **Desarrollador**
- **Usuario**: `dev`
- **Contraseña**: `Dev2024#`
- **Rol**: `SUPER_ADMIN`
- **Propósito**: Acceso de emergencia y mantenimiento

## 🛠️ **FUNCIONALIDADES IMPLEMENTADAS**

### **1. Autenticación y Autorización**

#### **Login Seguro**
- **Encriptación**: BCrypt para contraseñas
- **Sesiones**: Spring Security con invalidación automática
- **Validación**: Usuarios activos y no bloqueados

#### **Gestión de Contraseñas**
- **Cambio personal**: Validación de contraseña actual
- **Reset administrativo**: Solo SUPER_ADMIN
- **Validaciones**: Mínimo 6 caracteres, confirmación

### **2. Panel de Administración**

#### **Dashboard Principal**
- **Estadísticas**: Productos, consultas, usuarios
- **Navegación**: Enlaces según rol del usuario
- **Información**: Perfil del usuario actual

#### **Gestión de Usuarios** (Solo SUPER_ADMIN)
- **Lista completa**: Todos los usuarios del sistema
- **Información detallada**: Perfil, rol, estado, último login
- **Acciones disponibles**:
  - Reset de contraseña
  - Activar/desactivar usuario
  - Ver detalles del perfil

### **3. Gestión de Categorías**

#### **Categorías Dinámicas**
- **Creación automática**: 5 categorías por defecto
- **Personalización**: Color, icono, orden de visualización
- **Contadores**: Productos por categoría
- **Estado**: Activa/inactiva

#### **Categorías por Defecto**
1. **Remeras** - #FF6B6B - fa-tshirt
2. **Buzos** - #4ECDC4 - fa-hoodie
3. **Camisas** - #45B7D1 - fa-shirt
4. **Pantalones** - #96CEB4 - fa-pants
5. **Accesorios** - #FFEAA7 - fa-accessories

## 🔧 **IMPLEMENTACIÓN TÉCNICA**

### **Controladores**

#### **UserManagementController**
```java
@Controller
@RequestMapping("/admin/users")
public class UserManagementController {
    
    @GetMapping
    public String listUsers(Model model) {
        // Solo SUPER_ADMIN puede ver la lista
    }
    
    @GetMapping("/change-password")
    public String changePasswordForm(Model model) {
        // Formulario de cambio de contraseña
    }
    
    @PostMapping("/change-password")
    public String changePassword(...) {
        // Procesar cambio de contraseña
    }
    
    @PostMapping("/reset-password/{userId}")
    public String resetPassword(...) {
        // Reset de contraseña (solo SUPER_ADMIN)
    }
    
    @PostMapping("/toggle-status/{userId}")
    public String toggleUserStatus(...) {
        // Activar/desactivar usuario (solo SUPER_ADMIN)
    }
}
```

### **Servicios**

#### **UserService Mejorado**
```java
@Service
@Transactional
public class UserService implements UserDetailsService {
    
    // Autenticación
    public UserDetails loadUserByUsername(String username);
    
    // Gestión de contraseñas
    public void changePassword(Long userId, String currentPassword, String newPassword);
    public void changePasswordByAdmin(Long userId, String newPassword);
    
    // Gestión de usuarios
    public List<User> getAllUsers();
    public Optional<User> findByUsername(String username);
    public void toggleUserStatus(Long userId);
    
    // Validaciones
    public boolean validatePassword(String password);
    public boolean isPasswordExpired(User user, int maxDays);
}
```

#### **CategoryService**
```java
@Service
@Transactional
public class CategoryService {
    
    // CRUD básico
    public List<Category> findAllActiveCategories();
    public Category createCategory(Category category);
    public Category updateCategory(Long id, Category categoryDetails);
    public void deleteCategory(Long id);
    
    // Gestión de estado
    public Category toggleCategoryStatus(Long id);
    
    // Búsquedas
    public List<Category> searchCategories(String searchText);
    public List<Category> findCategoriesWithProducts();
    
    // Estadísticas
    public Long countActiveCategories();
    public Long countCategoriesWithProducts();
    
    // Inicialización
    public void createDefaultCategories();
}
```

## 🎨 **INTERFAZ DE USUARIO**

### **Plantillas HTML**

#### **admin/user-list.html**
- **Lista completa** de usuarios con información detallada
- **Acciones por usuario**: Reset, activar/desactivar
- **Filtros visuales**: Por rol, estado, último login
- **Modales**: Para confirmación de acciones

#### **admin/change-password.html**
- **Formulario seguro** con validaciones
- **Confirmación de contraseña** con JavaScript
- **Recomendaciones de seguridad**
- **Feedback visual** de éxito/error

#### **admin/dashboard.html**
- **Enlaces condicionales** según rol del usuario
- **Estadísticas visuales** con iconos
- **Navegación intuitiva** con Bootstrap

## 🔒 **SEGURIDAD IMPLEMENTADA**

### **Validaciones de Contraseña**
- **Longitud mínima**: 6 caracteres
- **Confirmación**: Debe coincidir con la confirmación
- **Contraseña actual**: Validación obligatoria para cambios

### **Control de Acceso**
- **Roles diferenciados**: ADMIN vs SUPER_ADMIN
- **Validación de permisos**: En cada endpoint
- **Sesiones seguras**: Invalidación automática

### **Protección de Datos**
- **Encriptación**: BCrypt para contraseñas
- **Validación de entrada**: Sanitización de datos
- **Transacciones**: Operaciones atómicas

## 📊 **MÉTRICAS Y MONITOREO**

### **Estadísticas de Usuarios**
- **Total de usuarios**: Activos e inactivos
- **Usuarios por rol**: Distribución de permisos
- **Último login**: Actividad reciente
- **Intentos fallidos**: Seguridad

### **Estadísticas de Categorías**
- **Categorías activas**: Total disponible
- **Productos por categoría**: Distribución
- **Categorías vacías**: Para limpieza

## 🚀 **DESPLIEGUE Y CONFIGURACIÓN**

### **Variables de Entorno**
```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/oriola_denim
spring.datasource.username=root
spring.datasource.password=tu_password

# Seguridad
spring.security.user.name=admin
spring.security.user.password=admin
```

### **Inicialización Automática**
- **Usuarios por defecto**: Creados automáticamente
- **Categorías por defecto**: Inicializadas al arrancar
- **Contadores**: Actualizados automáticamente

## 🔄 **MANTENIMIENTO**

### **Tareas Regulares**
1. **Revisar usuarios inactivos**: Limpiar cuentas no utilizadas
2. **Actualizar contraseñas**: Rotación periódica
3. **Monitorear intentos fallidos**: Detectar ataques
4. **Revisar categorías vacías**: Limpiar o poblar

### **Backup y Recuperación**
- **Base de datos**: Backup regular de la tabla `users`
- **Configuración**: Backup de roles y permisos
- **Logs**: Monitoreo de accesos y cambios

## 📈 **PRÓXIMAS MEJORAS**

### **Funcionalidades Adicionales**
1. **Auditoría**: Log de cambios en usuarios
2. **Notificaciones**: Alertas de seguridad
3. **API REST**: Gestión externa
4. **Integración**: Sistemas externos

### **Optimizaciones**
1. **Caché**: Para consultas frecuentes
2. **Paginación**: Listas grandes de usuarios
3. **Búsqueda**: Filtros avanzados
4. **Exportación**: Datos en Excel/PDF

---

**Sistema implementado y funcionando correctamente. Listo para producción.**
