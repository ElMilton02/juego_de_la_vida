package estados_celdas;

/**
 *representa una celda viva
 *
 *con menos de 2 vecinos vivos: muere por soledad
 *con 2 o 3 vecinos vivos: sobrevive a la siguiente generación
 *con más de 3 vecinos vivos: muere por sobrepoblación
 *cuenta como vecino vivo para otras celdas
 */
public class CeldaViva extends EstadoCelda {
    
    @Override
    public EstadoCelda calcularSiguienteEstado(int vecinosVivos) {
        if (vecinosVivos < 2 || vecinosVivos > 3) {
            return new CeldaMuerta();  // Muere por soledad o sobrepoblación (sin probabilidades)
        } else {
            return new CeldaViva();    // Sobrevive (sin probabilidades)
        }
    }
    
    @Override
    public boolean estaViva() {
        return true;
    }
    
    @Override
    public char getCaracter() {
        return 'O';
    }
    
    @Override
    public String getNombre() {
        return "Viva";
    }
}