package ejercicioIntegrador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Resultado de Prim para una ejecución desde un vértice de inicio.
 */
public class ResultadoPrim {

    private final String origen;
    private final List<AristaPonderada> aristas;
    private final int costoTotal;
    private final Set<String> verticesAlcanzados;
    private final int cantidadVerticesTotales;

    public ResultadoPrim(
            String origen,
            List<AristaPonderada> aristas,
            int costoTotal,
            Set<String> verticesAlcanzados,
            int cantidadVerticesTotales) {

        this.origen = origen;
        this.aristas = Collections.unmodifiableList(
                new ArrayList<>(aristas));
        this.costoTotal = costoTotal;
        this.verticesAlcanzados =
                Collections.unmodifiableSet(
                        new TreeSet<>(
                                verticesAlcanzados));
        this.cantidadVerticesTotales =
                cantidadVerticesTotales;
    }

    public String getOrigen() {
        return origen;
    }

    public List<AristaPonderada> getAristas() {
        return aristas;
    }

    public int getCostoTotal() {
        return costoTotal;
    }

    public Set<String> getVerticesAlcanzados() {
        return verticesAlcanzados;
    }

    public int getCantidadVerticesAlcanzados() {
        return verticesAlcanzados.size();
    }

    public int getCantidadVerticesTotales() {
        return cantidadVerticesTotales;
    }

    public boolean esArbolExpansionCompleto() {
        if (cantidadVerticesTotales == 0) {
            return false;
        }

        return verticesAlcanzados.size()
                == cantidadVerticesTotales
                && aristas.size()
                == cantidadVerticesTotales - 1;
    }
}
