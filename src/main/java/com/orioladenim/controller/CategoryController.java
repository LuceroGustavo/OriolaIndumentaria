package com.orioladenim.controller;

import com.orioladenim.entity.Category;
import com.orioladenim.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para gestión de categorías en el panel de administración
 */
@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * Listar todas las categorías
     */
    @GetMapping
    public String listCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {
        
        // Configurar paginación y ordenamiento
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Category> categories;
        if (search != null && !search.trim().isEmpty()) {
            // Buscar categorías
            List<Category> searchResults = categoryService.searchCategories(search);
            // Crear una página con los resultados de búsqueda
            categories = new org.springframework.data.domain.PageImpl<>(searchResults, pageable, searchResults.size());
            model.addAttribute("searchResults", searchResults);
        } else {
            // Obtener todas las categorías paginadas
            categories = categoryService.getCategoriesPaginated(pageable);
        }
        
        model.addAttribute("title", "Gestión de Categorías");
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("totalItems", categories.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        
        return "admin/categories/list";
    }
    
    /**
     * Mostrar formulario para crear nueva categoría
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/form";
    }
    
    /**
     * Mostrar formulario para editar categoría
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "admin/categories/form";
        } else {
            return "redirect:/admin/categories?error=category_not_found";
        }
    }
    
    /**
     * Crear nueva categoría
     */
    @PostMapping("/create")
    public String createCategory(
            @Valid @ModelAttribute("category") Category category,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "admin/categories/form";
        }
        
        try {
            categoryService.createCategory(category);
            redirectAttributes.addFlashAttribute("success", "Categoría creada exitosamente");
            return "redirect:/admin/categories";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.category", e.getMessage());
            return "admin/categories/form";
        }
    }
    
    /**
     * Actualizar categoría existente
     */
    @PostMapping("/update/{id}")
    public String updateCategory(
            @PathVariable Long id,
            @Valid @ModelAttribute("category") Category category,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "admin/categories/form";
        }
        
        try {
            categoryService.updateCategory(id, category);
            redirectAttributes.addFlashAttribute("success", "Categoría actualizada exitosamente");
            return "redirect:/admin/categories";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.category", e.getMessage());
            return "admin/categories/form";
        }
    }
    
    /**
     * Eliminar categoría (soft delete)
     */
    @PostMapping("/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Categoría eliminada exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/categories";
    }
    
    /**
     * Eliminar categoría permanentemente
     */
    @PostMapping("/delete-permanent/{id}")
    public String deleteCategoryPermanently(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            categoryService.deleteCategoryPermanently(id);
            redirectAttributes.addFlashAttribute("success", "Categoría eliminada permanentemente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/categories";
    }
    
    /**
     * Activar/Desactivar categoría
     */
    @PostMapping("/toggle-status/{id}")
    public String toggleCategoryStatus(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            Category category = categoryService.toggleCategoryStatus(id);
            String status = category.getIsActive() ? "activada" : "desactivada";
            redirectAttributes.addFlashAttribute("success", "Categoría " + status + " exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/categories";
    }
    
    /**
     * Ver detalles de categoría
     */
    @GetMapping("/view/{id}")
    public String viewCategory(@PathVariable Long id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "admin/categories/view";
        } else {
            return "redirect:/admin/categories?error=category_not_found";
        }
    }
    
    /**
     * Reordenar categorías
     */
    @PostMapping("/reorder")
    public String reorderCategories(
            @RequestParam("categoryIds") List<Long> categoryIds,
            RedirectAttributes redirectAttributes) {
        
        try {
            categoryService.reorderCategories(categoryIds);
            redirectAttributes.addFlashAttribute("success", "Orden de categorías actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al reordenar categorías: " + e.getMessage());
        }
        
        return "redirect:/admin/categories";
    }
    
    /**
     * Crear categorías por defecto
     */
    @PostMapping("/create-defaults")
    public String createDefaultCategories(RedirectAttributes redirectAttributes) {
        try {
            categoryService.createDefaultCategories();
            redirectAttributes.addFlashAttribute("success", "Categorías por defecto creadas exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear categorías por defecto: " + e.getMessage());
        }
        
        return "redirect:/admin/categories";
    }
    
    /**
     * API REST - Obtener categorías activas (para AJAX)
     */
    @GetMapping("/api/active")
    @ResponseBody
    public List<Category> getActiveCategories() {
        return categoryService.getActiveCategories();
    }
    
    /**
     * API REST - Buscar categorías (para AJAX)
     */
    @GetMapping("/api/search")
    @ResponseBody
    public List<Category> searchCategories(@RequestParam String search) {
        return categoryService.searchCategories(search);
    }
}
