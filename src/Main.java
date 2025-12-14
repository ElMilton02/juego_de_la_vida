

import interfaz.InterfazCLI;
import interfaz.InterfazGUI;
import modelo.Simulacion;
import modelo.Tablero;
import javax.swing.UIManager;

/*
 *clase principal del programa - Juego de la Vida.
 *permite elegir entre interfaz CLI (consola) o GUI (gráfica).
 */
public class Main {
    
    //metodo main - punto de entrada del programa
    public static void main(String[] args) {
        
        //determinar qué interfaz usar
        String modo = "";
        
        if (args.length > 0) {
            modo = args[0].toLowerCase();
        } else {
            System.out.println("╔═══════════════════════════════════════════╗");
            System.out.println("║  JUEGO DE LA VIDA - Conway's Game of Life ║");
            System.out.println("╚═══════════════════════════════════════════╝");
            System.out.println();
            System.out.println("Seleccione la interfaz:");
            System.out.println("  1. CLI (Consola/Terminal)");
            System.out.println("  2. GUI (Ventana Gráfica)");
            System.out.println();
            System.out.print("Opción (1 o 2): ");
            
            try {
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                int opcion = scanner.nextInt();
                modo = (opcion == 2) ? "gui" : "cli";
            } catch (Exception e) {
                modo = "cli"; 
            }
        }
        
        //iniciar la interfaz correspondiente
        if (modo.equals("gui")) {
            iniciarGUI();
        } else {
            iniciarCLI();
        }
    }
    
    //inicia la interfaz CLI (consola).
    private static void iniciarCLI() {
        InterfazCLI interfaz = new InterfazCLI();
        interfaz.iniciar();
    }
    
    //inicia la interfaz GUI (gráfica).
    private static void iniciarGUI() {
        //configurar Look and Feel para que se vea mejor
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //si falla, usar el look and feel por defecto
        }
        
        //crear simulación inicial con tablero pequeño
        Tablero tableroInicial = new Tablero(20, 30);
        Simulacion simulacion = new Simulacion(tableroInicial);
        
        //crear y mostrar la GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new InterfazGUI(simulacion);
        });
    }
}