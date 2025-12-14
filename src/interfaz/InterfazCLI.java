package interfaz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import modelo.*;
import estados_celdas.*;

/*
 *interfaz de línea de comandos (CLI) para el Juego de la Vida
 *proporciona un menú interactivo que permite:
 *
 *cargar configuración inicial desde archivo (con menú de selección)
 *generar tablero aleatorio
 *ejecutar N generaciones
 *ejecutar indefinidamente con intervalo configurable
 *visualizar el estado del tablero en cada generación
 */
public class InterfazCLI {
    
    private Scanner scanner;
    private Simulacion simulacion;
    private boolean salir;
    private static final String DIRECTORIO_EJEMPLOS = "ejemplos";
    
    //constructor de la interfaz CLI
    public InterfazCLI() {
        this.scanner = new Scanner(System.in);
        this.simulacion = null;
        this.salir = false;
    }
    
    //inicia la interfaz CLI mostrando el menú principal
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
    
    //muestra el mensaje de bienvenida
    private void mostrarBienvenida() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║     JUEGO DE LA VIDA - Conway's Game      ║");
        System.out.println("║          Simulación Celular               ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        System.out.println();
    }
    
    //muestra el menú principal de opciones
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
    
    //lee una opción numérica del usuario
    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    //procesa la opción seleccionada por el usuario
    private void procesarOpcion(int opcion) {
        if (simulacion == null) {
            procesarOpcionSinTablero(opcion);
        } else {
            procesarOpcionConTablero(opcion);
        }
    }
    
    //procesa opciones cuando no hay tablero cargado
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
    
    //procesa opciones cuando hay un tablero cargado
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
    
    //muestra un menú con los archivos disponibles en el directorio de ejemplos
    private void cargarDesdeArchivo() {
        //obtener lista de archivos en el directorio ejemplos
        List<File> archivos = listarArchivosEjemplos();
        
        if (archivos.isEmpty()) {
            System.out.println("\n⚠ No se encontraron archivos en el directorio '" + DIRECTORIO_EJEMPLOS + "'");
            System.out.print("Ingrese la ruta del archivo manualmente: ");
            String ruta = scanner.nextLine().trim();
            cargarArchivoDesdeRuta(ruta);
            return;
        }
        
        //mostrar menú de selección
        while (true) {
            mostrarMenuArchivos(archivos);
            int opcion = leerOpcion();
            
            if (opcion == 0) {
                return; //volver al menú principal
            } else if (opcion > 0 && opcion <= archivos.size()) {
                //cargar archivo seleccionado
                File archivo = archivos.get(opcion - 1);
                cargarArchivoDesdeRuta(archivo.getPath());
                return;
            } else if (opcion == archivos.size() + 1) {
                //opción de ingresar ruta manualmente
                System.out.print("\nIngrese la ruta del archivo: ");
                String ruta = scanner.nextLine().trim();
                cargarArchivoDesdeRuta(ruta);
                return;
            } else {
                System.out.println("✗ Opción inválida. Intente nuevamente.");
            }
        }
    }
    
    //muestra el menú con los archivos disponibles
    private void mostrarMenuArchivos(List<File> archivos) {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│  ARCHIVOS DISPONIBLES EN " + DIRECTORIO_EJEMPLOS + "/       │");
        System.out.println("├─────────────────────────────────────────┤");
        
        for (int i = 0; i < archivos.size(); i++) {
            String nombre = archivos.get(i).getName();
            System.out.printf("│ %d. %-37s│%n", (i + 1), nombre);
        }
        
        System.out.printf("│ %d. %-37s│%n", (archivos.size() + 1), "[Ingresar ruta manualmente]");
        System.out.println("│ 0. Volver                               │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.print("Seleccione un archivo: ");
    }
    
    //lista todos los archivos .txt en el directorio de ejemplos
    private List<File> listarArchivosEjemplos() {
        List<File> archivos = new ArrayList<>();
        File directorio = new File(DIRECTORIO_EJEMPLOS);
        
        if (!directorio.exists() || !directorio.isDirectory()) {
            return archivos;
        }
        
        File[] listaArchivos = directorio.listFiles((dir, nombre) -> 
            nombre.toLowerCase().endsWith(".txt")
        );
        
        if (listaArchivos != null) {
            for (File archivo : listaArchivos) {
                archivos.add(archivo);
            }
        }
        
        return archivos;
    }
    
    //carga un tablero desde una ruta de archivo específica
    private void cargarArchivoDesdeRuta(String ruta) {
        try {
            Tablero tablero = LectorArchivos.cargarDesdeArchivo(ruta);
            simulacion = new Simulacion(tablero);
            
            System.out.println("\n✓ Tablero cargado exitosamente!");
            System.out.println("  Archivo: " + ruta);
            System.out.println("  Dimensiones: " + tablero.getFilas() + "x" + tablero.getColumnas());
            System.out.println("  Celdas vivas: " + simulacion.contarCeldasVivas());
            mostrarTablero();
            
        } catch (IOException e) {
            System.err.println("\n✗ Error al leer el archivo:");
            System.err.println("  " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("\n✗ Formato de archivo inválido:");
            System.err.println("  " + e.getMessage());
        }
    }
    
    //genera un tablero aleatorio
    private void generarTableroAleatorio() {
        System.out.print("\nIngrese número de filas: ");
        int filas = leerOpcion();
        
        if (filas <= 0) {
            System.out.println("✗ El número de filas debe ser positivo.");
            return;
        }
        
        System.out.print("Ingrese número de columnas: ");
        int columnas = leerOpcion();
        
        if (columnas <= 0) {
            System.out.println("✗ El número de columnas debe ser positivo.");
            return;
        }
        
        System.out.print("Ingrese probabilidad de celda viva (0.0-1.0, ej: 0.3): ");
        double probabilidad;
        try {
            probabilidad = Double.parseDouble(scanner.nextLine().trim());
            if (probabilidad < 0 || probabilidad > 1) {
                System.out.println("✗ La probabilidad debe estar entre 0.0 y 1.0");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Probabilidad inválida.");
            return;
        }
        
        Tablero tablero = new Tablero(filas, columnas);
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (Math.random() < probabilidad) {
                    tablero.establecerEstadoCelda(i, j, new CeldaVivaExtendida());
                }
            }
        }
        
        simulacion = new Simulacion(tablero);
        System.out.println("\n✓ Tablero aleatorio generado!");
        System.out.println("  Dimensiones: " + filas + "x" + columnas);
        System.out.println("  Celdas vivas: " + simulacion.contarCeldasVivas());
        mostrarTablero();
    }  
    
    //muestra el tablero actual en consola
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
    
    //crea el marco superior del tablero
    private String crearMarcoSuperior() {
        int ancho = simulacion.getTablero().getColumnas() * 2 + 2;
        return "┌" + "─".repeat(ancho) + "┐";
    }
    
    //crea el marco inferior del tablero
    private String crearMarcoInferior() {
        int ancho = simulacion.getTablero().getColumnas() * 2 + 2;
        return "└" + "─".repeat(ancho) + "┘";
    }
    
    //ejecuta una sola generación
    private void ejecutarUnaGeneracion() {
        boolean huboCambios = simulacion.ejecutarGeneracion();
        mostrarTablero();
        
        if (!huboCambios) {
            System.out.println("⚠ El tablero se ha estabilizado (no hay más cambios).");
        }
    }
    
    //ejecuta N generaciones
    private void ejecutarNGeneraciones() {
        System.out.print("\n¿Cuántas generaciones desea ejecutar?: ");
        int n = leerOpcion();
        
        if (n <= 0) {
            System.out.println("✗ El número debe ser positivo.");
            return;
        }
        
        System.out.print("¿Mostrar cada generación? (s/n): ");
        boolean mostrar = scanner.nextLine().trim().equalsIgnoreCase("s");
        
        if (mostrar) {
            System.out.print("Intervalo entre generaciones en ms (ej: 300): ");
            int intervalo = leerOpcion();
            if (intervalo < 0) intervalo = 300;
            
            System.out.println("\nEjecutando " + n + " generaciones...\n");
            
            for (int i = 0; i < n; i++) {
                boolean huboCambios = simulacion.ejecutarGeneracion();
                
                mostrarTablero();
                
                if (!huboCambios) {
                    System.out.println("⚠ El tablero se estabilizó en la generación " + simulacion.getGeneracionActual());
                    break;
                }
                
                try {
                    Thread.sleep(intervalo);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } else {
            System.out.println("\nEjecutando " + n + " generaciones...");
            
            for (int i = 0; i < n; i++) {
                boolean huboCambios = simulacion.ejecutarGeneracion();
                
                if (!huboCambios) {
                    System.out.println("⚠ El tablero se estabilizó en la generación " + simulacion.getGeneracionActual());
                    mostrarTablero();
                    break;
                }
            }
            
            mostrarTablero();
        }
        
        System.out.println("✓ Simulación completada.");
    }
    
    //ejecuta la simulación en modo continuo
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
                
                //limpiar pantalla (simulado)
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
    
    //muestra estadísticas de la simulación
    private void mostrarEstadisticas() {
        System.out.println("\n📊 Estadísticas:");
        System.out.println("   " + simulacion.getEstadisticas());
    }
    
    //reinicia la simulación
    private void reiniciar() {
        simulacion = null;
        System.out.println("\n✓ Simulación reiniciada. Cargue un nuevo tablero.");
    }
    
    //muestra la ayuda con el formato de archivo
    private void mostrarAyuda() {
        System.out.println("\n" + LectorArchivos.getFormatoEsperado());
    }
}