package ejercicioIntegrador;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Estructura de conjuntos disjuntos utilizada por Kruskal.
 *
 * Mantiene un representante para cada componente. Si dos vértices tienen
 * el mismo representante, ya están conectados y agregar una nueva arista
 * entre ellos formaría un ciclo.
 */
public class UnionFind {

    private final Map<String, String> padre;

    public UnionFind(Set<String> vertices) {

        Objects.requireNonNull(vertices);
        padre = new HashMap<>();

        for (String vertice : vertices) {
            padre.put(vertice, vertice);
        }
    }

    public String find(String vertice) {

        validarVertice(vertice);

        String actual = vertice;

        while (!padre.get(actual).equals(actual)) {
            actual = padre.get(actual);
        }

        String raiz = actual;
        actual = vertice;

        while (!padre.get(actual).equals(actual)) {
            String siguiente = padre.get(actual);
            padre.put(actual, raiz);
            actual = siguiente;
        }

        return raiz;
    }

    /**
     * Une los conjuntos de dos vértices.
     *
     * @return true si pudo unirlos; false si ya estaban unidos.
     */
    public boolean union(
            String verticeA,
            String verticeB) {

        String raizA = find(verticeA);
        String raizB = find(verticeB);

        if (raizA.equals(raizB)) {
            return false;
        }

        padre.put(raizB, raizA);
        return true;
    }

    private void validarVertice(String vertice) {

        if (!padre.containsKey(vertice)) {
            throw new IllegalArgumentException(
                    "El vértice "
                    + vertice
                    + " no pertenece a UnionFind.");
        }
    }
}
