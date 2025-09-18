package com.orioladenim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
public class GeolocationService {

    private final WebClient webClient;

    @Autowired
    public GeolocationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://ip-api.com")
                .build();
    }

    /**
     * Obtiene la ubicación geográfica basada en la IP
     */
    public String getLocationFromIP(String ipAddress) {
        try {
            // IPs locales o de desarrollo
            if (isLocalIP(ipAddress)) {
                return "Desarrollo Local";
            }

            // Llamada a la API de geolocalización
            Map<String, Object> response = webClient
                    .get()
                    .uri("/json/{ip}?fields=status,country,regionName,city", ipAddress)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(5))
                    .onErrorReturn(Map.of("status", "fail"))
                    .block();

            if (response != null && "success".equals(response.get("status"))) {
                String country = (String) response.get("country");
                String region = (String) response.get("regionName");
                String city = (String) response.get("city");
                
                return buildLocationString(country, region, city);
            }
        } catch (Exception e) {
            // Log del error si es necesario
            System.err.println("Error obteniendo geolocalización para IP: " + ipAddress + " - " + e.getMessage());
        }
        
        return "Ubicación no disponible";
    }

    /**
     * Verifica si la IP es local o de desarrollo
     */
    private boolean isLocalIP(String ipAddress) {
        return ipAddress == null || 
               ipAddress.startsWith("127.") || 
               ipAddress.startsWith("192.168.") || 
               ipAddress.startsWith("10.") || 
               ipAddress.equals("0:0:0:0:0:0:0:1") ||
               ipAddress.equals("::1");
    }

    /**
     * Construye la cadena de ubicación
     */
    private String buildLocationString(String country, String region, String city) {
        StringBuilder location = new StringBuilder();
        
        if (city != null && !city.isEmpty()) {
            location.append(city);
        }
        
        if (region != null && !region.isEmpty() && !region.equals(city)) {
            if (location.length() > 0) {
                location.append(", ");
            }
            location.append(region);
        }
        
        if (country != null && !country.isEmpty()) {
            if (location.length() > 0) {
                location.append(", ");
            }
            location.append(country);
        }
        
        return location.length() > 0 ? location.toString() : "Ubicación no disponible";
    }
}
