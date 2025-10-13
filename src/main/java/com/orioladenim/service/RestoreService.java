package com.orioladenim.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orioladenim.entity.*;
import com.orioladenim.enums.Talle;
import com.orioladenim.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class RestoreService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductImageRepository productImageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ColorRepository colorRepository;
    
    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired
    private HistoriaRepository historiaRepository;
    
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Transactional
    public String restoreFromBackup(MultipartFile backupFile, String restoredBy, boolean clearExisting) throws IOException {
        // Validar archivo
        if (backupFile.isEmpty()) {
            throw new IllegalArgumentException("El archivo de backup está vacío");
        }
        
        if (!backupFile.getOriginalFilename().endsWith(".zip")) {
            throw new IllegalArgumentException("El archivo debe ser un ZIP");
        }
        
        // Crear directorio temporal para extraer
        Path tempDir = Files.createTempDirectory("backup_restore");
        
        try {
            // Extraer archivo ZIP
            extractZipFile(backupFile, tempDir);
            
            // Validar estructura del backup
            validateBackupStructure(tempDir);
            
            // Leer metadatos
            Map<String, Object> metadata = readMetadata(tempDir);
            
            if (clearExisting) {
                // Limpiar datos existentes
                clearExistingData();
            }
            
            // Restaurar datos en orden correcto (dependencias primero)
            restoreColors(tempDir);
            restoreCategories(tempDir);
            restoreProducts(tempDir);
            restoreRelationships(tempDir);
            restoreContacts(tempDir);
            restoreHistorias(tempDir);
            restoreProductImages(tempDir);
            restoreUsers(tempDir);
            
            return "Backup restaurado exitosamente. " + 
                   "Productos: " + metadata.get("productCount") + 
                   ", Categorías: " + metadata.get("categoryCount") + 
                   ", Colores: " + metadata.get("colorCount") +
                   ", Contactos: " + metadata.get("contactCount") +
                   ", Historias: " + metadata.get("historiaCount") +
                   ", Imágenes: " + metadata.get("imageCount") +
                   ", Usuarios: " + metadata.get("userCount");
            
        } finally {
            // Limpiar directorio temporal
            deleteDirectory(tempDir);
        }
    }
    
    private void extractZipFile(MultipartFile backupFile, Path tempDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(backupFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path filePath = tempDir.resolve(entry.getName());
                
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }
    
    private void validateBackupStructure(Path tempDir) throws IOException {
        Path metadataFile = tempDir.resolve("metadata.json");
        if (!Files.exists(metadataFile)) {
            throw new IllegalArgumentException("El archivo de backup no contiene metadatos válidos");
        }
        
        Path dataDir = tempDir.resolve("data");
        if (!Files.exists(dataDir)) {
            throw new IllegalArgumentException("El archivo de backup no contiene datos válidos");
        }
    }
    
    private Map<String, Object> readMetadata(Path tempDir) throws IOException {
        Path metadataFile = tempDir.resolve("metadata.json");
        String metadataJson = Files.readString(metadataFile);
        return objectMapper.readValue(metadataJson, new TypeReference<Map<String, Object>>() {});
    }
    
    @Transactional
    public void clearExistingData() {
        // Eliminar en orden correcto para respetar foreign keys
        // Limpiar en orden correcto (dependencias primero)
        productImageRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        colorRepository.deleteAll();
        contactRepository.deleteAll();
        historiaRepository.deleteAll();
        // No eliminar usuarios por seguridad
    }
    
    private void restoreCategories(Path tempDir) throws IOException {
        Path categoriesFile = tempDir.resolve("data/categories.json");
        if (Files.exists(categoriesFile)) {
            String categoriesJson = Files.readString(categoriesFile);
            List<Map<String, Object>> categoriesData = objectMapper.readValue(categoriesJson, new TypeReference<List<Map<String, Object>>>() {});
            
            for (Map<String, Object> categoryData : categoriesData) {
                // Verificar si la categoría ya existe
                String categoryName = (String) categoryData.get("name");
                Optional<Category> existingCategory = categoryRepository.findByNameIgnoreCase(categoryName);
                if (existingCategory.isEmpty()) {
                    Category category = new Category();
                    category.setName(categoryName);
                    category.setDescription((String) categoryData.get("description"));
                    category.setImagePath((String) categoryData.get("imagePath"));
                    category.setIsActive((Boolean) categoryData.get("isActive"));
                    category.setDisplayOrder((Integer) categoryData.get("displayOrder"));
                    category.setProductCount((Integer) categoryData.get("productCount"));
                    
                    // Ignorar productIds del backup (se restaurarán las relaciones después)
                    categoryRepository.save(category);
                }
            }
        }
    }
    
    private void restoreProducts(Path tempDir) throws IOException {
        Path productsFile = tempDir.resolve("data/products.json");
        if (Files.exists(productsFile)) {
            String productsJson = Files.readString(productsFile);
            List<Map<String, Object>> productsData = objectMapper.readValue(productsJson, new TypeReference<List<Map<String, Object>>>() {});
            
            for (Map<String, Object> productData : productsData) {
                Product product = new Product();
                product.setName((String) productData.get("name"));
                product.setDescripcion((String) productData.get("descripcion"));
                product.setPrice((Double) productData.get("price"));
                product.setMedidas((String) productData.get("medidas"));
                product.setColor((String) productData.get("color"));
                product.setActivo((Boolean) productData.get("activo"));
                product.setEsDestacado((Boolean) productData.get("esDestacado"));
                product.setEsNuevo((Boolean) productData.get("esNuevo"));
                product.setMaterial((String) productData.get("material"));
                product.setCuidados((String) productData.get("cuidados"));
                product.setEdadRecomendada((String) productData.get("edadRecomendada"));
                product.setQty((Integer) productData.get("qty"));
                product.setPrecioOriginal((Double) productData.get("precioOriginal"));
                product.setDescuentoPorcentaje((Double) productData.get("descuentoPorcentaje"));
                product.setTallasDisponibles((String) productData.get("tallasDisponibles"));
                product.setColoresDisponibles((String) productData.get("coloresDisponibles"));
                
                // Restaurar relaciones con categorías si existen
                @SuppressWarnings("unchecked")
                List<Integer> colorIds = (List<Integer>) productData.get("colores");
                if (colorIds != null && !colorIds.isEmpty()) {
                    // Buscar colores por IDs (simplificado - asumiendo que existen)
                    // TODO: Implementar restauración de colores si es necesario
                }
                
                productRepository.save(product);
            }
        }
    }
    
    private void restoreProductImages(Path tempDir) throws IOException {
        Path imagesFile = tempDir.resolve("data/product_images.json");
        if (Files.exists(imagesFile)) {
            String imagesJson = Files.readString(imagesFile);
            List<ProductImage> images = objectMapper.readValue(imagesJson, new TypeReference<List<ProductImage>>() {});
            
            // Crear directorio de uploads si no existe
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            for (ProductImage image : images) {
                // Restaurar producto asociado
                if (image.getProduct() != null) {
                    // Buscar producto por nombre (simplificado)
                    List<Product> products = productRepository.findByActivoTrue();
                    Optional<Product> product = products.stream()
                        .filter(p -> p.getName().equals(image.getProduct().getName()))
                        .findFirst();
                    if (product.isPresent()) {
                        image.setProduct(product.get());
                    } else {
                        continue; // Saltar imagen si no encuentra el producto
                    }
                }
                
                // Copiar archivo de imagen
                Path sourceImage = tempDir.resolve("images").resolve(image.getFileName());
                if (Files.exists(sourceImage)) {
                    Path targetImage = uploadPath.resolve(image.getFileName());
                    Files.copy(sourceImage, targetImage);
                    
                    // Crear thumbnail si existe
                    Path sourceThumbnail = tempDir.resolve("images/thumbnails").resolve(image.getFileName());
                    if (Files.exists(sourceThumbnail)) {
                        Path thumbnailsDir = uploadPath.resolve("thumbnails");
                        if (!Files.exists(thumbnailsDir)) {
                            Files.createDirectories(thumbnailsDir);
                        }
                        Path targetThumbnail = thumbnailsDir.resolve(image.getFileName());
                        Files.copy(sourceThumbnail, targetThumbnail);
                    }
                }
                
                image.setId(null); // Para generar nuevo ID
                productImageRepository.save(image);
            }
        }
    }
    
    private void restoreUsers(Path tempDir) throws IOException {
        Path usersFile = tempDir.resolve("data/users.json");
        if (Files.exists(usersFile)) {
            String usersJson = Files.readString(usersFile);
            List<Map<String, Object>> usersData = objectMapper.readValue(usersJson, new TypeReference<List<Map<String, Object>>>() {});
            
            for (Map<String, Object> userData : usersData) {
                String username = (String) userData.get("username");
                String email = (String) userData.get("email");
                
                // Verificar si el usuario ya existe
                Optional<User> existingUser = userRepository.findByUsername(username);
                if (existingUser.isEmpty()) {
                    User user = new User();
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setRole(User.Role.valueOf((String) userData.get("role")));
                    user.setIsActive((Boolean) userData.get("enabled"));
                    // No restaurar password por seguridad
                    user.setPassword("$2a$10$defaultPasswordHash"); // Password por defecto
                    
                    userRepository.save(user);
                }
            }
        }
    }
    
    private void restoreColors(Path tempDir) throws IOException {
        Path colorsFile = tempDir.resolve("data/colors.json");
        if (Files.exists(colorsFile)) {
            String colorsJson = Files.readString(colorsFile);
            List<Map<String, Object>> colorsData = objectMapper.readValue(colorsJson, new TypeReference<List<Map<String, Object>>>() {});
            
            for (Map<String, Object> colorData : colorsData) {
                String colorName = (String) colorData.get("name");
                Optional<Color> existingColor = colorRepository.findByNameIgnoreCase(colorName);
                if (existingColor.isEmpty()) {
                    Color color = new Color();
                    color.setName(colorName);
                    color.setDescription((String) colorData.get("description"));
                    color.setHexCode((String) colorData.get("hexCode"));
                    color.setIsActive((Boolean) colorData.get("isActive"));
                    color.setDisplayOrder((Integer) colorData.get("displayOrder"));
                    color.setProductCount((Integer) colorData.get("productCount"));
                    colorRepository.save(color);
                }
            }
        }
    }
    
    private void restoreContacts(Path tempDir) throws IOException {
        Path contactsFile = tempDir.resolve("data/contacts.json");
        if (Files.exists(contactsFile)) {
            String contactsJson = Files.readString(contactsFile);
            List<Map<String, Object>> contactsData = objectMapper.readValue(contactsJson, new TypeReference<List<Map<String, Object>>>() {});
            
            for (Map<String, Object> contactData : contactsData) {
                Contact contact = new Contact();
                contact.setNombre((String) contactData.get("nombre"));
                contact.setEmail((String) contactData.get("email"));
                contact.setTelefono((String) contactData.get("telefono"));
                contact.setAsunto((String) contactData.get("asunto"));
                contact.setMensaje((String) contactData.get("mensaje"));
                contact.setProductoInteres((String) contactData.get("productoInteres"));
                contact.setLeido((Boolean) contactData.get("leido"));
                contact.setRespondido((Boolean) contactData.get("respondido"));
                contact.setRespuesta((String) contactData.get("respuesta"));
                contact.setIpAddress((String) contactData.get("ipAddress"));
                contact.setUserAgent((String) contactData.get("userAgent"));
                contact.setUbicacion((String) contactData.get("ubicacion"));
                contact.setActivo((Boolean) contactData.get("activo"));
                contactRepository.save(contact);
            }
        }
    }
    
    private void restoreHistorias(Path tempDir) throws IOException {
        Path historiasFile = tempDir.resolve("data/historias.json");
        if (Files.exists(historiasFile)) {
            String historiasJson = Files.readString(historiasFile);
            List<Map<String, Object>> historiasData = objectMapper.readValue(historiasJson, new TypeReference<List<Map<String, Object>>>() {});
            
            for (Map<String, Object> historiaData : historiasData) {
                Historia historia = new Historia();
                historia.setTitulo((String) historiaData.get("titulo"));
                historia.setDescripcion((String) historiaData.get("descripcion"));
                historia.setVideoPath((String) historiaData.get("videoPath"));
                historia.setVideoThumbnail((String) historiaData.get("videoThumbnail"));
                historia.setDuracionSegundos((Integer) historiaData.get("duracionSegundos"));
                historia.setPesoArchivo((Long) historiaData.get("pesoArchivo"));
                historia.setActiva((Boolean) historiaData.get("activa"));
                historiaRepository.save(historia);
            }
        }
    }
    
    private void restoreRelationships(Path tempDir) throws IOException {
        Path relationshipsFile = tempDir.resolve("data/relationships.json");
        if (Files.exists(relationshipsFile)) {
            String relationshipsJson = Files.readString(relationshipsFile);
            Map<String, Object> relationshipsData = objectMapper.readValue(relationshipsJson, new TypeReference<Map<String, Object>>() {});
            
            // Restaurar relaciones Product-Colors
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> productColors = (List<Map<String, Object>>) relationshipsData.get("product_colors");
            if (productColors != null) {
                for (Map<String, Object> rel : productColors) {
                    Integer productId = (Integer) rel.get("productId");
                    Long colorId = ((Number) rel.get("colorId")).longValue();
                    
                    Optional<Product> product = productRepository.findById(productId);
                    Optional<Color> color = colorRepository.findById(colorId);
                    
                    if (product.isPresent() && color.isPresent()) {
                        if (product.get().getColores() == null) {
                            product.get().setColores(new ArrayList<>());
                        }
                        product.get().getColores().add(color.get());
                        productRepository.save(product.get());
                    }
                }
            }
            
            // Restaurar relaciones Product-Categories
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> productCategories = (List<Map<String, Object>>) relationshipsData.get("product_categories");
            if (productCategories != null) {
                for (Map<String, Object> rel : productCategories) {
                    Integer productId = (Integer) rel.get("productId");
                    Long categoryId = ((Number) rel.get("categoryId")).longValue();
                    
                    Optional<Product> product = productRepository.findById(productId);
                    Optional<Category> category = categoryRepository.findById(categoryId);
                    
                    if (product.isPresent() && category.isPresent()) {
                        if (product.get().getCategories() == null) {
                            product.get().setCategories(new ArrayList<>());
                        }
                        product.get().getCategories().add(category.get());
                        productRepository.save(product.get());
                    }
                }
            }
            
            // Restaurar relaciones Product-Talles
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> productTalles = (List<Map<String, Object>>) relationshipsData.get("product_talles");
            if (productTalles != null) {
                for (Map<String, Object> rel : productTalles) {
                    Integer productId = (Integer) rel.get("productId");
                    String talleStr = (String) rel.get("talle");
                    
                    Optional<Product> product = productRepository.findById(productId);
                    if (product.isPresent()) {
                        try {
                            Talle talle = Talle.valueOf(talleStr);
                            if (product.get().getTalles() == null) {
                                product.get().setTalles(new ArrayList<>());
                            }
                            product.get().getTalles().add(talle);
                            productRepository.save(product.get());
                        } catch (IllegalArgumentException e) {
                            // Ignorar talles inválidos
                        }
                    }
                }
            }
        }
    }
    
    private void deleteDirectory(Path directory) throws IOException {
        if (Files.exists(directory)) {
            Files.walk(directory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }
    
    public boolean validateBackupFile(MultipartFile file) {
        try {
            if (file.isEmpty() || !file.getOriginalFilename().endsWith(".zip")) {
                return false;
            }
            
            // Verificar que es un ZIP válido
            try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
                ZipEntry entry = zis.getNextEntry();
                return entry != null;
            }
        } catch (IOException e) {
            return false;
        }
    }
}