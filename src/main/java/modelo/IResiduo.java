package modelo;

/**
 * Interfaz que define el contrato para todos los tipos de residuos
 * gestionados en la planta de reciclaje EcoRecycle Tech SA.
 * <p>
 * Permite gestionar colecciones homogéneas ({@code List<IResiduo>})
 * mediante polimorfismo, sin necesidad de conocer las clases concretas.
 * </p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public interface IResiduo {

    /**
     * Devuelve el identificador único del residuo.
     *
     * @return cadena con el ID único (ej. "PLA-00012")
     */
    String getID();

    /**
     * Devuelve el peso del residuo en kilogramos.
     *
     * @return peso en kg (valor positivo)
     */
    double getPeso();

    /**
     * Devuelve una cadena descriptiva del tipo de residuo.
     *
     * @return tipo legible (ej. "PET Plástico", "Botella Vidrio")
     */
    String getTipo();
}
