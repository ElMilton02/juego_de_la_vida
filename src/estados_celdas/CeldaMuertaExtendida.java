package estados_celdas;

public class CeldaMuertaExtendida extends CeldaMuerta {
    
    @Override
    public EstadoCelda calcularSiguienteEstado(int vecinosVivos) {
        if (vecinosVivos == 3) {
            return new CeldaVivaExtendida();  // Revive con extensiones
        } else {
            return new CeldaMuertaExtendida();  // Permanece muerta (consistente)
        }
    }
}