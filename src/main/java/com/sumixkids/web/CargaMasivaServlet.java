package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.model.LogAuditoria;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@MultipartConfig(
    maxFileSize = 10 * 1024 * 1024,      // 10 MB
    maxRequestSize = 10 * 1024 * 1024   // 10 MB
)
public class CargaMasivaServlet extends HttpServlet {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final LogAuditoriaDAO logDAO = new LogAuditoriaDAO();
    
    // Patrones de validación
    private static final Pattern PATTERN_NOMBRES = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,30}$");
    private static final Pattern PATTERN_USERNAME = Pattern.compile("^[a-zA-Z0-9]{5,15}$");
    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PATTERN_PASSWORD = Pattern.compile("^(?=.*[A-Za-z]{5,})(?=.*\\d.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // Verificar sesión
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
        
        // Verificar si es solicitud de descarga de plantilla
        String action = req.getParameter("action");
        if ("plantilla".equals(action)) {
            generarPlantillaVacia(resp);
            return;
        } else if ("ejemplo".equals(action)) {
            generarPlantillaConEjemplos(resp);
            return;
        }
        
        req.getRequestDispatcher("/carga_masiva.jsp").forward(req, resp);
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
        
        try {
            // Obtener archivo
            Part filePart = req.getPart("archivo");
            if (filePart == null || filePart.getSize() == 0) {
                req.setAttribute("mensajeError", "No se seleccionó ningún archivo");
                req.getRequestDispatcher("/carga_masiva.jsp").forward(req, resp);
                return;
            }
            
            // Validar formato
            String fileName = filePart.getSubmittedFileName();
            if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
                req.setAttribute("mensajeError", "Solo se permiten archivos Excel (.xlsx, .xls)");
                req.getRequestDispatcher("/carga_masiva.jsp").forward(req, resp);
                return;
            }
            
            // Obtener parámetros
            String tipoImportacion = req.getParameter("tipoImportacion");
            boolean validarDuplicados = req.getParameter("validarDuplicados") != null;
            boolean enviarEmails = req.getParameter("enviarEmails") != null;
            
            // Procesar archivo
            Map<String, Object> resultados = procesarArchivoExcel(
                filePart.getInputStream(), 
                tipoImportacion, 
                validarDuplicados, 
                enviarEmails,
                admin,
                req.getRemoteAddr()
            );
            
            // Establecer resultados
            req.setAttribute("resultadosImportacion", true);
            req.setAttribute("usuariosCreados", resultados.get("creados"));
            req.setAttribute("usuariosDuplicados", resultados.get("duplicados"));
            req.setAttribute("usuariosErrores", resultados.get("errores"));
            req.setAttribute("totalProcesados", resultados.get("total"));
            req.setAttribute("erroresDetallados", resultados.get("listaErrores"));
            
            int creados = (int) resultados.get("creados");
            if (creados > 0) {
                req.setAttribute("mensajeExito", 
                    String.format("✅ Se importaron exitosamente %d usuario(s)", creados));
            }
            
        } catch (Exception e) {
            System.err.println("Error en carga masiva: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("mensajeError", "Error al procesar archivo: " + e.getMessage());
        }
        
        req.getRequestDispatcher("/carga_masiva.jsp").forward(req, resp);
    }
    
    /**
     * Procesa el archivo Excel y crea los usuarios
     */
    private Map<String, Object> procesarArchivoExcel(InputStream inputStream, String tipoImportacion,
                                                     boolean validarDuplicados, boolean enviarEmails,
                                                     Usuario admin, String ipAddress) throws Exception {
        
        Map<String, Object> resultados = new HashMap<>();
        int creados = 0, duplicados = 0, errores = 0, total = 0;
        List<String> listaErrores = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Saltar fila de encabezado
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                total++;
                
                try {
                    // Leer datos
                    String nombres = getCellValueAsString(row.getCell(0));
                    String apellidos = getCellValueAsString(row.getCell(1));
                    String username = getCellValueAsString(row.getCell(2));
                    String email = getCellValueAsString(row.getCell(3));
                    String password = getCellValueAsString(row.getCell(4));
                    int rolId = (int) getCellValueAsNumber(row.getCell(5));
                    String grado = getCellValueAsString(row.getCell(6));
                    
                    // Validar campos
                    if (!validarCampos(nombres, apellidos, username, email, password, rolId, grado, i, listaErrores)) {
                        errores++;
                        continue;
                    }
                    
                    // Validar duplicados si está activo
                    if (validarDuplicados) {
                        if (usuarioDAO.existeUsuario(username)) {
                            duplicados++;
                            listaErrores.add(String.format("Fila %d: Usuario '%s' ya existe", i + 1, username));
                            continue;
                        }
                        if (usuarioDAO.existeEmail(email)) {
                            duplicados++;
                            listaErrores.add(String.format("Fila %d: Email '%s' ya existe", i + 1, email));
                            continue;
                        }
                    }
                    
                    // Crear usuario
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setNombres(nombres);
                    nuevoUsuario.setApellidos(apellidos);
                    nuevoUsuario.setUsername(username);
                    nuevoUsuario.setEmail(email);
                    nuevoUsuario.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
                    nuevoUsuario.setRolId(rolId);
                    // Normalizar grado: agregar símbolo ° si no lo tiene
                    if (grado != null && !grado.isEmpty()) {
                        String gradoNormalizado = grado.trim();
                        if (!gradoNormalizado.endsWith("°")) {
                            gradoNormalizado += "°";
                        }
                        nuevoUsuario.setGrado(gradoNormalizado);
                    } else {
                        nuevoUsuario.setGrado(null);
                    }
                    nuevoUsuario.setFechaRegistro(LocalDateTime.now());
                    
                    // Guardar en BD
                    usuarioDAO.crearUsuario(nuevoUsuario);
                    creados++;
                    
                    // Enviar email si está activo
                    if (enviarEmails) {
                        try {
                            System.out.println("Email pendiente de enviar a: " + email + " con usuario: " + username);
                        } catch (Exception e) {
                            System.err.println("Error enviando email a " + email + ": " + e.getMessage());
                        }
                    }
                    
                } catch (Exception e) {
                    errores++;
                    listaErrores.add(String.format("Fila %d: Error procesando - %s", i + 1, e.getMessage()));
                }
            }
        }
        
        resultados.put("creados", creados);
        resultados.put("duplicados", duplicados);
        resultados.put("errores", errores);
        resultados.put("total", total);
        resultados.put("listaErrores", listaErrores);
        
        // Registrar UN SOLO log de auditoría al finalizar la carga masiva
        if (total > 0) {
            LogAuditoria log = new LogAuditoria();
            log.setFechaHora(LocalDateTime.now());
            log.setIdUsuario(admin.getId());
            log.setNombreUsuario(admin.getUsername());
            log.setIpUsuario(ipAddress);
            log.setAccion("CARGA_MASIVA");
            log.setTablaAfectada("usuarios");
            log.setDescripcion(String.format(
                "Carga masiva completada: %d creados, %d duplicados, %d errores (Total procesado: %d)", 
                creados, duplicados, errores, total
            ));
            log.setEstado(errores == 0 ? "EXITOSO" : "PARCIAL");
            logDAO.registrarLog(log);
        }
        
        return resultados;
    }
    
    /**
     * Valida los campos del usuario
     */
    private boolean validarCampos(String nombres, String apellidos, String username, 
                                  String email, String password, int rolId, String grado,
                                  int fila, List<String> errores) {
        boolean valido = true;
        
        if (nombres == null || !PATTERN_NOMBRES.matcher(nombres).matches()) {
            errores.add(String.format("Fila %d: Nombres inválidos (solo letras y espacios, 2-30 chars)", fila + 1));
            valido = false;
        }
        
        if (apellidos == null || !PATTERN_NOMBRES.matcher(apellidos).matches()) {
            errores.add(String.format("Fila %d: Apellidos inválidos (solo letras y espacios, 2-30 chars)", fila + 1));
            valido = false;
        }
        
        if (username == null || !PATTERN_USERNAME.matcher(username).matches()) {
            errores.add(String.format("Fila %d: Usuario inválido (5-15 caracteres alfanuméricos)", fila + 1));
            valido = false;
        }
        
        if (email == null || !PATTERN_EMAIL.matcher(email).matches()) {
            errores.add(String.format("Fila %d: Email inválido", fila + 1));
            valido = false;
        }
        
        if (password == null || !PATTERN_PASSWORD.matcher(password).matches()) {
            errores.add(String.format("Fila %d: Contraseña inválida (mín. 5 letras, 2 números, 1 especial)", fila + 1));
            valido = false;
        }
        
        if (rolId < 1 || rolId > 4) {
            errores.add(String.format("Fila %d: Rol inválido (debe ser 1-4)", fila + 1));
            valido = false;
        }
        
        // Validar grado solo si es estudiante (rolId = 3)
        if (rolId == 3) {
            if (grado == null || grado.trim().isEmpty()) {
                errores.add(String.format("Fila %d: Grado es obligatorio para estudiantes", fila + 1));
                valido = false;
            } else {
                String gradoTrim = grado.trim();
                // Aceptar formato con o sin símbolo de grado
                if (!gradoTrim.equals("3") && !gradoTrim.equals("3°") && 
                    !gradoTrim.equals("4") && !gradoTrim.equals("4°") && 
                    !gradoTrim.equals("5") && !gradoTrim.equals("5°")) {
                    errores.add(String.format("Fila %d: Grado inválido (solo se permiten 3, 4 o 5)", fila + 1));
                    valido = false;
                }
            }
        }
        
        return valido;
    }
    
    /**
     * Obtiene el valor de una celda como String
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
    
    /**
     * Obtiene el valor de una celda como número
     */
    private double getCellValueAsNumber(Cell cell) {
        if (cell == null) return 0;
        
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
    /**
     * Genera una plantilla Excel vacía
     */
    private void generarPlantillaVacia(HttpServletResponse resp) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios");
        
        // Estilos
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        // Crear encabezados
        Row headerRow = sheet.createRow(0);
        String[] columnas = {"Nombres", "Apellidos", "Usuario", "Email", "Contraseña", "Rol ID", "Grado"};
        
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 4000);
        }
        
        // Configurar respuesta
        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resp.setHeader("Content-Disposition", "attachment; filename=\"plantilla-usuarios.xlsx\"");
        
        ServletOutputStream outputStream = resp.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
    
    /**
     * Genera una plantilla Excel con ejemplos
     */
    private void generarPlantillaConEjemplos(HttpServletResponse resp) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios");
        
        // Estilos
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        // Crear encabezados
        Row headerRow = sheet.createRow(0);
        String[] columnas = {"Nombres", "Apellidos", "Usuario", "Email", "Contraseña", "Rol ID", "Grado"};
        
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 4000);
        }
        
        // Agregar ejemplos
        String[][] ejemplos = {
            {"María José", "González López", "mariagonzalez", "maria@gmail.com", "MiPass123@", "3", "5"},
            {"Juan Carlos", "Pérez Sánchez", "juanperez", "juan@gmail.com", "Secure456#", "3", "4"},
            {"Ana María", "Rodríguez Torres", "anarodriguez", "ana@gmail.com", "Ana2024$", "2", ""},
            {"Pedro Luis", "Martínez Ruiz", "pedromartinez", "pedro@gmail.com", "Pedro789!", "4", ""},
            {"Sofía Elena", "García Morales", "sofiagarcia", "sofia@gmail.com", "Sofia2024!", "3", "3"}
        };
        
        for (int i = 0; i < ejemplos.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < ejemplos[i].length; j++) {
                row.createCell(j).setCellValue(ejemplos[i][j]);
            }
        }
        
        // Configurar respuesta
        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resp.setHeader("Content-Disposition", "attachment; filename=\"ejemplo-usuarios.xlsx\"");
        
        ServletOutputStream outputStream = resp.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
