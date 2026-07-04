package modelo;

/**
 * Contenedor de almacenamiento final para un tipo específico de residuo.
 * <p>
 * Cada contenedor tiene una capacidad máxima en kg y un nivel de
 * llenado actual. Cuando alcanza su capacidad, no acepta más residuos
 * hasta que sea vaciado.
 * </p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public class Contenedor {

    /** Nombre descriptivo del contenedor (ej. "Depósito de Plástico"). */
    private final String nombre;

    /** Tipo de residuo que acepta (ej. "Plástico", "Vidrio"). */
    private final String tipoAceptado;

    /** Capacidad máxima en kilogramos. */
    private final double capacidadMaxima;

    /** Nivel actual de llenado en kilogramos. */
    private double nivelActual;

    /**
     * Crea un contenedor vacío con la capacidad indicada.
     *
     * @param nombre          nombre descriptivo
     * @param tipoAceptado    tipo de residuo que acepta
     * @param capacidadMaxima capacidad en kg (debe ser &gt; 0)
     * @throws IllegalArgumentException si la capacidad es negativa o cero
     */
    public Contenedor(String nombre, String tipoAceptado, double capacidadMaxima) {
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva.");
        }
        this.nombre = nombre;
        this.tipoAceptado = tipoAceptado;
        this.capacidadMaxima = capacidadMaxima;
        this.nivelActual = 0.0;
    }

    /**
     * Intenta depositar un residuo en el contenedor.
     *
     * @param peso peso del residuo en kg
     * @return {@code true} si se depositó correctamente,
     *         {@code false} si no cabe
     */
    public boolean depositar(double peso) {
        if (peso <= 0) {
            return false;
        }
        if (nivelActual + peso > capacidadMaxima) {
            return false;
        }
        nivelActual += peso;
        return true;
    }

    /**
     * Vacía completamente el contenedor.
     */
    public void vaciar() {
        nivelActual = 0.0;
    }

    /**
     * Devuelve el porcentaje de llenado (0–100).
     *
     * @return porcentaje de ocupación
     */
    public double getPorcentajeLlenado() {
        return (nivelActual / capacidadMaxima) * 100.0;
    }

    /**
     * Indica si el contenedor está lleno.
     *
     * @return {@code true} si no cabe ni 0.01 kg más
     */
    public boolean estaLleno() {
        return (capacidadMaxima - nivelActual) < 0.01;
    }

    // ---- Getters ----

    /** @return nombre del contenedor */
    public String getNombre() {
        return nombre;
    }

    /** @return tipo de residuo que acepta */
    public String getTipoAceptado() {
        return tipoAceptado;
    }

    /** @return capacidad máxima en kg */
    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    /** @return nivel actual de llenado en kg */
    public double getNivelActual() {
        return nivelActual;
    }

    /**
     * Establece directamente el nivel de llenado (usado al cargar
     * el estado desde persistencia).
     *
     * @param nivel nuevo nivel en kg
     */
    public void setNivelActual(double nivel) {
        this.nivelActual = Math.max(0, Math.min(nivel, capacidadMaxima));
    }

    @Override
    public String toString() {
        return String.format("%s [%.1f / %.1f kg] (%.0f%%)",
                nombre, nivelActual, capacidadMaxima, getPorcentajeLlenado());
    }
}
