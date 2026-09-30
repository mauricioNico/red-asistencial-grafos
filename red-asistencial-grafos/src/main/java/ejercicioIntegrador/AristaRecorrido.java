package ejercicioIntegrador;

import java.util.Objects;

/**
 * Representa una arista utilizada para descubrir un vértice durante
 * un recorrido del grafo.
 */
public class AristaRecorrido {

    private final String origen;
    private final String destino;

    /**
     * Construye una arista de recorrido.
     *
     * @param origen vértice desde el cual se descubre al vecino
     * @param destino vértice descubierto
     */
    public AristaRecorrido(String origen, String destino) {
        this.origen = origen;
        this.destino = destino;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof AristaRecorrido)) {
            return false;
        }

        AristaRecorrido otra = (AristaRecorrido) objeto;

        return Objects.equals(origen, otra.origen)
                && Objects.equals(destino, otra.destino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origen, destino);
    }

    @Override
    public String toString() {
        return origen + " -> " + destino;
    }
}
