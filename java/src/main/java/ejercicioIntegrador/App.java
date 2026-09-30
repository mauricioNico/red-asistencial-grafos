package ejercicioIntegrador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Aplicación de consola para construir una red asistencial ponderada
 * y comparar BFS, DFS, Dijkstra y Prim sobre el mismo grafo.
 */
public class App {

    public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in)) {
            GrafoPonderadoNoDirigido grafo = new GrafoPonderadoNoDirigido();

            System.out.println("=== RED ASISTENCIAL ===");

            List<String> vertices = cargarVertices(teclado, grafo);
            int[][] matriz = cargarMatrizPonderada(teclado, grafo, vertices);

            mostrarMatriz(vertices, matriz);
            mostrarRed(grafo);

            boolean continuar;
            do {
                String origen = leerVerticeExistente(
                        teclado, grafo, "\nVértice de origen: ");

                mostrarRecorridos(grafo, origen);

                String destino = leerVerticeExistente(
                        teclado, grafo,
                        "\nVértice de destino para Dijkstra: ");

                mostrarDijkstra(grafo, origen, destino);
                mostrarPrim(grafo, origen);

                System.out.println(
                        "\nResumen conceptual: BFS/DFS recorren; "
                        + "Dijkstra minimiza un camino; "
                        + "Prim minimiza el costo total de conectar la red.");

                continuar = leerSiNo(
                        teclado,
                        "\n¿Desea probar otro origen/destino? (S/N): ");

            } while (continuar);

            System.out.println("\nPrograma finalizado.");
        }
    }

    private static List<String> cargarVertices(
            Scanner teclado,
            GrafoPonderadoNoDirigido grafo) {

        List<String> vertices = new ArrayList<>();
        int cantidad = leerEnteroMinimo(
                teclado, "Cantidad de vértices: ", 1);

        for (int numero = 1; numero <= cantidad;) {
            String vertice = leerTexto(
                    teclado,
                    "Código del vértice " + numero + ": "
            ).toUpperCase();

            if (grafo.existeVertice(vertice)) {
                System.out.println("Ese vértice ya fue ingresado.");
                continue;
            }

            grafo.agregarVertice(vertice);
            vertices.add(vertice);
            numero++;
        }

        Collections.sort(vertices);
        System.out.println("Orden de la matriz: " + vertices);
        return vertices;
    }

    private static int[][] cargarMatrizPonderada(
            Scanner teclado,
            GrafoPonderadoNoDirigido grafo,
            List<String> vertices) {

        int[][] matriz = new int[vertices.size()][vertices.size()];

        System.out.println("\n=== CARGA DE LA MATRIZ PONDERADA ===");
        System.out.println(
                "Ingrese 0 si no existe conexión "
                + "o los minutos si existe.");
        System.out.println(
                "La diagonal queda en 0 y la matriz simétrica "
                + "se completa automáticamente.");

        for (int fila = 0; fila < vertices.size(); fila++) {
            for (int columna = fila + 1;
                    columna < vertices.size();
                    columna++) {

                String origen = vertices.get(fila);
                String destino = vertices.get(columna);

                int minutos = leerEnteroMinimo(
                        teclado,
                        "Peso [" + origen + "][" + destino + "]: ",
                        0);

                matriz[fila][columna] = minutos;
                matriz[columna][fila] = minutos;

                if (minutos > 0) {
                    grafo.agregarArista(origen, destino, minutos);
                }
            }
        }

        return matriz;
    }

    private static void mostrarMatriz(
            List<String> vertices,
            int[][] matriz) {

        int ancho = 10;
        System.out.println("\n=== MATRIZ DE ADYACENCIA PONDERADA ===");
        System.out.printf("%" + ancho + "s", "");

        for (String vertice : vertices) {
            System.out.printf("%" + ancho + "s", vertice);
        }
        System.out.println();

        for (int fila = 0; fila < matriz.length; fila++) {
            System.out.printf("%" + ancho + "s", vertices.get(fila));

            for (int valor : matriz[fila]) {
                System.out.printf("%" + ancho + "d", valor);
            }
            System.out.println();
        }
    }

    private static void mostrarRed(
            GrafoPonderadoNoDirigido grafo) {

        System.out.println("\n=== RED CARGADA ===");

        for (String vertice : grafo.obtenerVertices()) {
            Map<String, Integer> conexiones =
                    grafo.obtenerConexiones(vertice);

            System.out.println(vertice + " -> " + conexiones);
        }
    }

    private static void mostrarRecorridos(
            GrafoPonderadoNoDirigido grafo,
            String origen) {

        ResultadoBfs bfs = grafo.recorrerEnAnchura(origen);
        ResultadoDfs dfs = grafo.recorrerEnProfundidad(origen);

        System.out.println("\n=== BFS DESDE " + origen + " ===");
        System.out.println("Recorrido: " + bfs.getRecorrido());
        System.out.println("Niveles: " + bfs.getNiveles());
        System.out.println("Aristas: " + bfs.getAristas());

        System.out.println("\n=== DFS RECURSIVO DESDE " + origen + " ===");
        System.out.println("Recorrido: " + dfs.getRecorrido());
        System.out.println("Aristas: " + dfs.getAristas());

        System.out.println(
                "\nBFS y DFS recorren la red, "
                + "pero no utilizan los pesos.");
    }

    private static void mostrarDijkstra(
            GrafoPonderadoNoDirigido grafo,
            String origen,
            String destino) {

        ResultadoDijkstra resultado = grafo.dijkstra(origen);

        System.out.println("\n=== DIJKSTRA DESDE " + origen + " ===");
        System.out.println("\nDistancias mínimas:");

        for (Map.Entry<String, Integer> entrada
                : resultado.getDistancias().entrySet()) {

            String texto = entrada.getValue() == Integer.MAX_VALUE
                    ? "∞"
                    : String.valueOf(entrada.getValue());

            System.out.println(entrada.getKey() + " = " + texto);
        }

        System.out.println("\nPredecesores:");
        if (resultado.getPredecesores().isEmpty()) {
            System.out.println("No existen predecesores.");
        } else {
            for (Map.Entry<String, String> entrada
                    : resultado.getPredecesores().entrySet()) {
                System.out.println(
                        entrada.getKey() + " <- " + entrada.getValue());
            }
        }

        List<String> camino = resultado.reconstruirCamino(destino);

        System.out.println(
                "\n=== CAMINO MÍNIMO "
                + origen + " -> " + destino + " ===");

        if (camino.isEmpty()) {
            System.out.println(
                    "No existe un camino entre "
                    + origen + " y " + destino + ".");
            return;
        }

        System.out.println("Camino: " + String.join(" -> ", camino));
        System.out.println(
                "Tiempo total: "
                + resultado.getDistancia(destino)
                + " minutos");
    }

    private static void mostrarPrim(
            GrafoPonderadoNoDirigido grafo,
            String origen) {

        ResultadoPrim resultado = grafo.prim(origen);

        System.out.println(
                "\n=== PRIM DESDE " + origen + " ===");

        System.out.println(
                "Aristas del árbol de expansión mínima:");

        if (resultado.getAristas().isEmpty()) {
            System.out.println("(sin aristas)");
        } else {
            int numero = 1;
            for (AristaPonderada arista : resultado.getAristas()) {
                System.out.println(
                        numero + ". " + arista);
                numero++;
            }
        }

        System.out.println(
                "Costo total: "
                + resultado.getCostoTotal()
                + " minutos");

        if (resultado.esArbolExpansionCompleto()) {
            System.out.println(
                    "MST completo: "
                    + resultado.getAristas().size()
                    + " = V - 1 aristas.");
        } else {
            System.out.println(
                    "La red está desconectada: Prim alcanzó "
                    + resultado.getCantidadVerticesAlcanzados()
                    + " de "
                    + resultado.getCantidadVerticesTotales()
                    + " vértices.");
        }
    }

    private static String leerVerticeExistente(
            Scanner teclado,
            GrafoPonderadoNoDirigido grafo,
            String mensaje) {

        while (true) {
            String vertice = leerTexto(teclado, mensaje).toUpperCase();

            if (grafo.existeVertice(vertice)) {
                return vertice;
            }

            System.out.println(
                    "El vértice no pertenece a la red.");
        }
    }

    private static String leerTexto(
            Scanner teclado,
            String mensaje) {

        while (true) {
            System.out.print(mensaje);
            String texto = teclado.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            System.out.println(
                    "El valor no puede estar vacío.");
        }
    }

    private static int leerEnteroMinimo(
            Scanner teclado,
            String mensaje,
            int minimo) {

        while (true) {
            int numero = leerEntero(teclado, mensaje);

            if (numero >= minimo) {
                return numero;
            }

            System.out.println(
                    "El valor mínimo permitido es "
                    + minimo + ".");
        }
    }

    private static int leerEntero(
            Scanner teclado,
            String mensaje) {

        while (true) {
            System.out.print(mensaje);
            String entrada = teclado.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException excepcion) {
                System.out.println(
                        "Debe ingresar un número entero.");
            }
        }
    }

    private static boolean leerSiNo(
            Scanner teclado,
            String mensaje) {

        while (true) {
            String respuesta =
                    leerTexto(teclado, mensaje).toUpperCase();

            if (respuesta.equals("S")) {
                return true;
            }
            if (respuesta.equals("N")) {
                return false;
            }

            System.out.println("Ingrese S o N.");
        }
    }
}
