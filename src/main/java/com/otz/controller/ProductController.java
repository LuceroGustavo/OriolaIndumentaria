package com.otz.controller;

import com.otz.entity.Product;
import com.otz.repo.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/product-list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }

    @PostMapping("/save")
    public String addProduct(@Valid Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "admin/product-form";
        }
        product.setFechaCreacion(java.time.LocalDateTime.now());
        product.setFechaActualizacion(java.time.LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        return "redirect:/admin/products/" + savedProduct.getPId() + "/images";
    }

    @GetMapping("/edit/{pId}")
    public String editProduct(@PathVariable Integer pId, Model model) {
        Product product = productRepository.findById(pId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("product", product);
        return "admin/product-form";
    }

    @PostMapping("/edit/{pId}")
    public String updateProduct(@PathVariable Integer pId, @Valid Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "admin/product-form";
        }
        product.setPId(pId);
        product.setFechaActualizacion(java.time.LocalDateTime.now());
        productRepository.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{pId}")
    public String deleteProduct(@PathVariable Integer pId) {
        productRepository.deleteById(pId);
        return "redirect:/admin/products";
    }

    @GetMapping("/{pId}/images")
    public String manageImages(@PathVariable Integer pId, Model model) {
        Product product = productRepository.findById(pId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("product", product);
        return "admin/product-images";
    }
}

