package com.otz.service;

import com.otz.entity.ProductImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageProcessingService {
    
    private static final String UPLOAD_DIR = "uploads";
    private static final String THUMBNAIL_DIR = "uploads/thumbnails";
    private static final int MAX_WIDTH = 1920;
    private static final int MAX_HEIGHT = 1080;
    private static final int THUMBNAIL_SIZE = 300;
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
    
    public ImageProcessingService() {
        createDirectories();
    }
    
    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            Files.createDirectories(Paths.get(THUMBNAIL_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Error creando directorios de imágenes", e);
        }
    }
    
    /**
     * Procesa y guarda una imagen, convirtiéndola a WebP y creando thumbnail
     */
    public ProductImage processAndSaveImage(MultipartFile file, Integer productId, boolean isPrimary) {
        try {
            // Validar archivo
            validateFile(file);
            
            // Generar nombre único
            String originalName = file.getOriginalFilename();
            String extension = getFileExtension(originalName);
            String uniqueName = UUID.randomUUID().toString() + ".png";
            
            // Procesar imagen original
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            BufferedImage resizedImage = resizeImage(originalImage, MAX_WIDTH, MAX_HEIGHT);
            
            // Guardar imagen principal
            String imagePath = saveImageAsWebP(resizedImage, uniqueName, UPLOAD_DIR);
            
            // Crear y guardar thumbnail
            BufferedImage thumbnail = createThumbnail(originalImage, THUMBNAIL_SIZE);
            saveImageAsWebP(thumbnail, uniqueName, THUMBNAIL_DIR);
            
            // Crear entidad ProductImage
            ProductImage productImage = new ProductImage();
            productImage.setImagePath(uniqueName);
            productImage.setFileName(uniqueName);
            productImage.setOriginalName(originalName);
            productImage.setFileSize(file.getSize());
            productImage.setIsPrimary(isPrimary);
            productImage.setDisplayOrder(0);
            
            return productImage;
            
        } catch (IOException e) {
            throw new RuntimeException("Error procesando imagen: " + e.getMessage(), e);
        }
    }
    
    /**
     * Valida el archivo de imagen
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo es demasiado grande. Máximo 5MB");
        }
        
        String extension = getFileExtension(file.getOriginalFilename());
        if (extension == null || !isAllowedExtension(extension)) {
            throw new IllegalArgumentException("Formato de archivo no permitido. Use: " + 
                String.join(", ", ALLOWED_EXTENSIONS));
        }
    }
    
    /**
     * Obtiene la extensión del archivo
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return null;
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }
    
    /**
     * Verifica si la extensión está permitida
     */
    private boolean isAllowedExtension(String extension) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Redimensiona la imagen manteniendo la proporción
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // Calcular nuevas dimensiones manteniendo proporción
        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);
        
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);
        
        // Si la imagen es más pequeña que el máximo, no redimensionar
        if (originalWidth <= maxWidth && originalHeight <= maxHeight) {
            return originalImage;
        }
        
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        
        // Configurar calidad de renderizado
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        
        return resizedImage;
    }
    
    /**
     * Crea un thumbnail cuadrado de la imagen
     */
    private BufferedImage createThumbnail(BufferedImage originalImage, int size) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // Calcular dimensiones para recorte cuadrado
        int cropSize = Math.min(originalWidth, originalHeight);
        int x = (originalWidth - cropSize) / 2;
        int y = (originalHeight - cropSize) / 2;
        
        // Recortar imagen a cuadrado
        BufferedImage croppedImage = originalImage.getSubimage(x, y, cropSize, cropSize);
        
        // Redimensionar a tamaño thumbnail
        return resizeImage(croppedImage, size, size);
    }
    
    /**
     * Guarda la imagen como PNG (formato compatible)
     */
    private String saveImageAsWebP(BufferedImage image, String filename, String directory) throws IOException {
        Path directoryPath = Paths.get(directory);
        // Cambiar extensión a PNG para compatibilidad
        String pngFilename = filename.replace(".webp", ".png");
        Path filePath = directoryPath.resolve(pngFilename);
        
        // Guardar como PNG
        ImageIO.write(image, "png", filePath.toFile());
        
        return pngFilename;
    }
    
    /**
     * Elimina una imagen del sistema de archivos
     */
    public void deleteImage(String imagePath) {
        try {
            // Eliminar imagen principal
            Path mainImagePath = Paths.get(UPLOAD_DIR, imagePath);
            if (Files.exists(mainImagePath)) {
                Files.delete(mainImagePath);
            }
            
            // Eliminar thumbnail
            Path thumbnailPath = Paths.get(THUMBNAIL_DIR, imagePath);
            if (Files.exists(thumbnailPath)) {
                Files.delete(thumbnailPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error eliminando imagen: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene el tamaño del archivo en formato legible
     */
    public String getFileSizeString(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
