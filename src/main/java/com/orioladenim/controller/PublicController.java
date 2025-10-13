package com.orioladenim.controller;

import com.orioladenim.entity.Product;
import com.orioladenim.repo.ProductRepository;
import com.orioladenim.service.CategoryService;
import com.orioladenim.service.HistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public String catalog(Model model) {
        model.addAttribute("products", productRepository.findAll());
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

