package modelo;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Modelo principal de la planta de reciclaje EcoRecycle Tech SA.
 * <p>
 * Gestiona la cinta transportadora (lista de residuos pendientes),
 * los contenedores de almacenamiento, la persistencia del estado
 * ({@code estado_planta.json}) y el histórico ({@code recycle.log}).
 * </p>
 * <p><b>No importa javax.swing.*</b> — cumple estrictamente MVC.</p>
 *
 * @author Adrián Murciego Lobato
 * @version 1.0
 */
public class PlantaReciclaje {

    private static final String ARCHIVO_ESTADO = "estado_planta.json";
    private static final String ARCHIVO_LOG    = "recycle.log";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Residuos pendientes en la cinta transportadora. */
    private final List<Residuo> cinta;

    /** Contenedores de almacenamiento indexados por tipo. */
    private final Map<String, Contenedor> contenedores;

    /** Orden de inserción de los contenedores (para la vista). */
    private final List<String> ordenContenedores;

    /**
     * Inicializa la planta con los 4 contenedores (3 base + 1 extensión)
     * y carga el estado previo si existe.
     */
    public PlantaReciclaje() {
        cinta = new ArrayList<>();
        contenedores = new LinkedHashMap<>();
        ordenContenedores = new ArrayList<>();

        agregarContenedor(new Contenedor("Depósito de Plástico", "Plástico", 100.0));
        agregarContenedor(new Contenedor("Depósito de Vidrio",   "Vidrio",   150.0));
        agregarContenedor(new Contenedor("Depósito de Papel",    "Papel",    80.0));
        agregarContenedor(new Contenedor("Depósito de Metal",    "Metal",    120.0));

        cargarEstado();
    }

    private void agregarContenedor(Contenedor c) {
        contenedores.put(c.getTipoAceptado(), c);
        ordenContenedores.add(c.getTipoAceptado());
    }

    // ───────────── Operaciones de la cinta ─────────────

    /**
     * Simula la entrada de un residuo aleatorio a la cinta.
     *
     * @return el residuo generado
     */
    public Residuo simularEntrada() {
        Residuo r = ResiduoFactory.crearAleatorio();
        cinta.add(r);
        return r;
    }

    /**
     * Procesa el primer residuo de la cinta: lo clasifica y lo deposita
     * en su contenedor correspondiente.
     *
     * @return mensaje descriptivo del resultado
     */
    public String procesarSiguiente() {
        if (cinta.isEmpty()) {
            return "La cinta está vacía. Simula la entrada de un residuo.";
        }

        Residuo r = cinta.get(0);
        String tipoCont = ResiduoFactory.contenedorPara(r);
        Contenedor c = contenedores.get(tipoCont);

        if (c == null) {
            cinta.remove(0);
            return "No existe contenedor para el tipo: " + tipoCont;
        }

        if (!r.esReciclable()) {
            cinta.remove(0);
            return String.format("⚠ %s NO es reciclable (tóxico/dañado). Derivado a tratamiento especial.", r);
        }

        if (c.estaLleno()) {
            return String.format("🚫 Contenedor '%s' LLENO. Vacíalo antes de procesar más residuos.", c.getNombre());
        }

        if (!c.depositar(r.getPeso())) {
            return String.format("🚫 El residuo %s (%.2f kg) no cabe en '%s'. Vacía el contenedor.",
                    r.getID(), r.getPeso(), c.getNombre());
        }

        cinta.remove(0);
        escribirLog(r, c);
        return String.format("✅ %s depositado en '%s'. Nivel: %.0f%%",
                r.getID(), c.getNombre(), c.getPorcentajeLlenado());
    }

    /**
     * Vacía el contenedor del tipo indicado.
     *
     * @param tipoContenedor clave del contenedor (ej. "Plástico")
     * @return mensaje descriptivo
     */
    public String vaciarContenedor(String tipoContenedor) {
        Contenedor c = contenedores.get(tipoContenedor);
        if (c == null) {
            return "Contenedor no encontrado: " + tipoContenedor;
        }
        c.vaciar();
        return String.format("🗑 '%s' vaciado correctamente.", c.getNombre());
    }

    // ───────────── Getters para la vista ─────────────

    /** @return lista inmutable de residuos en la cinta */
    public List<Residuo> getCinta() {
        return Collections.unmodifiableList(cinta);
    }

    /** @return mapa inmutable de contenedores */
    public Map<String, Contenedor> getContenedores() {
        return Collections.unmodifiableMap(contenedores);
    }

    /** @return orden de los contenedores para visualización */
    public List<String> getOrdenContenedores() {
        return Collections.unmodifiableList(ordenContenedores);
    }

    // ───────────── Persistencia ─────────────

    /**
     * Escribe una línea en {@code recycle.log} al depositar un residuo.
     */
    private void escribirLog(Residuo r, Contenedor c) {
        String linea = String.format("%s | ID=%s | Tipo=%s | Peso=%.2f kg | Contenedor=%s%n",
                LocalDateTime.now().format(FMT),
                r.getID(), r.getTipo(), r.getPeso(), c.getNombre());
        try (FileWriter fw = new FileWriter(ARCHIVO_LOG, true)) {
            fw.write(linea);
        } catch (IOException e) {
            System.err.println("Error al escribir log: " + e.getMessage());
        }
    }

    /**
     * Guarda el nivel actual de cada contenedor en {@code estado_planta.json}.
     * Debe invocarse al cerrar la aplicación.
     */
    public void guardarEstado() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        int i = 0;
        for (Map.Entry<String, Contenedor> entry : contenedores.entrySet()) {
            Contenedor c = entry.getValue();
            sb.append(String.format(Locale.US,
                    "  \"%s\": { \"nivel\": %.2f, \"capacidad\": %.2f }",
                    entry.getKey(), c.getNivelActual(), c.getCapacidadMaxima()));
            if (++i < contenedores.size()) sb.append(",");
            sb.append("\n");
        }
        sb.append("}\n");

        try {
            Files.writeString(Path.of(ARCHIVO_ESTADO), sb.toString());
        } catch (IOException e) {
            System.err.println("Error al guardar estado: " + e.getMessage());
        }
    }

    /**
     * Carga el estado previo de los contenedores desde
     * {@code estado_planta.json}, si existe.
     */
    private void cargarEstado() {
        Path path = Path.of(ARCHIVO_ESTADO);
        if (!Files.exists(path)) return;

        try {
            String json = Files.readString(path);
            // Parseamos manualmente el JSON simple (sin dependencias externas)
            for (Map.Entry<String, Contenedor> entry : contenedores.entrySet()) {
                String clave = "\"" + entry.getKey() + "\"";
                int idx = json.indexOf(clave);
                if (idx < 0) continue;

                int nivelIdx = json.indexOf("\"nivel\":", idx);
                if (nivelIdx < 0) continue;
                nivelIdx += "\"nivel\":".length();
                int nivelEnd = json.indexOf(",", nivelIdx);
                if (nivelEnd < 0) nivelEnd = json.indexOf("}", nivelIdx);

                String nivelStr = json.substring(nivelIdx, nivelEnd).trim();
                double nivel = Double.parseDouble(nivelStr);
                entry.getValue().setNivelActual(nivel);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al cargar estado (se usarán valores por defecto): " + e.getMessage());
        }
    }
}
