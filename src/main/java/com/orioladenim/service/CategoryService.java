package com.orioladenim.service;

import com.orioladenim.entity.Category;
import com.orioladenim.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de categorías
 * Contiene la lógica de negocio para operaciones de categorías
 */
@Service
@Transactional
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * Obtener todas las categorías activas ordenadas
     */
    @Transactional(readOnly = true)
    public List<Category> getActiveCategories() {
        return categoryRepository.findActiveCategoriesOrdered();
    }
    
    /**
     * Obtener todas las categorías (activas e inactivas)
     */
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    /**
     * Obtener categorías paginadas
     */
    @Transactional(readOnly = true)
    public Page<Category> getCategoriesPaginated(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    
    /**
     * Buscar categoría por ID
     */
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    /**
     * Buscar categoría por nombre
     */
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name);
    }
    
    /**
     * Crear nueva categoría
     */
    public Category createCategory(Category category) {
        // Validar que el nombre no exista
        if (categoryRepository.findByNameIgnoreCase(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + category.getName());
        }
        
        // Asignar orden de visualización si no se especifica
        if (category.getDisplayOrder() == null) {
            category.setDisplayOrder(categoryRepository.getNextDisplayOrder());
        }
        
        // Inicializar contador de productos
        if (category.getProductCount() == null) {
            category.setProductCount(0);
        }
        
        return categoryRepository.save(category);
    }
    
    /**
     * Actualizar categoría existente
     */
    public Category updateCategory(Long id, Category categoryData) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
        
        // Validar que el nuevo nombre no exista en otra categoría
        if (!existingCategory.getName().equalsIgnoreCase(categoryData.getName()) &&
            categoryRepository.existsByNameIgnoreCaseAndIdNot(categoryData.getName(), id)) {
            throw new IllegalArgumentException("Ya existe otra categoría con el nombre: " + categoryData.getName());
        }
        
        // Actualizar campos
        existingCategory.setName(categoryData.getName());
        existingCategory.setDescription(categoryData.getDescription());
        existingCategory.setColorCode(categoryData.getColorCode());
        existingCategory.setIconName(categoryData.getIconName());
        existingCategory.setIsActive(categoryData.getIsActive());
        existingCategory.setDisplayOrder(categoryData.getDisplayOrder());
        
        return categoryRepository.save(existingCategory);
    }
    
    /**
     * Eliminar categoría (soft delete)
     */
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
        
        // Verificar si tiene productos
        if (category.hasProducts()) {
            throw new IllegalStateException("No se puede eliminar la categoría porque tiene productos asociados");
        }
        
        // Soft delete
        category.setIsActive(false);
        categoryRepository.save(category);
    }
    
    /**
     * Eliminar categoría permanentemente
     */
    public void deleteCategoryPermanently(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
        
        // Verificar si tiene productos
        if (category.hasProducts()) {
            throw new IllegalStateException("No se puede eliminar la categoría porque tiene productos asociados");
        }
        
        categoryRepository.delete(category);
    }
    
    /**
     * Activar/Desactivar categoría
     */
    public Category toggleCategoryStatus(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
        
        category.setIsActive(!category.getIsActive());
        return categoryRepository.save(category);
    }
    
    /**
     * Buscar categorías por texto
     */
    @Transactional(readOnly = true)
    public List<Category> searchCategories(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return getActiveCategories();
        }
        return categoryRepository.searchActiveCategories(searchText.trim());
    }
    
    /**
     * Obtener categorías con productos
     */
    @Transactional(readOnly = true)
    public List<Category> getCategoriesWithProducts() {
        return categoryRepository.findCategoriesWithActiveProducts();
    }
    
    /**
     * Obtener categorías vacías
     */
    @Transactional(readOnly = true)
    public List<Category> getEmptyCategories() {
        return categoryRepository.findEmptyCategories();
    }
    
    /**
     * Actualizar orden de visualización
     */
    public void updateDisplayOrder(Long categoryId, Integer newOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + categoryId));
        
        category.setDisplayOrder(newOrder);
        categoryRepository.save(category);
    }
    
    /**
     * Reordenar categorías
     */
    public void reorderCategories(List<Long> categoryIds) {
        for (int i = 0; i < categoryIds.size(); i++) {
            updateDisplayOrder(categoryIds.get(i), i + 1);
        }
    }
    
    /**
     * Actualizar contador de productos de una categoría
     */
    public void updateProductCount(Long categoryId) {
        categoryRepository.updateProductCount(categoryId);
    }
    
    /**
     * Obtener estadísticas de categorías
     */
    @Transactional(readOnly = true)
    public Object[] getCategoryStatistics() {
        return categoryRepository.getCategoryStatistics();
    }
    
    /**
     * Crear categorías por defecto
     */
    public void createDefaultCategories() {
        // Verificar si ya existen categorías
        if (categoryRepository.count() > 0) {
            return;
        }
        
        // Crear categorías por defecto
        Category[] defaultCategories = {
            new Category("Remeras", "Camisetas y remeras de algodón", "#ff6b6b", "shirt"),
            new Category("Buzos", "Buzos y sweaters de invierno", "#4ecdc4", "hoodie"),
            new Category("Camisas", "Camisas formales y casuales", "#45b7d1", "shirt"),
            new Category("Pantalones", "Jeans y pantalones de vestir", "#96ceb4", "pants"),
            new Category("Accesorios", "Cinturones, gorras y accesorios", "#feca57", "tag")
        };
        
        for (int i = 0; i < defaultCategories.length; i++) {
            defaultCategories[i].setDisplayOrder(i + 1);
            categoryRepository.save(defaultCategories[i]);
        }
    }
    
    /**
     * Validar datos de categoría
     */
    public void validateCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        
        if (category.getName().length() < 2 || category.getName().length() > 50) {
            throw new IllegalArgumentException("El nombre debe tener entre 2 y 50 caracteres");
        }
        
        if (category.getDescription() != null && category.getDescription().length() > 200) {
            throw new IllegalArgumentException("La descripción no puede exceder 200 caracteres");
        }
        
        if (category.getColorCode() != null && !category.getColorCode().matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("El código de color debe ser un hexadecimal válido (ej: #ff6b6b)");
        }
    }
}
