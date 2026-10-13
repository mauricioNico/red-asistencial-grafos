package ejercicioIntegrador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Resultado del algoritmo Floyd-Warshall.
 *
 * Mantiene la matriz de distancias mínimas entre todos los pares de vértices
 * y una matriz de siguientes para reconstruir caminos mínimos.
 */
public class ResultadoFloydWarshall {

    private static final int INFINITO = Integer.MAX_VALUE;

    private final List<String> vertices;
    private final int[][] distancias;
    private final int[][] siguientes;

    public ResultadoFloydWarshall(
            List<String> vertices,
            int[][] distancias,
            int[][] siguientes) {

        this.vertices = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(vertices)));

        this.distancias = copiarMatriz(
                Objects.requireNonNull(distancias));

        this.siguientes = copiarMatriz(
                Objects.requireNonNull(siguientes));
    }

    public List<String> getVertices() {
        return vertices;
    }

    public int[][] getDistancias() {
        return copiarMatriz(distancias);
    }

    public int getDistancia(
            String origen,
            String destino) {

        int indiceOrigen = buscarIndice(origen);
        int indiceDestino = buscarIndice(destino);

        return distancias[indiceOrigen][indiceDestino];
    }

    public boolean existeCamino(
            String origen,
            String destino) {

        return getDistancia(origen, destino) != INFINITO;
    }

    public List<String> reconstruirCamino(
            String origen,
            String destino) {

        int indiceOrigen = buscarIndice(origen);
        int indiceDestino = buscarIndice(destino);

        if (siguientes[indiceOrigen][indiceDestino] == -1) {
            return List.of();
        }

        List<String> camino = new ArrayList<>();
        int actual = indiceOrigen;
        camino.add(vertices.get(actual));

        while (actual != indiceDestino) {
            actual = siguientes[actual][indiceDestino];

            if (actual == -1) {
                return List.of();
            }

            camino.add(vertices.get(actual));
        }

        return Collections.unmodifiableList(camino);
    }

    public String formatearMatriz() {

        int ancho = 8;
        StringBuilder salida = new StringBuilder();
        salida.append(String.format("%" + ancho + "s", ""));

        for (String vertice : vertices) {
            salida.append(String.format(
                    "%" + ancho + "s", vertice));
        }
        salida.append(System.lineSeparator());

        for (int fila = 0; fila < vertices.size(); fila++) {
            salida.append(String.format(
                    "%" + ancho + "s", vertices.get(fila)));

            for (int columna = 0;
                    columna < vertices.size();
                    columna++) {

                salida.append(String.format(
                        "%" + ancho + "s",
                        formatearDistancia(
                                distancias[fila][columna])));
            }
            salida.append(System.lineSeparator());
        }

        return salida.toString();
    }

    public String formatearDistancia(int distancia) {
        if (distancia == INFINITO) {
            return "∞";
        }
        return String.valueOf(distancia);
    }

    private int buscarIndice(String vertice) {

        int indice = vertices.indexOf(vertice);

        if (indice == -1) {
            throw new IllegalArgumentException(
                    "El vértice "
                    + vertice
                    + " no existe en el resultado.");
        }

        return indice;
    }

    private static int[][] copiarMatriz(int[][] matriz) {

        int[][] copia = new int[matriz.length][];

        for (int fila = 0; fila < matriz.length; fila++) {
            copia[fila] = matriz[fila].clone();
        }

        return copia;
    }
}
