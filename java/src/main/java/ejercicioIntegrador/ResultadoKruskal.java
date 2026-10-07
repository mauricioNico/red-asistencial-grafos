package ejercicioIntegrador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Resultado de Kruskal: aristas seleccionadas, costo total y estado de
 * completitud del árbol de expansión mínima.
 */
public class ResultadoKruskal {

    private final List<AristaPonderada> aristas;
    private final int costoTotal;
    private final int cantidadVerticesTotales;

    public ResultadoKruskal(
            List<AristaPonderada> aristas,
            int costoTotal,
            int cantidadVerticesTotales) {

        this.aristas = new ArrayList<>(aristas);
        this.costoTotal = costoTotal;
        this.cantidadVerticesTotales = cantidadVerticesTotales;
    }

    public List<AristaPonderada> getAristas() {
        return Collections.unmodifiableList(aristas);
    }

    public int getCostoTotal() {
        return costoTotal;
    }

    public int getCantidadVerticesTotales() {
        return cantidadVerticesTotales;
    }

    public int getCantidadComponentesUnidas() {
        return aristas.size();
    }

    public boolean esArbolExpansionCompleto() {
        return cantidadVerticesTotales > 0
                && aristas.size() == cantidadVerticesTotales - 1;
    }
}
