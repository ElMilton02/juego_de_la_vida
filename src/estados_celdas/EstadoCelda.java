package estados_celdas;


//clase abstracta base del cual surguen todos los estados de las celdas
public abstract class EstadoCelda {
    
    /*
     *Calcula el próximo estado de la celda basándose en la cantidad de vecinos vivos que tiene en la generación actual
     *implementa las reglas específicas de evolución para cada estado
     *como parametro toma la cantidad de vecinos vivos (0-8) y devuelve el nuevo estado que tendra la celda
     */
    public abstract EstadoCelda calcularSiguienteEstado(int vecinosVivos);
    
    //indica si la celda se considera viva 
    public abstract boolean estaViva();
    
    //indica el caracter que tiene cada celda para mostrar en el tablero
    public abstract char getCaracter();
    
    //retorna el nombre del estado
    public abstract String getNombre();
    
    /**
     * Sobrescribimos equals para comparar estados por tipo.
     * Dos estados del mismo tipo se consideran iguales.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        return this.getClass() == obj.getClass();
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode();  
    }
    
    @Override
    public String toString() {
        return getNombre();
    }
}