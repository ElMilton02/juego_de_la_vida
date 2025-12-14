package modelo;

/*
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
            
            //si no hubo cambios, el tablero se estabilizó
            if (!huboCambios) {
                return false;
            }
        }
        return true;
    }
    
    /*
     *ejecuta generaciones indefinidamente con un intervalo de tiempo entre cada una
     *se detiene si el tablero se estabiliza o si se llama a detener()
     *este método sirve para la visualización en tiempo real.
     */
    public void ejecutarIndefinidamente(int intervaloMs) {
        enEjecucion = true;
        
        while (enEjecucion) {
            boolean huboCambios = ejecutarGeneracion();
            
            //si no hubo cambios, el tablero se estabilizó
            if (!huboCambios) {
                System.out.println("El tablero se estabilizó en la generación " + generacionActual);
                break;
            }
            
            //esperar el intervalo especificado
            try {
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                System.err.println("Simulación interrumpida: " + e.getMessage());
                break;
            }
        }
        
        enEjecucion = false;
    }
    
    //detiene la ejecución indefinida de la simulación
    public void detener() {
        enEjecucion = false;
    }
    
    /*
     *reinicia la simulación a la generación 0.
     *no reinicia el estado del tablero, solo el contador.
     */
    public void reiniciar() {
        generacionActual = 0;
        enEjecucion = false;
    }
    
    //retorna el tablero actual de la simulación
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
    
    //retorna el número de la generación actual
    public int getGeneracionActual() {
        return generacionActual;
    }
    
    //indica si la simulación está en ejecución
    public boolean estaEnEjecucion() {
        return enEjecucion;
    }
    
    //cuenta el total de celdas vivas en el tablero actual
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
    
    //retorna información del estado actual de la simulación
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