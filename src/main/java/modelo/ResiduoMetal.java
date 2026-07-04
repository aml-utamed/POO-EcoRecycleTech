package modelo;

/**
 * Residuo de tipo Metal (extensión SOLID — Principio Abierto/Cerrado).
 * <p>
 * Demuestra que la jerarquía permite añadir un nuevo tipo de residuo
 * sin modificar las clases base existentes.
 * Es reciclable si no es tóxico y es un metal ferroso (aluminio, acero, etc.).
 * </p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public class ResiduoMetal extends Residuo {

    /** Indica si el metal es ferroso (acero, hierro) o no ferroso (aluminio, cobre). */
    private final boolean ferroso;

    /**
     * Crea un residuo metálico.
     *
     * @param id      identificador único
     * @param peso    peso en kg
     * @param toxico  indica toxicidad
     * @param ferroso {@code true} si es metal ferroso
     */
    public ResiduoMetal(String id, double peso, boolean toxico, boolean ferroso) {
        super(id, peso, toxico);
        this.ferroso = ferroso;
    }

    @Override
    public String getTipo() {
        return ferroso ? "Metal Ferroso" : "Metal No Ferroso";
    }

    /**
     * El metal es reciclable si no es tóxico.
     * Tanto ferrosos como no ferrosos se reciclan, pero los tóxicos
     * (ej. con pintura con plomo) requieren tratamiento especial.
     */
    @Override
    public boolean esReciclable() {
        return !isToxico();
    }
}
