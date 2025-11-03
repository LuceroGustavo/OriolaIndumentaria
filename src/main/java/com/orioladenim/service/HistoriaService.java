package com.orioladenim.service;

import com.orioladenim.entity.Historia;
import com.orioladenim.repo.HistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class HistoriaService {
    
    @Autowired
    private HistoriaRepository historiaRepository;
    
    @Autowired
    private VideoProcessingService videoProcessingService;
    
    /**
     * Obtiene todas las historias
     */
    public List<Historia> findAll() {
        return historiaRepository.findAll();
    }
    
    /**
     * Obtiene todas las historias activas
     */
    public List<Historia> findActivas() {
        return historiaRepository.findByActivaTrueOrderByFechaCreacionDesc();
    }
    
    /**
     * Obtiene la historia activa principal (para mostrar en el index)
     */
    public Optional<Historia> findActivaPrincipal() {
        return historiaRepository.findFirstByActivaTrueOrderByFechaCreacionDesc();
    }
    
    /**
     * Obtiene una historia por ID
     */
    public Optional<Historia> findById(Integer id) {
        return historiaRepository.findById(id);
    }
    
    /**
     * Guarda una historia
     */
    public Historia save(Historia historia) {
        return historiaRepository.save(historia);
    }
    
    /**
     * Crea una nueva historia con video
     * Si es la única historia, se crea activa por defecto.
     * Si ya existen historias activas, se crea inactiva.
     */
    public Historia crearHistoria(String titulo, String descripcion, MultipartFile video) throws IOException {
        // Procesar video
        String videoPath = videoProcessingService.procesarVideo(video);
        
        // Generar thumbnail
        String thumbnailPath = videoProcessingService.generarThumbnail(videoPath);
        
        // Obtener información del video
        VideoProcessingService.VideoInfo videoInfo = videoProcessingService.obtenerInformacionVideo(video);
        
        // Verificar si hay historias activas
        Long historiasActivas = countActivas();
        
        // Crear entidad
        Historia historia = new Historia();
        historia.setTitulo(titulo);
        historia.setDescripcion(descripcion);
        historia.setVideoPath(videoPath);
        historia.setVideoThumbnail(thumbnailPath);
        historia.setPesoArchivo(video.getSize());
        historia.setDuracionSegundos(videoInfo.getDuracion());
        // Si es la única historia (contando esta nueva), se crea activa. Si hay otras activas, se crea inactiva.
        historia.setActiva(historiasActivas == 0);
        
        return historiaRepository.save(historia);
    }
    
    /**
     * Actualiza una historia existente
     */
    public Historia actualizarHistoria(Integer id, String titulo, String descripcion, MultipartFile video) throws IOException {
        Optional<Historia> historiaOpt = historiaRepository.findById(id);
        if (historiaOpt.isEmpty()) {
            throw new IllegalArgumentException("Historia no encontrada");
        }
        
        Historia historia = historiaOpt.get();
        historia.setTitulo(titulo);
        historia.setDescripcion(descripcion);
        
        // Si se proporciona un nuevo video, procesarlo
        if (video != null && !video.isEmpty()) {
            // Eliminar video anterior
            videoProcessingService.eliminarVideo(historia.getVideoPath());
            
            // Procesar nuevo video
            String videoPath = videoProcessingService.procesarVideo(video);
            String thumbnailPath = videoProcessingService.generarThumbnail(videoPath);
            VideoProcessingService.VideoInfo videoInfo = videoProcessingService.obtenerInformacionVideo(video);
            
            historia.setVideoPath(videoPath);
            historia.setVideoThumbnail(thumbnailPath);
            historia.setPesoArchivo(video.getSize());
            historia.setDuracionSegundos(videoInfo.getDuracion());
        }
        
        return historiaRepository.save(historia);
    }
    
    /**
     * Cambia el estado activo/inactivo de una historia
     * Si se activa una historia, desactiva automáticamente todas las demás
     */
    public Historia toggleActiva(Integer id) {
        Optional<Historia> historiaOpt = historiaRepository.findById(id);
        if (historiaOpt.isEmpty()) {
            throw new IllegalArgumentException("Historia no encontrada");
        }
        
        Historia historia = historiaOpt.get();
        boolean nuevoEstado = !historia.getActiva();
        
        // Si se está activando la historia, desactivar todas las demás
        if (nuevoEstado) {
            List<Historia> historiasActivas = historiaRepository.findByActivaTrueOrderByFechaCreacionDesc();
            for (Historia h : historiasActivas) {
                if (!h.getId().equals(id)) {
                    h.setActiva(false);
                    historiaRepository.save(h);
                }
            }
        }
        
        historia.setActiva(nuevoEstado);
        return historiaRepository.save(historia);
    }
    
    /**
     * Elimina una historia y sus archivos asociados (video y thumbnail)
     */
    public void deleteById(Integer id) {
        Optional<Historia> historiaOpt = historiaRepository.findById(id);
        if (historiaOpt.isPresent()) {
            Historia historia = historiaOpt.get();
            
            // Eliminar video del sistema de archivos
            if (historia.getVideoPath() != null && !historia.getVideoPath().isEmpty()) {
                System.out.println("🖼️ Eliminando video de historia: " + historia.getVideoPath());
                videoProcessingService.eliminarVideo(historia.getVideoPath());
            }
            
            // Eliminar thumbnail del sistema de archivos
            if (historia.getVideoThumbnail() != null && !historia.getVideoThumbnail().isEmpty()) {
                System.out.println("🖼️ Eliminando thumbnail de historia: " + historia.getVideoThumbnail());
                videoProcessingService.eliminarThumbnail(historia.getVideoThumbnail());
            }
            
            // Eliminar de la base de datos
            historiaRepository.deleteById(id);
            System.out.println("✅ Historia eliminada correctamente (ID: " + id + ")");
        }
    }
    
    /**
     * Busca historias por texto
     */
    public List<Historia> buscarHistorias(String busqueda) {
        return historiaRepository.buscarHistoriasActivas(busqueda);
    }
    
    /**
     * Cuenta el total de historias activas
     */
    public Long countActivas() {
        return historiaRepository.countHistoriasActivas();
    }
    
    /**
     * Obtiene estadísticas de historias
     */
    public HistoriaStats obtenerEstadisticas() {
        Long totalActivas = countActivas();
        Long totalInactivas = historiaRepository.count() - totalActivas;
        
        return new HistoriaStats(totalActivas, totalInactivas);
    }
    
    /**
     * Clase para estadísticas de historias
     */
    public static class HistoriaStats {
        private final Long activas;
        private final Long inactivas;
        
        public HistoriaStats(Long activas, Long inactivas) {
            this.activas = activas;
            this.inactivas = inactivas;
        }
        
        public Long getActivas() { return activas; }
        public Long getInactivas() { return inactivas; }
        public Long getTotal() { return activas + inactivas; }
    }
}
