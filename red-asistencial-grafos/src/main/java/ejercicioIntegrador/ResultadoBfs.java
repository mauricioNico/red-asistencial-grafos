package ejercicioIntegrador;

import java.util.ArrayList;
import java.util.List;

/**
 * Contiene la información producida por un recorrido en anchura.
 */
public class ResultadoBfs {

    private final List<String> recorrido;
    private final List<List<String>> niveles;
    private final List<AristaRecorrido> aristas;

    /**
     * Construye el resultado de BFS.
     *
     * @param recorrido orden en que se procesaron los vértices
     * @param niveles vértices agrupados por cantidad de conexiones
     * @param aristas aristas utilizadas para descubrir vértices
     */
    public ResultadoBfs(
            List<String> recorrido,
            List<List<String>> niveles,
            List<AristaRecorrido> aristas) {

        this.recorrido = List.copyOf(recorrido);
        this.niveles = copiarNiveles(niveles);
        this.aristas = List.copyOf(aristas);
    }

    public List<String> getRecorrido() {
        return recorrido;
    }

    public List<List<String>> getNiveles() {
        return niveles;
    }

    public List<AristaRecorrido> getAristas() {
        return aristas;
    }

    private List<List<String>> copiarNiveles(
            List<List<String>> nivelesOriginales) {

        List<List<String>> copia = new ArrayList<>();

        for (List<String> nivel : nivelesOriginales) {
            copia.add(List.copyOf(nivel));
        }

        return List.copyOf(copia);
    }
}
