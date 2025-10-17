package com.orioladenim.controller;

import com.orioladenim.entity.Category;
import com.orioladenim.service.CategoryImageService;
import com.orioladenim.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/categories")
public class CategoryImageController {

    @Autowired
    private CategoryImageService categoryImageService;
    
    @Autowired
    private CategoryService categoryService;

    /**
     * Sube una imagen para una categoría específica
     */
    @PostMapping("/{categoryId}/upload-image")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadCategoryImage(
            @PathVariable Long categoryId,
            @RequestParam("file") MultipartFile file) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar la categoría
            Category category = categoryService.findById(categoryId);
            if (category == null) {
                response.put("success", false);
                response.put("message", "Categoría no encontrada");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Procesar y guardar la imagen
            String imagePath = categoryImageService.saveCategoryImage(file, categoryId);
            
            // Actualizar la categoría con la nueva imagen
            category.setImagePath(imagePath);
            categoryService.updateCategory(categoryId, category);
            
            response.put("success", true);
            response.put("message", "Imagen subida correctamente");
            response.put("imagePath", imagePath);
            response.put("categoryId", categoryId);
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al procesar la imagen: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Elimina la imagen de una categoría
     */
    @DeleteMapping("/{categoryId}/delete-image")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCategoryImage(@PathVariable Long categoryId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar la categoría
            Category category = categoryService.findById(categoryId);
            if (category == null) {
                response.put("success", false);
                response.put("message", "Categoría no encontrada");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Eliminar la imagen del sistema de archivos
            if (category.getImagePath() != null && !category.getImagePath().isEmpty()) {
                boolean deleted = categoryImageService.deleteCategoryImage(category.getImagePath());
                if (deleted) {
                    // Limpiar la referencia en la categoría
                    category.setImagePath(null);
                    categoryService.updateCategory(categoryId, category);
                    
                    response.put("success", true);
                    response.put("message", "Imagen eliminada correctamente");
                } else {
                    response.put("success", false);
                    response.put("message", "Error al eliminar la imagen del sistema de archivos");
                    return ResponseEntity.internalServerError().body(response);
                }
            } else {
                response.put("success", false);
                response.put("message", "La categoría no tiene imagen asociada");
                return ResponseEntity.badRequest().body(response);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar la imagen: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Actualiza el estado de mostrar en carrusel
     */
    @PostMapping("/{categoryId}/toggle-carousel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleCarouselStatus(@PathVariable Long categoryId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar la categoría
            Category category = categoryService.findById(categoryId);
            if (category == null) {
                response.put("success", false);
                response.put("message", "Categoría no encontrada");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Verificar si tiene imagen
            if (category.getImagePath() == null || category.getImagePath().isEmpty()) {
                response.put("success", false);
                response.put("message", "La categoría debe tener una imagen para aparecer en el carrusel");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Cambiar el estado
            boolean newStatus = !Boolean.TRUE.equals(category.getShowInCarousel());
            category.setShowInCarousel(newStatus);
            
            // Si se está activando, asignar un orden si no tiene
            if (newStatus && category.getCarouselOrder() == null) {
                category.setCarouselOrder(0);
            }
            
            categoryService.updateCategory(categoryId, category);
            
            response.put("success", true);
            response.put("message", newStatus ? "Categoría agregada al carrusel" : "Categoría removida del carrusel");
            response.put("showInCarousel", newStatus);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar el estado del carrusel: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Actualiza el orden del carrusel
     */
    @PostMapping("/{categoryId}/update-carousel-order")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCarouselOrder(
            @PathVariable Long categoryId,
            @RequestParam("order") Integer order) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar la categoría
            Category category = categoryService.findById(categoryId);
            if (category == null) {
                response.put("success", false);
                response.put("message", "Categoría no encontrada");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Actualizar el orden
            category.setCarouselOrder(order);
            categoryService.updateCategory(categoryId, category);
            
            response.put("success", true);
            response.put("message", "Orden del carrusel actualizado correctamente");
            response.put("carouselOrder", order);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar el orden del carrusel: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Obtiene el siguiente orden disponible para el carrusel
     */
    @GetMapping("/next-carousel-order")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getNextCarouselOrder() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Integer nextOrder = categoryService.getNextCarouselOrder();
            response.put("success", true);
            response.put("nextOrder", nextOrder);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener el siguiente orden: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
