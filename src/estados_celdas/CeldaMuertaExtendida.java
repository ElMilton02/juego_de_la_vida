package estados_celdas;

public class CeldaMuertaExtendida extends CeldaMuerta {
    
    @Override
    public EstadoCelda calcularSiguienteEstado(int vecinosVivos) {
        if (vecinosVivos == 3) {
            return new CeldaVivaExtendida();  //revive con extensiones
        } else {
            return new CeldaMuertaExtendida();  //permanece muerta (consistente)
        }
    }
}