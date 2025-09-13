package com.otz.service;

import com.otz.entity.Product;
import com.otz.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
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
        productRepository.deleteById(id);
    }
    
    public List<Product> findByCategoria(String categoria) {
        return productRepository.findByCategoria(categoria);
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
