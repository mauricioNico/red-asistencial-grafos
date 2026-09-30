package ejercicioIntegrador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Contiene la información producida por el algoritmo de Dijkstra
 * ejecutado desde un vértice de origen.
 */
public class ResultadoDijkstra {

    private final String origen;
    private final Map<String, Integer> distancias;
    private final Map<String, String> predecesores;

    /**
     * Construye el resultado de Dijkstra.
     *
     * @param origen vértice desde el cual se ejecutó el algoritmo
     * @param distancias distancias mínimas desde el origen
     * @param predecesores vértices anteriores utilizados
     *                     para reconstruir los caminos
     */
    public ResultadoDijkstra(
            String origen,
            Map<String, Integer> distancias,
            Map<String, String> predecesores) {

        this.origen = origen;

        this.distancias = Collections.unmodifiableMap(
                new TreeMap<>(distancias)
        );

        this.predecesores = Collections.unmodifiableMap(
                new TreeMap<>(predecesores)
        );
    }

    /**
     * Devuelve el vértice de origen.
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Devuelve las distancias mínimas calculadas.
     */
    public Map<String, Integer> getDistancias() {
        return distancias;
    }

    /**
     * Devuelve los predecesores calculados.
     */
    public Map<String, String> getPredecesores() {
        return predecesores;
    }

    /**
     * Devuelve la distancia mínima desde el origen
     * hasta un destino determinado.
     */
    public int getDistancia(String destino) {

        if (!distancias.containsKey(destino)) {
            throw new IllegalArgumentException(
                    "El vértice " + destino
                            + " no pertenece al resultado."
            );
        }

        return distancias.get(destino);
    }

    /**
     * Indica si existe un camino desde el origen
     * hasta el destino.
     */
    public boolean existeCamino(String destino) {

        if (!distancias.containsKey(destino)) {
            throw new IllegalArgumentException(
                    "El vértice " + destino
                            + " no pertenece al resultado."
            );
        }

        return distancias.get(destino) != Integer.MAX_VALUE;
    }

    /**
     * Reconstruye el camino mínimo desde el origen
     * hasta un destino determinado utilizando
     * los predecesores.
     */
    public List<String> reconstruirCamino(String destino) {

        if (!distancias.containsKey(destino)) {
            throw new IllegalArgumentException(
                    "El vértice " + destino
                            + " no pertenece al resultado."
            );
        }

        if (!existeCamino(destino)) {
            return List.of();
        }

        List<String> camino = new ArrayList<>();

        String actual = destino;

        /*
         * Recorremos los predecesores hacia atrás:
         *
         * C <- F <- D <- B <- A
         */
        while (actual != null) {

            camino.add(actual);

            if (actual.equals(origen)) {
                break;
            }

            actual = predecesores.get(actual);
        }

        /*
         * Invertimos para obtener:
         *
         * A -> B -> D -> F -> C
         */
        Collections.reverse(camino);

        return List.copyOf(camino);
    }
}