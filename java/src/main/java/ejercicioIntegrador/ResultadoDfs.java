package ejercicioIntegrador;

import java.util.List;

/**
 * Contiene la información producida por un recorrido en profundidad.
 */
public class ResultadoDfs {

    private final List<String> recorrido;
    private final List<AristaRecorrido> aristas;

    /**
     * Construye el resultado de DFS.
     *
     * @param recorrido orden de primera visita
     * @param aristas aristas utilizadas para descubrir vértices
     */
    public ResultadoDfs(
            List<String> recorrido,
            List<AristaRecorrido> aristas) {

        this.recorrido = List.copyOf(recorrido);
        this.aristas = List.copyOf(aristas);
    }

    public List<String> getRecorrido() {
        return recorrido;
    }

    public List<AristaRecorrido> getAristas() {
        return aristas;
    }
}
