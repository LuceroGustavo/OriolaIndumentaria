package com.orioladenim.service;

import com.orioladenim.entity.Product;
import com.orioladenim.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ImageProcessingService imageProcessingService;
    
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }
    
    public Product save(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteById(Integer id) {
        // Buscar el producto antes de eliminarlo
        Product product = findById(id);
        if (product != null) {
            // Eliminar todas las imágenes del sistema de archivos
            product.getImages().forEach(image -> {
                imageProcessingService.deleteImage(image.getImagePath());
            });
            
            // Eliminar el producto (las imágenes se eliminan en cascada)
            productRepository.deleteById(id);
        }
    }
    
    
    public List<Product> findByActivoTrue() {
        return productRepository.findByActivoTrue();
    }
    
    public List<Product> findDestacados() {
        return productRepository.findByEsDestacadoTrueAndActivoTrue();
    }
    
    public List<Product> findNuevos() {
        return productRepository.findByEsNuevoTrueAndActivoTrue();
    }
}

