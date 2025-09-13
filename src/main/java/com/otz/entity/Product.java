package com.otz.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(exclude = "images")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pId;

    @NotBlank(message = "El nombre del producto es requerido")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "El talle es requerido")
    @Column(name = "talle", nullable = false)
    private String talle;

    @NotBlank(message = "Las medidas son requeridas")
    @Column(name = "medidas", nullable = false)
    private String medidas;

    @NotBlank(message = "El color es requerido")
    @Column(name = "color", nullable = false)
    private String color;

    @Positive(message = "El precio debe ser positivo")
    @Column(name = "price", nullable = false)
    private Double price;

    @PositiveOrZero(message = "La cantidad debe ser cero o positiva")
    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotBlank(message = "La categoría es requerida")
    @Column(name = "categoria", nullable = false)
    private String categoria;

    // Campos adicionales para indumentaria
    @Column(name = "material")
    private String material;

    @Column(name = "cuidados", columnDefinition = "TEXT")
    private String cuidados;

    @Column(name = "temporada")
    private String temporada; // Verano, Invierno, Todo el año

    @Column(name = "genero")
    private String genero; // Hombre, Mujer, Unisex, Niño, Niña

    @Column(name = "edad_recomendada")
    private String edadRecomendada;

    @Column(name = "tallas_disponibles")
    private String tallasDisponibles; // S,M,L,XL o 38,40,42,44

    @Column(name = "colores_disponibles")
    private String coloresDisponibles; // Rojo,Azul,Verde

    @Column(name = "es_destacado")
    private Boolean esDestacado = false;

    @Column(name = "es_nuevo")
    private Boolean esNuevo = false;

    @Column(name = "descuento_porcentaje")
    private Double descuentoPorcentaje = 0.0;

    @Column(name = "precio_original")
    private Double precioOriginal;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    // Relación con imágenes
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    // Método para obtener la imagen principal
    public ProductImage getImagenPrincipal() {
        return images.stream()
                .filter(img -> img.getIsPrimary())
                .findFirst()
                .orElse(images.isEmpty() ? null : images.get(0));
    }

    // Método para obtener la URL de la imagen principal
    public String getImagenPrincipalUrl() {
        ProductImage imagenPrincipal = getImagenPrincipal();
        return imagenPrincipal != null ? imagenPrincipal.getImageUrl() : "/images/no-image.png";
    }

    // Método para obtener el precio con descuento
    public Double getPrecioFinal() {
        if (descuentoPorcentaje > 0) {
            return price * (1 - descuentoPorcentaje / 100);
        }
        return price;
    }

    // Método para verificar si tiene descuento
    public Boolean tieneDescuento() {
        return descuentoPorcentaje > 0;
    }

    // Método para agregar imagen
    public void agregarImagen(ProductImage imagen) {
        images.add(imagen);
        imagen.setProduct(this);
    }

    // Método para remover imagen
    public void removerImagen(ProductImage imagen) {
        images.remove(imagen);
        imagen.setProduct(null);
    }

    // Getters y Setters manuales (por si Lombok no funciona)
    public Integer getPId() { return pId; }
    public void setPId(Integer pId) { this.pId = pId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getTalle() { return talle; }
    public void setTalle(String talle) { this.talle = talle; }
    
    public String getMedidas() { return medidas; }
    public void setMedidas(String medidas) { this.medidas = medidas; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    
    public String getCuidados() { return cuidados; }
    public void setCuidados(String cuidados) { this.cuidados = cuidados; }
    
    public String getTemporada() { return temporada; }
    public void setTemporada(String temporada) { this.temporada = temporada; }
    
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    
    public String getEdadRecomendada() { return edadRecomendada; }
    public void setEdadRecomendada(String edadRecomendada) { this.edadRecomendada = edadRecomendada; }
    
    public String getTallasDisponibles() { return tallasDisponibles; }
    public void setTallasDisponibles(String tallasDisponibles) { this.tallasDisponibles = tallasDisponibles; }
    
    public String getColoresDisponibles() { return coloresDisponibles; }
    public void setColoresDisponibles(String coloresDisponibles) { this.coloresDisponibles = coloresDisponibles; }
    
    public Boolean getEsDestacado() { return esDestacado; }
    public void setEsDestacado(Boolean esDestacado) { this.esDestacado = esDestacado; }
    
    public Boolean getEsNuevo() { return esNuevo; }
    public void setEsNuevo(Boolean esNuevo) { this.esNuevo = esNuevo; }
    
    public Double getDescuentoPorcentaje() { return descuentoPorcentaje; }
    public void setDescuentoPorcentaje(Double descuentoPorcentaje) { this.descuentoPorcentaje = descuentoPorcentaje; }
    
    public Double getPrecioOriginal() { return precioOriginal; }
    public void setPrecioOriginal(Double precioOriginal) { this.precioOriginal = precioOriginal; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public List<ProductImage> getImages() { return images; }
    public void setImages(List<ProductImage> images) { this.images = images; }
}
