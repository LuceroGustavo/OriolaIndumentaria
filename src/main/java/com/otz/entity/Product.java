package com.otz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer pId;

    @NotBlank(message = "El nombre del producto es requerido")
    public String name;

    @NotBlank(message = "El talle es requerido")
    public String talle;

    @NotBlank(message = "Las medidas son requeridas")
    public String medidas;

    @NotBlank(message = "El color es requerido")
    public String color;

    @Positive(message = "El precio debe ser positivo")
    public Double price;

    @PositiveOrZero(message = "La cantidad debe ser cero o positiva")
    public Integer qty;

    public String descripcion;
    public String categoria;
    public String imagenUrl;
    public Boolean activo = true;
}
