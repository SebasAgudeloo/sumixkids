package com.sumixkids.web;

import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.model.LogAuditoria;
import com.sumixkids.model.Usuario;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AuditoriaServlet extends HttpServlet {
    
    private final LogAuditoriaDAO logDAO = new LogAuditoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        System.out.println("=== INICIO AuditoriaServlet ===");
        
        // Verificar sesión
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            System.out.println("Sin sesión válida, redirigiendo a login");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Usuario admin = (Usuario) session.getAttribute("usuario");
        System.out.println("Usuario: " + admin.getUsername() + ", RolId: " + admin.getRolId());
        
        if (admin.getRolId() != 1) {
            System.out.println("Usuario no es administrador");
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        
        try {
            // Verificar si es una solicitud de exportación
            String exportar = req.getParameter("exportar");
            if ("excel".equalsIgnoreCase(exportar)) {
                exportarExcel(req, resp);
                return;
            }
            
            // Obtener parámetros de filtro
            String fechaInicio = req.getParameter("fechaInicio");
            String fechaFin = req.getParameter("fechaFin");
            String tipoEvento = req.getParameter("tipoEvento");
            String usuarioFiltro = req.getParameter("usuario");
            
            System.out.println("Filtros - Inicio: " + fechaInicio + ", Fin: " + fechaFin + 
                             ", Tipo: " + tipoEvento + ", Usuario: " + usuarioFiltro);

            // Convertir fechas
            LocalDateTime desde = null;
            LocalDateTime hasta = null;
            
            if (fechaInicio != null && !fechaInicio.trim().isEmpty()) {
                try {
                    desde = LocalDate.parse(fechaInicio).atStartOfDay();
                } catch (DateTimeParseException e) {
                    System.err.println("Error parseando fechaInicio: " + e.getMessage());
                }
            }
            
            if (fechaFin != null && !fechaFin.trim().isEmpty()) {
                try {
                    hasta = LocalDate.parse(fechaFin).atTime(23, 59, 59);
                } catch (DateTimeParseException e) {
                    System.err.println("Error parseando fechaFin: " + e.getMessage());
                }
            }
            
            // Buscar logs con filtros
            String filtroTexto = (usuarioFiltro != null && !usuarioFiltro.trim().isEmpty()) ? usuarioFiltro : null;
            List<LogAuditoria> logs = logDAO.buscarLogs(filtroTexto, desde, hasta, null);
            
            // Filtrar por tipo de evento si se especificó
            if (tipoEvento != null && !tipoEvento.trim().isEmpty()) {
                logs.removeIf(log -> !tipoEvento.equalsIgnoreCase(log.getAccion()));
            }
            
            System.out.println("=== LOGS ENCONTRADOS: " + logs.size() + " ===");
            
            if (logs.isEmpty()) {
                System.out.println("⚠️ ADVERTENCIA: No se encontraron logs en la base de datos");
                System.out.println("Verifica que la tabla log_auditoria tenga registros");
            } else {
                System.out.println("✅ Se encontraron " + logs.size() + " registros");
                // Mostrar primeros 3 logs para debug
                for (int i = 0; i < Math.min(3, logs.size()); i++) {
                    LogAuditoria log = logs.get(i);
                    System.out.println("  - Log #" + (i+1) + ": ID=" + log.getId() + 
                                     ", Usuario=" + log.getNombreUsuario() + 
                                     ", Acción=" + log.getAccion() +
                                     ", Fecha=" + log.getFechaHora());
                }
            }
            
            // Calcular estadísticas
            int totalEventos = logs.size();
            long eventosHoy = logs.stream()
                .filter(log -> log.getFechaHora().toLocalDate().equals(LocalDate.now()))
                .count();
            long eventosFallidos = logs.stream()
                .filter(log -> "FALLIDO".equalsIgnoreCase(log.getEstado()) || 
                              "ERROR".equalsIgnoreCase(log.getEstado()))
                .count();
            long usuariosActivos = logs.stream()
                .map(LogAuditoria::getIdUsuario)
                .distinct()
                .count();
            
            // Enviar datos al JSP
            req.setAttribute("eventos", logs);
            req.setAttribute("totalEventos", totalEventos);
            req.setAttribute("eventosHoy", eventosHoy);
            req.setAttribute("eventosFallidos", eventosFallidos);
            req.setAttribute("usuariosActivos", usuariosActivos);
            req.setAttribute("totalRegistros", totalEventos);
            
            System.out.println("=== DATOS ENVIADOS AL JSP ===");
            System.out.println("eventos (size): " + logs.size());
            System.out.println("totalEventos: " + totalEventos);
            System.out.println("eventosHoy: " + eventosHoy);
            System.out.println("eventosFallidos: " + eventosFallidos);
            System.out.println("usuariosActivos: " + usuariosActivos);
            System.out.println("totalRegistros: " + totalEventos);
            System.out.println("==============================");
            System.out.println("Enviando a auditoria.jsp");
            req.getRequestDispatcher("/auditoria.jsp").forward(req, resp);
            
        } catch (SQLException e) {
            System.err.println("Error al cargar logs: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Error al cargar logs", e);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Error inesperado", e);
        }
    }
    
    /**
     * Exporta los registros de auditoría a un archivo Excel
     */
    private void exportarExcel(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        System.out.println("=== EXPORTANDO A EXCEL ===");
        
        try {
            // Obtener parámetros de filtro
            String fechaInicio = req.getParameter("fechaInicio");
            String fechaFin = req.getParameter("fechaFin");
            String tipoEvento = req.getParameter("tipoEvento");
            String usuarioFiltro = req.getParameter("usuario");
            
            // Convertir fechas
            LocalDateTime desde = null;
            LocalDateTime hasta = null;
            
            if (fechaInicio != null && !fechaInicio.trim().isEmpty()) {
                desde = LocalDate.parse(fechaInicio).atStartOfDay();
            }
            
            if (fechaFin != null && !fechaFin.trim().isEmpty()) {
                hasta = LocalDate.parse(fechaFin).atTime(23, 59, 59);
            }
            
            // Buscar logs con filtros
            String filtroTexto = (usuarioFiltro != null && !usuarioFiltro.trim().isEmpty()) ? usuarioFiltro : null;
            List<LogAuditoria> logs = logDAO.buscarLogs(filtroTexto, desde, hasta, null);
            
            // Filtrar por tipo de evento
            if (tipoEvento != null && !tipoEvento.trim().isEmpty()) {
                logs.removeIf(log -> !tipoEvento.equalsIgnoreCase(log.getAccion()));
            }
            
            System.out.println("Exportando " + logs.size() + " registros a Excel");
            
            // Crear el workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Auditoría");
            
            // Crear estilos
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));
            
            // Crear cabecera
            Row headerRow = sheet.createRow(0);
            String[] columnas = {"ID", "Fecha/Hora", "Usuario", "ID Usuario", "Tipo Evento", 
                                "Descripción", "IP", "Estado", "Tabla Afectada"};
            
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Llenar datos
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            int rowNum = 1;
            
            for (LogAuditoria log : logs) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(log.getId());
                row.createCell(1).setCellValue(log.getFechaHora().format(formatter));
                row.createCell(2).setCellValue(log.getNombreUsuario());
                row.createCell(3).setCellValue(log.getIdUsuario());
                row.createCell(4).setCellValue(log.getAccion());
                row.createCell(5).setCellValue(log.getDescripcion());
                row.createCell(6).setCellValue(log.getIpUsuario());
                row.createCell(7).setCellValue(log.getEstado());
                row.createCell(8).setCellValue(log.getTablaAfectada() != null ? log.getTablaAfectada() : "");
            }
            
            // Auto-ajustar columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Configurar respuesta HTTP
            String nombreArchivo = "Auditoria_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"");
            
            // Escribir el archivo
            ServletOutputStream outputStream = resp.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            
            System.out.println("Excel generado exitosamente: " + nombreArchivo);
            
        } catch (SQLException e) {
            System.err.println("Error al exportar Excel: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Error al exportar Excel", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Usuario admin = (Usuario) session.getAttribute("usuario");
        if (admin.getRolId() != 1) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }

        String accion = req.getParameter("accion");
        String logIdStr = req.getParameter("logId");

        if ("eliminar".equals(accion) && logIdStr != null) {
            try {
                Long logId = Long.parseLong(logIdStr);
                // Redirigir a EliminarUsuarioServlet con el logId
                resp.sendRedirect(req.getContextPath() + 
                    "/eliminarUsuario?logId=" + logId);
                return;
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID de log inválido");
                doGet(req, resp);
                return;
            }
        }

        req.setAttribute("error", "Acción no válida");
        doGet(req, resp);
    }
}
