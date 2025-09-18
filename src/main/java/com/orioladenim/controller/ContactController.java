package com.orioladenim.controller;

import com.orioladenim.entity.Contact;
import com.orioladenim.service.ContactService;
import com.orioladenim.service.EmailService;
import com.orioladenim.service.GeolocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GeolocationService geolocationService;

    // Página de contacto pública
    @GetMapping("/contact")
    public String mostrarContacto(@RequestParam(required = false) String producto, Model model) {
        Contact contact = new Contact("", "", "");
        if (producto != null && !producto.trim().isEmpty()) {
            contact.setProductoInteres(producto.trim());
        }
        model.addAttribute("contact", contact);
        return "contact";
    }

    // Procesar formulario de contacto
    @PostMapping("/contact")
    public String procesarContacto(@ModelAttribute Contact contact,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        // Validar datos
        if (!contactService.esConsultaValida(contact)) {
            redirectAttributes.addFlashAttribute("error", "Por favor, complete todos los campos requeridos correctamente.");
            return "redirect:/contact";
        }

        try {
            // Obtener información del cliente
            String ipAddress = obtenerIpAddress(request);
            String userAgent = request.getHeader("User-Agent");

            // Obtener ubicación geográfica
            String ubicacion = geolocationService.getLocationFromIP(ipAddress);

            // Crear consulta
            Contact nuevaConsulta = contactService.crearConsulta(
                    contact.getNombre(),
                    contact.getEmail(),
                    contact.getTelefono(),
                    contact.getAsunto(),
                    contact.getMensaje(),
                    contact.getProductoInteres(),
                    ipAddress,
                    userAgent
            );

            // Agregar ubicación a la consulta y guardar
            nuevaConsulta.setUbicacion(ubicacion);
            contactService.guardar(nuevaConsulta);

            // Enviar notificación por email al administrador
            emailService.sendNewContactNotification(nuevaConsulta);

            // Enviar confirmación al cliente
            emailService.sendConfirmationToClient(nuevaConsulta);

            redirectAttributes.addFlashAttribute("success",
                    "¡Gracias por tu consulta! Te hemos enviado un correo de confirmación y te responderemos pronto.");

            return "redirect:/contact";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Hubo un error al enviar tu consulta. Por favor, inténtalo de nuevo.");
            return "redirect:/contact";
        }
    }

    // Panel de administración - Lista de consultas
    @GetMapping("/admin/contacts")
    public String listarConsultas(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaCreacion") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean leido,
            @RequestParam(required = false) Boolean respondido,
            Model model) {

        // Configurar paginación y ordenamiento
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Contact> consultas;

        // Aplicar filtros
        if (search != null && !search.trim().isEmpty()) {
            consultas = contactService.buscarPorTermino(search.trim(), pageable);
        } else {
            consultas = contactService.buscarTodos(pageable);
        }

        // Agregar datos al modelo
        model.addAttribute("consultas", consultas);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consultas.getTotalPages());
        model.addAttribute("totalElements", consultas.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("leido", leido);
        model.addAttribute("respondido", respondido);

        // Estadísticas
        model.addAttribute("stats", contactService.obtenerEstadisticas());

        return "admin/contacts";
    }

    // Panel de administración - Ver consulta individual
    @GetMapping("/admin/contacts/{id}")
    public String verConsulta(@PathVariable Long id, Model model) {
        Optional<Contact> contact = contactService.buscarPorId(id);

        if (contact.isPresent()) {
            // Marcar como leída si no lo está
            if (!contact.get().isLeido()) {
                contactService.marcarComoLeida(contact.get());
            }

            model.addAttribute("contact", contact.get());
            return "admin/contact-detail-simple";
        } else {
            return "redirect:/admin/contacts?error=Consulta no encontrada";
        }
    }

    // Panel de administración - Marcar como leída
    @PostMapping("/admin/contacts/{id}/leer")
    public String marcarComoLeida(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        contactService.marcarComoLeida(id);
        redirectAttributes.addFlashAttribute("success", "Consulta marcada como leída.");
        return "redirect:/admin/contacts/" + id;
    }

    // Panel de administración - Responder consulta
    @PostMapping("/admin/contacts/{id}/responder")
    public String responderConsulta(@PathVariable Long id,
            @RequestParam String respuesta,
            RedirectAttributes redirectAttributes) {

        if (respuesta == null || respuesta.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "La respuesta no puede estar vacía.");
            return "redirect:/admin/contacts/" + id;
        }

        contactService.responder(id, respuesta.trim());
        redirectAttributes.addFlashAttribute("success", "Respuesta enviada correctamente.");

        // TODO: Enviar email de respuesta al cliente
        return "redirect:/admin/contacts/" + id;
    }

    // Panel de administración - Eliminar consulta
    @PostMapping("/admin/contacts/{id}/eliminar")
    public String eliminarConsulta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        contactService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Consulta eliminada correctamente.");
        return "redirect:/admin/contacts";
    }

    // Panel de administración - Estadísticas
    @GetMapping("/admin/contacts/stats")
    public String mostrarEstadisticas(Model model) {
        model.addAttribute("stats", contactService.obtenerEstadisticas());
        model.addAttribute("productosMasConsultados", contactService.buscarProductosMasConsultados());
        model.addAttribute("consultasRecientes", contactService.buscarRecientes());
        return "admin/contact-stats";
    }

    // API REST - Obtener consultas (para AJAX)
    @GetMapping("/api/contacts")
    @ResponseBody
    public Page<Contact> obtenerConsultas(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaCreacion") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean leido,
            @RequestParam(required = false) Boolean respondido) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (search != null && !search.trim().isEmpty()) {
            return contactService.buscarPorTermino(search.trim(), pageable);
        } else {
            return contactService.buscarConFiltros(leido, respondido, null, null, pageable);
        }
    }

    // API REST - Obtener estadísticas (para AJAX)
    @GetMapping("/api/contacts/stats")
    @ResponseBody
    public ContactService.ContactStats obtenerEstadisticas() {
        return contactService.obtenerEstadisticas();
    }

    // API REST - Marcar como leída (para AJAX)
    @PostMapping("/api/contacts/{id}/leer")
    @ResponseBody
    public String marcarComoLeidaAjax(@PathVariable Long id) {
        contactService.marcarComoLeida(id);
        return "OK";
    }

    // API REST - Responder consulta (para AJAX)
    @PostMapping("/api/contacts/{id}/responder")
    @ResponseBody
    public String responderConsultaAjax(@PathVariable Long id, @RequestParam String respuesta) {
        if (respuesta == null || respuesta.trim().isEmpty()) {
            return "ERROR: La respuesta no puede estar vacía";
        }

        contactService.responder(id, respuesta.trim());
        return "OK";
    }

    // Método auxiliar para obtener IP del cliente
    private String obtenerIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
