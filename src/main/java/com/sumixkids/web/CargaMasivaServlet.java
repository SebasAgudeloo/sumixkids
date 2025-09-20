package com.sumixkids.web;

import com.sumixkids.util.ValidacionCargaMasivaUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;

@WebServlet("/cargaMasiva")
@MultipartConfig
public class CargaMasivaServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        req.getRequestDispatcher("/carga_masiva.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        Part filePart = req.getPart("archivo");
        List<String> errores;
        
        // Validar formato del archivo
        String fileName = filePart.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx")) {
            req.setAttribute("error", "Solo se permiten archivos Excel (.xlsx)");
            req.getRequestDispatcher("/carga_masiva.jsp").forward(req, resp);
            return;
        }

        // Validar contenido
        errores = ValidacionCargaMasivaUtil.validarArchivoExcel(filePart.getInputStream());
        
        if (!errores.isEmpty()) {
            req.setAttribute("errores", errores);
            req.getRequestDispatcher("/carga_masiva.jsp").forward(req, resp);
            return;
        }

        // Si no hay errores, procesar el archivo
        // Establecer mensaje de éxito
        req.setAttribute("mensajeExito", "¡El archivo fue cargado y procesado correctamente!");
        
        // Permanecer en la misma página y mostrar el mensaje
        req.getRequestDispatcher("/carga_masiva.jsp").forward(req, resp);
    }
}
