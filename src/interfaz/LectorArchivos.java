package interfaz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import modelo.Tablero;
import estados_celdas.*;

/**
 * Clase responsable de leer archivos de configuración y crear tableros
 * con estados iniciales específicos.
 * 
 * Formato del archivo:
 * - Primera línea: número de filas y columnas separados por espacio
 * - Siguientes líneas: caracteres que representan el estado de cada celda
 * 
 * Caracteres soportados:
 * - '.' o espacio: celda muerta
 * - 'O', 'o', 'X', 'x': celda viva
 * - 'E', 'e': celda enferma (extensión)
 * - 'L', 'l': celda latente (extensión)
 * 
 * Ejemplo de archivo:
 * 4 5
 * .....
 * ..x..
 * ..x..
 * ..x..
 */
public class LectorArchivos {
    
    /**
     * Lee un archivo y crea un tablero con la configuración especificada.
     * 
     * @param rutaArchivo ruta del archivo a leer
     * @return Tablero configurado según el archivo
     * @throws IOException si hay problemas al leer el archivo
     * @throws IllegalArgumentException si el formato del archivo es inválido
     */
    public static Tablero cargarDesdeArchivo(String rutaArchivo) 
            throws IOException, IllegalArgumentException {
        
        File archivo = new File(rutaArchivo);
        
        // Verificar que el archivo existe
        if (!archivo.exists()) {
            throw new IOException("El archivo no existe: " + rutaArchivo);
        }
        
        // Verificar que es un archivo (no un directorio)
        if (!archivo.isFile()) {
            throw new IOException("La ruta no corresponde a un archivo: " + rutaArchivo);
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            return procesarArchivo(reader);
        }
    }
    
    /**
     * Procesa el contenido del archivo y crea el tablero.
     * 
     * @param reader BufferedReader del archivo
     * @return Tablero configurado
     * @throws IOException si hay problemas de lectura
     * @throws IllegalArgumentException si el formato es inválido
     */
    private static Tablero procesarArchivo(BufferedReader reader) 
            throws IOException, IllegalArgumentException {
        
        // Leer primera línea: dimensiones del tablero
        String primeraLinea = reader.readLine();
        if (primeraLinea == null || primeraLinea.trim().isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío o la primera línea es inválida");
        }
        
        // Parse de dimensiones
        String[] dimensiones = primeraLinea.trim().split("\\s+");
        if (dimensiones.length != 2) {
            throw new IllegalArgumentException(
                "Formato inválido en primera línea. Esperado: 'filas columnas', encontrado: " + primeraLinea
            );
        }
        
        int filas, columnas;
        try {
            filas = Integer.parseInt(dimensiones[0]);
            columnas = Integer.parseInt(dimensiones[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Las dimensiones deben ser números enteros. Encontrado: " + primeraLinea
            );
        }
        
        // Validar dimensiones
        if (filas <= 0 || columnas <= 0) {
            throw new IllegalArgumentException(
                "Las dimensiones deben ser positivas. Encontrado: " + filas + "x" + columnas
            );
        }
        
        if (filas > 1000 || columnas > 1000) {
            throw new IllegalArgumentException(
                "Las dimensiones son demasiado grandes (máximo 1000x1000). Encontrado: " + filas + "x" + columnas
            );
        }
        
        // Crear tablero
        Tablero tablero = new Tablero(filas, columnas);
        
        // Leer y procesar cada fila del tablero
        for (int i = 0; i < filas; i++) {
            String linea = reader.readLine();
            
            if (linea == null) {
                throw new IllegalArgumentException(
                    "El archivo tiene menos filas de las especificadas. Esperadas: " + filas + ", encontradas: " + i
                );
            }
            
            // Procesar cada carácter de la línea
            procesarLineaTablero(tablero, linea, i, columnas);
        }
        
        return tablero;
    }
    
    /**
     * Procesa una línea del archivo y configura las celdas correspondientes.
     * 
     * @param tablero tablero a configurar
     * @param linea línea del archivo
     * @param fila número de fila actual
     * @param columnasEsperadas número de columnas esperadas
     * @throws IllegalArgumentException si el formato es inválido
     */
    private static void procesarLineaTablero(Tablero tablero, String linea, int fila, int columnasEsperadas) 
            throws IllegalArgumentException {
        
        // Verificar longitud de la línea
        if (linea.length() < columnasEsperadas) {
            throw new IllegalArgumentException(
                "La fila " + fila + " tiene menos columnas de las esperadas. Esperadas: " + 
                columnasEsperadas + ", encontradas: " + linea.length()
            );
        }
        
        // Procesar cada carácter
        for (int j = 0; j < columnasEsperadas; j++) {
            char c = linea.charAt(j);
            EstadoCelda estado = interpretarCaracter(c, fila, j);
            tablero.setCelda(fila, j, estado);
        }
    }
    
    /**
     * Interpreta un carácter y retorna el estado correspondiente.
     * 
     * @param c carácter a interpretar
     * @param fila fila actual (para mensajes de error)
     * @param columna columna actual (para mensajes de error)
     * @return EstadoCelda correspondiente
     * @throws IllegalArgumentException si el carácter no es reconocido
     */
    private static EstadoCelda interpretarCaracter(char c, int fila, int columna) 
        throws IllegalArgumentException {
    
        switch (Character.toLowerCase(c)) {
            case '.':
            case ' ':
                return new CeldaMuertaExtendida();
            
            case 'o':
                return new CeldaVivaExtendida();  // Vivo: solo 'O'/'o'
            
            case 'e':
                return new CeldaEnferma();
            
            case 'x':  // Latente: 'X'/'x' según PDF CLI
                return new CeldaLatente();
            
            default:
                throw new IllegalArgumentException(
                    "Carácter no reconocido '" + c + "' en [" + fila + "," + columna + "]. Válidos: . (muerta), O (viva), E (enferma), X (latente)"
                );
        }
    }
    
    /**
     * Verifica si un archivo tiene un formato válido sin crear el tablero.
     * Útil para validación previa.
     * 
     * @param rutaArchivo ruta del archivo a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean validarFormato(String rutaArchivo) {
        try {
            cargarDesdeArchivo(rutaArchivo);
            return true;
        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Retorna información sobre el formato de archivo esperado.
     * 
     * @return String con la descripción del formato
     */
    public static String getFormatoEsperado() {
        return "Formato de archivo:\n" +
               "Primera línea: filas columnas (números separados por espacio)\n" +
               "Siguientes líneas: caracteres representando estados de celdas\n" +
               "\nCaracteres válidos:\n" +
               "  . o espacio = celda muerta\n" +
               "  O, o, X, x = celda viva\n" +
               "  E, e = celda enferma (extensión)\n" +
               "  L, l = celda latente (extensión)\n" +
               "\nEjemplo:\n" +
               "4 5\n" +
               ".....\n" +
               "..x..\n" +
               "..x..\n" +
               "..x..";
    }
}