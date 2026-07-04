package modelo;

/**
 * Residuo de tipo Plástico.
 * <p>
 * Es reciclable siempre que no sea tóxico y su peso no exceda 10 kg
 * (piezas muy grandes requieren trituración previa).
 * </p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public class ResiduoPlastico extends Residuo {

    /** Subtipo del plástico (ej. "PET", "HDPE", "PVC"). */
    private final String subtipo;

    /**
     * Crea un residuo plástico.
     *
     * @param id      identificador único
     * @param peso    peso en kg
     * @param toxico  indica toxicidad
     * @param subtipo subtipo de plástico
     */
    public ResiduoPlastico(String id, double peso, boolean toxico, String subtipo) {
        super(id, peso, toxico);
        this.subtipo = subtipo;
    }

    @Override
    public String getTipo() {
        return subtipo + " Plástico";
    }

    /**
     * Un plástico es reciclable si no es tóxico y pesa menos de 10 kg.
     */
    @Override
    public boolean esReciclable() {
        return !isToxico() && getPeso() < 10.0;
    }
}
