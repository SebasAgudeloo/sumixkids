package com.sumixkids.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilidades para manejo de información del cliente y dispositivos
 */
public class ClienteUtil {
    
    /**
     * Obtiene la dirección IP real del cliente, considerando proxies y load balancers
     * @param request Request HTTP
     * @return IP del cliente
     */
    public static String getClienteIP(HttpServletRequest request) {
        // Headers que pueden contener la IP real cuando hay proxies
        String[] headerNames = {
            "X-Forwarded-For",
            "X-Real-IP", 
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED"
        };
        
        String ip = null;
        
        // Buscar en los headers de proxy
        for (String headerName : headerNames) {
            ip = request.getHeader(headerName);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // El header X-Forwarded-For puede contener múltiples IPs separadas por comas
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                break;
            }
        }
        
        // Si no se encontró en los headers, usar getRemoteAddr()
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // Normalizar direcciones localhost
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }
        
        return ip != null ? ip : "unknown";
    }
    
    /**
     * Obtiene el User-Agent del navegador
     * @param request Request HTTP
     * @return User-Agent del cliente
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : "unknown";
    }
    
    /**
     * Genera un identificador único del dispositivo basado en IP + User-Agent
     * @param request Request HTTP
     * @return Device ID único
     */
    public static String generarDeviceId(HttpServletRequest request) {
        String ip = getClienteIP(request);
        String userAgent = getUserAgent(request);
        return com.sumixkids.dao.DispositivoReconocidoDAO.generarDeviceId(ip, userAgent);
    }
    
    /**
     * Obtiene información resumida del cliente para logs
     * @param request Request HTTP
     * @return String con información del cliente
     */
    public static String getInfoCliente(HttpServletRequest request) {
        String ip = getClienteIP(request);
        String userAgent = getUserAgent(request);
        String deviceId = generarDeviceId(request);
        
        return String.format("IP: %s, DeviceID: %s, UserAgent: %s", 
                           ip, deviceId, userAgent);
    }
}