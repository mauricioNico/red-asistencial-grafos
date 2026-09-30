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
 * Grafo ponderado no dirigido representado mediante lista de adyacencia.
 * Los pesos representan minutos de traslado.
 */
public class GrafoPonderadoNoDirigido {

    private static final int INFINITO = Integer.MAX_VALUE;

    private final Map<String, Map<String, Integer>> adyacencias;

    public GrafoPonderadoNoDirigido() {
        adyacencias = new TreeMap<>();
    }

    public void agregarVertice(String vertice) {
        validarNombreVertice(vertice);
        adyacencias.putIfAbsent(vertice, new TreeMap<>());
    }

    public void agregarArista(
            String origen,
            String destino,
            int minutos) {

        validarExistencia(origen);
        validarExistencia(destino);

        if (origen.equals(destino)) {
            throw new IllegalArgumentException(
                    "No se permiten conexiones de un vértice consigo mismo.");
        }

        if (minutos <= 0) {
            throw new IllegalArgumentException(
                    "Los minutos deben ser mayores que cero.");
        }

        adyacencias.get(origen).put(destino, minutos);
        adyacencias.get(destino).put(origen, minutos);
    }

    public boolean existeVertice(String vertice) {
        return vertice != null
                && adyacencias.containsKey(vertice);
    }

    public boolean existeArista(
            String origen,
            String destino) {

        validarExistencia(origen);
        validarExistencia(destino);
        return adyacencias.get(origen).containsKey(destino);
    }

    public Set<String> obtenerVertices() {
        return Collections.unmodifiableSet(
                new TreeSet<>(adyacencias.keySet()));
    }

    public Map<String, Integer> obtenerConexiones(
            String vertice) {

        validarExistencia(vertice);
        return Collections.unmodifiableMap(
                new TreeMap<>(adyacencias.get(vertice)));
    }

    public int cantidadVertices() {
        return adyacencias.size();
    }

    public Set<String> obtenerVecinos(
            String vertice) {

        validarExistencia(vertice);
        return Collections.unmodifiableSet(
                new TreeSet<>(
                        adyacencias.get(vertice).keySet()));
    }

    public int obtenerPeso(
            String origen,
            String destino) {

        validarExistencia(origen);
        validarExistencia(destino);

        Integer peso =
                adyacencias.get(origen).get(destino);

        if (peso == null) {
            throw new IllegalArgumentException(
                    "No existe conexión entre "
                    + origen + " y " + destino + ".");
        }

        return peso;
    }

    // =========================================================
    // BFS
    // =========================================================

    public ResultadoBfs recorrerEnAnchura(
            String origen) {

        validarExistencia(origen);

        Set<String> visitados = new HashSet<>();
        Queue<String> cola = new ArrayDeque<>();
        List<String> recorrido = new ArrayList<>();
        List<AristaRecorrido> aristas = new ArrayList<>();
        Map<String, Integer> nivelPorVertice = new HashMap<>();
        List<List<String>> niveles = new ArrayList<>();

        visitados.add(origen);
        cola.add(origen);
        nivelPorVertice.put(origen, 0);
        niveles.add(new ArrayList<>());
        niveles.get(0).add(origen);

        while (!cola.isEmpty()) {
            String actual = cola.remove();
            recorrido.add(actual);

            for (String vecino
                    : adyacencias.get(actual).keySet()) {

                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);

                    aristas.add(
                            new AristaRecorrido(
                                    actual, vecino));

                    int nivelVecino =
                            nivelPorVertice.get(actual) + 1;

                    nivelPorVertice.put(
                            vecino, nivelVecino);

                    while (niveles.size()
                            <= nivelVecino) {
                        niveles.add(
                                new ArrayList<>());
                    }

                    niveles.get(nivelVecino)
                            .add(vecino);
                }
            }
        }

        return new ResultadoBfs(
                recorrido, niveles, aristas);
    }

    // =========================================================
    // DFS
    // =========================================================

    public ResultadoDfs recorrerEnProfundidad(
            String origen) {

        validarExistencia(origen);

        Set<String> visitados = new HashSet<>();
        List<String> recorrido = new ArrayList<>();
        List<AristaRecorrido> aristas = new ArrayList<>();

        dfs(origen, visitados, recorrido, aristas);

        return new ResultadoDfs(
                recorrido, aristas);
    }

    private void dfs(
            String actual,
            Set<String> visitados,
            List<String> recorrido,
            List<AristaRecorrido> aristas) {

        visitados.add(actual);
        recorrido.add(actual);

        for (String vecino
                : adyacencias.get(actual).keySet()) {

            if (!visitados.contains(vecino)) {
                aristas.add(
                        new AristaRecorrido(
                                actual, vecino));

                dfs(
                        vecino,
                        visitados,
                        recorrido,
                        aristas);
            }
        }
    }

    // =========================================================
    // DIJKSTRA
    // =========================================================

    public ResultadoDijkstra dijkstra(
            String origen) {

        validarExistencia(origen);

        Map<String, Integer> distancias =
                new LinkedHashMap<>();
        Map<String, String> predecesores =
                new HashMap<>();
        Set<String> visitados =
                new LinkedHashSet<>();

        for (String vertice : obtenerVertices()) {
            distancias.put(vertice, INFINITO);
        }

        distancias.put(origen, 0);

        while (visitados.size()
                < cantidadVertices()) {

            String actual =
                    menorNoVisitado(
                            distancias,
                            visitados);

            if (actual == null) {
                break;
            }

            visitados.add(actual);

            for (String vecino
                    : obtenerVecinos(actual)) {

                if (!visitados.contains(vecino)) {
                    int alternativa =
                            distancias.get(actual)
                            + obtenerPeso(
                                    actual, vecino);

                    if (alternativa
                            < distancias.get(vecino)) {

                        distancias.put(
                                vecino, alternativa);

                        predecesores.put(
                                vecino, actual);
                    }
                }
            }
        }

        return new ResultadoDijkstra(
                origen,
                distancias,
                predecesores);
    }

    private String menorNoVisitado(
            Map<String, Integer> distancias,
            Set<String> visitados) {

        String menorVertice = null;
        int menorDistancia = INFINITO;

        for (Map.Entry<String, Integer> entrada
                : distancias.entrySet()) {

            String vertice = entrada.getKey();
            int distancia = entrada.getValue();

            if (!visitados.contains(vertice)
                    && distancia < menorDistancia) {

                menorDistancia = distancia;
                menorVertice = vertice;
            }
        }

        return menorVertice;
    }

    // =========================================================
    // PRIM
    // =========================================================

    /**
     * Construye un árbol de expansión mínima desde un vértice.
     *
     * Si el grafo está desconectado, devuelve el árbol mínimo de la
     * componente alcanzable y ResultadoPrim informa que no es completo.
     */
    public ResultadoPrim prim(String origen) {

        validarExistencia(origen);

        Set<String> incorporados =
                new LinkedHashSet<>();

        List<AristaPonderada> aristas =
                new ArrayList<>();

        int costoTotal = 0;

        incorporados.add(origen);

        while (incorporados.size()
                < cantidadVertices()) {

            AristaPonderada menor =
                    buscarMenorAristaSalida(
                            incorporados);

            if (menor == null) {
                break;
            }

            aristas.add(menor);
            costoTotal += menor.getPeso();
            incorporados.add(
                    menor.getDestino());
        }

        return new ResultadoPrim(
                origen,
                aristas,
                costoTotal,
                incorporados,
                cantidadVertices());
    }

    /**
     * Busca la arista de menor peso que sale del árbol actual
     * hacia un vértice todavía no incorporado.
     */
    private AristaPonderada buscarMenorAristaSalida(
            Set<String> incorporados) {

        AristaPonderada mejor = null;

        for (String origen : incorporados) {
            for (String destino
                    : obtenerVecinos(origen)) {

                if (incorporados.contains(destino)) {
                    continue;
                }

                AristaPonderada candidata =
                        new AristaPonderada(
                                origen,
                                destino,
                                obtenerPeso(
                                        origen,
                                        destino));

                if (esMejorCandidata(
                        candidata, mejor)) {
                    mejor = candidata;
                }
            }
        }

        return mejor;
    }

    /**
     * Desempate determinista para que las demostraciones y pruebas
     * produzcan siempre la misma secuencia.
     */
    private boolean esMejorCandidata(
            AristaPonderada candidata,
            AristaPonderada actual) {

        if (actual == null) {
            return true;
        }

        if (candidata.getPeso()
                != actual.getPeso()) {

            return candidata.getPeso()
                    < actual.getPeso();
        }

        int comparacionOrigen =
                candidata.getOrigen()
                        .compareTo(
                                actual.getOrigen());

        if (comparacionOrigen != 0) {
            return comparacionOrigen < 0;
        }

        return candidata.getDestino()
                .compareTo(
                        actual.getDestino()) < 0;
    }

    // =========================================================
    // VALIDACIONES
    // =========================================================

    private void validarNombreVertice(
            String vertice) {

        if (vertice == null
                || vertice.isBlank()) {

            throw new IllegalArgumentException(
                    "El vértice no puede ser nulo o vacío.");
        }
    }

    private void validarExistencia(
            String vertice) {

        validarNombreVertice(vertice);

        if (!adyacencias.containsKey(vertice)) {
            throw new IllegalArgumentException(
                    "El vértice "
                    + vertice
                    + " no existe en el grafo.");
        }
    }
}
