package interfaz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import modelo.Tablero;
import estados_celdas.*;

/**
 *clase responsable de leer archivos de configuración y crear tableros
 *con estados iniciales específicos.
 * 
 *formato del archivo:
 * - primera línea: número de filas y columnas separados por espacio
 * - segunda líneas: caracteres que representan el estado de cada celda
 * 
 *caracteres soportados:
 * - '.' o espacio: celda muerta
 * - 'O', 'o', 'X', 'x': celda viva
 * - 'E', 'e': celda enferma (extensión)
 * - 'L', 'l': celda latente (extensión)
 */
public class LectorArchivos {
    
    //lee un archivo y crea un tablero con la configuración especificada
    public static Tablero cargarDesdeArchivo(String rutaArchivo) 
            throws IOException, IllegalArgumentException {
        
        File archivo = new File(rutaArchivo);
        
        //verificar que el archivo existe
        if (!archivo.exists()) {
            throw new IOException("El archivo no existe: " + rutaArchivo);
        }
        
        //verificar que es un archivo (no un directorio)
        if (!archivo.isFile()) {
            throw new IOException("La ruta no corresponde a un archivo: " + rutaArchivo);
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            return procesarArchivo(reader);
        }
    }
    
    //procesa el contenido del archivo y crea el tablero
    private static Tablero procesarArchivo(BufferedReader reader) 
            throws IOException, IllegalArgumentException {
        
        //leer primera línea: dimensiones del tablero
        String primeraLinea = reader.readLine();
        if (primeraLinea == null || primeraLinea.trim().isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío o la primera línea es inválida");
        }
        
        //parse de dimensiones
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
        
        //validar dimensiones
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
        
        //crear tablero
        Tablero tablero = new Tablero(filas, columnas);
        
        //leer y procesar cada fila del tablero
        for (int i = 0; i < filas; i++) {
            String linea = reader.readLine();
            
            if (linea == null) {
                throw new IllegalArgumentException(
                    "El archivo tiene menos filas de las especificadas. Esperadas: " + filas + ", encontradas: " + i
                );
            }
            
            //procesar cada carácter de la línea
            procesarLineaTablero(tablero, linea, i, columnas);
        }
        
        return tablero;
    }
    
    //procesa una línea del archivo y configura las celdas correspondientes
    private static void procesarLineaTablero(Tablero tablero, String linea, int fila, int columnasEsperadas) 
            throws IllegalArgumentException {
        
        //verificar longitud de la línea
        if (linea.length() < columnasEsperadas) {
            throw new IllegalArgumentException(
                "La fila " + fila + " tiene menos columnas de las esperadas. Esperadas: " + 
                columnasEsperadas + ", encontradas: " + linea.length()
            );
        }
        
        //procesar cada carácter
        for (int j = 0; j < columnasEsperadas; j++) {
            char c = linea.charAt(j);
            EstadoCelda estado = interpretarCaracter(c, fila, j);
            tablero.setCelda(fila, j, estado);
        }
    }
    
    //Interpreta un carácter y retorna el estado correspondiente
    private static EstadoCelda interpretarCaracter(char c, int fila, int columna) 
        throws IllegalArgumentException {
    
        switch (Character.toLowerCase(c)) {
            case '.':
            case ' ':
                return new CeldaMuerta();
            
            case 'o':
                return new CeldaVivaExtendida();  
            
            case 'e':
                return new CeldaEnferma();
            
            case 'x':  
                return new CeldaLatente();
            
            default:
                throw new IllegalArgumentException(
                    "Carácter no reconocido '" + c + "' en [" + fila + "," + columna + "]. Válidos: . (muerta), O (viva), E (enferma), X (latente)"
                );
        }
    }
    
    //verifica si un archivo tiene un formato válido sin crear el tablero
    public static boolean validarFormato(String rutaArchivo) {
        try {
            cargarDesdeArchivo(rutaArchivo);
            return true;
        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
    }
    
    //retorna información sobre el formato de archivo esperado
    public static String getFormatoEsperado() {
        return "Formato de archivo:\n" +
               "Primera línea: filas columnas (números separados por espacio)\n" +
               "Siguientes líneas: caracteres representando estados de celdas\n" +
               "\nCaracteres válidos:\n" +
               "  . = celda muerta\n" +
               "  O, o = celda viva\n" +
               "  E, e = celda enferma (extensión)\n" +
               "  X, x = celda latente (extensión)\n" +
               "\nEjemplo:\n" +
               "4 5\n" +
               ".....\n" +
               "..O..\n" +
               "..O..\n" +
               "..O..";
    }
}