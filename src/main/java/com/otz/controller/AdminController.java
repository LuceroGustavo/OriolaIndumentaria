package com.otz.controller;

import com.otz.entity.Product;
import com.otz.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        // Obtener estadísticas para el dashboard
        long totalProducts = productRepository.count();
        
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("username", authentication.getName());
        
        return "admin/dashboard";
    }
    
    @GetMapping("/help")
    public String help() {
        return "admin/help";
    }
}
