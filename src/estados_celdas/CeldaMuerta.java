package estados_celdas;

/*
 *representa una celda muerta
 *
 *si tiene exactamente 3 vecinos vivos revive(con cualquier otra cantidad, sigue muerta)
 *no cuenta como vecino vivo para otras celdas
 */
public class CeldaMuerta extends EstadoCelda {
    
    /**
     *Calcula el siguiente estado según las reglas del Juego de la Vida
     *si tiene 3 vecinos vivos se combierte en una selda viva
     */
    @Override
    public EstadoCelda calcularSiguienteEstado(int vecinosVivos) {
        if (vecinosVivos == 3) {
            // Revive por reproducción
            return new CeldaViva();
        } else {
            // Permanece muerta
            return new CeldaMuerta();
        }
    }
    
    //una celda muerta se considera muerta
    @Override
    public boolean estaViva() {
        return false;
    }
    
    //representacion visual de una celda muerta
    @Override
    public char getCaracter() {
        return '.';
    }
    
    //nombre descriptivo
    @Override
    public String getNombre() {
        return "Muerta";
    }
}