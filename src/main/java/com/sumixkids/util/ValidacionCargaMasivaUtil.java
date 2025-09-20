package com.sumixkids.util;

import java.io.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ValidacionCargaMasivaUtil {
    
    public static List<String> validarArchivoExcel(InputStream inputStream) {
        List<String> errores = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Validar que no esté vacío
            if (sheet.getPhysicalNumberOfRows() < 2) {
                errores.add("El archivo está vacío o no contiene datos");
                return errores;
            }

            // Validar encabezados
            Row headerRow = sheet.getRow(0);
            if (!validarEncabezados(headerRow)) {
                errores.add("Los encabezados no coinciden con el formato esperado");
            }

            // Validar cada fila
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Saltar encabezados
            
            int rowNum = 1;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                List<String> erroresFila = validarFila(row, rowNum);
                errores.addAll(erroresFila);
                rowNum++;
            }
            
        } catch (IOException e) {
            errores.add("Error al procesar el archivo: " + e.getMessage());
        }
        
        return errores;
    }

    private static boolean validarEncabezados(Row headerRow) {
        String[] encabezadosEsperados = {
            "Nombres", "Apellidos", "Email", "Usuario", "Rol"
        };
        
        for (int i = 0; i < encabezadosEsperados.length; i++) {
            Cell cell = headerRow.getCell(i);
            if (cell == null || !cell.getStringCellValue().trim()
                    .equalsIgnoreCase(encabezadosEsperados[i])) {
                return false;
            }
        }
        return true;
    }

    private static List<String> validarFila(Row row, int rowNum) {
        List<String> errores = new ArrayList<>();
        
        // Validar nombres
        String nombres = getCellValueAsString(row.getCell(0));
        if (!ValidacionUtil.esNombreValido(nombres)) {
            errores.add("Fila " + rowNum + ": Nombres inválidos");
        }
        
        // Validar apellidos
        String apellidos = getCellValueAsString(row.getCell(1));
        if (!ValidacionUtil.esNombreValido(apellidos)) {
            errores.add("Fila " + rowNum + ": Apellidos inválidos");
        }
        
        // Validar email
        String email = getCellValueAsString(row.getCell(2));
        if (!ValidacionUtil.esCorreoValido(email)) {
            errores.add("Fila " + rowNum + ": Email inválido");
        }
        
        // Validar usuario
        String usuario = getCellValueAsString(row.getCell(3));
        if (!ValidacionUtil.esUsuarioValido(usuario)) {
            errores.add("Fila " + rowNum + ": Usuario inválido (debe contener al menos 2 números)");
        }
        
        return errores;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int)cell.getNumericCellValue());
            default:
                return "";
        }
    }
}
