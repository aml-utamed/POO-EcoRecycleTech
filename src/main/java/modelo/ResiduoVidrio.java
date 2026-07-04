package modelo;

/**
 * Residuo de tipo Vidrio.
 * <p>
 * Es reciclable siempre que no sea tóxico (el vidrio se funde y
 * recicla indefinidamente si no está contaminado).
 * </p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public class ResiduoVidrio extends Residuo {

    /** Color del vidrio (ej. "Verde", "Ámbar", "Transparente"). */
    private final String color;

    /**
     * Crea un residuo de vidrio.
     *
     * @param id     identificador único
     * @param peso   peso en kg
     * @param toxico indica toxicidad
     * @param color  color del vidrio
     */
    public ResiduoVidrio(String id, double peso, boolean toxico, String color) {
        super(id, peso, toxico);
        this.color = color;
    }

    @Override
    public String getTipo() {
        return "Botella Vidrio " + color;
    }

    /**
     * El vidrio es reciclable si no es tóxico.
     */
    @Override
    public boolean esReciclable() {
        return !isToxico();
    }
}
