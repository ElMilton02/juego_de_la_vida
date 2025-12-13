package estados_celdas;

/*
 *representa el estado de una celda latente (Extensión)
 * 
 *se considera MUERTA para el conteo de vecinos
 *revive con exactamente 1 vecino vivo (diferente a celda muerta)
 *con cualquier otro número de vecinos, permanece latente
 *en el enunciado no se menciona la condicion para que aparesca, asique hice que tuviera la misma que celda enferma
 */
public class CeldaLatente extends EstadoCelda {
    
    @Override
    public EstadoCelda calcularSiguienteEstado(int vecinosVivos) {
        if (vecinosVivos == 1) {
            return new CeldaVivaExtendida();  // Revive con extensiones
        } else {
            return new CeldaLatente();
        }
    }
    
    @Override
    public boolean estaViva() {
        return false;
    }
    
    @Override
    public char getCaracter() {
        return 'X';  // Según PDF
    }
    
    @Override
    public String getNombre() {
        return "Latente";
    }
}