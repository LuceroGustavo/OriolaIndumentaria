package com.orioladenim.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class VideoProcessingService {
    
    @Value("${upload.path}")
    private String uploadPath;
    
    @Value("${upload.thumbnail.path}")
    private String thumbnailPath;
    
    // Formatos de video permitidos
    private static final String[] FORMATOS_PERMITIDOS = {
        "video/mp4", "video/webm", "video/quicktime", "video/x-msvideo"
    };
    
    // Tamaño máximo: 15MB
    private static final long TAMANO_MAXIMO = 15 * 1024 * 1024;
    
    // Duración máxima: 15 segundos
    private static final int DURACION_MAXIMA = 15;
    
    /**
     * Procesa y guarda un video de historia
     */
    public String procesarVideo(MultipartFile video) throws IOException {
        // Validar formato
        if (!esFormatoValido(video)) {
            throw new IllegalArgumentException("Formato de video no válido. Use MP4, WebM, MOV o AVI.");
        }
        
        // Validar tamaño
        if (video.getSize() > TAMANO_MAXIMO) {
            throw new IllegalArgumentException("El video no puede superar los 10MB.");
        }
        
        // Crear directorio de historias
        Path historiasDir = Paths.get(uploadPath, "historias");
        Files.createDirectories(historiasDir);
        
        // Generar nombre único
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String extension = obtenerExtension(video.getOriginalFilename());
        String fileName = "historia_" + timestamp + extension;
        
        // Guardar archivo
        Path filePath = historiasDir.resolve(fileName);
        Files.copy(video.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return "historias/" + fileName;
    }
    
    /**
     * Genera un thumbnail para el video
     */
    public String generarThumbnail(String videoPath) {
        try {
            // Crear directorio de thumbnails
            Path thumbnailsDir = Paths.get(thumbnailPath, "historias");
            Files.createDirectories(thumbnailsDir);
            
            // Generar nombre del thumbnail
            String videoFileName = Paths.get(videoPath).getFileName().toString();
            String thumbnailFileName = "thumb_" + videoFileName.replaceFirst("[.][^.]+$", ".jpg");
            
            // Por ahora, creamos un placeholder
            // En una implementación real, usarías FFmpeg para extraer un frame
            Path thumbnailPath = thumbnailsDir.resolve(thumbnailFileName);
            
            // Crear un archivo placeholder (en producción usar FFmpeg)
            Files.write(thumbnailPath, "thumbnail placeholder".getBytes());
            
            return "historias/" + thumbnailFileName;
        } catch (IOException e) {
            // Si falla la generación del thumbnail, retornar null
            return null;
        }
    }
    
    /**
     * Valida si el formato del video es válido
     */
    private boolean esFormatoValido(MultipartFile video) {
        String contentType = video.getContentType();
        if (contentType == null) return false;
        
        for (String formato : FORMATOS_PERMITIDOS) {
            if (contentType.equals(formato)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtiene la extensión del archivo
     */
    private String obtenerExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ".mp4";
    }
    
    /**
     * Elimina un video y su thumbnail
     */
    public void eliminarVideo(String videoPath) {
        try {
            // Eliminar video
            Path videoFile = Paths.get(uploadPath, videoPath);
            if (Files.exists(videoFile)) {
                Files.delete(videoFile);
            }
            
            // Eliminar thumbnail si existe
            String thumbnailPath = videoPath.replaceFirst("[.][^.]+$", ".jpg");
            Path thumbnailFile = Paths.get(this.thumbnailPath, thumbnailPath);
            if (Files.exists(thumbnailFile)) {
                Files.delete(thumbnailFile);
            }
        } catch (IOException e) {
            // Log del error pero no lanzar excepción
            System.err.println("Error al eliminar video: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene información del video (duración, tamaño, etc.)
     */
    public VideoInfo obtenerInformacionVideo(MultipartFile video) {
        return new VideoInfo(
            video.getSize(),
            DURACION_MAXIMA, // En una implementación real, extraerías la duración real
            video.getContentType(),
            video.getOriginalFilename()
        );
    }
    
    /**
     * Clase para información del video
     */
    public static class VideoInfo {
        private final long tamano;
        private final int duracion;
        private final String tipo;
        private final String nombre;
        
        public VideoInfo(long tamano, int duracion, String tipo, String nombre) {
            this.tamano = tamano;
            this.duracion = duracion;
            this.tipo = tipo;
            this.nombre = nombre;
        }
        
        public long getTamano() { return tamano; }
        public int getDuracion() { return duracion; }
        public String getTipo() { return tipo; }
        public String getNombre() { return nombre; }
        
        public String getTamanoFormateado() {
            double mb = tamano / (1024.0 * 1024.0);
            return String.format("%.1f MB", mb);
        }
    }
    
    /**
     * Procesa y guarda un video de producto
     * Similar a procesarVideo pero guarda en directorio de productos
     */
    public String procesarVideoProducto(MultipartFile video, Integer productId) throws IOException {
        // Validar formato
        if (!esFormatoValido(video)) {
            throw new IllegalArgumentException("Formato de video no válido. Use MP4, WebM, MOV o AVI.");
        }
        
        // Validar tamaño (productos pueden tener videos más largos)
        long tamanoMaximoProducto = 50 * 1024 * 1024; // 50MB
        if (video.getSize() > tamanoMaximoProducto) {
            throw new IllegalArgumentException("El video no puede superar los 50MB.");
        }
        
        // Crear directorio de videos de productos
        Path productosDir = Paths.get(uploadPath, "productos", "videos");
        Files.createDirectories(productosDir);
        
        // Generar nombre único
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String extension = obtenerExtension(video.getOriginalFilename());
        String fileName = "producto_" + productId + "_" + timestamp + extension;
        
        // Guardar archivo
        Path filePath = productosDir.resolve(fileName);
        Files.copy(video.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return "productos/videos/" + fileName;
    }
    
    /**
     * Genera un thumbnail para el video de producto
     */
    public String generarThumbnailProducto(String videoPath, Integer productId) {
        try {
            // Crear directorio de thumbnails
            Path thumbnailsDir = Paths.get(thumbnailPath, "productos", "videos");
            Files.createDirectories(thumbnailsDir);
            
            // Generar nombre del thumbnail
            String videoFileName = Paths.get(videoPath).getFileName().toString();
            String thumbnailFileName = "thumb_" + videoFileName.replaceFirst("[.][^.]+$", ".jpg");
            
            // Por ahora, creamos un placeholder
            // En una implementación real, usarías FFmpeg para extraer un frame
            Path thumbnailPath = thumbnailsDir.resolve(thumbnailFileName);
            
            // Crear un archivo placeholder (en producción usar FFmpeg)
            Files.write(thumbnailPath, "thumbnail placeholder".getBytes());
            
            return "productos/videos/" + thumbnailFileName;
        } catch (IOException e) {
            // Si falla la generación del thumbnail, retornar null
            return null;
        }
    }
}
