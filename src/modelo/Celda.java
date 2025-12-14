package modelo;

import estados_celdas.EstadoCelda;

/*
 *representa una celda individual en el tablero:
 * 
 *cada celda tiene una posición fija (x, y) y un estado que puede cambiar
 *la celda delega su comportamiento al estado actual 
 */
public class Celda {
    
    //posición de la celda en el tablero
    private final int fila;
    private final int columna;
    
    //estado actual de la celda (Viva, Muerta, Enferma, Latente, etc...)
    private EstadoCelda estado;
    
    //constructor de la celda
    public Celda(int fila, int columna, EstadoCelda estadoInicial) {
        this.fila = fila;
        this.columna = columna;
        this.estado = estadoInicial;
    }
    
    //calcula el próximo estado de esta celda basándose en la cantidad de vecinos vivos delega el cálculo a simulacion
    public EstadoCelda calcularSiguienteEstado(int vecinosVivos) {
        return estado.calcularSiguienteEstado(vecinosVivos);
    }
    
    /*
     *actualiza el estado de la celda
     *este método se llama después de calcular todos los nuevos estados
     *actualiza el tablero a la siguiente generación
     */
    public void setEstado(EstadoCelda nuevoEstado) {
        this.estado = nuevoEstado;
    }
    
    //retorna el estado de la celda.
    public EstadoCelda getEstado() {
        return estado;
    }
    
    //indica si la celda está viva (delega al estado)
    public boolean estaViva() {
        return estado.estaViva();
    }
    
    //retorna el caracter de la celda 
    public char getCaracter() {
        return estado.getCaracter();
    }
    
    //retorna la fila
    public int getFila() {
        return fila;
    }
    
    //retorna la columna
    public int getColumna() {
        return columna;
    }
    
    //retorna una representación textual de la celda para debugging.
    @Override
    public String toString() {
        return "Celda[" + fila + "," + columna + "]=" + estado.getNombre();
    }

    /*
     *Compara dos celdas, son iguales si tienen la misma
     *posición y el mismo estado.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Celda otraCelda = (Celda) obj;
        return fila == otraCelda.fila && 
               columna == otraCelda.columna && 
               estado.equals(otraCelda.estado);
    }
    
    @Override
    public int hashCode() {
        int result = fila;
        result = 31 * result + columna;
        result = 31 * result + estado.hashCode();
        return result;
    }
}