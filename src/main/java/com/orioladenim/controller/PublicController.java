package com.orioladenim.controller;

import com.orioladenim.entity.Product;
import com.orioladenim.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PublicController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping("/")
    public String home(Model model) {
        // Obtener productos destacados para la página principal
        model.addAttribute("featuredProducts", productRepository.findAll());
        return "index";
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

