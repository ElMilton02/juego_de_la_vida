# Juego de la Vida

Implementación en Java del autómata celular de John Conway con extensiones de estados adicionales.

## Descripción

Este proyecto implementa una simulación del Juego de la Vida de Conway con soporte para múltiples estados de celdas y dos interfaces de usuario (CLI y GUI). El diseño orientado a objetos permite extender fácilmente el juego con nuevos estados y reglas sin modificar el código existente.

### Características principales

- Simulación completa del Juego de la Vida clásico
- Estados extendidos: Celda Enferma y Celda Latente
- Interfaz de línea de comandos (CLI) obligatoria
- Interfaz gráfica (GUI)
- Carga de configuraciones desde archivo
- Generación de tableros aleatorios
- Ejecución paso a paso o continua
- Detección automática de estabilización
- Control de velocidad de simulación

## Reglas del Juego

### Reglas Básicas (Estados Clásicos)

**Celda Viva (O)**
- Con menos de 2 vecinos vivos → muere por soledad
- Con 2 o 3 vecinos vivos → sobrevive
- Con más de 3 vecinos vivos → muere por sobrepoblación

**Celda Muerta (.)**
- Con exactamente 3 vecinos vivos → revive (reproducción)
- Con cualquier otro número → permanece muerta

### Extensiones (Estados Adicionales)

**Celda Enferma (E)**
- Se considera viva para conteo de vecinos
- Una celda viva tiene 25% de probabilidad de enfermarse al sobrevivir
- Una celda enferma siempre muere en la siguiente generación

**Celda Latente (X)**
- Se considera muerta para conteo de vecinos
- Con exactamente 1 vecino vivo → revive
- Con cualquier otro número → permanece latente
- Una celda viva tiene 25% de probabilidad de volverse latente al morir

### Estructura del Proyecto
```
juego-de-la-vida/
├── modelo/
│   ├── Celda.java              # Representa una celda individual
│   ├── Tablero.java            # Grilla completa del juego
│   └── Simulacion.java         # Controlador de la simulación
├── estados_celdas/
│   ├── EstadoCelda.java        # Clase abstracta base (patrón State)
│   ├── CeldaViva.java          # Estado vivo clásico
│   ├── CeldaMuerta.java        # Estado muerto clásico
│   ├── CeldaVivaExtendida.java # Viva con probabilidad de enfermarse
│   ├── CeldaMuertaExtendida.java # Muerta extendida
│   ├── CeldaEnferma.java       # Estado enfermo (extensión)
│   └── CeldaLatente.java       # Estado latente (extensión)
├── interfaz/
│   ├── InterfazCLI.java        # Interfaz de consola
│   ├── InterfazGUI.java        # Interfaz gráfica
│   └── LectorArchivos.java     # Carga desde archivos
├── ejemplos/
│   ├── glider.txt              # Planeador clásico
│   ├── blinker.txt             # Oscilador simple
│   └── pulsar.txt              # Oscilador complejo
└── Main.java                   # Punto de entrada
```

### Requisitos

- Java JDK 8 o superior
- No se requieren dependencias externas

### Compilación

```bash
# Compilar todos los archivos
javac Main.java modelo/*.java estados_celdas/*.java interfaz/*.java

# Alternativamente, compilar todo el directorio
javac -d bin *.java modelo/*.java estados_celdas/*.java interfaz/*.java
```

### Ejecución

**Modo interactivo** (el programa pregunta qué interfaz usar):
```bash
java Main
```

**Interfaz CLI directamente**:
```bash
java Main cli
```

**Interfaz GUI directamente**:
```bash
java Main gui
```

## Formato de Archivos

Los archivos de configuración deben seguir este formato:

```
<filas> <columnas>
<línea1>
<línea2>
...
<líneaN>
```

# Archivos de Ejemplo Incluidos

### `ejemplos/glider.txt`
### `ejemplos/corazon.txt`
### `ejemplos/caos.txt`
### `ejemplos/blinker.txt`
### `ejemplos/pulsar.txt`
