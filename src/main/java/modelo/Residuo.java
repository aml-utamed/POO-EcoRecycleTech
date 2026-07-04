package modelo;

/**
 * Clase abstracta que implementa {@link IResiduo} y centraliza los
 * atributos comunes a todos los residuos: ID, peso y toxicidad.
 * <p>
 * Las subclases concretas ({@link ResiduoPlastico}, {@link ResiduoVidrio},
 * {@link ResiduoPapel}, {@link ResiduoMetal}) deben implementar
 * {@link #esReciclable()} según sus propias reglas de negocio.
 * </p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public abstract class Residuo implements IResiduo {

    /** Identificador único del residuo. */
    private final String id;

    /** Peso del residuo en kilogramos. */
    private final double peso;

    /** Indica si el residuo contiene sustancias tóxicas. */
    private final boolean toxico;

    /**
     * Construye un residuo con sus atributos básicos.
     *
     * @param id     identificador único
     * @param peso   peso en kg (debe ser &gt; 0)
     * @param toxico {@code true} si contiene sustancias tóxicas
     * @throws IllegalArgumentException si el peso es negativo o cero
     */
    protected Residuo(String id, double peso, boolean toxico) {
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser positivo: " + peso);
        }
        this.id = id;
        this.peso = peso;
        this.toxico = toxico;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public double getPeso() {
        return peso;
    }

    /**
     * Indica si el residuo contiene sustancias tóxicas.
     *
     * @return {@code true} si es tóxico
     */
    public boolean isToxico() {
        return toxico;
    }

    /**
     * Método polimórfico que determina si, bajo las condiciones propias
     * de cada tipo, el residuo puede ser procesado directamente en la
     * planta sin tratamiento especial.
     *
     * @return {@code true} si el residuo es reciclable directamente
     */
    public abstract boolean esReciclable();

    @Override
    public String toString() {
        return String.format("[%s] %s — %.2f kg | tóxico=%b | reciclable=%b",
                id, getTipo(), peso, toxico, esReciclable());
    }
}
