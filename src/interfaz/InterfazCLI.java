package interfaz;

import java.io.IOException;
import java.util.Scanner;
import modelo.*;
import estados_celdas.*;

/**
 * Interfaz de línea de comandos (CLI) para el Juego de la Vida.
 * 
 * Proporciona un menú interactivo que permite:
 * - Cargar configuración inicial desde archivo
 * - Generar tablero aleatorio
 * - Ejecutar N generaciones
 * - Ejecutar indefinidamente con intervalo configurable
 * - Visualizar el estado del tablero en cada generación
 * 
 * Esta es la interfaz OBLIGATORIA según el enunciado del TPE.
 */
public class InterfazCLI {
    
    private Scanner scanner;
    private Simulacion simulacion;
    private boolean salir;
    
    /**
     * Constructor de la interfaz CLI.
     */
    public InterfazCLI() {
        this.scanner = new Scanner(System.in);
        this.simulacion = null;
        this.salir = false;
    }
    
    /**
     * Inicia la interfaz CLI mostrando el menú principal.
     */
    public void iniciar() {
        mostrarBienvenida();
        
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerOpcion();
            procesarOpcion(opcion);
        }
        
        scanner.close();
        System.out.println("\n¡Hasta luego!");
    }
    
    /**
     * Muestra el mensaje de bienvenida.
     */
    private void mostrarBienvenida() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║     JUEGO DE LA VIDA - Conway's Game      ║");
        System.out.println("║          Simulación Celular               ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Muestra el menú principal de opciones.
     */
    private void mostrarMenuPrincipal() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│           MENÚ PRINCIPAL                │");
        System.out.println("├─────────────────────────────────────────┤");
        
        if (simulacion == null) {
            System.out.println("│ 1. Cargar tablero desde archivo         │");
            System.out.println("│ 2. Generar tablero aleatorio            │");
            System.out.println("│ 3. Ayuda (formato de archivo)           │");
            System.out.println("│ 0. Salir                                │");
        } else {
            System.out.println("│ 1. Ver tablero actual                   │");
            System.out.println("│ 2. Ejecutar 1 generación                │");
            System.out.println("│ 3. Ejecutar N generaciones              │");
            System.out.println("│ 4. Ejecutar modo continuo               │");
            System.out.println("│ 5. Ver estadísticas                     │");
            System.out.println("│ 6. Reiniciar (cargar nuevo tablero)     │");
            System.out.println("│ 0. Salir                                │");
        }
        
        System.out.println("└─────────────────────────────────────────┘");
        System.out.print("Seleccione una opción: ");
    }
    
    /**
     * Lee una opción numérica del usuario.
     * 
     * @return número de opción ingresado
     */
    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Procesa la opción seleccionada por el usuario.
     * 
     * @param opcion número de opción
     */
    private void procesarOpcion(int opcion) {
        if (simulacion == null) {
            procesarOpcionSinTablero(opcion);
        } else {
            procesarOpcionConTablero(opcion);
        }
    }
    
    /**
     * Procesa opciones cuando no hay tablero cargado.
     * 
     * @param opcion número de opción
     */
    private void procesarOpcionSinTablero(int opcion) {
        switch (opcion) {
            case 1:
                cargarDesdeArchivo();
                break;
            case 2:
                generarTableroAleatorio();
                break;
            case 3:
                mostrarAyuda();
                break;
            case 0:
                salir = true;
                break;
            default:
                System.out.println("✗ Opción inválida. Intente nuevamente.");
        }
    }
    
    /**
     * Procesa opciones cuando hay un tablero cargado.
     * 
     * @param opcion número de opción
     */
    private void procesarOpcionConTablero(int opcion) {
        switch (opcion) {
            case 1:
                mostrarTablero();
                break;
            case 2:
                ejecutarUnaGeneracion();
                break;
            case 3:
                ejecutarNGeneraciones();
                break;
            case 4:
                ejecutarModoContinuo();
                break;
            case 5:
                mostrarEstadisticas();
                break;
            case 6:
                reiniciar();
                break;
            case 0:
                salir = true;
                break;
            default:
                System.out.println("✗ Opción inválida. Intente nuevamente.");
        }
    }
    
    /**
     * Carga un tablero desde un archivo.
     */
    private void cargarDesdeArchivo() {
        System.out.print("\nIngrese la ruta del archivo: ");
        String ruta = scanner.nextLine().trim();
        
        try {
            Tablero tablero = LectorArchivos.cargarDesdeArchivo(ruta);
            simulacion = new Simulacion(tablero);
            
            System.out.println("✓ Tablero cargado exitosamente!");
            System.out.println("  Dimensiones: " + tablero.getFilas() + "x" + tablero.getColumnas());
            System.out.println("  Celdas vivas: " + simulacion.contarCeldasVivas());
            mostrarTablero();
            
        } catch (IOException e) {
            System.err.println("✗ Error al leer el archivo:");
            System.err.println("  " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("✗ Formato de archivo inválido:");
            System.err.println("  " + e.getMessage());
        }
    }
    
    /**
     * Genera un tablero aleatorio.
     */
    private void generarTableroAleatorio() {
        System.out.print("Ingrese filas: ");
        int filas = scanner.nextInt();
        System.out.print("Ingrese columnas: ");
        int columnas = scanner.nextInt();
        
        Tablero tablero = new Tablero(filas, columnas);  // Esto ya crea todas las celdas como muertas extendidas
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (Math.random() < 0.3) {  // Por ejemplo, 30% de probabilidad de estar viva (ajustable)
                    tablero.establecerEstadoCelda(i, j, new CeldaVivaExtendida());
                }
                // Si no, queda como CeldaMuertaExtendida (ya inicializada en el constructor)
            }
        }
        
        simulacion = new Simulacion(tablero);
        System.out.println("\n¡Tablero aleatorio generado!");
        mostrarTablero();
    }  
    
    /**
     * Muestra el tablero actual en consola.
     */
    private void mostrarTablero() {
        System.out.println("\n" + crearMarcoSuperior());
        System.out.println("  Generación: " + simulacion.getGeneracionActual());
        System.out.println(crearMarcoSuperior());
        
        Tablero tablero = simulacion.getTablero();
        for (int i = 0; i < tablero.getFilas(); i++) {
            System.out.print("│ ");
            for (int j = 0; j < tablero.getColumnas(); j++) {
                System.out.print(tablero.getCelda(i, j).getCaracter() + " ");
            }
            System.out.println("│");
        }
        
        System.out.println(crearMarcoInferior());
    }
    
    /**
     * Crea el marco superior del tablero.
     */
    private String crearMarcoSuperior() {
        int ancho = simulacion.getTablero().getColumnas() * 2 + 2;
        return "┌" + "─".repeat(ancho) + "┐";
    }
    
    /**
     * Crea el marco inferior del tablero.
     */
    private String crearMarcoInferior() {
        int ancho = simulacion.getTablero().getColumnas() * 2 + 2;
        return "└" + "─".repeat(ancho) + "┘";
    }
    
    /**
     * Ejecuta una sola generación.
     */
    private void ejecutarUnaGeneracion() {
        boolean huboCambios = simulacion.ejecutarGeneracion();
        mostrarTablero();
        
        if (!huboCambios) {
            System.out.println("⚠ El tablero se ha estabilizado (no hay más cambios).");
        }
    }
    
    /**
     * Ejecuta N generaciones.
     */
    private void ejecutarNGeneraciones() {
        System.out.print("\n¿Cuántas generaciones desea ejecutar?: ");
        int n = leerOpcion();
        
        if (n <= 0) {
            System.out.println("✗ El número debe ser positivo.");
            return;
        }
        
        System.out.print("¿Mostrar cada generación? (s/n): ");
        boolean mostrar = scanner.nextLine().trim().equalsIgnoreCase("s");
        
        System.out.println("\nEjecutando " + n + " generaciones...\n");
        
        for (int i = 0; i < n; i++) {
            boolean huboCambios = simulacion.ejecutarGeneracion();
            
            if (mostrar) {
                mostrarTablero();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    break;
                }
            }
            
            if (!huboCambios) {
                System.out.println("⚠ El tablero se estabilizó en la generación " + simulacion.getGeneracionActual());
                break;
            }
        }
        
        if (!mostrar) {
            mostrarTablero();
        }
        
        System.out.println("✓ Simulación completada.");
    }
    
    /**
     * Ejecuta la simulación en modo continuo.
     */
    private void ejecutarModoContinuo() {
        System.out.print("\nIntervalo entre generaciones (ms): ");
        int intervalo = leerOpcion();
        
        if (intervalo < 0) {
            System.out.println("✗ El intervalo debe ser positivo.");
            return;
        }
        
        System.out.println("\nEjecutando en modo continuo...");
        System.out.println("(Presione Ctrl+C para detener)\n");
        
        try {
            while (true) {
                boolean huboCambios = simulacion.ejecutarGeneracion();
                
                // Limpiar pantalla (simulado)
                System.out.print("\033[H\033[2J");
                System.out.flush();
                
                mostrarTablero();
                mostrarEstadisticas();
                
                if (!huboCambios) {
                    System.out.println("\n⚠ El tablero se ha estabilizado.");
                    break;
                }
                
                Thread.sleep(intervalo);
            }
        } catch (InterruptedException e) {
            System.out.println("\n✓ Simulación detenida.");
        }
        
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
    
    /**
     * Muestra estadísticas de la simulación.
     */
    private void mostrarEstadisticas() {
        System.out.println("\n📊 Estadísticas:");
        System.out.println("   " + simulacion.getEstadisticas());
    }
    
    /**
     * Reinicia la simulación.
     */
    private void reiniciar() {
        simulacion = null;
        System.out.println("\n✓ Simulación reiniciada. Cargue un nuevo tablero.");
    }
    
    /**
     * Muestra la ayuda con el formato de archivo.
     */
    private void mostrarAyuda() {
        System.out.println("\n" + LectorArchivos.getFormatoEsperado());
    }
}