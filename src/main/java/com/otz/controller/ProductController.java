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
    public String showForm(Product product) {
        return "admin/product-form";
    }

    @PostMapping
    public String addProduct(@Valid Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/product-form";
        }
        productRepository.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{pId}")
    public String editProduct(@PathVariable Integer pId, Model model) {
        Product product = productRepository.findById(pId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("product", product);
        return "admin/product-form";
    }

    @GetMapping("/delete/{pId}")
    public String deleteProduct(@PathVariable Integer pId) {
        productRepository.deleteById(pId);
        return "redirect:/admin/products";
    }
}

