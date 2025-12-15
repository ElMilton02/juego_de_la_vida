package modelo;

import estados_celdas.EstadoCelda;
import estados_celdas.CeldaMuerta;

/*
 *representa el tablero completo del Juego de la Vida
 * 
 *el tablero es una matriz de n×m celdas que evolucionan generación tras
 *generación según las reglas del juego. Esta clase es responsable de:
 * - mantener el estado actual de todas las celdas
 * - contar vecinos vivos de cada celda
 * - calcular la siguiente generación
 * - detectar si hubo cambios (estabilidad)
 */
public class Tablero {
    
    private Celda[][] celdas;
    private final int filas;
    private final int columnas;
    
    //Constructor del tablero, inicializa todas las celdas en estado muerto por defecto
    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.celdas = new Celda[filas][columnas];
        
        //inicializar todas las celdas en estado muerto
        inicializarTablero();
    }
    
    //inicializa el tablero con todas las celdas muertas
    private void inicializarTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = new Celda(i, j, new CeldaMuerta());
            }
        }
    }

    //establece el estado de una celda específica en el tablero
    public void establecerEstadoCelda(int fila, int columna, EstadoCelda nuevoEstado) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            celdas[fila][columna].setEstado(nuevoEstado);
        } else {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
    }
    
    //establece el estado de una celda específica
    public void setCelda(int fila, int columna, EstadoCelda estado) {
        if (posicionValida(fila, columna)) {
            celdas[fila][columna].setEstado(estado);
        }
    }
    
    //obtiene una celda específica del tablero
    public Celda getCelda(int fila, int columna) {
        if (posicionValida(fila, columna)) {
            return celdas[fila][columna];
        }
        return null;
    }
    
    //verifica si una posición es válida dentro del tablero
    private boolean posicionValida(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }
    
    /*
     *cuenta la cantidad de vecinos vivos de una celda específica
     *considera los 8 vecinos adyacentes 
     */
    public int contarVecinosVivos(int fila, int columna) {
        int vecinosVivos = 0;
        
        //recorrer las 8 posiciones adyacentes
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                //saltar la celda central (la propia celda)
                if (i == 0 && j == 0) {
                    continue;
                }
                
                int vecinoFila = fila + i;
                int vecinoColumna = columna + j;
                
                //verificar si el vecino está dentro del tablero y está vivo
                if (posicionValida(vecinoFila, vecinoColumna) && 
                    celdas[vecinoFila][vecinoColumna].estaViva()) {
                    vecinosVivos++;
                }
            }
        }
        
        return vecinosVivos;
    }
    
    /*
     *evoluciona el tablero a la siguiente generación.
     * 
     *proceso:
     *1. Calcula el siguiente estado de todas las celdas
     *2. Actualiza todas las celdas simultáneamente
     *3. Detecta si hubo cambios
     */
    public boolean siguienteGeneracion() {
        //matriz temporal para almacenar los nuevos estados
        EstadoCelda[][] nuevosEstados = new EstadoCelda[filas][columnas];
        boolean huboCambios = false;
        
        //primero calcular todos los nuevos estados
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int vecinosVivos = contarVecinosVivos(i, j);
                nuevosEstados[i][j] = celdas[i][j].calcularSiguienteEstado(vecinosVivos);
                
                //verificar si hay cambio en esta celda
                if (!nuevosEstados[i][j].equals(celdas[i][j].getEstado())) {
                    huboCambios = true;
                }
            }
        }
        
        //luego actualizar todas las celdas con los nuevos estados
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j].setEstado(nuevosEstados[i][j]);
            }
        }
        
        return huboCambios;
    }
    
    //retorna el número de filas del tablero
    public int getFilas() {
        return filas;
    }
    
    //retorna el número de columnas del tablero
    public int getColumnas() {
        return columnas;
    }
    
    //genera una representación textual del tablero para visualización
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                sb.append(celdas[i][j].getCaracter());
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}