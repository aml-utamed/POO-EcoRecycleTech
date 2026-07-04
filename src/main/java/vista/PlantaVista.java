package vista;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Vista Swing de la planta de reciclaje EcoRecycle Tech SA.
 * <p>
 * Muestra el estado de la cinta transportadora, barras de progreso
 * con el porcentaje de llenado de cada contenedor, un log de mensajes
 * y botones de acción. <b>No contiene lógica de negocio</b> — se limita
 * a pintar y capturar eventos que delega al Controlador.
 * </p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public class PlantaVista extends JFrame {

    // ─── Componentes ───

    private final JTextArea areaCinta;
    private final JTextArea areaLog;
    private final JButton btnSimular;
    private final JButton btnProcesar;
    private final Map<String, JProgressBar> barrasContenedor;
    private final Map<String, JLabel> etiquetasContenedor;
    private final Map<String, JButton> botonesVaciar;

    // Colores corporativos
    private static final Color COLOR_FONDO     = new Color(30, 39, 46);
    private static final Color COLOR_PANEL     = new Color(45, 55, 65);
    private static final Color COLOR_TEXTO     = new Color(220, 221, 225);
    private static final Color COLOR_ACENTO    = new Color(0, 184, 148);
    private static final Color COLOR_PELIGRO   = new Color(214, 48, 49);

    /**
     * Construye la interfaz gráfica completa.
     *
     * @param tiposContenedor lista ordenada de tipos de contenedor a mostrar
     */
    public PlantaVista(java.util.List<String> tiposContenedor) {
        super("EcoRecycle Tech SA — Planta de Clasificación Automatizada");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(960, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(10, 10));

        // ─── Panel superior: título ───
        JLabel titulo = new JLabel("♻ EcoRecycle Tech SA", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(COLOR_ACENTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        titulo.setOpaque(true);
        titulo.setBackground(COLOR_FONDO);
        add(titulo, BorderLayout.NORTH);

        // ─── Panel central: cinta + contenedores ───
        JPanel centro = new JPanel(new GridLayout(1, 2, 10, 10));
        centro.setBackground(COLOR_FONDO);
        centro.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Cinta transportadora
        areaCinta = crearAreaTexto(8);
        JPanel panelCinta = crearPanelConTitulo("Cinta Transportadora",
                new JScrollPane(areaCinta));
        centro.add(panelCinta);

        // Contenedores
        barrasContenedor = new LinkedHashMap<>();
        etiquetasContenedor = new LinkedHashMap<>();
        botonesVaciar = new LinkedHashMap<>();

        JPanel panelContenedores = new JPanel();
        panelContenedores.setLayout(new BoxLayout(panelContenedores, BoxLayout.Y_AXIS));
        panelContenedores.setBackground(COLOR_PANEL);
        panelContenedores.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_ACENTO),
                "Contenedores", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14), COLOR_ACENTO));

        for (String tipo : tiposContenedor) {
            JPanel fila = new JPanel(new BorderLayout(5, 2));
            fila.setBackground(COLOR_PANEL);
            fila.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));

            JLabel lbl = new JLabel(tipo + ": 0%");
            lbl.setForeground(COLOR_TEXTO);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setPreferredSize(new Dimension(180, 20));
            etiquetasContenedor.put(tipo, lbl);

            JProgressBar bar = new JProgressBar(0, 100);
            bar.setStringPainted(true);
            bar.setForeground(COLOR_ACENTO);
            bar.setBackground(new Color(60, 70, 80));
            barrasContenedor.put(tipo, bar);

            JButton btnVaciar = new JButton("Vaciar");
            btnVaciar.setActionCommand("VACIAR_" + tipo);
            btnVaciar.setFont(new Font("SansSerif", Font.PLAIN, 11));
            btnVaciar.setFocusPainted(false);
            botonesVaciar.put(tipo, btnVaciar);

            fila.add(lbl, BorderLayout.WEST);
            fila.add(bar, BorderLayout.CENTER);
            fila.add(btnVaciar, BorderLayout.EAST);
            panelContenedores.add(fila);
        }

        centro.add(panelContenedores);
        add(centro, BorderLayout.CENTER);

        // ─── Panel inferior: log + botones ───
        JPanel inferior = new JPanel(new BorderLayout(10, 5));
        inferior.setBackground(COLOR_FONDO);
        inferior.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        areaLog = crearAreaTexto(6);
        JPanel panelLog = crearPanelConTitulo("Registro de Operaciones",
                new JScrollPane(areaLog));
        inferior.add(panelLog, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panelBotones.setBackground(COLOR_FONDO);

        btnSimular = crearBoton("Simular Entrada de Residuo", COLOR_ACENTO);
        btnSimular.setActionCommand("SIMULAR");
        btnProcesar = crearBoton("Procesar Residuo", new Color(9, 132, 227));
        btnProcesar.setActionCommand("PROCESAR");

        panelBotones.add(btnSimular);
        panelBotones.add(btnProcesar);
        inferior.add(panelBotones, BorderLayout.SOUTH);

        add(inferior, BorderLayout.SOUTH);
    }

    // ─── Métodos de actualización (llamados por el Controlador) ───

    /**
     * Actualiza el texto de la cinta transportadora.
     *
     * @param texto contenido a mostrar
     */
    public void actualizarCinta(String texto) {
        areaCinta.setText(texto);
    }

    /**
     * Actualiza la barra y la etiqueta de un contenedor.
     *
     * @param tipo        clave del contenedor
     * @param porcentaje  porcentaje de llenado (0–100)
     * @param nivelKg     nivel actual en kg
     * @param capacidadKg capacidad total en kg
     */
    public void actualizarContenedor(String tipo, int porcentaje,
                                     double nivelKg, double capacidadKg) {
        JProgressBar bar = barrasContenedor.get(tipo);
        JLabel lbl = etiquetasContenedor.get(tipo);
        if (bar == null || lbl == null) return;

        bar.setValue(porcentaje);
        bar.setForeground(porcentaje >= 90 ? COLOR_PELIGRO : COLOR_ACENTO);
        lbl.setText(String.format("%s: %.1f/%.0f kg (%d%%)",
                tipo, nivelKg, capacidadKg, porcentaje));

        if (porcentaje >= 100) {
            lbl.setForeground(COLOR_PELIGRO);
        } else {
            lbl.setForeground(COLOR_TEXTO);
        }
    }

    /**
     * Añade un mensaje al log visual.
     *
     * @param mensaje texto a añadir
     */
    public void agregarMensajeLog(String mensaje) {
        areaLog.append(mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    /**
     * Muestra una alerta modal cuando un contenedor está lleno.
     *
     * @param nombreContenedor nombre del contenedor
     */
    public void mostrarAlertaLleno(String nombreContenedor) {
        JOptionPane.showMessageDialog(this,
                "⚠ El contenedor '" + nombreContenedor + "' está LLENO.\nVacíalo para seguir procesando.",
                "Contenedor Lleno",
                JOptionPane.WARNING_MESSAGE);
    }

    // ─── Registro de listeners (el Controlador los conecta) ───

    /**
     * Registra el listener para el botón "Simular Entrada".
     * @param al listener
     */
    public void addSimularListener(ActionListener al) {
        btnSimular.addActionListener(al);
    }

    /**
     * Registra el listener para el botón "Procesar Residuo".
     * @param al listener
     */
    public void addProcesarListener(ActionListener al) {
        btnProcesar.addActionListener(al);
    }

    /**
     * Registra listeners para los botones "Vaciar" de cada contenedor.
     * @param al listener (usa {@code getActionCommand()} para distinguir)
     */
    public void addVaciarListeners(ActionListener al) {
        for (JButton btn : botonesVaciar.values()) {
            btn.addActionListener(al);
        }
    }

    /**
     * Registra el listener de cierre de ventana.
     * @param wl listener
     */
    public void addCierreListener(java.awt.event.WindowListener wl) {
        addWindowListener(wl);
    }

    // ─── Utilidades de construcción de GUI ───

    private JTextArea crearAreaTexto(int filas) {
        JTextArea area = new JTextArea(filas, 30);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setBackground(new Color(40, 50, 60));
        area.setForeground(COLOR_TEXTO);
        area.setCaretColor(COLOR_TEXTO);
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return area;
    }

    private JPanel crearPanelConTitulo(String titulo, JComponent contenido) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_ACENTO),
                titulo, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14), COLOR_ACENTO));
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
