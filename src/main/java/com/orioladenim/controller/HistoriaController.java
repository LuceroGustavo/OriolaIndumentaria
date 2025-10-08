package com.orioladenim.controller;

import com.orioladenim.entity.Historia;
import com.orioladenim.service.HistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

@Controller
@RequestMapping("/admin/historias")
public class HistoriaController {
    
    @Autowired
    private HistoriaService historiaService;
    
    /**
     * Lista todas las historias
     */
    @GetMapping
    public String listarHistorias(Model model) {
        model.addAttribute("historias", historiaService.findAll());
        model.addAttribute("estadisticas", historiaService.obtenerEstadisticas());
        return "admin/historias/listar";
    }
    
    /**
     * Muestra el formulario para crear una nueva historia
     */
    @GetMapping("/nueva")
    public String nuevaHistoria(Model model) {
        model.addAttribute("historia", new Historia());
        return "admin/historias/formulario";
    }
    
    /**
     * Guarda una nueva historia
     */
    @PostMapping("/guardar")
    public String guardarHistoria(@RequestParam("titulo") String titulo,
                                 @RequestParam("descripcion") String descripcion,
                                 @RequestParam("video") MultipartFile video,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (video.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar un video");
                return "redirect:/admin/historias/nueva";
            }
            
            historiaService.crearHistoria(titulo, descripcion, video);
            redirectAttributes.addFlashAttribute("success", "Historia creada exitosamente");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el video: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/historias";
    }
    
    /**
     * Muestra el formulario para editar una historia
     */
    @GetMapping("/editar/{id}")
    public String editarHistoria(@PathVariable Integer id, Model model) {
        Historia historia = historiaService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Historia no encontrada"));
        model.addAttribute("historia", historia);
        return "admin/historias/formulario-editar";
    }
    
    /**
     * Actualiza una historia existente
     */
    @PostMapping("/actualizar/{id}")
    public String actualizarHistoria(@PathVariable Integer id,
                                   @RequestParam("titulo") String titulo,
                                   @RequestParam("descripcion") String descripcion,
                                   @RequestParam(value = "video", required = false) MultipartFile video,
                                   RedirectAttributes redirectAttributes) {
        try {
            historiaService.actualizarHistoria(id, titulo, descripcion, video);
            redirectAttributes.addFlashAttribute("success", "Historia actualizada exitosamente");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el video: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/historias";
    }
    
    /**
     * Cambia el estado activo/inactivo de una historia
     */
    @PostMapping("/{id}/toggle")
    public String toggleActiva(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Historia historia = historiaService.toggleActiva(id);
            String estado = historia.getActiva() ? "activada" : "desactivada";
            redirectAttributes.addFlashAttribute("success", "Historia " + estado + " exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/historias";
    }
    
    /**
     * Elimina una historia
     */
    @PostMapping("/{id}/eliminar")
    public String eliminarHistoria(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            historiaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Historia eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la historia: " + e.getMessage());
        }
        return "redirect:/admin/historias";
    }
    
    /**
     * Muestra los detalles de una historia
     */
    @GetMapping("/{id}")
    public String verHistoria(@PathVariable Integer id, Model model) {
        Historia historia = historiaService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Historia no encontrada"));
        model.addAttribute("historia", historia);
        return "admin/historias/detalle";
    }
    
    /**
     * Busca historias
     */
    @GetMapping("/buscar")
    public String buscarHistorias(@RequestParam("q") String busqueda, Model model) {
        model.addAttribute("historias", historiaService.buscarHistorias(busqueda));
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("estadisticas", historiaService.obtenerEstadisticas());
        return "admin/historias/listar";
    }
}
