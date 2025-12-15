package estados_celdas;

/*
 *representa el estado de una celda enferma (Extensión):
 *
 *se considera viva para el conteo de vecinos
 *una celda viva tiene 25% de probabilidad de enfermarse en lugar de sobrevivir
 *una celda enferma SIEMPRE muere en la siguiente generación
 *extiende el juego sin modificar ninguna clase existente,
 */
public class CeldaEnferma extends EstadoCelda {
    
    @Override
    public EstadoCelda calcularSiguienteEstado(int vecinosVivos) {
        return new CeldaMuerta();  // Siempre muere
    }
    
    @Override
    public boolean estaViva() {
        return true;
    }
    
    @Override
    public char getCaracter() {
        return 'E';
    }
    
    @Override
    public String getNombre() {
        return "Enferma";
    }
}