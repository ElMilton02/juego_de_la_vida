package estados_celdas;

public class CeldaVivaExtendida extends CeldaViva {

    @Override
    public EstadoCelda calcularSiguienteEstado(int vecinosVivos) {
        // Primero, usamos las reglas clásicas del padre (sin extensiones)
        EstadoCelda estadoBase = super.calcularSiguienteEstado(vecinosVivos);

        // Si el base indica que moriría (retorna Muerta)
        if (estadoBase instanceof CeldaMuerta) {
            if (Math.random() < 0.25) {
                return new CeldaLatente();  // 25% de volverse latente en lugar de muerta
            } else {
                return estadoBase;  // 75% muere normal
            }
        } 
        // Si el base indica que sobreviviría (retorna Viva)
        else if (estadoBase instanceof CeldaViva) {
            if (Math.random() < 0.25) {
                return new CeldaEnferma();  // 25% de enfermarse en lugar de sobrevivir normal
            } else {
                return new CeldaVivaExtendida();  // 75% sobrevive, pero mantiene la extensión
            }
        }

        // No debería llegar aquí, pero por seguridad
        return this;
    }

    @Override
    public char getCaracter() {
        return 'O';  // Igual que una viva normal
    }

    @Override
    public String getNombre() {
        return "Viva";
    }
}