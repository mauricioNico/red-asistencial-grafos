package ejercicioIntegrador;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Representa una red ponderada y no dirigida mediante
 * una lista de adyacencia.
 *
 * Cada peso representa minutos de traslado.
 */
public class GrafoPonderadoNoDirigido {

    /*
     * Valor utilizado inicialmente para representar
     * una distancia desconocida o infinita.
     */
    private static final int INFINITO =
            Integer.MAX_VALUE;

    /*
     * Cada vértice posee un mapa con:
     *
     * vecino -> peso
     */
    private final Map<String, Map<String, Integer>>
            adyacencias;

    public GrafoPonderadoNoDirigido() {

        adyacencias = new TreeMap<>();
    }

    /**
     * Agrega un vértice si todavía
     * no pertenece al grafo.
     */
    public void agregarVertice(String vertice) {

        validarNombreVertice(vertice);

        adyacencias.putIfAbsent(
                vertice,
                new TreeMap<>()
        );
    }

    /**
     * Agrega una conexión bidireccional
     * entre dos vértices existentes.
     */
    public void agregarArista(
            String origen,
            String destino,
            int minutos) {

        validarExistencia(origen);
        validarExistencia(destino);

        if (origen.equals(destino)) {

            throw new IllegalArgumentException(
                    "No se permiten conexiones "
                            + "de un vértice consigo mismo."
            );
        }

        if (minutos <= 0) {

            throw new IllegalArgumentException(
                    "Los minutos deben ser mayores que cero."
            );
        }

        /*
         * Como el grafo es NO DIRIGIDO,
         * guardamos la conexión en ambos sentidos.
         */
        adyacencias.get(origen).put(
                destino,
                minutos
        );

        adyacencias.get(destino).put(
                origen,
                minutos
        );
    }

    /**
     * Indica si un vértice pertenece al grafo.
     */
    public boolean existeVertice(String vertice) {

        return vertice != null
                && adyacencias.containsKey(vertice);
    }

    /**
     * Indica si existe una arista entre
     * dos vértices.
     */
    public boolean existeArista(
            String origen,
            String destino) {

        validarExistencia(origen);
        validarExistencia(destino);

        return adyacencias
                .get(origen)
                .containsKey(destino);
    }

    /**
     * Devuelve todos los vértices.
     */
    public Set<String> obtenerVertices() {

        return Collections.unmodifiableSet(
                new TreeSet<>(
                        adyacencias.keySet()
                )
        );
    }

    /**
     * Devuelve las conexiones de un vértice
     * junto con sus pesos.
     */
    public Map<String, Integer> obtenerConexiones(
            String vertice) {

        validarExistencia(vertice);

        return Collections.unmodifiableMap(
                new TreeMap<>(
                        adyacencias.get(vertice)
                )
        );
    }

    /**
     * Devuelve la cantidad total de vértices.
     */
    public int cantidadVertices() {

        return adyacencias.size();
    }

    /**
     * Devuelve los vecinos de un vértice.
     */
    public Set<String> obtenerVecinos(
            String vertice) {

        validarExistencia(vertice);

        return Collections.unmodifiableSet(
                new TreeSet<>(
                        adyacencias
                                .get(vertice)
                                .keySet()
                )
        );
    }

    /**
     * Devuelve el peso de una arista.
     */
    public int obtenerPeso(
            String origen,
            String destino) {

        validarExistencia(origen);
        validarExistencia(destino);

        Integer peso =
                adyacencias
                        .get(origen)
                        .get(destino);

        if (peso == null) {

            throw new IllegalArgumentException(
                    "No existe conexión entre "
                            + origen
                            + " y "
                            + destino
                            + "."
            );
        }

        return peso;
    }

    // =========================================================
    // BFS
    // =========================================================

    /**
     * Realiza un recorrido en anchura desde un origen.
     *
     * Los pesos permanecen almacenados,
     * pero BFS no los utiliza.
     */
    public ResultadoBfs recorrerEnAnchura(
            String origen) {

        validarExistencia(origen);

        Set<String> visitados =
                new HashSet<>();

        Queue<String> cola =
                new ArrayDeque<>();

        List<String> recorrido =
                new ArrayList<>();

        List<AristaRecorrido> aristas =
                new ArrayList<>();

        Map<String, Integer> nivelPorVertice =
                new HashMap<>();

        List<List<String>> niveles =
                new ArrayList<>();

        /*
         * Inicializamos BFS.
         */
        visitados.add(origen);

        cola.add(origen);

        nivelPorVertice.put(
                origen,
                0
        );

        niveles.add(
                new ArrayList<>()
        );

        niveles.get(0).add(
                origen
        );

        /*
         * Mientras existan elementos
         * en la cola...
         */
        while (!cola.isEmpty()) {

            String actual =
                    cola.remove();

            recorrido.add(actual);

            /*
             * Recorremos sus vecinos.
             */
            for (String vecino
                    : adyacencias
                            .get(actual)
                            .keySet()) {

                if (!visitados.contains(vecino)) {

                    visitados.add(vecino);

                    cola.add(vecino);

                    aristas.add(
                            new AristaRecorrido(
                                    actual,
                                    vecino
                            )
                    );

                    int nivelVecino =
                            nivelPorVertice
                                    .get(actual)
                                    + 1;

                    nivelPorVertice.put(
                            vecino,
                            nivelVecino
                    );

                    while (niveles.size()
                            <= nivelVecino) {

                        niveles.add(
                                new ArrayList<>()
                        );
                    }

                    niveles
                            .get(nivelVecino)
                            .add(vecino);
                }
            }
        }

        return new ResultadoBfs(
                recorrido,
                niveles,
                aristas
        );
    }

    // =========================================================
    // DFS
    // =========================================================

    /**
     * Realiza un recorrido en profundidad
     * recursivo desde un origen.
     *
     * Los pesos permanecen almacenados,
     * pero DFS no los utiliza.
     */
    public ResultadoDfs recorrerEnProfundidad(
            String origen) {

        validarExistencia(origen);

        Set<String> visitados =
                new HashSet<>();

        List<String> recorrido =
                new ArrayList<>();

        List<AristaRecorrido> aristas =
                new ArrayList<>();

        dfs(
                origen,
                visitados,
                recorrido,
                aristas
        );

        return new ResultadoDfs(
                recorrido,
                aristas
        );
    }

    /**
     * Método auxiliar recursivo para DFS.
     */
    private void dfs(
            String actual,
            Set<String> visitados,
            List<String> recorrido,
            List<AristaRecorrido> aristas) {

        /*
         * Visitamos el vértice actual.
         */
        visitados.add(actual);

        recorrido.add(actual);

        /*
         * Analizamos todos sus vecinos.
         */
        for (String vecino
                : adyacencias
                        .get(actual)
                        .keySet()) {

            if (!visitados.contains(vecino)) {

                aristas.add(
                        new AristaRecorrido(
                                actual,
                                vecino
                        )
                );

                dfs(
                        vecino,
                        visitados,
                        recorrido,
                        aristas
                );
            }
        }
    }

    // =========================================================
    // DIJKSTRA
    // =========================================================

    /**
     * Ejecuta el algoritmo de Dijkstra desde
     * un vértice de origen.
     *
     * Calcula:
     *
     * - distancia mínima desde el origen;
     * - predecesor de cada vértice.
     *
     * A diferencia de BFS y DFS,
     * Dijkstra SÍ utiliza los pesos.
     */
    public ResultadoDijkstra dijkstra(
            String origen) {

        validarExistencia(origen);

        /*
         * Distancia mínima conocida
         * desde el origen hasta cada vértice.
         */
        Map<String, Integer> distancias =
                new LinkedHashMap<>();

        /*
         * Permite después reconstruir
         * los caminos mínimos.
         */
        Map<String, String> predecesores =
                new HashMap<>();

        /*
         * Vértices cuya distancia mínima
         * ya fue fijada definitivamente.
         */
        Set<String> visitados =
                new LinkedHashSet<>();

        /*
         * PASO 1:
         *
         * Inicialmente todos los vértices
         * tienen distancia infinita.
         */
        for (String vertice
                : obtenerVertices()) {

            distancias.put(
                    vertice,
                    INFINITO
            );
        }

        /*
         * PASO 2:
         *
         * La distancia desde el origen
         * hasta sí mismo es cero.
         */
        distancias.put(
                origen,
                0
        );

        /*
         * PASO 3:
         *
         * Repetimos hasta visitar
         * todos los vértices alcanzables.
         */
        while (visitados.size()
                < cantidadVertices()) {

            /*
             * Elegimos el vértice NO visitado
             * que tenga la menor distancia.
             */
            String actual =
                    menorNoVisitado(
                            distancias,
                            visitados
                    );

            /*
             * Si no existe otro vértice
             * alcanzable, terminamos.
             *
             * Esto puede ocurrir si el grafo
             * está desconectado.
             */
            if (actual == null) {
                break;
            }

            /*
             * La distancia del vértice actual
             * queda fijada.
             */
            visitados.add(actual);

            /*
             * PASO 4:
             *
             * Revisamos todos sus vecinos.
             */
            for (String vecino
                    : obtenerVecinos(actual)) {

                /*
                 * Sólo consideramos vecinos
                 * que todavía no fueron fijados.
                 */
                if (!visitados.contains(vecino)) {

                    /*
                     * Calculamos cuánto costaría
                     * llegar al vecino pasando
                     * por el vértice actual.
                     */
                    int alternativa =
                            distancias.get(actual)
                                    + obtenerPeso(
                                            actual,
                                            vecino
                                    );

                    /*
                     * PASO 5: RELAJACIÓN
                     *
                     * Si encontramos un camino
                     * más corto, actualizamos:
                     *
                     * - distancia
                     * - predecesor
                     */
                    if (alternativa
                            < distancias.get(vecino)) {

                        distancias.put(
                                vecino,
                                alternativa
                        );

                        predecesores.put(
                                vecino,
                                actual
                        );
                    }
                }
            }
        }

        return new ResultadoDijkstra(
                origen,
                distancias,
                predecesores
        );
    }

    /**
     * Busca entre los vértices todavía
     * no visitados el que posee
     * la menor distancia provisional.
     */
    private String menorNoVisitado(
            Map<String, Integer> distancias,
            Set<String> visitados) {

        String menorVertice = null;

        int menorDistancia =
                INFINITO;

        for (Map.Entry<String, Integer> entrada
                : distancias.entrySet()) {

            String vertice =
                    entrada.getKey();

            int distancia =
                    entrada.getValue();

            /*
             * Debe ser un vértice todavía
             * no visitado y además poseer
             * una distancia conocida.
             */
            if (!visitados.contains(vertice)
                    && distancia < menorDistancia) {

                menorDistancia =
                        distancia;

                menorVertice =
                        vertice;
            }
        }

        return menorVertice;
    }

    // =========================================================
    // VALIDACIONES
    // =========================================================

    /**
     * Verifica que el nombre de un vértice
     * sea válido.
     */
    private void validarNombreVertice(
            String vertice) {

        if (vertice == null
                || vertice.isBlank()) {

            throw new IllegalArgumentException(
                    "El vértice no puede ser "
                            + "nulo o vacío."
            );
        }
    }

    /**
     * Verifica que un vértice exista
     * dentro del grafo.
     */
    private void validarExistencia(
            String vertice) {

        validarNombreVertice(vertice);

        if (!adyacencias.containsKey(vertice)) {

            throw new IllegalArgumentException(
                    "El vértice "
                            + vertice
                            + " no existe en el grafo."
            );
        }
    }
}