package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import modelo.*;
import estados_celdas.*;

/**
 * Interfaz Gráfica de Usuario (GUI) para el Juego de la Vida.
 * 
 * Proporciona una visualización gráfica del tablero con controles
 * interactivos para ejecutar la simulación.
 * 
 * Componentes:
 * - Panel de tablero con visualización gráfica
 * - Botones de control (Iniciar, Pausar, Paso, Reiniciar)
 * - Control de velocidad (Slider)
 * - Estadísticas en tiempo real
 * - Menú para cargar archivos
 * 
 * Esta interfaz es OPCIONAL (Bonus) según el enunciado.
 */
public class InterfazGUI extends JFrame {
    
    // Componentes principales
    private Simulacion simulacion;
    private PanelTablero panelTablero;
    private Timer timer;
    
    // Controles
    private JButton btnIniciar;
    private JButton btnPausar;
    private JButton btnPaso;
    private JButton btnReiniciar;
    private JSlider sliderVelocidad;
    private JLabel lblEstadisticas;
    private JLabel lblVelocidad;
    
    // Estado
    private boolean ejecutando;
    private int intervalo; // milisegundos entre generaciones
    
    // Constantes de visualización
    private static final int TAMAÑO_CELDA = 20; // pixels por celda
    private static final Color COLOR_VIVA = new Color(34, 139, 34);      // Verde
    private static final Color COLOR_MUERTA = new Color(240, 240, 240);  // Gris claro
    private static final Color COLOR_ENFERMA = new Color(255, 69, 0);    // Rojo-naranja
    private static final Color COLOR_LATENTE = new Color(135, 206, 250); // Azul claro
    private static final Color COLOR_GRID = new Color(200, 200, 200);    // Gris para bordes
    
    /**
     * Constructor de la interfaz GUI.
     * 
     * @param simulacion la simulación del juego
     */
    public InterfazGUI(Simulacion simulacion) {
        this.simulacion = simulacion;
        this.ejecutando = false;
        this.intervalo = 500; // 500ms por defecto
        
        configurarVentana();
        crearComponentes();
        crearTimer();
        
        setVisible(true);
    }
    
    /**
     * Configura las propiedades básicas de la ventana.
     */
    private void configurarVentana() {
        setTitle("Juego de la Vida - Conway's Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Tamaño basado en el tablero
        Tablero tablero = simulacion.getTablero();
        int ancho = tablero.getColumnas() * TAMAÑO_CELDA + 100;
        int alto = tablero.getFilas() * TAMAÑO_CELDA + 250;
        setSize(Math.max(ancho, 600), Math.max(alto, 500));
        
        setLocationRelativeTo(null); // Centrar en pantalla
    }
    
    /**
     * Crea todos los componentes de la interfaz.
     */
    private void crearComponentes() {
        // Panel del tablero (centro)
        panelTablero = new PanelTablero();
        JScrollPane scrollPane = new JScrollPane(panelTablero);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de controles (sur)
        JPanel panelControles = crearPanelControles();
        add(panelControles, BorderLayout.SOUTH);
        
        // Barra de menú (norte)
        JMenuBar menuBar = crearBarraMenu();
        setJMenuBar(menuBar);
    }
    
    /**
     * Crea el panel de controles con botones y estadísticas.
     */
    private JPanel crearPanelControles() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        btnIniciar = new JButton("▶ Iniciar");
        btnIniciar.setToolTipText("Ejecutar simulación continuamente");
        btnIniciar.addActionListener(e -> iniciar());
        
        btnPausar = new JButton("⏸ Pausar");
        btnPausar.setToolTipText("Pausar la simulación");
        btnPausar.setEnabled(false);
        btnPausar.addActionListener(e -> pausar());
        
        btnPaso = new JButton("⏭ Paso");
        btnPaso.setToolTipText("Ejecutar una sola generación");
        btnPaso.addActionListener(e -> ejecutarPaso());
        
        btnReiniciar = new JButton("🔄 Reiniciar");
        btnReiniciar.setToolTipText("Reiniciar el tablero");
        btnReiniciar.addActionListener(e -> reiniciar());
        
        panelBotones.add(btnIniciar);
        panelBotones.add(btnPausar);
        panelBotones.add(btnPaso);
        panelBotones.add(btnReiniciar);
        
        // Panel de velocidad
        JPanel panelVelocidad = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelVelocidad.add(new JLabel("Velocidad: "));
        
        sliderVelocidad = new JSlider(50, 2000, 500);
        sliderVelocidad.setMajorTickSpacing(500);
        sliderVelocidad.setMinorTickSpacing(100);
        sliderVelocidad.setPaintTicks(true);
        sliderVelocidad.setToolTipText("Ajustar velocidad de simulación");
        sliderVelocidad.addChangeListener(e -> {
            intervalo = sliderVelocidad.getValue();
            timer.setDelay(intervalo);
            lblVelocidad.setText(intervalo + "ms");
        });
        
        lblVelocidad = new JLabel(intervalo + "ms");
        
        panelVelocidad.add(sliderVelocidad);
        panelVelocidad.add(lblVelocidad);
        
        // Panel de estadísticas
        JPanel panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblEstadisticas = new JLabel(obtenerEstadisticas());
        lblEstadisticas.setFont(new Font("Monospaced", Font.BOLD, 12));
        panelEstadisticas.add(lblEstadisticas);
        
        // Agregar todo al panel principal
        panel.add(panelBotones);
        panel.add(panelVelocidad);
        panel.add(panelEstadisticas);
        
        return panel;
    }
    
    /**
     * Crea la barra de menú.
     */
    private JMenuBar crearBarraMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        
        JMenuItem itemCargar = new JMenuItem("Cargar desde archivo...");
        itemCargar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itemCargar.addActionListener(e -> cargarDesdeArchivo());
        
        JMenuItem itemAleatorio = new JMenuItem("Generar aleatorio...");
        itemAleatorio.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        itemAleatorio.addActionListener(e -> generarAleatorio());
        
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        itemSalir.addActionListener(e -> System.exit(0));
        
        menuArchivo.add(itemCargar);
        menuArchivo.add(itemAleatorio);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        
        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de...");
        itemAcercaDe.addActionListener(e -> mostrarAcercaDe());
        
        JMenuItem itemReglas = new JMenuItem("Reglas del juego");
        itemReglas.addActionListener(e -> mostrarReglas());
        
        menuAyuda.add(itemReglas);
        menuAyuda.add(itemAcercaDe);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);
        
        return menuBar;
    }
    
    /**
     * Crea el Timer para la animación automática.
     */
    private void crearTimer() {
        timer = new Timer(intervalo, e -> {
            boolean huboCambios = simulacion.ejecutarGeneracion();
            panelTablero.repaint();
            actualizarEstadisticas();
            
            if (!huboCambios) {
                pausar();
                JOptionPane.showMessageDialog(this,
                    "El tablero se ha estabilizado en la generación " + 
                    simulacion.getGeneracionActual(),
                    "Simulación Finalizada",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    /**
     * Inicia la simulación continua.
     */
    private void iniciar() {
        if (!ejecutando) {
            ejecutando = true;
            timer.start();
            btnIniciar.setEnabled(false);
            btnPausar.setEnabled(true);
            btnPaso.setEnabled(false);
        }
    }
    
    /**
     * Pausa la simulación.
     */
    private void pausar() {
        if (ejecutando) {
            ejecutando = false;
            timer.stop();
            btnIniciar.setEnabled(true);
            btnPausar.setEnabled(false);
            btnPaso.setEnabled(true);
        }
    }
    
    /**
     * Ejecuta un solo paso de la simulación.
     */
    private void ejecutarPaso() {
        boolean huboCambios = simulacion.ejecutarGeneracion();
        panelTablero.repaint();
        actualizarEstadisticas();
        
        if (!huboCambios) {
            JOptionPane.showMessageDialog(this,
                "El tablero se ha estabilizado.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Reinicia la simulación con un nuevo tablero.
     */
    private void reiniciar() {
        pausar();
        
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Desea cargar un nuevo tablero?",
            "Reiniciar",
            JOptionPane.YES_NO_CANCEL_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            cargarDesdeArchivo();
        } else if (opcion == JOptionPane.NO_OPTION) {
            generarAleatorio();
        }
    }
    
    /**
     * Carga un tablero desde archivo.
     */
    private void cargarDesdeArchivo() {
        JFileChooser fileChooser = new JFileChooser("ejemplos");
        fileChooser.setDialogTitle("Seleccionar archivo de configuración");
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                pausar();
                Tablero tablero = LectorArchivos.cargarDesdeArchivo(archivo.getPath());
                simulacion = new Simulacion(tablero);
                panelTablero.actualizarTablero();
                actualizarEstadisticas();
                ajustarTamañoVentana();
                
                JOptionPane.showMessageDialog(this,
                    "Tablero cargado: " + tablero.getFilas() + "x" + tablero.getColumnas(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al cargar archivo:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Genera un tablero aleatorio.
     */
    private void generarAleatorio() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JTextField txtFilas = new JTextField("20");
        JTextField txtColumnas = new JTextField("30");
        JTextField txtProbabilidad = new JTextField("0.3");
        
        panel.add(new JLabel("Filas:"));
        panel.add(txtFilas);
        panel.add(new JLabel("Columnas:"));
        panel.add(txtColumnas);
        panel.add(new JLabel("Probabilidad (0.0-1.0):"));
        panel.add(txtProbabilidad);
        
        int resultado = JOptionPane.showConfirmDialog(this, panel,
            "Generar Tablero Aleatorio", JOptionPane.OK_CANCEL_OPTION);
        
        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int filas = Integer.parseInt(txtFilas.getText());
                int columnas = Integer.parseInt(txtColumnas.getText());
                double prob = Double.parseDouble(txtProbabilidad.getText());
                
                if (filas <= 0 || columnas <= 0 || prob < 0 || prob > 1) {
                    throw new IllegalArgumentException("Valores inválidos");
                }
                
                pausar();
                Tablero tablero = new Tablero(filas, columnas);
                
                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        if (Math.random() < prob) {
                            tablero.setCelda(i, j, new CeldaViva());
                        }
                    }
                }
                
                simulacion = new Simulacion(tablero);
                panelTablero.actualizarTablero();
                actualizarEstadisticas();
                ajustarTamañoVentana();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Ajusta el tamaño de la ventana según el tablero.
     */
    private void ajustarTamañoVentana() {
        Tablero tablero = simulacion.getTablero();
        int ancho = tablero.getColumnas() * TAMAÑO_CELDA + 100;
        int alto = tablero.getFilas() * TAMAÑO_CELDA + 250;
        setSize(Math.max(ancho, 600), Math.max(alto, 500));
        setLocationRelativeTo(null);
    }
    
    /**
     * Actualiza las estadísticas mostradas.
     */
    private void actualizarEstadisticas() {
        lblEstadisticas.setText(obtenerEstadisticas());
    }
    
    /**
     * Obtiene el texto de estadísticas.
     */
    private String obtenerEstadisticas() {
        return String.format("Generación: %d  |  Celdas vivas: %d  |  Estado: %s",
            simulacion.getGeneracionActual(),
            simulacion.contarCeldasVivas(),
            ejecutando ? "Ejecutando" : "Pausado");
    }
    
    /**
     * Muestra información sobre el programa.
     */
    private void mostrarAcercaDe() {
        JOptionPane.showMessageDialog(this,
            "Juego de la Vida - Conway's Game of Life\n\n" +
            "Simulador de autómata celular\n" +
            "Implementación en Java con GUI\n\n" +
            "Trabajo Práctico Especial\n" +
            "Programación 2 - TUDAI",
            "Acerca de",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Muestra las reglas del juego.
     */
    private void mostrarReglas() {
        String reglas = 
            "Reglas extensión:\n- Viva con 2-3 vecinos: 25% enferma, 75% sobrevive.\n- Enferma: siempre muere.\n- Latente: revive con 1 vecino (extensión extra: 25% al morir).";
        
        JOptionPane.showMessageDialog(this, reglas,
            "Reglas del Juego", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Panel personalizado para dibujar el tablero.
     */
    private class PanelTablero extends JPanel {
        
        public PanelTablero() {
            actualizarTablero();
        }
        
        /**
         * Actualiza el tamaño del panel según el tablero.
         */
        public void actualizarTablero() {
            Tablero tablero = simulacion.getTablero();
            int ancho = tablero.getColumnas() * TAMAÑO_CELDA;
            int alto = tablero.getFilas() * TAMAÑO_CELDA;
            setPreferredSize(new Dimension(ancho, alto));
            revalidate();
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            dibujarTablero(g);
        }
        
        /**
         * Dibuja el tablero completo.
         */
        private void dibujarTablero(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                RenderingHints.VALUE_ANTIALIAS_ON);
            
            Tablero tablero = simulacion.getTablero();
            
            for (int i = 0; i < tablero.getFilas(); i++) {
                for (int j = 0; j < tablero.getColumnas(); j++) {
                    dibujarCelda(g2d, tablero.getCelda(i, j), i, j);
                }
            }
        }
        
        /**
         * Dibuja una celda individual.
         */
        private void dibujarCelda(Graphics2D g, Celda celda, int fila, int col) {
            int x = col * TAMAÑO_CELDA;
            int y = fila * TAMAÑO_CELDA;
            
            // Color según el estado
            Color color = obtenerColorEstado(celda);
            g.setColor(color);
            g.fillRect(x, y, TAMAÑO_CELDA, TAMAÑO_CELDA);
            
            // Borde
            g.setColor(COLOR_GRID);
            g.drawRect(x, y, TAMAÑO_CELDA, TAMAÑO_CELDA);
        }
        
        /**
         * Obtiene el color según el estado de la celda.
         */
        private Color obtenerColorEstado(Celda celda) {
            char caracter = celda.getCaracter();
            switch (caracter) {
                case 'O': return COLOR_VIVA;
                case 'E': return COLOR_ENFERMA;
                case 'L': return COLOR_LATENTE;
                default:  return COLOR_MUERTA;
            }
        }
    }
}