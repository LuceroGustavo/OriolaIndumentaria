package com.orioladenim.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orioladenim.entity.*;
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
            
            // Restaurar datos
            restoreCategories(tempDir);
            restoreProducts(tempDir);
            restoreProductImages(tempDir);
            restoreUsers(tempDir);
            
            return "Backup restaurado exitosamente. " + 
                   "Productos: " + metadata.get("productCount") + 
                   ", Categorías: " + metadata.get("categoryCount") + 
                   ", Imágenes: " + metadata.get("imageCount");
            
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
        productImageRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        // No eliminar usuarios por seguridad
    }
    
    private void restoreCategories(Path tempDir) throws IOException {
        Path categoriesFile = tempDir.resolve("data/categories.json");
        if (Files.exists(categoriesFile)) {
            String categoriesJson = Files.readString(categoriesFile);
            List<Category> categories = objectMapper.readValue(categoriesJson, new TypeReference<List<Category>>() {});
            
            for (Category category : categories) {
                // Verificar si la categoría ya existe
                Optional<Category> existingCategory = categoryRepository.findByNameIgnoreCase(category.getName());
                if (existingCategory.isEmpty()) {
                    category.setId(null); // Para generar nuevo ID
                    categoryRepository.save(category);
                }
            }
        }
    }
    
    private void restoreProducts(Path tempDir) throws IOException {
        Path productsFile = tempDir.resolve("data/products.json");
        if (Files.exists(productsFile)) {
            String productsJson = Files.readString(productsFile);
            List<Product> products = objectMapper.readValue(productsJson, new TypeReference<List<Product>>() {});
            
            for (Product product : products) {
                product.setPId(null); // Para generar nuevo ID
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