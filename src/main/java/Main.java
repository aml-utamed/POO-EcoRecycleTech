import controlador.PlantaControlador;
import modelo.PlantaReciclaje;
import vista.PlantaVista;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación EcoRecycle Tech SA.
 * <p>
 * Instancia el Modelo, la Vista y el Controlador siguiendo el patrón MVC,
 * y lanza la interfaz gráfica en el hilo de eventos de Swing.
 * </p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public class Main {

    /**
     * Método principal.
     *
     * @param args argumentos de línea de comandos (no usados)
     */
    public static void main(String[] args) {
        // Look & Feel del sistema para integración nativa
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Se usará el L&F por defecto
        }

        // Lanzar en el EDT (Event Dispatch Thread) de Swing
        SwingUtilities.invokeLater(() -> {
            PlantaReciclaje modelo = new PlantaReciclaje();
            PlantaVista vista = new PlantaVista(modelo.getOrdenContenedores());
            new PlantaControlador(modelo, vista);
        });
    }
}
