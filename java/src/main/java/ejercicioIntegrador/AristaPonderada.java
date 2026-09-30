package ejercicioIntegrador;

import java.util.Objects;

/**
 * Arista ponderada aceptada por un algoritmo de árbol de expansión.
 * En Prim, origen indica el extremo que ya pertenecía al árbol y
 * destino el vértice que fue incorporado.
 */
public class AristaPonderada {

    private final String origen;
    private final String destino;
    private final int peso;

    public AristaPonderada(
            String origen,
            String destino,
            int peso) {

        this.origen = Objects.requireNonNull(origen);
        this.destino = Objects.requireNonNull(destino);
        this.peso = peso;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof AristaPonderada)) {
            return false;
        }

        AristaPonderada otra =
                (AristaPonderada) objeto;

        return peso == otra.peso
                && Objects.equals(
                        origen, otra.origen)
                && Objects.equals(
                        destino, otra.destino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                origen, destino, peso);
    }

    @Override
    public String toString() {
        return origen
                + " --("
                + peso
                + ")-- "
                + destino;
    }
}
