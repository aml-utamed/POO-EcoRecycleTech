package controlador;

import modelo.Contenedor;
import modelo.PlantaReciclaje;
import modelo.Residuo;
import vista.PlantaVista;

import java.awt.event.*;
import java.util.Map;

/**
 * Controlador MVC de la planta de reciclaje.
 * <p>
 * Captura las interacciones del usuario (botones de la Vista),
 * invoca operaciones en el Modelo ({@link PlantaReciclaje}) y
 * ordena a la Vista que se actualice con los datos resultantes.
 * </p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public class PlantaControlador {

    private final PlantaReciclaje modelo;
    private final PlantaVista vista;

    /**
     * Crea el controlador vinculando modelo y vista, registra todos
     * los listeners y realiza la primera actualización visual.
     *
     * @param modelo instancia del modelo de negocio
     * @param vista  instancia de la vista Swing
     */
    public PlantaControlador(PlantaReciclaje modelo, PlantaVista vista) {
        this.modelo = modelo;
        this.vista = vista;

        // Registrar listeners
        vista.addSimularListener(new SimularListener());
        vista.addProcesarListener(new ProcesarListener());
        vista.addVaciarListeners(new VaciarListener());
        vista.addCierreListener(new CierreListener());

        // Actualización inicial
        actualizarVista();
        vista.setVisible(true);
    }

    // ───────────── Listeners ─────────────

    /**
     * Listener del botón "Simular Entrada de Residuo".
     */
    private class SimularListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Residuo r = modelo.simularEntrada();
            vista.agregarMensajeLog("→ Nuevo residuo en cinta: " + r);
            actualizarCinta();
        }
    }

    /**
     * Listener del botón "Procesar Residuo".
     */
    private class ProcesarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String resultado = modelo.procesarSiguiente();
            vista.agregarMensajeLog(resultado);
            actualizarVista();

            // Comprobar alertas de contenedores llenos
            for (Map.Entry<String, Contenedor> entry : modelo.getContenedores().entrySet()) {
                Contenedor cont = entry.getValue();
                if (cont.estaLleno() || cont.getPorcentajeLlenado() >= 95) {
                    vista.mostrarAlertaLleno(cont.getNombre());
                }
            }
        }
    }

    /**
     * Listener genérico de los botones "Vaciar" de cada contenedor.
     * Distingue el contenedor por {@code getActionCommand()} ("VACIAR_Plástico", etc.).
     */
    private class VaciarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand(); // "VACIAR_Plástico"
            String tipo = cmd.replace("VACIAR_", "");
            String resultado = modelo.vaciarContenedor(tipo);
            vista.agregarMensajeLog(resultado);
            actualizarVista();
        }
    }

    /**
     * Listener de cierre de ventana: guarda el estado antes de salir.
     */
    private class CierreListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            modelo.guardarEstado();
            vista.agregarMensajeLog("💾 Estado guardado en estado_planta.json");
            vista.dispose();
            System.exit(0);
        }
    }

    // ───────────── Actualización de la vista ─────────────

    /**
     * Actualiza todos los componentes de la vista a partir del modelo.
     */
    private void actualizarVista() {
        actualizarCinta();
        actualizarContenedores();
    }

    private void actualizarCinta() {
        StringBuilder sb = new StringBuilder();
        if (modelo.getCinta().isEmpty()) {
            sb.append("(cinta vacía)");
        } else {
            int i = 1;
            for (Residuo r : modelo.getCinta()) {
                sb.append(String.format("%d. %s%n", i++, r));
            }
        }
        vista.actualizarCinta(sb.toString());
    }

    private void actualizarContenedores() {
        for (Map.Entry<String, Contenedor> entry : modelo.getContenedores().entrySet()) {
            Contenedor c = entry.getValue();
            int pct = (int) Math.round(c.getPorcentajeLlenado());
            vista.actualizarContenedor(entry.getKey(), pct,
                    c.getNivelActual(), c.getCapacidadMaxima());
        }
    }
}
