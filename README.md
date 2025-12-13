# Juego de la Vida

## Compilar y Ejecutar
javac -d bin src/**/*.java
java -cp bin Main cli  # o gui

## Diseño
- Modelo: Celda delega comportamiento a EstadoCelda (State pattern, polimorfismo).
- Tablero gestiona celdas y evolución.
- Simulacion controla generaciones y estabilidad.
- Interfaces: CLI (obligatoria), GUI (bonus con Swing).

## Extender
Crea subclase de EstadoCelda (ej. NuevaEstado), implementa calcularSiguienteEstado, estaViva, getCaracter, getNombre.
Actualiza LectorArchivos.interpretarCaracter para nuevo char.
Usa en archivos iniciales o en lógica (ej. probabilidades en Extendida).

## Extensiones
- Enferma: 25% al sobrevivir (como PDF).
- Latente: revive con 1 vecino (como PDF); extra: 25% al morir.

## Ejemplos
En ejemplos/: glider.txt (O para vivo), enferma.txt (E), latente.txt (X).