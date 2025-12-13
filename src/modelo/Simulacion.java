package modelo;

/**
 *controla la simulación
 * 
 *coordina la evolución del tablero a través de múltiples generaciones, maneja la velocidad de simulación y detecta condiciones de parada (estabilidad).
 * 
 *puede:
 *ejecutar un número determinado de generaciones
 *ejecutar generaciones indefinidamente
 *controlar intervalos de tiempo entre generaciones
 *detectar cuando el tablero se estabiliza
 *mantener estadísticas de la simulación
 */
public class Simulacion {
    
    private Tablero tablero;
    private int generacionActual;
    private boolean enEjecucion;
    
    //constructor de la simulación
    public Simulacion(Tablero tablero) {
        this.tablero = tablero;
        this.generacionActual = 0;
        this.enEjecucion = false;
    }
    
    /*
     *ejecuta una única generación de la simulación
     *retorna true si hubo cambios, de lo contrario retorna false (tablero estabilizado) 
     */
    public boolean ejecutarGeneracion() {
        boolean huboCambios = tablero.siguienteGeneracion();
        generacionActual++;
        return huboCambios;
    }
    
    //ejecuta un número específico de generaciones y se detiene antes si el tablero se estabiliza
    public boolean ejecutarGeneraciones(int numGeneraciones) {
        for (int i = 0; i < numGeneraciones; i++) {
            boolean huboCambios = ejecutarGeneracion();
            
            // Si no hubo cambios, el tablero se estabilizó
            if (!huboCambios) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Ejecuta generaciones indefinidamente con un intervalo de tiempo entre cada una.
     * Se detiene si el tablero se estabiliza o si se llama a detener().
     * 
     * Este método es útil para visualización en tiempo real.
     * 
     * @param intervaloMs intervalo en milisegundos entre generaciones
     */
    public void ejecutarIndefinidamente(int intervaloMs) {
        enEjecucion = true;
        
        while (enEjecucion) {
            boolean huboCambios = ejecutarGeneracion();
            
            // Si no hubo cambios, el tablero se estabilizó
            if (!huboCambios) {
                System.out.println("El tablero se estabilizó en la generación " + generacionActual);
                break;
            }
            
            // Esperar el intervalo especificado
            try {
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                System.err.println("Simulación interrumpida: " + e.getMessage());
                break;
            }
        }
        
        enEjecucion = false;
    }
    
    /**
     * Detiene la ejecución indefinida de la simulación.
     */
    public void detener() {
        enEjecucion = false;
    }
    
    /**
     * Reinicia la simulación a la generación 0.
     * NOTA: No reinicia el estado del tablero, solo el contador.
     */
    public void reiniciar() {
        generacionActual = 0;
        enEjecucion = false;
    }
    
    /**
     * Retorna el tablero actual de la simulación.
     * 
     * @return el tablero
     */
    public Tablero getTablero() {
        return tablero;
    }
    
    /**
     * Establece un nuevo tablero para la simulación.
     * Reinicia el contador de generaciones.
     * 
     * @param tablero el nuevo tablero
     */
    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
        this.generacionActual = 0;
        this.enEjecucion = false;
    }
    
    /**
     * Retorna el número de la generación actual.
     * 
     * @return número de generación actual
     */
    public int getGeneracionActual() {
        return generacionActual;
    }
    
    /**
     * Indica si la simulación está en ejecución.
     * 
     * @return true si está ejecutándose, false en caso contrario
     */
    public boolean estaEnEjecucion() {
        return enEjecucion;
    }
    
    /**
     * Cuenta el total de celdas vivas en el tablero actual.
     * Útil para estadísticas y visualización.
     * 
     * @return cantidad de celdas vivas
     */
    public int contarCeldasVivas() {
        int vivas = 0;
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                if (tablero.getCelda(i, j).estaViva()) {
                    vivas++;
                }
            }
        }
        return vivas;
    }
    
    /**
     * Retorna información del estado actual de la simulación.
     * 
     * @return String con estadísticas de la simulación
     */
    public String getEstadisticas() {
        return String.format(
            "Generación: %d | Celdas vivas: %d | En ejecución: %s",
            generacionActual,
            contarCeldasVivas(),
            enEjecucion ? "Sí" : "No"
        );
    }
    
    @Override
    public String toString() {
        return "Simulación - " + getEstadisticas();
    }
}