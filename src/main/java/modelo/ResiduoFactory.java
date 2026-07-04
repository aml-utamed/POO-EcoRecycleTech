package modelo;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Factoría creacional (Factory Method) que encapsula la lógica de
 * creación de objetos {@link Residuo}.
 * <p>
 * Ni la Vista ni el Controlador conocen las clases concretas:
 * solo solicitan un residuo y la factoría devuelve la instancia
 * apropiada, ya sea de forma aleatoria o parametrizada.
 * </p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public class ResiduoFactory {

    /** Tipos soportados por la factoría. */
    public enum TipoResiduo {
        /** Plástico */ PLASTICO,
        /** Vidrio */   VIDRIO,
        /** Papel */    PAPEL,
        /** Metal */    METAL
    }

    private static final Random RNG = new Random();
    private static final AtomicInteger CONTADOR = new AtomicInteger(1);

    private static final String[] SUBTIPOS_PLASTICO = {"PET", "HDPE", "PVC", "LDPE"};
    private static final String[] COLORES_VIDRIO    = {"Verde", "Ámbar", "Transparente"};

    /**
     * Crea un residuo de tipo aleatorio con atributos aleatorios
     * realistas.
     *
     * @return nueva instancia concreta de {@link Residuo}
     */
    public static Residuo crearAleatorio() {
        TipoResiduo[] tipos = TipoResiduo.values();
        TipoResiduo tipo = tipos[RNG.nextInt(tipos.length)];
        return crearPorTipo(tipo);
    }

    /**
     * Crea un residuo del tipo indicado con atributos aleatorios.
     *
     * @param tipo tipo de residuo deseado
     * @return nueva instancia concreta de {@link Residuo}
     */
    public static Residuo crearPorTipo(TipoResiduo tipo) {
        int num = CONTADOR.getAndIncrement();
        double peso = 0.1 + RNG.nextDouble() * 9.9; // 0.1 – 10.0 kg
        boolean toxico = RNG.nextDouble() < 0.1;     // 10 % de probabilidad

        switch (tipo) {
            case PLASTICO:
                String sub = SUBTIPOS_PLASTICO[RNG.nextInt(SUBTIPOS_PLASTICO.length)];
                return new ResiduoPlastico(
                        String.format("PLA-%05d", num), peso, toxico, sub);

            case VIDRIO:
                String color = COLORES_VIDRIO[RNG.nextInt(COLORES_VIDRIO.length)];
                return new ResiduoVidrio(
                        String.format("VID-%05d", num), peso, toxico, color);

            case PAPEL:
                boolean mojado = RNG.nextDouble() < 0.15; // 15 %
                return new ResiduoPapel(
                        String.format("PAP-%05d", num), peso, toxico, mojado);

            case METAL:
                boolean ferroso = RNG.nextBoolean();
                return new ResiduoMetal(
                        String.format("MET-%05d", num), peso, toxico, ferroso);

            default:
                throw new IllegalArgumentException("Tipo desconocido: " + tipo);
        }
    }

    /**
     * Devuelve el tipo de contenedor adecuado para un residuo dado,
     * basándose en la clase concreta.
     *
     * @param residuo residuo a clasificar
     * @return cadena identificadora del contenedor destino
     */
    public static String contenedorPara(Residuo residuo) {
        if (residuo instanceof ResiduoPlastico) return "Plástico";
        if (residuo instanceof ResiduoVidrio)   return "Vidrio";
        if (residuo instanceof ResiduoPapel)    return "Papel";
        if (residuo instanceof ResiduoMetal)    return "Metal";
        return "Desconocido";
    }
}
