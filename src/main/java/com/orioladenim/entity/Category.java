package com.orioladenim.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Category para gestionar categorías dinámicas de productos
 * Permite al administrador crear, editar y gestionar categorías personalizadas
 */
@Entity
@Table(name = "categories")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;
    
    @Size(max = 200, message = "La descripción no puede exceder 200 caracteres")
    @Column(name = "description", length = 200)
    private String description;
    
    @Size(max = 7, message = "El código de color debe tener máximo 7 caracteres")
    @Column(name = "color_code", length = 7)
    private String colorCode; // Código hexadecimal del color (#ff6b6b)
    
    @Size(max = 50, message = "El nombre del icono no puede exceder 50 caracteres")
    @Column(name = "icon_name", length = 50)
    private String iconName; // Nombre del icono (ej: "shirt", "dress", "pants")
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0; // Orden de visualización en el catálogo
    
    @Column(name = "product_count")
    private Integer productCount = 0; // Contador de productos en esta categoría
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Relación con Product (One-to-Many)
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
    
    // Constructores
    public Category() {}
    
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.isActive = true;
        this.displayOrder = 0;
        this.productCount = 0;
    }
    
    public Category(String name, String description, String colorCode, String iconName) {
        this.name = name;
        this.description = description;
        this.colorCode = colorCode;
        this.iconName = iconName;
        this.isActive = true;
        this.displayOrder = 0;
        this.productCount = 0;
    }
    
    // Métodos de utilidad
    public void incrementProductCount() {
        this.productCount = (this.productCount == null) ? 1 : this.productCount + 1;
    }
    
    public void decrementProductCount() {
        if (this.productCount != null && this.productCount > 0) {
            this.productCount--;
        }
    }
    
    public boolean hasProducts() {
        return this.productCount != null && this.productCount > 0;
    }
    
    public String getDisplayName() {
        return this.name != null ? this.name : "Sin categoría";
    }
    
    public String getColorCodeOrDefault() {
        return this.colorCode != null ? this.colorCode : "#6c757d";
    }
    
    public String getIconNameOrDefault() {
        return this.iconName != null ? this.iconName : "tag";
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getColorCode() {
        return colorCode;
    }
    
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
    
    public String getIconName() {
        return iconName;
    }
    
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public Integer getProductCount() {
        return productCount;
    }
    
    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", iconName='" + iconName + '\'' +
                ", isActive=" + isActive +
                ", displayOrder=" + displayOrder +
                ", productCount=" + productCount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id != null && id.equals(category.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
