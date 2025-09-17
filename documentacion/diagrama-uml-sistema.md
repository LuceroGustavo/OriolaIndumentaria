# Diagrama UML - Sistema ORIOLA Denim

**Fecha:** 17 de septiembre de 2025  
**Versión:** 2.0

---

## 🏗️ **DIAGRAMA DE CLASES PRINCIPALES**

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                SISTEMA ORIOLA DENIM                            │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                    ENTIDADES                                   │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                    PRODUCT                                     │
├─────────────────────────────────────────────────────────────────────────────────┤
│ - pId: Integer (PK)                                                             │
│ - name: String                                                                  │
│ - price: Double                                                                 │
│ - qty: Integer                                                                  │
│ - descripcion: String                                                           │
│ - categoria: Categoria                                                          │
│ - talle: Talle                                                                  │
│ - genero: Genero                                                                │
│ - temporada: Temporada                                                          │
│ - color: String                                                                 │
│ - medidas: String                                                               │
│ - material: String                                                              │
│ - cuidados: String                                                              │
│ - edadRecomendada: String                                                       │
│ - tallasDisponibles: String                                                     │
│ - coloresDisponibles: String                                                    │
│ - esDestacado: Boolean                                                          │
│ - esNuevo: Boolean                                                              │
│ - descuentoPorcentaje: Double                                                   │
│ - precioOriginal: Double                                                        │
│ - activo: Boolean                                                               │
│ - fechaCreacion: LocalDateTime                                                  │
│ - fechaActualizacion: LocalDateTime                                             │
│ - images: List<ProductImage>                                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + getImagenPrincipal(): ProductImage                                            │
│ + getImagenPrincipalUrl(): String                                               │
│ + getPrecioFinal(): Double                                                      │
│ + tieneDescuento(): Boolean                                                     │
│ + agregarImagen(ProductImage): void                                             │
│ + removerImagen(ProductImage): void                                             │
└─────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ 1..*
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                 PRODUCTIMAGE                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│ - id: Long (PK)                                                                 │
│ - imagePath: String                                                             │
│ - fileName: String                                                              │
│ - originalName: String                                                          │
│ - fileSize: Long                                                                │
│ - isPrimary: Boolean                                                            │
│ - displayOrder: Integer                                                         │
│ - product: Product                                                              │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + getImageUrl(): String                                                         │
│ + getThumbnailUrl(): String                                                     │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                     USER                                        │
├─────────────────────────────────────────────────────────────────────────────────┤
│ - id: Long (PK)                                                                 │
│ - username: String (UNIQUE)                                                     │
│ - email: String (UNIQUE)                                                        │
│ - password: String                                                              │
│ - fullName: String                                                              │
│ - isActive: Boolean                                                             │
│ - role: Role                                                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + Role: ADMIN, USER                                                             │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                   ENUMS                                        │
├─────────────────────────────────────────────────────────────────────────────────┤
│ CATEGORIA: REMERAS, BUZOS, CAMISAS, PANTALONES, ACCESORIOS, VESTIDOS, SHORTS,  │
│           CHOMBAS                                                               │
│ TALLE: XS, S, M, L, XL, XXL, XXXL, UNICO                                      │
│ GENERO: HOMBRE, MUJER, UNISEX, NINO, NINA                                      │
│ TEMPORADA: VERANO, INVIERNO, PRIMAVERA, OTONO, TODO_EL_ANO                     │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                 CONTROLADORES                                  │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                PUBLICCONTROLLER                                │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + home(Model): String                                                           │
│ + catalog(Model): String                                                        │
│ + productDetail(Integer, Model): String                                         │
│ + contact(): String                                                             │
│ + about(): String                                                               │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                               PRODUCTCONTROLLER                                │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + listProducts(Model): String                                                   │
│ + showForm(Model): String                                                       │
│ + addProduct(Product, BindingResult, Model): String                             │
│ + editProduct(Integer, Model): String                                           │
│ + updateProduct(Integer, Product, BindingResult, Model): String                 │
│ + deleteProduct(Integer): String                                                │
│ + manageImages(Integer, Model): String                                          │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                              FILEUPLOADCONTROLLER                              │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + uploadProductImage(Integer, MultipartFile, Boolean): ResponseEntity           │
│ + uploadMultipleProductImages(Integer, MultipartFile[]): ResponseEntity        │
│ + deleteProductImage(Long): ResponseEntity                                      │
│ + setPrimaryImage(Long): ResponseEntity                                         │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                ADMINCONTROLLER                                 │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + login(): String                                                               │
│ + dashboard(Model, Authentication): String                                      │
│ + help(): String                                                                │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                   SERVICIOS                                    │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                PRODUCTSERVICE                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + findAll(): List<Product>                                                      │
│ + findById(Integer): Product                                                    │
│ + save(Product): Product                                                        │
│ + deleteById(Integer): void                                                     │
│ + findByCategoria(String): List<Product>                                        │
│ + findByActivoTrue(): List<Product>                                             │
│ + findDestacados(): List<Product>                                               │
│ + findNuevos(): List<Product>                                                   │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                             IMAGEPROCESSINGSERVICE                             │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + processAndSaveImage(MultipartFile, Integer, Boolean): ProductImage            │
│ + deleteImage(String): void                                                     │
│ + getFileSizeString(Long): String                                               │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                USERSERVICE                                     │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + loadUserByUsername(String): UserDetails                                       │
│ + createUser(User): User                                                        │
│ + findByUsername(String): Optional<User>                                        │
│ + existsByUsername(String): Boolean                                             │
│ + existsByEmail(String): Boolean                                                │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                              WEBPCONVERSIONSERVICE                             │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + convertToWebP(byte[], String): byte[]                                         │
│ + isWebPSupported(): Boolean                                                    │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                 REPOSITORIOS                                   │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                               PRODUCTREPOSITORY                                │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + findByCategoria(String): List<Product>                                        │
│ + findByActivoTrue(): List<Product>                                             │
│ + findByEsDestacadoTrueAndActivoTrue(): List<Product>                           │
│ + findByEsNuevoTrueAndActivoTrue(): List<Product>                               │
│ + findByGeneroAndActivoTrue(String): List<Product>                              │
│ + findByTemporadaAndActivoTrue(String): List<Product>                           │
│ + findProductosConDescuento(): List<Product>                                    │
│ + findByPrecioBetween(Double, Double): List<Product>                            │
│ + findByNombreContainingIgnoreCase(String): List<Product>                       │
│ + findByColorContainingIgnoreCase(String): List<Product>                        │
│ + countByCategoria(String): Long                                                │
│ + findProductosRecientes(): List<Product>                                       │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                            PRODUCTIMAGEREPOSITORY                              │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + findByProductIdOrderByDisplayOrderAsc(Integer): List<ProductImage>            │
│ + findPrimaryImageByProductId(Integer): Optional<ProductImage>                  │
│ + countByProductId(Integer): Long                                               │
│ + findByImagePath(String): Optional<ProductImage>                               │
│ + deleteByProductId(Integer): void                                              │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                USERREPOSITORY                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + findByUsername(String): Optional<User>                                        │
│ + findByEmail(String): Optional<User>                                           │
│ + existsByUsername(String): Boolean                                             │
│ + existsByEmail(String): Boolean                                                │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                 CONFIGURACIÓN                                 │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                               SECURITYCONFIG                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + filterChain(HttpSecurity): SecurityFilterChain                                │
│ + passwordEncoder(): PasswordEncoder                                            │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                 WEBCONFIG                                      │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + addResourceHandlers(ResourceHandlerRegistry): void                           │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                               DATAINITIALIZER                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + run(String...): void                                                          │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                            ORIOLADENIMAPPLICATION                              │
├─────────────────────────────────────────────────────────────────────────────────┤
│ + main(String[]): void                                                          │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 🔄 **DIAGRAMA DE FLUJO DE FUNCIONAMIENTO**

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              FLUJO DE USUARIO                                  │
└─────────────────────────────────────────────────────────────────────────────────┘

USUARIO PÚBLICO:
┌─────────┐    ┌──────────────┐    ┌─────────────┐    ┌──────────────┐
│   /     │───▶│   Página     │───▶│  Productos  │───▶│   Detalle    │
│         │    │  Principal   │    │ Destacados  │    │  Producto    │
└─────────┘    └──────────────┘    └─────────────┘    └──────────────┘
     │
     ▼
┌──────────────┐    ┌─────────────┐    ┌──────────────┐
│   /catalog   │───▶│  Catálogo   │───▶│   Filtros    │
│              │    │  Completo   │    │  Búsqueda    │
└──────────────┘    └─────────────┘    └──────────────┘

ADMINISTRADOR:
┌──────────────┐    ┌─────────────┐    ┌──────────────┐
│ /admin/login │───▶│  Dashboard  │───▶│  Gestión     │
│              │    │             │    │ Productos    │
└──────────────┘    └─────────────┘    └──────────────┘
                           │
                           ▼
                    ┌──────────────┐    ┌─────────────┐
                    │  Gestión     │───▶│  Subida     │
                    │  Imágenes    │    │ Imágenes    │
                    └──────────────┘    └─────────────┘
```

---

## 📊 **DIAGRAMA DE BASE DE DATOS**

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              ESTRUCTURA DE BD                                  │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                  PRODUCT                                       │
├─────────────────────────────────────────────────────────────────────────────────┤
│ p_id (PK) │ name │ price │ qty │ descripcion │ categoria │ talle │ genero │ ... │
├───────────┼──────┼───────┼─────┼─────────────┼───────────┼───────┼────────┼─────┤
│     1     │ Buzo │ 8500  │ 10  │ Buzo rosa   │ BUZOS     │ L     │ UNISEX │ ... │
│     2     │ Rem  │ 3500  │ 25  │ Remera bl   │ REMERAS   │ M     │ UNISEX │ ... │
└───────────┴──────┴───────┴─────┴─────────────┴───────────┴───────┴────────┴─────┘
                                    │
                                    │ 1..*
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                               PRODUCT_IMAGE                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│ id (PK) │ image_path │ file_name │ original_name │ file_size │ is_primary │ ... │
├─────────┼────────────┼───────────┼───────────────┼───────────┼────────────┼─────┤
│    1    │ abc123.webp│ abc123    │ buzo-rosa.jpg │ 2048576   │ true       │ ... │
│    2    │ def456.webp│ def456    │ buzo-rosa2.jpg│ 1536000   │ false      │ ... │
└─────────┴────────────┴───────────┴───────────────┴───────────┴────────────┴─────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                  USERS                                         │
├─────────────────────────────────────────────────────────────────────────────────┤
│ id (PK) │ username │ email │ password │ full_name │ is_active │ role │ ... │
├─────────┼──────────┼───────┼──────────┼───────────┼───────────┼──────┼─────┤
│    1    │ admin    │ admin │ $2a$10$...│ Admin     │ true      │ ADMIN│ ... │
└─────────┴──────────┴───────┴──────────┴───────────┴───────────┴──────┴─────┘
```

---

## 🎯 **RESUMEN DE ARQUITECTURA**

### **Patrones Implementados:**
- **MVC (Model-View-Controller)** - Separación clara de responsabilidades
- **Repository Pattern** - Abstracción de acceso a datos
- **Service Layer** - Lógica de negocio encapsulada
- **Dependency Injection** - Inyección de dependencias con Spring

### **Tecnologías Utilizadas:**
- **Spring Boot** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **Thymeleaf** - Motor de plantillas
- **MySQL** - Base de datos relacional
- **Maven** - Gestión de dependencias

### **Características Técnicas:**
- **Arquitectura en capas** bien definida
- **Separación de responsabilidades** clara
- **Código mantenible** y escalable
- **Configuración externa** para diferentes entornos
- **Validaciones** en frontend y backend
- **Manejo de errores** robusto

---

**Desarrollado por:** Equipo de Desarrollo ORIOLA  
**Fecha:** 17 de septiembre de 2025  
**Versión:** 2.0  
**Estado:** Documentación completa del sistema
