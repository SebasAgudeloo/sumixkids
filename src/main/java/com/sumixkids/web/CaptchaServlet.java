package com.sumixkids.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * Servlet que genera imágenes CAPTCHA con operaciones matemáticas simples.
 * El resultado se almacena en la sesión para su posterior validación.
 */
public class CaptchaServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 60;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // CAPTCHA siempre disponible para mayor seguridad
        HttpSession session = request.getSession(true);
        
        // Configurar respuesta como imagen PNG
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        // Crear imagen
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        
        // Generar operación matemática simple
        Random random = new Random();
        int num1 = random.nextInt(10) + 1; // 1-10
        int num2 = random.nextInt(10) + 1; // 1-10
        int operator = random.nextInt(2); // 0: suma, 1: resta
        
        String operatorSymbol;
        int result;
        
        if (operator == 0) {
            operatorSymbol = "+";
            result = num1 + num2;
        } else {
            // Asegurar que la resta no dé negativo
            if (num1 < num2) {
                int temp = num1;
                num1 = num2;
                num2 = temp;
            }
            operatorSymbol = "-";
            result = num1 - num2;
        }
        
        // Guardar resultado en sesión
        session.setAttribute("captchaCode", String.valueOf(result));
        
        // Configurar fondo con degradado
        GradientPaint gradient = new GradientPaint(0, 0, new Color(230, 240, 255), 
                                                    WIDTH, HEIGHT, new Color(200, 220, 255));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Agregar líneas de ruido
        g2d.setColor(new Color(180, 180, 180, 100));
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g2d.drawLine(x1, y1, x2, y2);
        }
        
        // Agregar puntos de ruido
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            g2d.setColor(new Color(random.nextInt(100) + 100, 
                                  random.nextInt(100) + 100, 
                                  random.nextInt(100) + 100));
            g2d.fillOval(x, y, 2, 2);
        }
        
        // Configurar fuente para el texto
        Font font = new Font("Arial", Font.BOLD, 32);
        g2d.setFont(font);
        
        // Dibujar sombra del texto
        g2d.setColor(new Color(100, 100, 100, 80));
        String captchaText = num1 + " " + operatorSymbol + " " + num2 + " = ?";
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(captchaText);
        int x = (WIDTH - textWidth) / 2 + 2;
        int y = (HEIGHT - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent() + 2;
        g2d.drawString(captchaText, x, y);
        
        // Dibujar texto principal
        g2d.setColor(new Color(50, 50, 150));
        g2d.drawString(captchaText, x - 2, y - 2);
        
        // Agregar borde
        g2d.setColor(new Color(100, 150, 200));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
        
        g2d.dispose();
        
        // Enviar imagen
        OutputStream os = response.getOutputStream();
        ImageIO.write(bufferedImage, "png", os);
        os.close();
    }
}
