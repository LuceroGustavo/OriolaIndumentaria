package com.orioladenim.controller;

import com.orioladenim.entity.Product;
import com.orioladenim.entity.Category;
import com.orioladenim.repo.ProductRepository;
import com.orioladenim.service.CategoryService;
import com.orioladenim.service.HistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PublicController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private HistoriaService historiaService;
    
    @GetMapping("/")
    public String home(Model model) {
        try {
            // Mostrar solo productos de novedades (esNuevo = true y activo = true)
            model.addAttribute("products", productRepository.findByEsNuevoTrueAndActivoTrue());
            
            // Agregar categorías para el dropdown
            model.addAttribute("categories", categoryService.getActiveCategories());
            
            // Obtener historia principal (la más reciente y activa)
            model.addAttribute("historiaPrincipal", historiaService.findActivaPrincipal().orElse(null));
            
            return "index";
        } catch (Exception e) {
            // En caso de error, mostrar página sin categorías ni historia
            model.addAttribute("products", productRepository.findByEsNuevoTrueAndActivoTrue());
            model.addAttribute("categories", new java.util.ArrayList<>());
            model.addAttribute("historiaPrincipal", null);
            return "index";
        }
    }
    
    @GetMapping("/catalog")
    public String catalog(@RequestParam(required = false) String category, 
                         @RequestParam(required = false) String search, 
                         Model model) {
        // Obtener solo productos activos
        List<Product> products = productRepository.findByActivoTrue();
        System.out.println("🔍 Productos activos encontrados: " + products.size());
        
        // Filtrar por categoría si se especifica
        if (category != null && !category.trim().isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getCategories().stream()
                            .anyMatch(c -> c.getName().equalsIgnoreCase(category.trim())))
                    .collect(java.util.stream.Collectors.toList());
            System.out.println("🔍 Productos filtrados por categoría '" + category + "': " + products.size());
        }
        
        // Filtrar por búsqueda si se especifica
        if (search != null && !search.trim().isEmpty()) {
            String searchTerm = search.trim().toLowerCase();
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(searchTerm) ||
                               p.getCategories().stream().anyMatch(c -> c.getName().toLowerCase().contains(searchTerm)))
                    .collect(java.util.stream.Collectors.toList());
            System.out.println("🔍 Productos filtrados por búsqueda '" + search + "': " + products.size());
        }
        
        // Obtener solo categorías que tienen productos activos
        List<Category> categories = categoryService.getCategoriesWithProducts();
        System.out.println("🔍 Categorías con productos activos: " + categories.size());
        for (Category cat : categories) {
            System.out.println("  - " + cat.getName() + " (productos: " + cat.getProductCount() + ")");
        }
        
        // Fallback: si no hay categorías con productos, mostrar todas las categorías activas
        if (categories.isEmpty()) {
            System.out.println("⚠️ No hay categorías con productos activos, mostrando todas las categorías activas");
            categories = categoryService.getActiveCategories();
            System.out.println("🔍 Categorías activas totales: " + categories.size());
        }
        
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("search", search);
        
        return "catalog";
    }
    
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Integer id, Model model) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("product", product);
        return "product-detail";
    }
    
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
}

