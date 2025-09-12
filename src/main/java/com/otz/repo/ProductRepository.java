package com.otz.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otz.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}
