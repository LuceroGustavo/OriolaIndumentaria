package com.orioladenim.service;

import com.orioladenim.entity.Color;
import com.orioladenim.repo.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de colores
 * Contiene la lógica de negocio para operaciones de colores
 */
@Service
@Transactional
public class ColorService {
    
    @Autowired
    private ColorRepository colorRepository;
    
    /**
     * Obtener todos los colores activos ordenados
     */
    @Transactional(readOnly = true)
    public List<Color> getActiveColors() {
        return colorRepository.findActiveColorsOrdered();
    }
    
    /**
     * Obtener todos los colores (activos e inactivos)
     */
    @Transactional(readOnly = true)
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }
    
    /**
     * Obtener colores paginados
     */
    @Transactional(readOnly = true)
    public Page<Color> getColorsPaginated(Pageable pageable) {
        return colorRepository.findAll(pageable);
    }
    
    /**
     * Buscar color por ID
     */
    @Transactional(readOnly = true)
    public Optional<Color> getColorById(Long id) {
        return colorRepository.findById(id);
    }
    
    /**
     * Buscar color por nombre
     */
    @Transactional(readOnly = true)
    public Optional<Color> getColorByName(String name) {
        return colorRepository.findByNameIgnoreCase(name);
    }
    
    /**
     * Buscar color por código hexadecimal
     */
    @Transactional(readOnly = true)
    public Optional<Color> getColorByHexCode(String hexCode) {
        return colorRepository.findByHexCodeIgnoreCase(hexCode);
    }
    
    /**
     * Crear nuevo color
     */
    public Color createColor(Color color) {
        // Validar datos
        validateColor(color);
        
        // Validar que el nombre no exista
        if (colorRepository.findByNameIgnoreCase(color.getName()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un color con el nombre: " + color.getName());
        }
        
        // Validar que el código hexadecimal no exista
        if (color.getHexCode() != null && 
            colorRepository.findByHexCodeIgnoreCase(color.getHexCode()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un color con el código hexadecimal: " + color.getHexCode());
        }
        
        // Asignar orden de visualización si no se especifica
        if (color.getDisplayOrder() == null) {
            color.setDisplayOrder(colorRepository.getNextDisplayOrder());
        }
        
        // Inicializar contador de productos
        if (color.getProductCount() == null) {
            color.setProductCount(0);
        }
        
        return colorRepository.save(color);
    }
    
    /**
     * Actualizar color existente
     */
    public Color updateColor(Long id, Color colorData) {
        Color existingColor = colorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Color no encontrado con ID: " + id));
        
        // Validar datos
        validateColor(colorData);
        
        // Validar que el nuevo nombre no exista en otro color
        if (!existingColor.getName().equalsIgnoreCase(colorData.getName()) &&
            colorRepository.existsByNameIgnoreCaseAndIdNot(colorData.getName(), id)) {
            throw new IllegalArgumentException("Ya existe otro color con el nombre: " + colorData.getName());
        }
        
        // Validar que el nuevo código hexadecimal no exista en otro color
        if (colorData.getHexCode() != null && 
            !colorData.getHexCode().equalsIgnoreCase(existingColor.getHexCode()) &&
            colorRepository.existsByHexCodeIgnoreCaseAndIdNot(colorData.getHexCode(), id)) {
            throw new IllegalArgumentException("Ya existe otro color con el código hexadecimal: " + colorData.getHexCode());
        }
        
        // Actualizar campos
        existingColor.setName(colorData.getName());
        existingColor.setDescription(colorData.getDescription());
        existingColor.setHexCode(colorData.getHexCode());
        existingColor.setIsActive(colorData.getIsActive());
        existingColor.setDisplayOrder(colorData.getDisplayOrder());
        
        return colorRepository.save(existingColor);
    }
    
    /**
     * Eliminar color (soft delete)
     */
    public void deleteColor(Long id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Color no encontrado con ID: " + id));
        
        // Verificar si tiene productos
        if (color.hasProducts()) {
            throw new IllegalStateException("No se puede eliminar el color porque tiene productos asociados");
        }
        
        // Soft delete
        color.setIsActive(false);
        colorRepository.save(color);
    }
    
    /**
     * Eliminar color permanentemente
     */
    public void deleteColorPermanently(Long id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Color no encontrado con ID: " + id));
        
        // Verificar si tiene productos
        if (color.hasProducts()) {
            throw new IllegalStateException("No se puede eliminar el color porque tiene productos asociados");
        }
        
        colorRepository.delete(color);
    }
    
    /**
     * Activar/Desactivar color
     */
    public Color toggleColorStatus(Long id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Color no encontrado con ID: " + id));
        
        color.setIsActive(!color.getIsActive());
        return colorRepository.save(color);
    }
    
    /**
     * Buscar colores por texto
     */
    @Transactional(readOnly = true)
    public List<Color> searchColors(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return getActiveColors();
        }
        return colorRepository.searchActiveColors(searchText.trim());
    }
    
    /**
     * Obtener colores con productos
     */
    @Transactional(readOnly = true)
    public List<Color> getUsedColors() {
        return colorRepository.findUsedColors();
    }
    
    /**
     * Obtener colores sin productos
     */
    @Transactional(readOnly = true)
    public List<Color> getUnusedColors() {
        return colorRepository.findUnusedColors();
    }
    
    /**
     * Obtener colores más utilizados
     */
    @Transactional(readOnly = true)
    public List<Color> getMostUsedColors() {
        return colorRepository.findMostUsedColors();
    }
    
    /**
     * Obtener colores menos utilizados
     */
    @Transactional(readOnly = true)
    public List<Color> getLeastUsedColors() {
        return colorRepository.findLeastUsedColors();
    }
    
    /**
     * Reordenar colores
     */
    public void reorderColors(List<Long> colorIds) {
        for (int i = 0; i < colorIds.size(); i++) {
            Long colorId = colorIds.get(i);
            Color color = colorRepository.findById(colorId)
                    .orElseThrow(() -> new IllegalArgumentException("Color no encontrado con ID: " + colorId));
            color.setDisplayOrder(i + 1);
            colorRepository.save(color);
        }
    }
    
    /**
     * Crear colores por defecto
     */
    public void createDefaultColors() {
        // Verificar si ya existen colores
        if (colorRepository.count() > 0) {
            return;
        }
        
        // Crear colores por defecto
        Color[] defaultColors = {
            new Color("Blanco", "Color blanco clásico", "#FFFFFF"),
            new Color("Negro", "Color negro elegante", "#000000"),
            new Color("Azul", "Color azul marino", "#000080"),
            new Color("Rojo", "Color rojo vibrante", "#FF0000"),
            new Color("Verde", "Color verde natural", "#008000"),
            new Color("Gris", "Color gris neutro", "#808080"),
            new Color("Rosa", "Color rosa suave", "#FFC0CB"),
            new Color("Amarillo", "Color amarillo brillante", "#FFFF00"),
            new Color("Marrón", "Color marrón tierra", "#A52A2A"),
            new Color("Violeta", "Color violeta elegante", "#800080")
        };
        
        for (int i = 0; i < defaultColors.length; i++) {
            defaultColors[i].setDisplayOrder(i + 1);
            colorRepository.save(defaultColors[i]);
        }
    }
    
    /**
     * Validar datos de color
     */
    public void validateColor(Color color) {
        if (color.getName() == null || color.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del color es obligatorio");
        }
        
        if (color.getName().length() < 2 || color.getName().length() > 50) {
            throw new IllegalArgumentException("El nombre debe tener entre 2 y 50 caracteres");
        }
        
        if (color.getDescription() != null && color.getDescription().length() > 200) {
            throw new IllegalArgumentException("La descripción no puede exceder 200 caracteres");
        }
        
        if (color.getHexCode() != null && !color.getHexCode().matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("El código hexadecimal debe tener formato #RRGGBB");
        }
    }
    
    /**
     * Obtener el número total de colores
     */
    @Transactional(readOnly = true)
    public long getColorCount() {
        return colorRepository.count();
    }
}
