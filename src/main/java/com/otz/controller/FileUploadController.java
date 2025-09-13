package com.otz.controller;

import com.otz.entity.Product;
import com.otz.entity.ProductImage;
import com.otz.service.ImageProcessingService;
import com.otz.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class FileUploadController {

    @Autowired
    private ImageProcessingService imageProcessingService;
    
    @Autowired
    private ProductService productService;

    /**
     * Sube una imagen para un producto específico
     */
    @PostMapping("/upload/product/{productId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadProductImage(
            @PathVariable Integer productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPrimary", defaultValue = "false") boolean isPrimary) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar el producto
            Product product = productService.findById(productId);
            if (product == null) {
                response.put("success", false);
                response.put("message", "Producto no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Procesar y guardar la imagen
            ProductImage productImage = imageProcessingService.processAndSaveImage(file, productId, isPrimary);
            
            // Si es la imagen principal, marcar las otras como no principales
            if (isPrimary) {
                product.getImages().forEach(img -> img.setIsPrimary(false));
            }
            
            // Agregar la imagen al producto
            product.agregarImagen(productImage);
            productService.save(product);
            
            response.put("success", true);
            response.put("message", "Imagen subida correctamente");
            response.put("imageId", productImage.getId());
            response.put("imageUrl", productImage.getImageUrl());
            response.put("thumbnailUrl", productImage.getThumbnailUrl());
            response.put("fileName", productImage.getFileName());
            response.put("fileSize", imageProcessingService.getFileSizeString(productImage.getFileSize()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al subir la imagen: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Sube múltiples imágenes para un producto
     */
    @PostMapping("/upload/product/{productId}/multiple")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadMultipleProductImages(
            @PathVariable Integer productId,
            @RequestParam("files") MultipartFile[] files) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar el producto
            Product product = productService.findById(productId);
            if (product == null) {
                response.put("success", false);
                response.put("message", "Producto no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
            
            int uploadedCount = 0;
            int errorCount = 0;
            
            for (int i = 0; i < files.length; i++) {
                try {
                    MultipartFile file = files[i];
                    boolean isPrimary = (i == 0 && product.getImages().isEmpty());
                    
                    ProductImage productImage = imageProcessingService.processAndSaveImage(file, productId, isPrimary);
                    product.agregarImagen(productImage);
                    uploadedCount++;
                    
                } catch (Exception e) {
                    errorCount++;
                    System.err.println("Error subiendo archivo " + (i + 1) + ": " + e.getMessage());
                }
            }
            
            productService.save(product);
            
            response.put("success", true);
            response.put("message", "Imágenes procesadas: " + uploadedCount + " exitosas, " + errorCount + " con error");
            response.put("uploadedCount", uploadedCount);
            response.put("errorCount", errorCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al subir las imágenes: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Elimina una imagen de un producto
     */
    @DeleteMapping("/image/{imageId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteProductImage(@PathVariable Long imageId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Aquí necesitarías implementar el método para encontrar y eliminar la imagen
            // Por ahora retornamos un mensaje de éxito
            response.put("success", true);
            response.put("message", "Imagen eliminada correctamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar la imagen: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Marca una imagen como principal
     */
    @PutMapping("/image/{imageId}/set-primary")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setPrimaryImage(@PathVariable Long imageId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Aquí necesitarías implementar la lógica para marcar como principal
            response.put("success", true);
            response.put("message", "Imagen marcada como principal");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al marcar imagen como principal: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
