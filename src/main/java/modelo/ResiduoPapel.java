package modelo;

/**
 * Residuo de tipo Papel/Cartón.
 * <p>
 * Es reciclable si no es tóxico y no está mojado (la humedad
 * degrada las fibras de celulosa e impide su reutilización).
 * </p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public class ResiduoPapel extends Residuo {

    /** Indica si el papel está mojado o húmedo. */
    private final boolean mojado;

    /**
     * Crea un residuo de papel/cartón.
     *
     * @param id     identificador único
     * @param peso   peso en kg
     * @param toxico indica toxicidad
     * @param mojado {@code true} si el papel está mojado
     */
    public ResiduoPapel(String id, double peso, boolean toxico, boolean mojado) {
        super(id, peso, toxico);
        this.mojado = mojado;
    }

    @Override
    public String getTipo() {
        return mojado ? "Cartón Húmedo" : "Papel/Cartón";
    }

    /**
     * El papel es reciclable si no es tóxico ni está mojado.
     */
    @Override
    public boolean esReciclable() {
        return !isToxico() && !mojado;
    }
}
